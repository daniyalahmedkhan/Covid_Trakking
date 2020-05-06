package com.tplcorp.covid_trakking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Model.CovidStats;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Room.Tables.Notifications;

import java.util.List;

public class CovidStatesAdapter extends RecyclerView.Adapter<CovidStatesAdapter.ViewHolder> {

    private Context context;
    private List<CovidStats> covidStatsList;


    public CovidStatesAdapter(Context context, List<CovidStats> covidStatsList) {
        this.context = context;
        this.covidStatsList = covidStatsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.covid_stats_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!covidStatsList.get(position).getProvince().equalsIgnoreCase("pakistan")){
            holder.CV_Main.setVisibility(View.VISIBLE);
            holder.province.setText("Total cases in"+" "+covidStatsList.get(position).getProvince()+":  "+ GeneralHelper.NumberStandard(covidStatsList.get(position).getTotalcases()));
            holder.date.setText(covidStatsList.get(position).getUpdatedOn());
        }else{
            holder.CV_Main.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return covidStatsList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView province , date;
        CardView CV_Main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            province  =  itemView.findViewById(R.id.province);
            date  =  itemView.findViewById(R.id.date);
            CV_Main  =  itemView.findViewById(R.id.CV_Main);
        }
    }
}
