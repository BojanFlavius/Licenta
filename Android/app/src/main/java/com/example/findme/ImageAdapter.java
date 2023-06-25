package com.example.findme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageAdapter extends ArrayAdapter<Image> {
    private ArrayList<Image> imageList;
    private ImageView imageView;
    private TextView TextViewName;
    private Button button;

    public ImageAdapter(@NonNull Context context, int resource, ArrayList<Image> imageList) {
        super(context, resource, imageList);
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        imageView = convertView.findViewById(R.id.location_imageview);
        TextViewName = convertView.findViewById(R.id.TextViewName);
        Button button = (Button) convertView.findViewById(R.id.delete_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Locations").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(imageList.get(position).key).removeValue();
            }
        });
        TextViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),HistoryContent.class);
                intent.putExtra("EXTRA_NAME",imageList.get(position).key);
                intent.putExtra("EXTRA_PHOTO",imageList.get(position).bitmap);
                intent.putExtra("EXTRA_LAT",imageList.get(position).lat);
                intent.putExtra("EXTRA_LON",imageList.get(position).lon);
                view.getContext().startActivity(intent);
            }
        });


        TextViewName.setText(imageList.get(position).key);
        imageView.setImageBitmap(Utils.StringToBitMap(imageList.get(position).bitmap));



        return convertView;
    }

}
