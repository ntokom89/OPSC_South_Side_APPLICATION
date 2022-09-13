package com.company.opsc_south_side_application.placesModel;

public class Datasource {
    private String sourcename;

    private String attribution;

    private String license;

    private String url;

    private Raw raw;

    public void setSourcename(String sourcename){
        this.sourcename = sourcename;
    }
    public String getSourcename(){
        return this.sourcename;
    }
    public void setAttribution(String attribution){
        this.attribution = attribution;
    }
    public String getAttribution(){
        return this.attribution;
    }
    public void setLicense(String license){
        this.license = license;
    }
    public String getLicense(){
        return this.license;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setRaw(Raw raw){
        this.raw = raw;
    }
    public Raw getRaw(){
        return this.raw;
    }
}
