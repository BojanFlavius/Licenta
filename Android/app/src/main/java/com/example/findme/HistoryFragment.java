package com.example.findme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment{

    private DatabaseReference reference;
    private String userId;
    private ArrayList<Image> imageList;
    private ImageAdapter adapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        listView = view.findViewById(R.id.location_list);
        imageList = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        FirebaseDatabase.getInstance().getReference("Locations").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageList.clear();
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Image img = postSnapshot.getValue(Image.class);
                    img.key = postSnapshot.getKey();
                    imageList.add(img);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new ImageAdapter(view.getContext(), R.layout.list_item, imageList);
        listView.setAdapter(adapter);

        return view;
    }

}
