package com.company.opsc_south_side_application.directionsModel.step;

public class Distance {
    private String text;

    private int value;

    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }
    public void setValue(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
