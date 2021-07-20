package com.example.menstrualproductlocator;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public final class Utils {

    public PendingIntent geofencePendingIntent;
    public Context context;
    private static final int REQUEST_LOCATION = 1;


    private Utils() {
        throw new UnsupportedOperationException("Utility class shouldn't be instantiated");
    }

    public static GoogleMap showSuppliesInMap(final GoogleMap googleMap) {
        ParseQuery <ParseObject> query = ParseQuery.getQuery("supply");
        query.whereExists("location");
        query.findInBackground(new FindCallback <ParseObject> () {
            @Override public void done(List <ParseObject> supplies, ParseException e) {
                if (e == null) {
                    for (ParseObject supply : supplies) {
                        LatLng supplyLocation = new LatLng(
                                supply.getParseGeoPoint("location").getLatitude(),
                                supply.getParseGeoPoint("location").getLongitude());

                        googleMap.addMarker(new MarkerOptions()
                                .position(supplyLocation)
                                .title(supply.getString("Building"))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                }
            }
        });
        ParseQuery.clearAllCachedResults();
        return googleMap;
    }

    public static GoogleMap showRequestsInMap(final GoogleMap googleMap) {
        ParseQuery <ParseObject> query = ParseQuery.getQuery("request");
        query.whereExists("location");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override public void done(List<ParseObject> requests, ParseException e) {
                if (e == null) {
                    for (ParseObject request : requests) {
                        LatLng requestLocation = new LatLng(
                                request.getParseGeoPoint("location").getLatitude(),
                                request.getParseGeoPoint("location").getLongitude());

                        googleMap.addMarker(new MarkerOptions()
                                .position(requestLocation)
                                .title(request.getString("Building"))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    }
                }
            }
        });
        ParseQuery.clearAllCachedResults();
        return googleMap;
    }

    public static void createGeofence(List<Geofence> list, LatLng location) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        list.add(new Geofence.Builder()
                .setRequestId(currentUser.getUsername() + " location")
                .setCircularRegion(location.latitude, location.longitude, (float) 100)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setNotificationResponsiveness(1000)
                .build());
    }

    public static List<Geofence> returnGeofence(List<Geofence> list) {
        return list;
    }

    public static void showCurrentUserInMap(final GoogleMap googleMap, Activity activity, LocationManager locationManager) {
        int ZOOM_SCALE = 18; //18 is the zoom scope level since it zooms close enough to place building titles on the map
        ParseGeoPoint currentUserLocation = getCurrentUserLocation(activity, locationManager);

        LatLng currentUser = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(currentUser)
                .title(ParseUser.getCurrentUser().getUsername())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUser, ZOOM_SCALE));
    }

    public static ParseGeoPoint getCurrentUserLocation(Activity activity, LocationManager locationManager) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        saveCurrentUserLocation(activity, locationManager);
        return currentUser.getParseGeoPoint("userLocation");
    }

    public static void saveCurrentUserLocation(Activity activity, LocationManager locationManager) {
        boolean hasNoFineLocationPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean hasNoCoarseLocationPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;

        if (hasNoFineLocationPermission && hasNoCoarseLocationPermission) {
            ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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
}
