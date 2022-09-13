package com.company.opsc_south_side_application.directionsModel;

import java.util.List;

public class Root {
    private List<Geocoded_waypoints> geocoded_waypoints;

    private List<Routes> routes;

    private String status;

    public void setGeocoded_waypoints(List<Geocoded_waypoints> geocoded_waypoints){
        this.geocoded_waypoints = geocoded_waypoints;
    }
    public List<Geocoded_waypoints> getGeocoded_waypoints(){
        return this.geocoded_waypoints;
    }
    public void setRoutes(List<Routes> routes){
        this.routes = routes;
    }
    public List<Routes> getRoutes(){
        return this.routes;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
}
