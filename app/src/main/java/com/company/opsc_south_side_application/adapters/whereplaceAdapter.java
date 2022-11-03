package com.company.opsc_south_side_application.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.opsc_south_side_application.R;
import com.company.opsc_south_side_application.models.PlacesModel;

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
        switch(myImages.getPlaceType()){
            case "Gas Station":
                holder.categoryImage.setImageResource(R.drawable.baseline_local_gas_station_black_24dp);
                break;
            case "Restaurant":
                holder.categoryImage.setImageResource(R.drawable.ic_baseline_restaurant);
                break;
            case "Museum":
                holder.categoryImage.setImageResource(R.drawable.baseline_museum_black_24dp);
                break;
            case "Park":
                holder.categoryImage.setImageResource(R.drawable.baseline_park_black_24dp);
                break;
            case "Supermarket":
                holder.categoryImage.setImageResource(R.drawable.ic_baseline_supermarket);
                break;
        }
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
