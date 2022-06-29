package com.metauniversity.behindthebusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.metauniversity.behindthebusiness.Fragments.BusinessProfileFragment;
import com.metauniversity.behindthebusiness.Fragments.DailyVideosFragment;
import com.metauniversity.behindthebusiness.Fragments.HomeFragment;
import com.metauniversity.behindthebusiness.Fragments.IndividualProfileFragment;
import com.metauniversity.behindthebusiness.Fragments.MapSearchFragment;
import com.metauniversity.behindthebusiness.R;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private Toolbar topAppBar;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        topAppBar = findViewById(R.id.topAppBar);
        getSupportActionBar().setCustomView(topAppBar);
        // set up the top app bar
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set up drop down menu with logout option
                logout();
            }
        });
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // set up search
                return true;
            }
        });
        // set up the navigation tool bar
        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                boolean isBusiness = (boolean) ParseUser.getCurrentUser().get("isBusiness");
                switch (item.getItemId()) {
                    case R.id.action_dailyVideos:
                        fragment = new DailyVideosFragment();
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
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // set default selection
        bottomNavigation.setSelectedItemId(R.id.action_home);
    }

    public void logout() {
        ParseUser.logOut();
        // explicit intent to return to login page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}