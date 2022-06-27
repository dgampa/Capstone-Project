package com.metauniversity.behindthebusiness.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.metauniversity.behindthebusiness.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class BusinessSignupActivity extends AppCompatActivity {
    // private instance variables for layout fields
    private Button btnNext;
    private EditText etBusinessName;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup);
        getSupportActionBar().hide();

        etBusinessName = findViewById(R.id.etBusinessName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etBusinessName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                createBusAccount(name, username, password);
            }
        });

    }

    protected void createBusAccount(String name, String username, String password){

        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.put("Name", name);
        user.setUsername(username);
        user.setPassword(password);
        // Set custom properties
        user.put("isBusiness", true);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(BusinessSignupActivity.this, "Issue with account creation", Toast.LENGTH_SHORT).show();
                    return;
                }
                // fire an intent to the next LoginPage
                jumpToPage2();
                Toast.makeText(BusinessSignupActivity.this, "Please complete additional information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void jumpToPage2() {
        Intent intent = new Intent(this, BusinessSignup2Activity.class);
        startActivity(intent);
    }


}