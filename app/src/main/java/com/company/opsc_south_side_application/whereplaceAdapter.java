package com.company.opsc_south_side_application;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class whereplaceAdapter extends RecyclerView.Adapter<whereplaceAdapter.MyViewHoler>{

    @NonNull
    @Override
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHoler extends RecyclerView.ViewHolder{

        public MyViewHoler(@NonNull View itemView) {
            super(itemView);
        }
    }
}
