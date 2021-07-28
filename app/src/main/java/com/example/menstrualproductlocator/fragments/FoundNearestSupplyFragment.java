package com.example.menstrualproductlocator.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.menstrualproductlocator.NearestSupplyAlgorithm.Edge;
import com.example.menstrualproductlocator.NearestSupplyAlgorithm.FindNearestSupply;
import com.example.menstrualproductlocator.NearestSupplyAlgorithm.Graph;
import com.example.menstrualproductlocator.NearestSupplyAlgorithm.Vertex;
import com.example.menstrualproductlocator.R;
import com.example.menstrualproductlocator.fragments.FindNearestSupplyFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class FoundNearestSupplyFragment extends DialogFragment {

    TextView tvNearestSupply;
    TextView tvFoundSupplyTitle;

    public FoundNearestSupplyFragment() {

    }

    public static FoundNearestSupplyFragment newInstance(String title) {
        FoundNearestSupplyFragment frag = new FoundNearestSupplyFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNearestSupply = (TextView) view.findViewById(R.id.tvNearestSupply);
        tvFoundSupplyTitle = (TextView) view.findViewById(R.id.tvFoundSupplyTitle);
        Bundle bundle = this.getArguments();
        int index = bundle.getInt("index");
        index += 65;
        char vertex = (char)index;
        tvNearestSupply.setText(FindNearestSupply.returnMap(new Vertex<>(vertex), createGraph()));
    }

    public Graph<Character> createGraph() {
        Set<Vertex<Character>> vertices = new HashSet<Vertex<Character>>();
//        vertices.add(new Vertex<>("CULC"));
        vertices.add(new Vertex<>('A'));
//        vertices.add(new Vertex<>("McCamish Arena"));
        vertices.add(new Vertex<>('B'));
//        vertices.add(new Vertex<>("Scheller College of Business"));
        vertices.add(new Vertex<>('C'));
//        vertices.add(new Vertex<>("Klaus College of Computing"));
        vertices.add(new Vertex<>('D'));
//        vertices.add(new Vertex<>("Van Leer Engineering Building"));
        vertices.add(new Vertex<>('E'));
//        vertices.add(new Vertex<>("Kendeda"));
        vertices.add(new Vertex<>('F'));
//        vertices.add(new Vertex<>("GTRI North"));
        vertices.add(new Vertex<>('G'));
//        vertices.add(new Vertex<>("West Village Dining Hall"));
        vertices.add(new Vertex<>('H'));
//        vertices.add(new Vertex<>("Campus Recreation Center"));
        vertices.add(new Vertex<>('I'));
//        vertices.add(new Vertex<>("Stamps Health Center"));
        vertices.add(new Vertex<>('J'));
//        vertices.add(new Vertex<>("Exhibition Hall"));
        vertices.add(new Vertex<>('K'));
//        vertices.add(new Vertex<>("Ferst Center for the Arts"));
        vertices.add(new Vertex<>('L'));
//        vertices.add(new Vertex<>("Tech Tower"));
        vertices.add(new Vertex<>('M'));
//        vertices.add(new Vertex<>("GT Library"));
        vertices.add(new Vertex<>('N'));
//        vertices.add(new Vertex<>("North Avenue Dining Hall"));
        vertices.add(new Vertex<>('O'));

        Set<Edge<Character>> edges = new LinkedHashSet<Edge<Character>>();
        edges.add(new Edge<>(new Vertex<>('A'), new Vertex<>('N'), 1));
        edges.add(new Edge<>(new Vertex<>('N'), new Vertex<>('A'), 1));
        edges.add(new Edge<>(new Vertex<>('A'), new Vertex<>('E'), 4));
        edges.add(new Edge<>(new Vertex<>('E'), new Vertex<>('A'), 4));
        edges.add(new Edge<>(new Vertex<>('A'), new Vertex<>('D'), 6));
        edges.add(new Edge<>(new Vertex<>('D'), new Vertex<>('A'), 6));
        edges.add(new Edge<>(new Vertex<>('N'), new Vertex<>('D'), 7));
        edges.add(new Edge<>(new Vertex<>('D'), new Vertex<>('N'), 7));
        edges.add(new Edge<>(new Vertex<>('N'), new Vertex<>('M'), 6));
        edges.add(new Edge<>(new Vertex<>('M'), new Vertex<>('N'), 6));
        edges.add(new Edge<>(new Vertex<>('M'), new Vertex<>('O'), 5));
        edges.add(new Edge<>(new Vertex<>('O'), new Vertex<>('M'), 5));
        edges.add(new Edge<>(new Vertex<>('O'), new Vertex<>('C'), 13));
        edges.add(new Edge<>(new Vertex<>('C'), new Vertex<>('O'), 13));
        edges.add(new Edge<>(new Vertex<>('C'), new Vertex<>('B'), 15));
        edges.add(new Edge<>(new Vertex<>('B'), new Vertex<>('C'), 15));
        edges.add(new Edge<>(new Vertex<>('B'), new Vertex<>('D'), 8));
        edges.add(new Edge<>(new Vertex<>('D'), new Vertex<>('B'), 8));
        edges.add(new Edge<>(new Vertex<>('B'), new Vertex<>('G'), 11));
        edges.add(new Edge<>(new Vertex<>('G'), new Vertex<>('B'), 11));
        edges.add(new Edge<>(new Vertex<>('G'), new Vertex<>('F'), 6));
        edges.add(new Edge<>(new Vertex<>('F'), new Vertex<>('G'), 6));
        edges.add(new Edge<>(new Vertex<>('F'), new Vertex<>('D'), 7));
        edges.add(new Edge<>(new Vertex<>('D'), new Vertex<>('F'), 7));
        edges.add(new Edge<>(new Vertex<>('F'), new Vertex<>('L'), 6));
        edges.add(new Edge<>(new Vertex<>('L'), new Vertex<>('F'), 6));
        edges.add(new Edge<>(new Vertex<>('F'), new Vertex<>('H'), 9));
        edges.add(new Edge<>(new Vertex<>('H'), new Vertex<>('F'), 9));
        edges.add(new Edge<>(new Vertex<>('H'), new Vertex<>('G'), 8));
        edges.add(new Edge<>(new Vertex<>('G'), new Vertex<>('H'), 8));
        edges.add(new Edge<>(new Vertex<>('I'), new Vertex<>('H'), 9));
        edges.add(new Edge<>(new Vertex<>('H'), new Vertex<>('I'), 9));
        edges.add(new Edge<>(new Vertex<>('E'), new Vertex<>('D'), 6));
        edges.add(new Edge<>(new Vertex<>('D'), new Vertex<>('E'), 6));
        edges.add(new Edge<>(new Vertex<>('E'), new Vertex<>('L'), 5));
        edges.add(new Edge<>(new Vertex<>('L'), new Vertex<>('E'), 5));
        edges.add(new Edge<>(new Vertex<>('D'), new Vertex<>('C'), 11));
        edges.add(new Edge<>(new Vertex<>('C'), new Vertex<>('D'), 11));
        edges.add(new Edge<>(new Vertex<>('O'), new Vertex<>('D'), 12));
        edges.add(new Edge<>(new Vertex<>('D'), new Vertex<>('O'), 12));
        edges.add(new Edge<>(new Vertex<>('I'), new Vertex<>('H'), 9));
        edges.add(new Edge<>(new Vertex<>('H'), new Vertex<>('I'), 9));
        edges.add(new Edge<>(new Vertex<>('I'), new Vertex<>('F'), 11));
        edges.add(new Edge<>(new Vertex<>('F'), new Vertex<>('I'), 11));
        edges.add(new Edge<>(new Vertex<>('I'), new Vertex<>('K'), 3));
        edges.add(new Edge<>(new Vertex<>('K'), new Vertex<>('I'), 3));
        edges.add(new Edge<>(new Vertex<>('I'), new Vertex<>('J'), 2));
        edges.add(new Edge<>(new Vertex<>('J'), new Vertex<>('I'), 2));
        edges.add(new Edge<>(new Vertex<>('J'), new Vertex<>('K'), 2));
        edges.add(new Edge<>(new Vertex<>('K'), new Vertex<>('J'), 2));
        edges.add(new Edge<>(new Vertex<>('K'), new Vertex<>('L'), 5));
        edges.add(new Edge<>(new Vertex<>('L'), new Vertex<>('K'), 5));
        edges.add(new Edge<>(new Vertex<>('M'), new Vertex<>('K'), 12));
        edges.add(new Edge<>(new Vertex<>('K'), new Vertex<>('M'), 12));

        return new Graph<Character>(vertices, edges);
    }

    private Graph<Character> createUndirectedGraph() {
        Set<Vertex<Character>> vertices = new HashSet<>();
        for (int i = 65; i <= 70; i++) {
            vertices.add(new Vertex<Character>((char) i));
        }

        Set<Edge<Character>> edges = new LinkedHashSet<>();
        edges.add(new Edge<>(new Vertex<>('A'), new Vertex<>('B'), 7));
        edges.add(new Edge<>(new Vertex<>('B'), new Vertex<>('A'), 7));
        edges.add(new Edge<>(new Vertex<>('A'), new Vertex<>('C'), 5));
        edges.add(new Edge<>(new Vertex<>('C'), new Vertex<>('A'), 5));
        edges.add(new Edge<>(new Vertex<>('C'), new Vertex<>('D'), 2));
        edges.add(new Edge<>(new Vertex<>('D'), new Vertex<>('C'), 2));
        edges.add(new Edge<>(new Vertex<>('A'), new Vertex<>('D'), 4));
        edges.add(new Edge<>(new Vertex<>('D'), new Vertex<>('A'), 4));
        edges.add(new Edge<>(new Vertex<>('D'), new Vertex<>('E'), 1));
        edges.add(new Edge<>(new Vertex<>('E'), new Vertex<>('D'), 1));
        edges.add(new Edge<>(new Vertex<>('B'), new Vertex<>('E'), 3));
        edges.add(new Edge<>(new Vertex<>('E'), new Vertex<>('B'), 3));
        edges.add(new Edge<>(new Vertex<>('B'), new Vertex<>('F'), 8));
        edges.add(new Edge<>(new Vertex<>('F'), new Vertex<>('B'), 8));
        edges.add(new Edge<>(new Vertex<>('E'), new Vertex<>('F'), 6));
        edges.add(new Edge<>(new Vertex<>('F'), new Vertex<>('E'), 6));

        return new Graph<Character>(vertices, edges);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_found_nearest_supply, container, false);
    }
}

