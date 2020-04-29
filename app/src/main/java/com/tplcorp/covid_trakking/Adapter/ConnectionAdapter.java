package com.tplcorp.covid_trakking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Model.Connections;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.UI.MapActivity;

import java.util.List;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ViewHolder> {

    private List<Connections> connectionsList;
    private Context context;


    public ConnectionAdapter(List<Connections> connectionsList, Context context) {
        this.connectionsList = connectionsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.connections_layout_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.Distance.setText(connectionsList.get(position).getDistance()+" "+"In Meter");
        holder.Affected.setText(connectionsList.get(position).getAffected().equals("0") ? "No" : "Yes");

        if (connectionsList.get(position).getAffected().equals("0")){
            holder.Affected.setTextColor(context.getResources().getColor(R.color.md_green_500));
            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.md_green_500));
        }else{
            holder.Affected.setTextColor(Color.RED);
            holder.view1.setBackgroundColor(Color.RED);
        } 

        try {
            holder.timeStamp.setText(GeneralHelper.getTime(connectionsList.get(position).getTimeStamp()));
        }catch (Exception e){}


       holder.map.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context , MapActivity.class);
               intent.putExtra("LAT" , connectionsList.get(position).getLat());
               intent.putExtra("LNG" , connectionsList.get(position).getLng());
               context.startActivity(intent);
           }
       }); 
    }

    @Override
    public int getItemCount() {
        return connectionsList.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        public TextView Distance , Affected , timeStamp;
        public LinearLayout map;
        public  View view1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            map = itemView.findViewById(R.id.map);
            Distance = itemView.findViewById(R.id.distance);
            Affected = itemView.findViewById(R.id.affected);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            view1 = itemView.findViewById(R.id.view);
        }
    }
}
