package com.metauniversity.behindthebusiness.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.metauniversity.behindthebusiness.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class BusinessSignupActivity extends AppCompatActivity {
    private static final String KEY_BUSINESS_USER = "businessUser";
    private static final String KEY_ONLINE = "isOnline";
    // private instance variables for layout fields
    private Button btnNext;
    private EditText etBusinessName;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup);

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
                try {
                    createBusinessAccount(name, username, password);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                jumpToPage2();
                Toast.makeText(BusinessSignupActivity.this, "Please complete additional information", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void createBusinessAccount(String name, String username, String password) throws ParseException {

        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.put("Name", name);
        user.setUsername(username);
        user.setPassword(password);
        // Set custom properties
        user.put("isBusiness", true);
        // Invoke signUp
        user.signUp();
    }

    private void jumpToPage2() {
        Intent intent = new Intent(this, BusinessSignup2Activity.class);
        startActivity(intent);
    }


}