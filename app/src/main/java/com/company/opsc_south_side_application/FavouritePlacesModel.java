package com.company.opsc_south_side_application;

public class FavouritePlacesModel {
    private String name;
    private String location;
    private String Address;
    private Double latitude;
    private Double longitude;

    public FavouritePlacesModel(String name, String location, String address, Double latitude, Double longitude) {
        this.name = name;
        this.location = location;
        Address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
