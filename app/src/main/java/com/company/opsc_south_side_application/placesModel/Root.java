package com.company.opsc_south_side_application.placesModel;

import java.util.ArrayList;

public class Root {
    private String type;
    private ArrayList<Features> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Features> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Features> features) {
        this.features = features;
    }
}
