package com.example.menstrualproductlocator.fragments;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts.*;

import com.example.menstrualproductlocator.CustomInfoWindow;
import com.example.menstrualproductlocator.GeofenceBroadcastReceiver;
import com.example.menstrualproductlocator.GeofenceHelper;
import com.example.menstrualproductlocator.R;
import com.example.menstrualproductlocator.Supply;
import com.example.menstrualproductlocator.Request;
import com.example.menstrualproductlocator.Utils;
import com.example.menstrualproductlocator.databinding.FragmentMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;


import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    private FragmentMapsBinding binding;
    private Button btnLogSupply;
    private Button btnRequestProduct;
    public static final String TAG = "Map Fragment";
    private GeofencingClient geofencingClient;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_BACKGROUND_LOCATION = 2;
    public static final int RADIUS = 100;
    private GoogleMap map;
    private GeofenceHelper geofenceHelper;
    private FusedLocationProviderClient fusedLocationProviderClient;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            geofencingClient = LocationServices.getGeofencingClient(getContext());
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            geofenceHelper = new GeofenceHelper(getContext());
            map = googleMap;
            googleMap.setInfoWindowAdapter(new CustomInfoWindow(getLayoutInflater()));
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));

            Utils.getCurrentUserLocation(getActivity(), locationManager);
            //Utils.saveCurrentUserLocation(getActivity(), locationManager);
            Utils.showCurrentUserInMap(map, getActivity(), locationManager);
            Utils.showSuppliesInMap(map);
            Utils.showRequestsInMap(map, geofenceHelper, geofencingClient, getContext());

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

                }
            }

            btnLogSupply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Supply supply = new Supply();
                    supply.setSupplyBuilding("test");
                    supply.setSupplyLocation(Utils.getCurrentUserLocation(getActivity(), locationManager));
                    supply.saveInBackground();
                    Utils.showSuppliesInMap(map);
                }
            });
            btnRequestProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= 29) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Request request = new Request();
                            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
                            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    request.setRequestLocation(latLng);
                                    Utils.showAlertDialogToMakeRequest(getContext(), latLng, googleMap, request, geofenceHelper, geofencingClient);
                                    Utils.showRequestsInMap(googleMap, geofenceHelper, geofencingClient, getContext());
                                }
                            });
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_BACKGROUND_LOCATION);
                            } else {
                                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_BACKGROUND_LOCATION);
                            }
                        }
                    } else {
                        Request request = new Request();
                        request.setRequestLocation(Utils.getCurrentUserLocation(getActivity(), locationManager));
                        Utils.showAlertDialogToMakeRequest(getContext(), request.getRequestLatLng(), googleMap, request, geofenceHelper, geofencingClient);
                        Utils.showRequestsInMap(googleMap, geofenceHelper, geofencingClient, getContext());
                    }
                }
            });

            Log.i(TAG, "Location: " + Utils.getCurrentUserLocation(getActivity(), locationManager));
            Log.i(TAG, "User: " + ParseUser.getCurrentUser().getUsername());
        }
    };

    @SuppressLint("MissingPermission")
    private void enableUserLocation() {
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }
        });
        map.setMyLocationEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        btnLogSupply = binding.btnLogSupply;
        btnRequestProduct = binding.btnRequestProduct;

        geofencingClient = LocationServices.getGeofencingClient(getContext());
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

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new RequestPermission(), isGranted -> {
        if (isGranted) {
            enableUserLocation();
        }
    });
}