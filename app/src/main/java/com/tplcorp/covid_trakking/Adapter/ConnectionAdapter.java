package com.tplcorp.covid_trakking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tplcorp.covid_trakking.Model.Connections;
import com.tplcorp.covid_trakking.R;

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
        holder.Name.setText("Anonymous");
        holder.Distance.setText(connectionsList.get(position).getDistance()+" "+"meter");
        holder.Affected.setText(connectionsList.get(position).getAffected().equals("0") ? "Affected: NO" : "Affected: YES");
    }

    @Override
    public int getItemCount() {
        return connectionsList.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
        public TextView Name , Distance , Affected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name);
            Distance = itemView.findViewById(R.id.distance);
            Affected = itemView.findViewById(R.id.affected);
        }
    }
}
