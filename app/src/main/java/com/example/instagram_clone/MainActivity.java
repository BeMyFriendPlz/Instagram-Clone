package com.example.instagram_clone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.instagram_clone.Fragments.HomeFragment;
import com.example.instagram_clone.Fragments.NotificationFragment;
import com.example.instagram_clone.Fragments.ProfileFragment;
import com.example.instagram_clone.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectorFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectorFragment = new SearchFragment();
                        break;
                    case R.id.nav_add:
                        selectorFragment = null;
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                        break;
                    case R.id.nav_heart:
                        selectorFragment = new NotificationFragment();
                        break;
                    case R.id.nav_profile:
                        selectorFragment = new ProfileFragment();
                        break;
                }

                if (selectorFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();
                }
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

    }
}