package com.tplcorp.covid_trakking.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tplcorp.covid_trakking.R;

import java.util.Arrays;
import java.util.List;

public class ProtectedHelper {

    public static final List<Intent> POWERMANAGER_INTENTS = Arrays.asList(
            /***
             * Xiaomi
             */
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            /***
             * Letv
             */
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"))
                    .setData(Uri.parse("mobilemanager://function/entry/AutoStart")),
            /***
             * huawei
             */
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),

            /***
             * Oppo and HTc
             */
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            /***
             * Vivo
             */
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            /**
             * infiniz
             */
            new Intent().setComponent(new ComponentName("com.transsion.mobilebutler",
                    "com.transsion.mobilebutler.SettingsActivity")),
            /***
             * asus
             */
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.autostart.AutoStartActivity")),
            /**
             * Samsung
             */
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.dewav.dwappmanager", "com.dewav.dwappmanager.memory.SmartClearupWhiteList")),

            new Intent().setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.SHOW_APPSEC")).addCategory(Intent.CATEGORY_DEFAULT).putExtra("packageName", "com.tplcorp.tpllife")
    );


    public static void startPowerSaverIntent(final Activity context) {
        //boolean showProtectedDialog = Sp_Get_Store_Data.getBoolData(context, SharedPrefencesKeys.ShowProtectedAppCheck.toString());
        boolean showPDialog = PrefsHelper.getBoolean("ProtectedApps" , false);
        if (!showPDialog) {
            boolean foundCorrectIntent = false;
            for (final Intent intent : POWERMANAGER_INTENTS) {
                // Huawei - Only Pre-EMUI 5.0 / Android 7 - Go to Settings > "Protected apps", check your app.
                if(intent.getComponent().equals("com.huawei.systemmanager") && Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                {
                    return;
                }

                if (isCallable(context, intent)) {
                    foundCorrectIntent = true;
                    final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(context);
                    dontShowAgain.setText("Do not show again");
                    dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            PrefsHelper.putBoolean("ProtectedApps" , true);
                        }
                    });

                    new AlertDialog.Builder(context)
                            .setTitle(Build.MANUFACTURER + " Protected Apps")
                            .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly.%n", context.getString(R.string.app_name)))
                            .setView(dontShowAgain)
                            .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    context.startActivityForResult(intent,1);
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                    break;
                }
            }
            if (!foundCorrectIntent) {
                PrefsHelper.putBoolean("ProtectedApps" , true);
            }
        }
    }

    private static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    public static void batteryOptimization(Context context){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Intent intent = new Intent();
//            String packageName = context.getPackageName();
//            PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
//            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                intent.setData(Uri.parse("package:" + packageName));
//                context.startActivity(intent);
//            }
//        }


    }

    public static void autoStartService(Context context){

        if (Build.MANUFACTURER.equalsIgnoreCase("oppo")) {
            try {
                Intent intent = new Intent();
                intent.setClassName("com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                context.startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.oppo.safe",
                            "com.oppo.safe.permission.startup.StartupAppListActivity");
                    context.startActivity(intent);

                } catch (Exception ex) {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("com.coloros.safecenter",
                                "com.coloros.safecenter.startupapp.StartupAppListActivity");
                        context.startActivity(intent);
                    } catch (Exception exx) {

                    }
                }
            }
        }else if (Build.MANUFACTURER.equalsIgnoreCase("vivo")){
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.iqoo.secure",
                        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
                context.startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                            "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                    context.startActivity(intent);
                } catch (Exception ex) {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("com.iqoo.secure",
                                "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                        context.startActivity(intent);
                    } catch (Exception exx) {
                        ex.printStackTrace();
                    }
                }
            }

        }

    }
}
