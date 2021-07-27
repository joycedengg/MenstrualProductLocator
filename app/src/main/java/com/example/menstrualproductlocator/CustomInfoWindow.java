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

    @Override
    public View getInfoContents(Marker marker) {
        View view = inflater.inflate(R.layout.custom_info_window, null);
        TextView building = (TextView) view.findViewById(R.id.tvSupplyBuilding);
        building.setText(marker.getTitle());

        TextView productType = (TextView) view.findViewById(R.id.tvSupplyProductType);
        productType.setText(marker.getSnippet());
        return view;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}
