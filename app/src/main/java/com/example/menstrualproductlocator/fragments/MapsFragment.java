package com.example.menstrualproductlocator.fragments;

import androidx.activity.result.ActivityResultLauncher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts.*;
import androidx.fragment.app.FragmentManager;

import com.example.menstrualproductlocator.CustomInfoWindow;
import com.example.menstrualproductlocator.GeofenceHelper;
import com.example.menstrualproductlocator.R;
import com.example.menstrualproductlocator.Supply;
import com.example.menstrualproductlocator.Request;
import com.example.menstrualproductlocator.Utils;
import com.example.menstrualproductlocator.databinding.FragmentMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

public class MapsFragment extends Fragment {

    private FragmentMapsBinding binding;
    public static final String TAG = "Map Fragment";
    private GeofencingClient geofencingClient;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_BACKGROUND_LOCATION = 2;
    public static final int RADIUS = 50;
    public GoogleMap map;
    private GeofenceHelper geofenceHelper;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FloatingActionButton floatingActionMenu;
    private FloatingActionButton btnRequest;
    private FloatingActionButton btnLog;
    private FloatingActionButton btnFindNearestSupply;
    private boolean clicked = false;


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
            //commented out for running purposes
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

            floatingActionMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFABclick();
                }
            });

            btnLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Supply supply = new Supply();
                    supply.setSupplyBuilding("test");
                    supply.setSupplyLocation(Utils.getCurrentUserLocation(getActivity(), locationManager));
                    supply.saveInBackground();
                    Utils.showSuppliesInMap(map);
                }
            });

            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(@NonNull @NotNull LatLng latLng) {
                    Supply supply = new Supply();
                    supply.setSupplyBuilding("test");
                    supply.setSupplyLocation(new ParseGeoPoint(latLng.latitude, latLng.longitude));
                    supply.saveInBackground();
                    Utils.showSuppliesInMap(map);
                }
            });

            btnFindNearestSupply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFindNearestSupplyDialog();
                }
            });
            btnRequest.setOnClickListener(new View.OnClickListener() {
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

    private void onFABclick() {
        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);
        if (!clicked) {
            clicked = true;
        } else {
            clicked = false;
        }
    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            btnRequest.setVisibility(View.VISIBLE);
            btnLog.setVisibility(View.VISIBLE);
            btnFindNearestSupply.setVisibility(View.VISIBLE);
        } else {
            btnRequest.setVisibility(View.INVISIBLE);
            btnLog.setVisibility(View.INVISIBLE);
            btnFindNearestSupply.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {
        Animation fromBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.from_bottom_anim);
        Animation rotateOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_open_anim);
        Animation toBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.to_bottom_anim);
        Animation rotateClose = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_close_anim);
        if (!clicked) {
            btnRequest.startAnimation(fromBottom);
            btnLog.startAnimation(fromBottom);
            btnFindNearestSupply.startAnimation(fromBottom);
            floatingActionMenu.startAnimation(rotateOpen);
        } else {
            btnRequest.startAnimation(toBottom);
            btnLog.startAnimation(toBottom);
            btnFindNearestSupply.startAnimation(toBottom);
            floatingActionMenu.startAnimation(rotateClose);
        }
    }

    private void setClickable(boolean clicked) {
        if (!clicked) {
            btnRequest.setClickable(true);
            btnLog.setClickable(true);
            btnFindNearestSupply.setClickable(true);
        } else {
            btnRequest.setClickable(false);
            btnRequest.setClickable(false);
            btnRequest.setClickable(false);

        }
    }

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

        floatingActionMenu = binding.floatingActionMenu;
        btnRequest = binding.btnRequest;
        btnLog = binding.btnLog;
        btnFindNearestSupply = binding.btnFindNearestSupply;


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

    private void showFindNearestSupplyDialog() {
        FragmentManager fm = getFragmentManager();
        FindNearestSupplyFragment findNearestSupplyFragment = FindNearestSupplyFragment.newInstance("Some Title");
        findNearestSupplyFragment.setTargetFragment(MapsFragment.this, 300);
        findNearestSupplyFragment.show(fm, "fragment_edit_name");
    }
}