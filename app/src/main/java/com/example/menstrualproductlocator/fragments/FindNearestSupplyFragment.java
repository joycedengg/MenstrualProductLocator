package com.example.menstrualproductlocator.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.menstrualproductlocator.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class FindNearestSupplyFragment extends DialogFragment {

    TextInputLayout tilSearchBuilding;
    AutoCompleteTextView actSearchBuilding;
    ProgressBar progressBar;

    public FindNearestSupplyFragment() {

    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
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
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        ArrayAdapter<String> buildingsAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, getArray());
        actSearchBuilding.setAdapter(buildingsAdapter);
        actSearchBuilding.setThreshold(1);

        actSearchBuilding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Building: " + buildingsAdapter.getItem(position) + position, Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putInt("index", position);
                progressBar.setVisibility(View.VISIBLE);

                ValueAnimator animator = ValueAnimator.ofInt(0, progressBar.getMax());
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        progressBar.setProgress((Integer) animation.getAnimatedValue());
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        FindNearestSupplyFragment.super.dismiss();
                        dismiss();
                        FoundNearestSupplyFragment foundNearestSupplyFragment = new FoundNearestSupplyFragment();
                        foundNearestSupplyFragment.setArguments(bundle);

                        foundNearestSupplyFragment.show(getFragmentManager(), "fragment_foundNearestSupply");                    }
                });
                animator.start();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_find_nearest_supply, container, false);
    }

    public static ArrayList<String> getArray() {
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
        return buildings;
    }
}