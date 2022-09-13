package com.company.opsc_south_side_application.placesModel;


public class Raw {
    private String name;

    private String shop;

    private String brand;

    private Long osm_id;

    private String osm_type;
    //private String brand:wikidata;
    //private String brand:wikipedia;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setShop(String shop){
        this.shop = shop;
    }
    public String getShop(){
        return this.shop;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public String getBrand(){
        return this.brand;
    }
    public void setOsm_id(long osm_id){
        this.osm_id = osm_id;
    }
    public long getOsm_id(){
        return this.osm_id;
    }
    public void setOsm_type(String osm_type){
        this.osm_type = osm_type;
    }
    public String getOsm_type(){
        return this.osm_type;
    }

}
