package com.example.findme;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    private Location location;
    private GoogleMap map;
    private DatabaseReference reference;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMaxZoomPreference(20);

        reference.child(userId).child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                location = snapshot.getValue(Location.class);

                MarkerOptions markerOptions = new MarkerOptions();
               if(location != null) {
                   LatLng latLng = location.transformToLatLng();
                   markerOptions.position(latLng);
                   markerOptions.title(latLng.latitude + " " + latLng.longitude);
                   googleMap.clear();
                   googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                   googleMap.addMarker(markerOptions);
               }else {
                   Toast.makeText(getContext(), "No location available!", Toast.LENGTH_LONG).show();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
