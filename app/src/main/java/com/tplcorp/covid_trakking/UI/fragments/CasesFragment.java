package com.tplcorp.covid_trakking.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.tplcorp.covid_trakking.Adapter.CovidStatesAdapter;
import com.tplcorp.covid_trakking.Model.CovidStats;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.retrofit.WebService;
import com.tplcorp.covid_trakking.retrofit.WebServiceConstants;
import com.tplcorp.covid_trakking.retrofit.WebServiceFactory;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CasesFragment extends BaseFragment {

    List<CovidStats> covidStatsList;
    @BindView(R.id.tvTotalCases)
    TextView tvTotalCases;
    @BindView(R.id.tvRecovered)
    TextView tvRecovered;
    @BindView(R.id.tvActive)
    TextView tvActive;
    @BindView(R.id.tvDeath)
    TextView tvDeath;
    @BindView(R.id.pbRecovered)
    ProgressBar pbRecovered;
    @BindView(R.id.pbActive)
    ProgressBar pbActive;
    @BindView(R.id.pbDeath)
    ProgressBar pbDeath;
    @BindView(R.id.RV_covid)
    RecyclerView RV_covid;
    @BindView(R.id.loading)
    LottieAnimationView loading;
    @BindView(R.id.LL_Main)
    LinearLayout LL_Main;
    CovidStatesAdapter covidStatesAdapter;


    public static CasesFragment newInstance() {

        Bundle args = new Bundle();

        CasesFragment fragment = new CasesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loading.setVisibility(View.VISIBLE);
        LL_Main.setVisibility(View.GONE);
        getCoivdStats();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.covid_cases;
    }

    @Override
    public String getTitleBarName() {
        return "Dashboard";
    }

    @Override
    public boolean isBackButton() {
        return false;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    private void getCoivdStats() {

        WebService webService = WebServiceFactory.getInstanceCOVID();
        Call<List<CovidStats>> call = webService.getCovidStats();

        call.enqueue(new Callback<List<CovidStats>>() {
            @Override
            public void onResponse(Call<List<CovidStats>> call, Response<List<CovidStats>> response) {

                loading.setVisibility(View.GONE);
                LL_Main.setVisibility(View.VISIBLE);


                if (response.body() != null) {
                    if (response.body().size() > 0) {
                        covidStatsList = response.body();
                        setUpValues();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<CovidStats>> call, Throwable t) {

            }
        });


    }

    private void setUpValues() {
        try {
            for (int i = 0; i < covidStatsList.size(); i++) {
                if (covidStatsList.get(i).getProvince().equalsIgnoreCase("pakistan")) {
                    tvTotalCases.setText(String.valueOf(covidStatsList.get(i).getTotalcases()));
                    tvRecovered.setText(String.valueOf("Recovered" + " " + covidStatsList.get(i).getRecovered()));
                    tvActive.setText(String.valueOf("Active" + " " + covidStatsList.get(i).getActive()));
                    tvDeath.setText(String.valueOf("Deaths" + " " + covidStatsList.get(i).getTotaldeaths()));
                    ///  ---  ///
                    pbRecovered.setMax(covidStatsList.get(i).getTotalcases());
                    pbActive.setMax(covidStatsList.get(i).getTotalcases());
                    pbDeath.setMax(covidStatsList.get(i).getTotalcases());

                    pbRecovered.setProgress(covidStatsList.get(i).getRecovered());
                    pbActive.setProgress(covidStatsList.get(i).getActive());
                    pbDeath.setProgress(covidStatsList.get(i).getTotaldeaths());

                    setUpList();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong while updating data", Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpList() {
        covidStatesAdapter = new CovidStatesAdapter(getActivity(), covidStatsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        RV_covid.setLayoutManager(mLayoutManager);
        RV_covid.setItemAnimator(new DefaultItemAnimator());
        RV_covid.setAdapter(covidStatesAdapter);
    }
}
