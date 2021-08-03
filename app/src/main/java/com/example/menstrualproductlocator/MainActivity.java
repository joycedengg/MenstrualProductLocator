package com.example.menstrualproductlocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.menstrualproductlocator.databinding.ActivityMainBinding;
import com.example.menstrualproductlocator.fragments.ChatFragment;
import com.example.menstrualproductlocator.fragments.MapsFragment;
import com.example.menstrualproductlocator.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {

    private AnimatedBottomBar bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setTabEnabledById(R.id.action_map, true);
        bottomNavigationView.setTabEnabledById(R.id.action_chat, true);
        bottomNavigationView.setTabEnabledById(R.id.action_profile, true);


        bottomNavigationView.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NotNull AnimatedBottomBar.Tab newTab) {
                Fragment fragment = null;
                switch (newTab.getId()) {
                    case R.id.action_map:
                        fragment = new MapsFragment();
                        break;
                    case R.id.action_chat:
                        fragment = new ChatFragment();
                        break;
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            }

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {

            }
        });

        bottomNavigationView.selectTabById(R.id.action_map, true);

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment = null;
//                switch (item.getItemId()) {
//                    case R.id.action_map:
//                        fragment = new MapsFragment();
//                        break;
//                    case R.id.action_chat:
//                        fragment = new ChatFragment();
//                        break;
//                    case R.id.action_profile:
//                        fragment = new ProfileFragment();
//                        break;
//                    default:
//                        break;
//                }
//                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
//                return true;
//            }
//        });
//        bottomNavigationView.setSelectedItemId(R.id.action_map);

    }
}