package com.example.findme;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private TextView headerName;
    private TextView headerEmail;
    private ImageView imageView;
    private DatabaseReference reference;
    private String userId;
    private String userName;
    private String userEmail;
    private Location location;

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        if(ContextCompat.checkSelfPermission(DrawerActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DrawerActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bitmap captureImage = (Bitmap) result.getData().getExtras().get("data");
                    showNameInputDialog(captureImage);
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        headerEmail = headerView.findViewById(R.id.header_email);
        headerName = headerView.findViewById(R.id.header_name);

        headerEmail.setText("");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User dataUser = snapshot.getValue(User.class);
                userName = dataUser.fullName;
                userEmail = dataUser.email;
                headerEmail.setText(userEmail);
                headerName.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void showNameInputDialog(Bitmap capturedImage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
        builder.setTitle("Enter Image Name");

        // Set up the input
        final EditText input = new EditText(DrawerActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String imageName = input.getText().toString().trim();
            if (!imageName.isEmpty()) {
                // Save the captured image with the entered name
                saveImageWithCustomName(capturedImage, imageName);
            } else {
                Toast.makeText(DrawerActivity.this, "Please enter a name for the image", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void saveImageWithCustomName(Bitmap image, String imageName) {
        reference.child(userId).child("location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                location = snapshot.getValue(Location.class);
                Image img = new Image(location.lat, location.lon, image);
                FirebaseDatabase.getInstance().getReference("Locations")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(imageName).setValue(img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DrawerActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_add_photo:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null) {
                    activityResultLauncher.launch(intent);
                }else{
                    Toast.makeText(DrawerActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryFragment()).commit();
                break;
            case R.id.nav_log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DrawerActivity.this, MainActivity.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}