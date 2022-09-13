package com.company.opsc_south_side_application.directionsModel;

import java.util.List;

public class Geocoded_waypoints {
    private String geocoder_status;

    private String place_id;

    private List<String> types;

    public void setGeocoder_status(String geocoder_status){
        this.geocoder_status = geocoder_status;
    }
    public String getGeocoder_status(){
        return this.geocoder_status;
    }
    public void setPlace_id(String place_id){
        this.place_id = place_id;
    }
    public String getPlace_id(){
        return this.place_id;
    }
    public void setTypes(List<String> types){
        this.types = types;
    }
    public List<String> getTypes(){
        return this.types;
    }
}
