package com.example.menstrualproductlocator.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.menstrualproductlocator.LoginActivity;
import com.example.menstrualproductlocator.R;
import com.example.menstrualproductlocator.databinding.FragmentProfileBinding;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    TextView tvProfileEmail;
    ImageView ivProfilePic;
    Button btnLogout;

    FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvProfileEmail = binding.tvProfileEmail;
        ivProfilePic = binding.ivProfilePic;
        btnLogout = binding.btnLogout;

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void logoutUser(){
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}