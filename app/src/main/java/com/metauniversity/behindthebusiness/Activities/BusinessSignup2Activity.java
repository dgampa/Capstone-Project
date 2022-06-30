package com.metauniversity.behindthebusiness.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.metauniversity.behindthebusiness.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BusinessSignup2Activity extends AppCompatActivity {
    private static final String KEY_BUSINESS_USER = "businessUser";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_SocialFb = "socialFacebook";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_ONLINE = "isOnline";
    private static final String KEY_PROFILEPIC = "profilePic";
    private Button btnSelectPhoto;
    private ImageView ivProfilePic;
    private File imageFile;
    private EditText etDescription;
    private TextInputLayout tilCategory;
    private AutoCompleteTextView actCategory;
    private ArrayList<String> arrayList_categories;
    private ArrayAdapter<String> arrayAdapter_categories;
    private EditText etLocation;
    private EditText etSocialFb;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup2);

        btnSelectPhoto = (Button) findViewById(R.id.btnSelectPhoto);
        ivProfilePic = (ImageView) findViewById(R.id.ivProfileImage);
        etDescription = (EditText) findViewById(R.id.etDescription);
        tilCategory = (TextInputLayout) findViewById(R.id.tilCategory);
        actCategory = (AutoCompleteTextView) findViewById(R.id.actCategory);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etSocialFb = (EditText) findViewById(R.id.etSocialFB);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        arrayList_categories = new ArrayList<>();
        arrayList_categories.add("Restaurants");
        arrayList_categories.add("Food");
        arrayList_categories.add("Home Services");
        arrayList_categories.add("Hotels and Travel");
        arrayList_categories.add("Local Flavor");
        arrayList_categories.add("Nightlife");
        arrayList_categories.add("Event Planning and Services");
        arrayList_categories.add("Pets");

        arrayAdapter_categories = new ArrayAdapter<>(getApplicationContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, arrayList_categories);
        actCategory.setAdapter(arrayAdapter_categories);
        actCategory.setThreshold(1);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBusinessUser();
                ParseUser.logOut();
                jumpToLogin();
                Toast.makeText(BusinessSignup2Activity.this, "Account Created! Please Sign in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBusinessUser() {
        ParseObject businessUser = new ParseObject("BusinessUser");
        businessUser.put(KEY_BUSINESS_USER, ParseUser.getCurrentUser());
        businessUser.put(KEY_DESCRIPTION, etDescription.getText().toString());
        businessUser.put(KEY_CATEGORY, tilCategory.getEditText().getText().toString());
        businessUser.put(KEY_LOCATION, etLocation.getText().toString());
        businessUser.put(KEY_SocialFb, etSocialFb.getText().toString());
        //businessUser.put(KEY_PROFILEPIC, new ParseFile(imageFile));
        // Saves the new object.
        businessUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //Save was done
                } else {
                    //Something went wrong
                    Log.e("Business Signup 2 Activity", e.getMessage());
                    Toast.makeText(BusinessSignup2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessSignup2Activity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    launchSomeActivity.launch(i);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                imageFile = getPhotoFileUri("image.jpg");
                Uri selectedImageUri = data.getData();
                Bitmap selectedImageBitmap = null;
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivProfilePic.setImageBitmap(selectedImageBitmap);
            }
        }
    });

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "BusinessSignup2Activity");
        // create the storage directory if it does not exit
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("BusinessSignup2Activity", "failed to create directory");
        }
        // return the file target for the photo based on the file name
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);
        return file;
    }

    // helper method that fires an intent to the loginActivity
    protected void jumpToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}