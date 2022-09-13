package com.company.opsc_south_side_application.directionsModel.step;

public class Steps {
    private Distance distance;

    private Duration duration;

    private End_location end_location;

    private String html_instructions;

    private Polyline polyline;

    private Start_location start_location;

    private String travel_mode;

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setEnd_location(End_location end_location) {
        this.end_location = end_location;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public void setStart_location(Start_location start_location) {
        this.start_location = start_location;
    }

    public Distance getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public End_location getEnd_location() {
        return end_location;
    }

    public String getHtml_instructions() {
        return html_instructions;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public Start_location getStart_location() {
        return start_location;
    }

    public String getTravel_mode() {
        return travel_mode;
    }
}
