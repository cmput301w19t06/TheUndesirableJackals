package com.cmput301.w19t06.theundesirablejackals.Classes;

public class Geolocation {
    private Double longitude;
    private Double latitude;

    public Geolocation() {

    }

    public Geolocation(Double latitude, Double longitude) {}

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void pickLocation() {}
}
