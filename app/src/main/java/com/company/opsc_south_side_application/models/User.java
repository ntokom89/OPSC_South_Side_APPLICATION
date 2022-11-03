package com.company.opsc_south_side_application.models;

public class User {
    private String userID;
    private String email;
    private String name;
    private String landmarkPreference;
    private String distanceUnit;
    private String phoneNumber;
    private String about;

    public User() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLandmarkPreference() {
        return landmarkPreference;
    }

    public void setLandmarkPreference(String landmarkPreference) {
        this.landmarkPreference = landmarkPreference;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
