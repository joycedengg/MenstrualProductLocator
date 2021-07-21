package com.example.menstrualproductlocator.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.menstrualproductlocator.AppBroadcastReceiver;
import com.example.menstrualproductlocator.R;
import com.example.menstrualproductlocator.Supply;
import com.example.menstrualproductlocator.Request;
import com.example.menstrualproductlocator.Utils;
import com.example.menstrualproductlocator.databinding.FragmentMapsBinding;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    private FragmentMapsBinding binding;
    private Button btnLogSupply;
    private Button btnRequestProduct;
    private PendingIntent geofencePendingIntent;
    public static final String TAG = "Map Fragment";
    private GeofencingClient geofencingClient;
    LocationManager locationManager;
    private static List<Geofence> geofenceList = new ArrayList<>();
    private static final int REQUEST_LOCATION = 1;
    private GoogleMap map;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            Utils.getCurrentUserLocation(getActivity(), locationManager);
            Utils.saveCurrentUserLocation(getActivity(), locationManager);
            Utils.showCurrentUserInMap(map, getActivity(), locationManager);
            Utils.showSuppliesInMap(map);
            Utils.showRequestsInMap(map);

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
                    Request request = new Request(false);
                    request.setRequestLocation(Utils.getCurrentUserLocation(getActivity(), locationManager));
                    request.showAlertDialogForRequest(getContext(), request.getRequestLatLng(), map, request);

                    Utils.showRequestsInMap(map);
                    Utils.createGeofence(geofenceList, request.getRequestLatLng());

                    Log.i(TAG, "Geofence: " + Utils.returnGeofence(geofenceList));

                }
            });

            Log.i(TAG, "Location: " + Utils.getCurrentUserLocation(getActivity(), locationManager));
            Log.i(TAG, "User: " + ParseUser.getCurrentUser().getUsername());
        }
    };

    @SuppressLint("MissingPermission")
    private void enableUserLocation() {
        map.setMyLocationEnabled(true);
    }

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

    private void addGeofence() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(getActivity(), aVoid -> {
                        Toast.makeText(getContext()
                                , "Geofencing has started", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(getActivity(), e -> {
                        Toast.makeText(getContext()
                                , "Geofencing failed", Toast.LENGTH_SHORT).show();

                    });
            return;
        }
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(getActivity(), aVoid -> {
                    Toast.makeText(getContext()
                            , "Geofencing has started", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(getActivity(), e -> {
                    Toast.makeText(getContext()
                            , "Geofencing failed", Toast.LENGTH_SHORT).show();

                });
    }

    private void removeGeofence() {
        geofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(getActivity(), aVoid -> {
                    Toast.makeText(getContext()
                            , "Geofencing has been removed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(getActivity(), e -> {
                    Toast.makeText(getContext()
                            , "Geofencing could not be removed", Toast.LENGTH_SHORT).show();
                });
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Toast.makeText(getContext(), "starting broadcast", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), AppBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            } else {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}