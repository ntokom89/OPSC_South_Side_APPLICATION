package com.company.opsc_south_side_application.models;

public class PlacesModel {
    private String placeID;
    private String name;
    private String placeType;
    private String Address;
    private Double latitude;
    private Double longitude;

    public PlacesModel(String placeID, String name, String placeType, String address, Double latitude, Double longitude) {
        this.placeID = placeID;
        this.name = name;
        this.placeType = placeType;
        Address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PlacesModel() {
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
