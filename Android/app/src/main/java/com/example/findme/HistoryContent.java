package com.example.findme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HistoryContent extends AppCompatActivity implements OnMapReadyCallback{

    private TextView textView;
    private ImageView imageView;
    private String lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_content);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        textView = findViewById(R.id.name);
        String name = intent.getStringExtra("EXTRA_NAME");
        textView.setText(name);

        imageView = findViewById(R.id.imageView);
        String img = intent.getStringExtra("EXTRA_PHOTO");
        imageView.setImageBitmap(Utils.StringToBitMap(img));

        lat = intent.getStringExtra("EXTRA_LAT");
        lon = intent.getStringExtra("EXTRA_LON");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng markerPosition = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

        MarkerOptions markerOptions = new MarkerOptions()
                .position(markerPosition)
                .title("Location");

        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 10));
        googleMap.addMarker(markerOptions);
    }
}