package com.tahakassar.ecommerce;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.tahakassar.ecommerce.controllers.UpdateProfileController;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment {

    View rootView;
    private EditText nameET;
    private EditText usernameET;
    private EditText passwordET;
    private EditText locationET;
    private Button editBTN;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    MapView mMapView;
    BasicData basicData;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mMapView = rootView.findViewById(R.id.mapAddPark);
        mMapView.onCreate(savedInstanceState);
        initViews();
        Log.e("profile", String.valueOf(basicData.getUserData()));
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(googleMap -> {
            mMap = googleMap;
            LatLng latLng1 = new LatLng(Double.parseDouble(basicData.getUserData().getLat()),
                    Double.parseDouble(basicData.getUserData().getLng()));
            mMap.addMarker(new MarkerOptions().position(latLng1));
            mMap.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(latLng1, 12.0f));
            mMap.setOnMapClickListener(latLng -> {
                mMap.clear();
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .draggable(true));
                locationET.setText("" + String.valueOf(latLng.latitude).substring(0, 8) + "-" +
                        String.valueOf(latLng.longitude).substring(0, 8));
            });
        });
        showData();
        editBTN.setOnClickListener(v -> {
            if (!checkData()) {
                Snackbar.make(editBTN, "Please enter full information", Snackbar.LENGTH_LONG)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                        .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                        .show();
            } else if (checkData() && checkChanged()) {
                try {
                    final RequestBody service = RequestBody
                            .create(MediaType.parse("text/plain"), "update_profile_full");
                    final RequestBody username_ = RequestBody
                            .create(MediaType.parse("text/plain"), usernameET.getText().toString().trim());
                    final RequestBody password_ = RequestBody
                            .create(MediaType.parse("text/plain"), passwordET.getText().toString().trim());
                    final RequestBody name_ = RequestBody
                            .create(MediaType.parse("text/plain"), nameET.getText().toString().trim());
                    final RequestBody lat = RequestBody
                            .create(MediaType.parse("text/plain"), locationET.getText().toString().substring(0, 8));
                    final RequestBody lng = RequestBody
                            .create(MediaType.parse("text/plain"), locationET.getText().toString().substring(9, 17));
                    final RequestBody userID = RequestBody
                            .create(MediaType.parse("text/plain"), basicData.getUserData().getId());
                    UpdateProfileController controller = new UpdateProfileController();
                    controller.start(getContext(), service, userID, name_, username_, password_, lat, lng);
                    JsonObject user = new JsonObject();
                    user.addProperty("id", basicData.getUserData().getId());
                    user.addProperty("name", nameET.getText().toString().trim());
                    user.addProperty("username", usernameET.getText().toString().trim());
                    user.addProperty("password", passwordET.getText().toString().trim());
                    user.addProperty("lat", locationET.getText().toString().substring(0, 8));
                    user.addProperty("lng", locationET.getText().toString().substring(9, 17));
                    basicData.setUserData(user);
                } catch (Exception e) {

                }
            }
        });
        return rootView;
    }

    private boolean checkChanged() {
        return !nameET.getText().toString().trim().equals(basicData.getUserData().getName()) ||
                !usernameET.getText().toString().trim().equals(basicData.getUserData().getUsername()) ||
                !passwordET.getText().toString().trim().equals(basicData.getUserData().getPassword()) ||
                !locationET.getText().toString().trim().equals(basicData.getUserData().getLat() + "-" +
                        basicData.getUserData().getLng());
    }

    private boolean checkData() {
        return !nameET.getText().toString().trim().isEmpty()
                && !usernameET.getText().toString().trim().isEmpty()
                && !passwordET.getText().toString().trim().isEmpty()
                && !locationET.getText().toString().trim().isEmpty();
    }

    @SuppressLint("SetTextI18n")
    private void showData() {
        nameET.setText(basicData.getUserData().getName());
        usernameET.setText(basicData.getUserData().getUsername());
        passwordET.setText(basicData.getUserData().getPassword());
        locationET.setText(basicData.getUserData().getLat() + "-" + basicData.getUserData().getLng());
    }

    private void initViews() {
        nameET = rootView.findViewById(R.id.nameET);
        usernameET = rootView.findViewById(R.id.usernameET);
        passwordET = rootView.findViewById(R.id.passwordET);
        locationET = rootView.findViewById(R.id.locationET);
        editBTN = rootView.findViewById(R.id.editBTN);

        basicData = BasicData.getInstance(getContext());
    }

}