package com.example.menstrualproductlocator;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("request")
public class Request extends ParseObject {

    boolean isCompleted;

    public static final String KEY_BUILDING = "building";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_PRODUCT_TYPE = "productType";

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

    public String getRequestProductType() {
        return getString(KEY_PRODUCT_TYPE);
    }

    public void setRequestProductType(String productType) {
        put(KEY_PRODUCT_TYPE, productType);
    }

    public void setCompleted() {
        isCompleted = true;
    }

    public boolean isComplete() {
        return isCompleted;
    }
}
