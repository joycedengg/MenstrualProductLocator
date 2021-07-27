package com.example.menstrualproductlocator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    LayoutInflater inflater;

    public CustomInfoWindow(LayoutInflater i){
        inflater = i;
    }

    // This defines the contents within the info window based on the marker
    @Override
    public View getInfoContents(Marker marker) {
        // Getting view from the layout file
        View view = inflater.inflate(R.layout.custom_info_window, null);
        // Populate fields
        TextView building = (TextView) view.findViewById(R.id.tvSupplyBuilding);
        building.setText(marker.getTitle());

        TextView productType = (TextView) view.findViewById(R.id.tvSupplyProductType);
//        productType.setText(marker.getSnippet());
        // Return info window contents
        return view;
    }

    // This changes the frame of the info window; returning null uses the default frame.
    // This is just the border and arrow surrounding the contents specified above
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}
