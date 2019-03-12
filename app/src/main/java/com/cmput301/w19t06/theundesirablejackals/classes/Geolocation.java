package com.cmput301.w19t06.theundesirablejackals.classes;


/**
 * This is class is responsible for managing location using longitude and latitude. This class
 * is used in BookRequest for setting up meet up location for book hand off
 * @author Art Limbaga
 */
public class Geolocation {
    private Double longitude;
    private Double latitude;

    /**
     * No argument constructor required for Firebase
     * DO NOT USE
     */
    @Deprecated
    public Geolocation(){}

    /**
     *
     * @param lat latitude of the location
     * @param lon longitude off the location
     */
    public Geolocation(Double lat, Double lon) {
        latitude = lat;
        longitude = lon;
    }
    /**
     *
     * @return returns the longitude of the location
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude sets the latitude of the location
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return the latitude of the lcoation
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude the latitude of the location
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "{"+
                "Lat=" + latitude +
                ", Lon=" + longitude +
                "}";
    }
}
