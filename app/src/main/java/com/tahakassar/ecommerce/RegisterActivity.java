package com.tahakassar.ecommerce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.tahakassar.ecommerce.controllers.RegisterController;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText name;
    private EditText username;
    private EditText password;
    private EditText location;
    private SupportMapFragment mapFragment;
    private boolean isActive = false;
    private Button registerBTN;
    @SuppressLint("StaticFieldLeak")
    public static Activity registerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();

        registerBTN.setOnClickListener(v -> {
            if (!checkData()) {
                Snackbar.make(registerBTN, "Please enter all fields", Snackbar.LENGTH_LONG)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                        .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                        .show();
            } else {
                try {
                    final RequestBody service = RequestBody
                            .create(MediaType.parse("text/plain"), "signup");
                    final RequestBody username_ = RequestBody
                            .create(MediaType.parse("text/plain"), username.getText().toString().trim());
                    final RequestBody password_ = RequestBody
                            .create(MediaType.parse("text/plain"), password.getText().toString().trim());
                    final RequestBody name_ = RequestBody
                            .create(MediaType.parse("text/plain"), name.getText().toString().trim());
                    final RequestBody lat = RequestBody
                            .create(MediaType.parse("text/plain"), location.getText().toString().substring(0, 8));
                    final RequestBody lng = RequestBody
                            .create(MediaType.parse("text/plain"), location.getText().toString().substring(9, 17));
                    RegisterController controller = new RegisterController();
                    controller.start(RegisterActivity.this, service, username_, password_, name_, lat, lng);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        location.setOnClickListener(v -> {
            setMap();
            Toast.makeText(this, "Choose location from the map below", Toast.LENGTH_LONG).show();
        });
    }

    private boolean checkData() {
        return !name.getText().toString().isEmpty() && !username.getText().toString().isEmpty() &&
                !password.getText().toString().isEmpty() && !location.getText().toString().isEmpty();
    }

    private void initViews() {
        name = findViewById(R.id.nameET);
        username = findViewById(R.id.usernameET);
        password = findViewById(R.id.passwordET);
        location = findViewById(R.id.locationET);
        registerBTN = findViewById(R.id.registerBTN);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            Objects.requireNonNull(mapFragment.getView()).setVisibility(View.GONE);
        }
        registerActivity = this;
    }

    private void setMap() {
        assert mapFragment != null;
        if (!isActive) {
            mapFragment.getView().setVisibility(View.VISIBLE);
            isActive = true;
        } else {
            mapFragment.getView().setVisibility(View.GONE);
            isActive = false;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng riyadh = new LatLng(24.774265, 46.738586);
        mMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(riyadh, 12.0f));
        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latLng.latitude, latLng.longitude))
                    .draggable(true));
            location.setText(String.valueOf(latLng.latitude).substring(0, 8) + "-" +
                    String.valueOf(latLng.longitude).substring(0, 8));
        });

    }
}