package com.company.opsc_south_side_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class whereplaceAdapter extends RecyclerView.Adapter<whereplaceAdapter.MyViewHoler>{
    private List<PlacesModel> placeList= new ArrayList<>();

    public whereplaceAdapter(List<PlacesModel> placeList) {
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearbyview, parent, false);

        return new MyViewHoler(view);
    }

    public void setPlaceList(List<PlacesModel> placeList) {
        this.placeList = placeList;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler holder, int position) {
        PlacesModel myImages = placeList.get(position);
        holder.placeName.setText(myImages.getName());
        holder.placeAddress.setText(myImages.getAddress());
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class MyViewHoler extends RecyclerView.ViewHolder{

        ImageView placeImage, categoryImage;
        TextView placeName, placeAddress;
        public MyViewHoler(@NonNull View itemView) {
            super(itemView);

            placeImage = itemView.findViewById(R.id.imageViewPlace);
            categoryImage = itemView.findViewById(R.id.imageViewPlaceType);
            placeName = itemView.findViewById(R.id.textViewPlaceName);
            placeAddress = itemView.findViewById(R.id.textViewPlaceAddress);
        }
    }
}
