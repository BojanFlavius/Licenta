package com.example.findme;

import android.graphics.Bitmap;

public class Image {

    public String lat;
    public String lon;
    public String bitmap;
    public String key;

    public Image(String lat, String lon, Bitmap bitmap) {
        this.lat = lat;
        this.lon = lon;
        this.bitmap = Utils.BitMapToString(bitmap);
    }

    public Image(String lat, String lon, String bitmap) {
        this.lat = lat;
        this.lon = lon;
        this.bitmap = bitmap;
    }

    public Image() {

    }

    @Override
    public String toString() {
        return "Image{" +
                "lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}
