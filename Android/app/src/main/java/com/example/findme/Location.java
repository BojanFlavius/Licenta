package com.example.findme;

import com.google.android.gms.maps.model.LatLng;

public class Location {

    public String lat;
    public String lon;

    public Location() {
    }

    public Location(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }

    public LatLng transformToLatLng() {
        return new LatLng(Float.parseFloat(lat),Float.parseFloat(lon));
    }
}
