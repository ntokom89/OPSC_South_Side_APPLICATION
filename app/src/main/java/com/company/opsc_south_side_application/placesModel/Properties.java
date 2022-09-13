package com.company.opsc_south_side_application.placesModel;

import java.util.List;

public class Properties {
    private String name;

    private String street;

    private String suburb;

    private String city;

    private String county;

    private String state;

    private String postcode;

    private String country;

    private String country_code;

    private double lon;

    private double lat;

    private String formatted;

    private String address_line1;

    private String address_line2;

    private List<String> categories;

    private List<String> details;

    private Datasource datasource;

    private int distance;

    private String place_id;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setStreet(String street){
        this.street = street;
    }
    public String getStreet(){
        return this.street;
    }
    public void setSuburb(String suburb){
        this.suburb = suburb;
    }
    public String getSuburb(){
        return this.suburb;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
    }
    public void setCounty(String county){
        this.county = county;
    }
    public String getCounty(){
        return this.county;
    }
    public void setState(String state){
        this.state = state;
    }
    public String getState(){
        return this.state;
    }
    public void setPostcode(String postcode){
        this.postcode = postcode;
    }
    public String getPostcode(){
        return this.postcode;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public String getCountry(){
        return this.country;
    }
    public void setCountry_code(String country_code){
        this.country_code = country_code;
    }
    public String getCountry_code(){
        return this.country_code;
    }
    public void setLon(double lon){
        this.lon = lon;
    }
    public double getLon(){
        return this.lon;
    }
    public void setLat(double lat){
        this.lat = lat;
    }
    public double getLat(){
        return this.lat;
    }
    public void setFormatted(String formatted){
        this.formatted = formatted;
    }
    public String getFormatted(){
        return this.formatted;
    }
    public void setAddress_line1(String address_line1){
        this.address_line1 = address_line1;
    }
    public String getAddress_line1(){
        return this.address_line1;
    }
    public void setAddress_line2(String address_line2){
        this.address_line2 = address_line2;
    }
    public String getAddress_line2(){
        return this.address_line2;
    }
    public void setCategories(List<String> categories){
        this.categories = categories;
    }
    public List<String> getCategories(){
        return this.categories;
    }
    public void setDetails(List<String> details){
        this.details = details;
    }
    public List<String> getDetails(){
        return this.details;
    }
    public void setDatasource(Datasource datasource){
        this.datasource = datasource;
    }
    public Datasource getDatasource(){
        return this.datasource;
    }
    public void setDistance(int distance){
        this.distance = distance;
    }
    public int getDistance(){
        return this.distance;
    }
    public void setPlace_id(String place_id){
        this.place_id = place_id;
    }
    public String getPlace_id(){
        return this.place_id;
    }
}
