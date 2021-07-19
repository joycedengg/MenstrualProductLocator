package com.example.menstrualproductlocator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public final class Utils {
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
}
