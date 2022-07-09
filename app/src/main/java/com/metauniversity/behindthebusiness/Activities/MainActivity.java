package com.metauniversity.behindthebusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.metauniversity.behindthebusiness.Fragments.BusinessProfileFragment;
import com.metauniversity.behindthebusiness.Fragments.BusinessVideoUploadFragment;
import com.metauniversity.behindthebusiness.Fragments.HomeFragment;
import com.metauniversity.behindthebusiness.Fragments.IndividualProfileFragment;
import com.metauniversity.behindthebusiness.Fragments.MapSearchFragment;
import com.metauniversity.behindthebusiness.Fragments.LocationChangeFragment;
import com.metauniversity.behindthebusiness.R;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    public static final boolean isBusiness = (boolean) ParseUser.getCurrentUser().get("isBusiness");
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        if (isBusiness) {
            bottomNavigation = findViewById(R.id.businessBottomNavigation);
        } else {
            bottomNavigation = findViewById(R.id.individualBottomNavigation);
        }
        // set up the navigation tool bar
        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_dailyVideos:
                        if (isBusiness)
                            fragment = new BusinessVideoUploadFragment();
                        else {
                            Intent intent = new Intent(MainActivity.this, CustomerReviewActivity.class);
                            startActivity(intent);
                            fragment = new HomeFragment();
                        }
                        break;
                    case R.id.action_profile:
                        if (isBusiness)
                            fragment = new BusinessProfileFragment();
                        else
                            fragment = new IndividualProfileFragment();
                        break;
                    case R.id.action_mapSearch:
                        fragment = new MapSearchFragment();
                        break;
                    default:
                        fragment = new HomeFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flSubContainer, fragment).commit();
                return true;
            }
        });
        // set default selection
        bottomNavigation.setSelectedItemId(R.id.action_home);
    }

}