package com.company.opsc_south_side_application.placesModel;

import java.util.List;

public class Geometry {
    private String type;

    private List<Double> coordinates;

    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setCoordinates(List<Double> coordinates){
        this.coordinates = coordinates;
    }
    public List<Double> getCoordinates(){
        return this.coordinates;
    }
}
