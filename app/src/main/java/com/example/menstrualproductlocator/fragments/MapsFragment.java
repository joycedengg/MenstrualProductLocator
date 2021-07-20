package com.example.menstrualproductlocator.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.menstrualproductlocator.R;
import com.example.menstrualproductlocator.Supply;
import com.example.menstrualproductlocator.Request;
import com.example.menstrualproductlocator.Utils;
import com.example.menstrualproductlocator.databinding.FragmentMapsBinding;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment {

    private FragmentMapsBinding binding;
    private Button btnLogSupply;
    private Button btnRequestProduct;
    public static final String TAG = "Map Fragment";
//    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    private static List<Geofence> geofenceList = new ArrayList<>();


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            Utils.getCurrentUserLocation(getActivity(), locationManager);
            Utils.saveCurrentUserLocation(getActivity(), locationManager);
            Utils.showCurrentUserInMap(googleMap, getActivity(), locationManager);
            Utils.showSuppliesInMap(googleMap);
            Utils.showRequestsInMap(googleMap);

            btnLogSupply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Supply supply = new Supply();
                    supply.setSupplyBuilding("test");
                    supply.setSupplyLocation(Utils.getCurrentUserLocation(getActivity(), locationManager));
                    supply.saveInBackground();
                    Utils.showSuppliesInMap(googleMap);
                }
            });
            btnRequestProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Request request = new Request(false);
                    request.setRequestLocation(Utils.getCurrentUserLocation(getActivity(), locationManager));
                    request.showAlertDialogForRequest(getContext(), request.getRequestLatLng(), googleMap, request);
                    request.saveInBackground();

                    Utils.showRequestsInMap(googleMap);
                    Utils.createGeofence(geofenceList, request.getRequestLatLng());
                    Log.i(TAG, "Geofence: " + Utils.returnGeofence(geofenceList));

                }
            });

            Log.i(TAG, "Location: " + Utils.getCurrentUserLocation(getActivity(), locationManager));
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
}