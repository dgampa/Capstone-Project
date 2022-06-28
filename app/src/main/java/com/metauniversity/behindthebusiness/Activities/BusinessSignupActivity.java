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
import com.parse.SignUpCallback;

public class BusinessSignupActivity extends AppCompatActivity {
    private static final String KEY_BUSINESS_USER = "businessUser";
    private static final String KEY_ONLINE = "isOnline";
    // private instance variables for layout fields
    private Button btnNext;
    private EditText etBusinessName;
    private EditText etUsername;
    private EditText etPassword;
    private RadioButton rbPhysical;
    private RadioButton rbOnline;
    private RadioButton rbBoth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup);
        getSupportActionBar().hide();

        etBusinessName = findViewById(R.id.etBusinessName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnNext = findViewById(R.id.btnNext);
        rbPhysical = findViewById(R.id.rbPhysical);
        rbOnline = findViewById(R.id.rbOnline);
        rbBoth = findViewById(R.id.rbBoth);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etBusinessName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                createBusAccount(name, username, password);
                boolean isOnline = false;
                if (rbOnline.isChecked()){
                    isOnline = true;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                setBusinessUser(currentUser, isOnline);
                // fire an intent to the next LoginPage
                jumpToPage2();
                Toast.makeText(BusinessSignupActivity.this, "Please complete additional information", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setBusinessUser( ParseUser currentUser, Boolean isOnline) {
        ParseObject businessUser = ParseObject.create("BusinessUser");
        businessUser.put(KEY_ONLINE, isOnline);
    }

    protected void createBusAccount(String name, String username, String password) {

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
            }
        });
    }

    private void jumpToPage2() {
        Intent intent = new Intent(this, BusinessSignup2Activity.class);
        startActivity(intent);
    }


}