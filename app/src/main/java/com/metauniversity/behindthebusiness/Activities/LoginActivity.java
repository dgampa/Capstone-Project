package com.metauniversity.behindthebusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.metauniversity.behindthebusiness.R;
import com.metauniversity.behindthebusiness.Fragments.UserTypeFragment;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    // private instance variables for layout fields
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String username = etUsername.getText().toString();
//                String password = etPassword.getText().toString();
//                createAccount(username, password);
                showUserTypeDialog();
            }
        });
    }

    private void showUserTypeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        UserTypeFragment userTypeFragment = new UserTypeFragment();
        userTypeFragment.show(fm, "fragment_user_type");
    }

    // helper method for onCreate to verify Username and Password
    private void loginUser(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e!=null){
                    Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                // navigate to the main activity if the user has signed in properly
                goToMainActivity();
            }
        });

    }

    // helper method for loginUser
    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    // helper method onCreate to create a new user account
    private void createAccount(String username, String password){
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
//        user.setEmail("email@example.com");
//        // Set custom properties
//        user.put("phone", "650-253-0000");
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(LoginActivity.this, "Issue with account creation", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginUser(username, password);
                Toast.makeText(LoginActivity.this, "Welcome to Instagram", Toast.LENGTH_SHORT).show();
            }
        });
        goToMainActivity();
    }
}
