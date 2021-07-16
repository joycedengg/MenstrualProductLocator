package com.example.menstrualproductlocator;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.google.android.gms.maps.model.LatLng;

@ParseClassName("supply")
public class Supply extends ParseObject {

    public static final String KEY_BUILDING = "building";
    public static final String KEY_LOCATION = "location";

    public Supply() {

    }
    public String getSupplyBuilding() {
        return getString(KEY_BUILDING);
    }
    public void setSupplyBuilding(String building) {
        put(KEY_BUILDING, building);
    }
    public ParseGeoPoint getSupplyParseGeoPoint() {
        return getParseGeoPoint(KEY_LOCATION);
    }
    public void setSupplyLocation(ParseGeoPoint location) {
        put(KEY_LOCATION, location);
    }
    public LatLng getSupplyLatLng() {
        return new LatLng(getParseGeoPoint(KEY_LOCATION).getLatitude(), getParseGeoPoint(KEY_LOCATION).getLongitude());
    }

}
