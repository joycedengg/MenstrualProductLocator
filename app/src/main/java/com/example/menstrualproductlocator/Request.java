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

    public void showAlertDialogForRequest(Context context, final LatLng point, GoogleMap googleMap, Request request) {
        View messageView = LayoutInflater.from(context).inflate(R.layout.log_request_item, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(messageView);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BitmapDescriptor defaultMarker =
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                        String requestBuilding = ((EditText) alertDialog.findViewById(R.id.etBuilding)).getText().toString();
                        request.setRequestBuilding(requestBuilding);
                        String requestProductType = ((EditText) alertDialog.findViewById(R.id.etProductType)).getText().toString();
                        request.setRequestProductType(requestProductType);
                        request.saveInBackground();

                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title("Request")
                                .snippet(requestProductType)
                                .icon(defaultMarker));
                    }
                });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                });

        alertDialog.show();
    }
}
