package com.example.menstrualproductlocator.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.menstrualproductlocator.R;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FindNearestSupplyFragment extends DialogFragment {

    TextInputLayout tilSearchBuilding;
    AutoCompleteTextView actSearchBuilding;

    public FindNearestSupplyFragment() {

    }

    public static FindNearestSupplyFragment newInstance(String title) {
        FindNearestSupplyFragment frag = new FindNearestSupplyFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tilSearchBuilding = (TextInputLayout) view.findViewById(R.id.tilSearchBuilding);
        actSearchBuilding = (AutoCompleteTextView) view.findViewById(R.id.actSearchBuilding);

        ArrayList<String> buildings = new ArrayList<>();
        buildings.add("CULC");
        buildings.add("McCamish Arena");
        buildings.add("Scheller College of Business");
        buildings.add("Klaus College of Computing");
        buildings.add("Van Leer Engineering Building");
        buildings.add("Kendeda");
        buildings.add("GTRI North");
        buildings.add("West Village Dining Hall");
        buildings.add("Campus Recreation Center");
        buildings.add("Stamps Health Center");
        buildings.add("Exhibition Hall");
        buildings.add("Ferst Center for the Arts");
        buildings.add("Tech Tower");
        buildings.add("GT Library");
        buildings.add("North Avenue Dining Hall");

        ArrayAdapter<String> buildingsAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, buildings);
        actSearchBuilding.setAdapter(buildingsAdapter);
        actSearchBuilding.setThreshold(1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_nearest_supply, container, false);
    }
}