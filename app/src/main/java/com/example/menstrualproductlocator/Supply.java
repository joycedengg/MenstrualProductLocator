package com.example.menstrualproductlocator;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseClassName;

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
    public ParseGeoPoint getSupplyLocation() {
        return getParseGeoPoint(KEY_LOCATION);
    }
    public void setSupplyLocation(ParseGeoPoint location) {
        put(KEY_LOCATION, location);
    }

}
