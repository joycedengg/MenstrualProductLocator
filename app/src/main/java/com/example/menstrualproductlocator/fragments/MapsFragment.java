package com.example.menstrualproductlocator.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.menstrualproductlocator.R;
import com.example.menstrualproductlocator.Supply;
import com.example.menstrualproductlocator.Request;
import com.example.menstrualproductlocator.databinding.FragmentMapsBinding;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment {

    private FragmentMapsBinding binding;
    private GeofencingClient geofencingClient;
    private Button btnLogSupply;
    private Button btnRequestProduct;
    public static final String TAG = "Map Fragment";
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            getCurrentUserLocation();
            saveCurrentUserLocation();
            showCurrentUserInMap(googleMap);
            showSuppliesInMap(googleMap);
            showRequestsInMap(googleMap);

            btnLogSupply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Supply supply = new Supply();
                    supply.setSupplyBuilding("test");
                    supply.setSupplyLocation(getCurrentUserLocation());
                    supply.saveInBackground();
                    showSuppliesInMap(googleMap);
                }
            });
            btnRequestProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Request request = new Request();
                    request.setRequestBuilding("request test");
                    request.setRequestLocation(getCurrentUserLocation());
                    request.saveInBackground();
                    showRequestsInMap(googleMap);
                }
            });

            Log.i(TAG, "Location: " + getCurrentUserLocation());
            Log.i(TAG, "User: " + ParseUser.getCurrentUser().getUsername());
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        btnLogSupply = binding.btnLogSupply;
        btnRequestProduct = binding.btnRequestProduct;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    private void saveCurrentUserLocation() {
        boolean hasNoFineLocationPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean hasNoCoarseLocationPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;

        if (hasNoFineLocationPermission && hasNoCoarseLocationPermission) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                ParseGeoPoint currentUserLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

                ParseUser currentUser = ParseUser.getCurrentUser();

                if (currentUser != null) {
                    currentUser.put("userLocation", currentUserLocation);
                    currentUser.saveInBackground();
                }
            }
        }
    }

    private ParseGeoPoint getCurrentUserLocation() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        saveCurrentUserLocation();
        return currentUser.getParseGeoPoint("userLocation");
    }

    private void showCurrentUserInMap(final GoogleMap googleMap) {
        ParseGeoPoint currentUserLocation = getCurrentUserLocation();

        LatLng currentUser = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(currentUser).title(ParseUser.getCurrentUser().getUsername()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUser, 18));
    }

    private void showSuppliesInMap(final GoogleMap googleMap) {
        ParseQuery < ParseObject > query = ParseQuery.getQuery("supply");
        query.whereExists("location");
        query.findInBackground(new FindCallback < ParseObject > () {
            @Override public void done(List < ParseObject > supplies, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < supplies.size(); i++) {
                        LatLng supplyLocation = new LatLng(supplies.get(i).getParseGeoPoint("location").getLatitude(), supplies.get(i).getParseGeoPoint("location").getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(supplyLocation).title(supplies.get(i).getString("Building")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }

    private void showRequestsInMap(final GoogleMap googleMap) {
        ParseQuery < ParseObject > query = ParseQuery.getQuery("request");
        query.whereExists("location");
        query.findInBackground(new FindCallback < ParseObject > () {
            @Override public void done(List < ParseObject > requests, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < requests.size(); i++) {
                        LatLng requestLocation = new LatLng(requests.get(i).getParseGeoPoint("location").getLatitude(), requests.get(i).getParseGeoPoint("location").getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(requestLocation).title(requests.get(i).getString("Building")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    }
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }
}