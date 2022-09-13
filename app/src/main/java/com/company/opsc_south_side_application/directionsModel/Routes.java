package com.company.opsc_south_side_application.directionsModel;

import java.util.List;

public class Routes {
    private Bounds bounds;

    private String copyrights;

    private List<Legs> legs;

    private Overview_polyline overview_polyline;

    private String summary;

    private List<String> warnings;

    private List<String> waypoint_order;

    public void setBounds(Bounds bounds){
        this.bounds = bounds;
    }
    public Bounds getBounds(){
        return this.bounds;
    }
    public void setCopyrights(String copyrights){
        this.copyrights = copyrights;
    }
    public String getCopyrights(){
        return this.copyrights;
    }
    public void setLegs(List<Legs> legs){
        this.legs = legs;
    }
    public List<Legs> getLegs(){
        return this.legs;
    }
    public void setOverview_polyline(Overview_polyline overview_polyline){
        this.overview_polyline = overview_polyline;
    }
    public Overview_polyline getOverview_polyline(){
        return this.overview_polyline;
    }
    public void setSummary(String summary){
        this.summary = summary;
    }
    public String getSummary(){
        return this.summary;
    }
    public void setWarnings(List<String> warnings){
        this.warnings = warnings;
    }
    public List<String> getWarnings(){
        return this.warnings;
    }
    public void setWaypoint_order(List<String> waypoint_order){
        this.waypoint_order = waypoint_order;
    }
    public List<String> getWaypoint_order(){
        return this.waypoint_order;
    }
}
