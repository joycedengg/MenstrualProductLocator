package com.example.menstrualproductlocator;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("request")
public class Request extends ParseObject {

    boolean isCompleted;

    public static final String KEY_BUILDING = "building";
    public static final String KEY_LOCATION = "location";

    public Request() {

    }

    public Request(boolean isCompleted) {
        isCompleted = this.isCompleted;
    }
    public String getRequestBuilding() {
        return getString(KEY_BUILDING);
    }
    public void setRequestBuilding(String building) {
        put(KEY_BUILDING, building);
    }
    public ParseGeoPoint getRequestParseGeoPoint() {
        return getParseGeoPoint(KEY_LOCATION);
    }
    public void setRequestLocation(ParseGeoPoint location) {
        put(KEY_LOCATION, location);
    }
    public LatLng getRequestLatLng() {
        return new LatLng(getParseGeoPoint(KEY_LOCATION).getLatitude(), getParseGeoPoint(KEY_LOCATION).getLongitude());
    }
    public void setCompleted() {
        isCompleted = true;
    }
    public boolean isComplete() {
        return isCompleted;
    }
}
