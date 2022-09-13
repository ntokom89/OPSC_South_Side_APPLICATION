package com.company.opsc_south_side_application.directionsModel;

public class Distance {
    private String text;

    private long value;

    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }
    public void setValue(long value){
        this.value = value;
    }
    public long getValue(){
        return this.value;
    }
}
