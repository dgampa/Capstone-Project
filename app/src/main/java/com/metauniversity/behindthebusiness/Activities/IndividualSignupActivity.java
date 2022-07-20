package com.metauniversity.behindthebusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.metauniversity.behindthebusiness.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;

public class IndividualSignupActivity extends AppCompatActivity {

    // private instance variables for layout fields
    private EditText etFullName;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnSubmit;
    // all checkboxes for the different category options
    private CheckBox optRestaurants;
    private CheckBox optFood;
    private CheckBox optEventPlanningAndServices;
    private CheckBox optHomeServices;
    private CheckBox optHotelAndTravel;
    private CheckBox optNightlife;
    private CheckBox optLocalFlavor;
    private CheckBox optPets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_signup);

        etFullName = findViewById(R.id.etFullName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSubmit = findViewById(R.id.btnSubmit);

        optRestaurants = findViewById(R.id.optRestaurants);
        optFood = findViewById(R.id.optFood);
        optEventPlanningAndServices = findViewById(R.id.optEventPlanningAndServices);
        optHomeServices = findViewById(R.id.optEventPlanningAndServices);
        optHotelAndTravel = findViewById(R.id.optHotelAndTravel);
        optNightlife = findViewById(R.id.optNightlife);
        optLocalFlavor = findViewById(R.id.optLocalFlavor);
        optPets = findViewById(R.id.optPets);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etFullName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                createAccount(name, username, password);
            }
        });
    }

    // helper method onCreate to create a new user account
    protected void createAccount(String name, String username, String password) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.put("Name", name);
        user.setUsername(username);
        user.setPassword(password);
        // Set custom properties
        ArrayList<String> categories = new ArrayList<>();
        if (optRestaurants.isChecked()) {
            categories.add("Restaurants");
        }
        if (optFood.isChecked()) {
            categories.add("Food");
        }
        if (optEventPlanningAndServices.isChecked()) {
            categories.add("Event Planning and Services");
        }
        if (optHomeServices.isChecked()) {
            categories.add("Home Services");
        }
        if (optHotelAndTravel.isChecked()) {
            categories.add("Hotel and Travel");
        }
        if (optNightlife.isChecked()) {
            categories.add("Nightlife");
        }
        if (optLocalFlavor.isChecked()) {
            categories.add("Local Flavor");
        }
        if (optPets.isChecked()) {
            categories.add("Pets");
        }
        user.put("favoriteCategories", categories);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(IndividualSignupActivity.this, "Issue with account creation", Toast.LENGTH_SHORT).show();
                    return;
                }
                // fire an intent to the loginActivity and have user sign in with new credentials
                ParseUser.logOut();
                jumpToLogin();
                Toast.makeText(IndividualSignupActivity.this, "Account Created! Please Sign in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // helper method that fires an intent to the loginActivity
    protected void jumpToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}