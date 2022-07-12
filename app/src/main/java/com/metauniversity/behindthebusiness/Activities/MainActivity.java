package com.metauniversity.behindthebusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.metauniversity.behindthebusiness.Fragments.BusinessProfileFragment;
import com.metauniversity.behindthebusiness.Fragments.BusinessHomeFragment;
import com.metauniversity.behindthebusiness.Fragments.BusinessVideoUploadFragment;
import com.metauniversity.behindthebusiness.Fragments.IndividualHomeFragment;
import com.metauniversity.behindthebusiness.Fragments.IndividualProfileFragment;
import com.metauniversity.behindthebusiness.Fragments.MapSearchFragment;
import com.metauniversity.behindthebusiness.R;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView BottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigation = findViewById(R.id.BottomNavigation);
        boolean isBusiness = (boolean) ParseUser.getCurrentUser().get("isBusiness");
        if (isBusiness) {
            BottomNavigation.inflateMenu(R.menu.business_bottom_navigation_menu);
            BottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment;
                    switch (item.getItemId()) {
                        case R.id.action_dailyVideos:
                            fragment = new BusinessVideoUploadFragment();
                            break;
                        case R.id.action_profile:
                            fragment = new BusinessProfileFragment();
                            break;
                        default:
                            fragment = new BusinessHomeFragment();
                            break;
                    }
                    fragmentManager.beginTransaction().replace(R.id.flSubContainer, fragment).commit();
                    return true;
                }
            });
        } else {
            // set up the navigation tool bar
            BottomNavigation.inflateMenu(R.menu.individual_bottom_navigation_menu);
            BottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment;
                    switch (item.getItemId()) {
                        case R.id.action_mapSearch:
                            fragment = new MapSearchFragment();
                            break;
                        case R.id.action_profile:
                            fragment = new IndividualProfileFragment();
                            break;
                        default:
                            fragment = new IndividualHomeFragment();
                            break;
                    }
                    fragmentManager.beginTransaction().replace(R.id.flSubContainer, fragment).commit();
                    return true;
                }
            });
        }
        // set default selection
        BottomNavigation.setSelectedItemId(R.id.action_home);
    }

}