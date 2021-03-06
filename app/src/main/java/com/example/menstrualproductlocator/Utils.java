package com.example.menstrualproductlocator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public final class Utils {

    public Context context;
    public static final String TAG = "utils";
    public static final int REQUEST_LOCATION = 1;
    public static String ID = "ID";
    public static final int RADIUS = 50; //goefence radius is 50m
    public static Circle circle;

    private Utils() {
        throw new UnsupportedOperationException("Utility class shouldn't be instantiated");
    }

    /**
     * Displays markers on the map corresponding to supplies in Parse
     *
     * @param googleMap map that the markers are being added to
     * @return the map that is being edited
     */
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
                                .title(supply.getString("building"))
//                                .snippet(supply.getString("productType"))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                }
            }
        });
        ParseQuery.clearAllCachedResults();
        return googleMap;
    }

    /**
     * Displays markers on the map corresponding to requests in Parse
     *
     * @param googleMap map that the markers are being added to
     * @return the map that is being edited
     */
    public static GoogleMap showRequestsInMap(final GoogleMap googleMap, GeofenceHelper geofenceHelper, GeofencingClient geofencingClient, Context context) {
        ParseQuery <ParseObject> query = ParseQuery.getQuery("request");
        query.whereDoesNotExist("isCompleted");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override public void done(List<ParseObject> geofences, ParseException e) {
                if (e == null) {
                    for (ParseObject uncompletedRequest : geofences) {
                        LatLng requestLocation = new LatLng(
                                uncompletedRequest.getParseGeoPoint("location").getLatitude(),
                                uncompletedRequest.getParseGeoPoint("location").getLongitude());
                        addGeofence(requestLocation, RADIUS, geofenceHelper, geofencingClient);
                        addCircle(requestLocation, RADIUS, googleMap);

                        googleMap.addMarker(new MarkerOptions()
                                .position(requestLocation)
                                .title("Request " + geofences.indexOf(uncompletedRequest))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));


                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                String[] stringsIndex = marker.getTitle().split(" ");
                                if (stringsIndex[0].equals("Request")) {
                                    View messageView = LayoutInflater.from(context).inflate(R.layout.view_request_item, null);
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                    alertDialogBuilder.setView(messageView);

                                    TextView building = messageView.findViewById(R.id.tvViewBuilding);
                                    TextView productType = messageView.findViewById(R.id.tvViewProductType);
                                    String tvBuilding = geofences.get(Integer.parseInt(stringsIndex[1])).getString("building");
                                    String tvProductType = geofences.get(Integer.parseInt(stringsIndex[1])).getString("productType");
                                    building.setText(tvBuilding);
                                    productType.setText(tvProductType);

                                    final AlertDialog alertDialog = alertDialogBuilder.create();

                                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Accept",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    marker.remove();
                                                    try {
                                                        geofences.get(Integer.parseInt(stringsIndex[1])).delete();
                                                    } catch (ParseException parseException) {
                                                        parseException.printStackTrace();
                                                    }
                                                }
                                            });

                                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                                            });

                                    alertDialog.show();
                                }
                                return true;
                            }
                        });
                    }
                }
                ParseQuery.clearAllCachedResults();
            }
        });
        ParseQuery.clearAllCachedResults();
        return googleMap;
    }

    /**
     * Adds a red circle on the map to visualize a geofence
     *
     * @param location geofence's location
     * @param radius geofence's radius
     * @param map map that the circle is being added to
     */
    public static void addCircle(LatLng location, float radius, GoogleMap map) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(location);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0 ,0));
        circleOptions.fillColor(Color.argb(64, 255, 0 ,0));
        circleOptions.strokeWidth(4);
        circle = map.addCircle(circleOptions);
    }

    /**
     * Zooms the map onto the user's current location
     *
     * @param googleMap map that is being zoomed on
     * @param activity activity that the method is being called in
     * @param locationManager LocationManager being used to access user's location
     */
    public static void showCurrentUserInMap(final GoogleMap googleMap, Activity activity, LocationManager locationManager) {
        int ZOOM_SCALE = 18; //18 is the zoom scope level since it zooms close enough to place building titles on the map
        ParseGeoPoint currentUserLocation = getCurrentUserLocation(activity, locationManager);

        LatLng currentUser = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUser, ZOOM_SCALE));
    }

    /**
     * Returns the user's current location
     *
     * @param activity Activity being called in
     * @param locationManager LocationManager being used ot access user's location
     * @return user's current location as a ParseGeoPoint
     */
    public static ParseGeoPoint getCurrentUserLocation(Activity activity, LocationManager locationManager) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        saveCurrentUserLocation(activity, locationManager);
        return currentUser.getParseGeoPoint("userLocation");
    }

    /**
     * Asks user for location permissions, retrieves user's current location, then saves it in Parse
     *
     * @param activity Activity being called in
     * @param locationManager LocationManager being used ot access user's location
     */
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

                //commented out for running purposes
//                if (currentUser != null) {
//                    currentUser.put("userLocation", currentUserLocation);
//                    currentUser.saveInBackground();
//                }
            }
        }
    }

    /**
     * Displays an AlertDialog to log a request on the map, activates Geofence, and shows a marker on the map
     *
     * @param context
     * @param point
     * @param googleMap
     * @param request
     * @param geofenceHelper
     * @param geofencingClient
     */
    public static void showAlertDialogToMakeRequest(Context context, final LatLng point, GoogleMap googleMap, Request request, GeofenceHelper geofenceHelper, GeofencingClient geofencingClient) {
        View messageView = LayoutInflater.from(context).inflate(R.layout.log_request_item, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(messageView);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String requestBuilding = ((EditText) alertDialog.findViewById(R.id.etBuilding)).getText().toString();
                        request.setRequestBuilding(requestBuilding);
                        String requestProductType = ((EditText) alertDialog.findViewById(R.id.etProductType)).getText().toString();
                        request.setRequestProductType(requestProductType);
                        request.saveInBackground();
                        Utils.addGeofence(request.getRequestLatLng(), RADIUS, geofenceHelper, geofencingClient);
                        Utils.addCircle(request.getRequestLatLng(), RADIUS, googleMap);
                        Utils.showRequestsInMap(googleMap, geofenceHelper, geofencingClient, context);
                    }
                });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                });

        alertDialog.show();
    }

    /**
     * Activates a Geofence around a request
     *
     * @param locaton Geofence's location
     * @param radius Geofence's radius
     * @param geofenceHelper Stores geofences, adds them to the Google API, and informs about the status of the operation.
     * @param geofencingClient Geofencing client used to add the geofence
     */
    @SuppressLint("MissingPermission")
    public static void addGeofence(LatLng locaton, float radius, GeofenceHelper geofenceHelper, GeofencingClient geofencingClient) {
        Geofence geofence = geofenceHelper.getGeofence(ID, locaton, radius,Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Geofence added with ID: " + ID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
    }
}
