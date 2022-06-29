package com.metauniversity.behindthebusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.metauniversity.behindthebusiness.R;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class BusinessSignup2Activity extends AppCompatActivity{

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_SocialFb = "socialFacebook";
    private static final String KEY_RATING = "rating";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_ONLINE = "isOnline";
    private static final String KEY_PROFILEPIC = "profilePic";
    Button btnSelectPhoto;
    ImageView ivProfilePic;
    private File imageFile;
    EditText etDescription;
    TextInputLayout tilCategory;
    AutoCompleteTextView actCategory;
    ArrayList<String> arrayList_categories;
    ArrayAdapter<String> arrayAdapter_categories;
    EditText etLocation;
    EditText etSocialFb;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup2);

        btnSelectPhoto = (Button) findViewById(R.id.btnSelectPhoto);
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        etDescription = (EditText) findViewById(R.id.etDescription);
        tilCategory = (TextInputLayout) findViewById(R.id.tilCategory);
        actCategory = (AutoCompleteTextView) findViewById(R.id.actCategory);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etSocialFb = (EditText) findViewById(R.id.etSocialFB);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

//        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectImage();
//            }
//        });

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
        ParseObject businessUser = ParseObject.createWithoutData("BusinessUser", ParseUser.getCurrentUser().getObjectId());
        businessUser.put(KEY_PROFILEPIC, new ParseFile(imageFile));
        businessUser.put(KEY_DESCRIPTION, etDescription.getText().toString());
        businessUser.put(KEY_CATEGORY, tilCategory.getEditText().getText().toString());
        businessUser.put(KEY_LOCATION, etLocation.getText().toString());
        businessUser.put(KEY_SocialFb, etSocialFb.getText().toString());
    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessSignup2Activity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageFile = getPhotoFileUri("image.jpg");
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                if (selectedImage != null) {
                    Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePath[0]);
                        String picturePath = cursor.getString(columnIndex);
                        ivProfilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                        cursor.close();
                    }
                }
                Log.w("path of image from gallery......******************.........", filePath + "");
            }
        }
    }

    private File getPhotoFileUri(String s) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "BusinessSignup2Activity");
        // create the storage directory if it does not exit
        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("BusinessSignup2Activity", "failed to create directory");
        }
        // return the file target for the photo based on the file name
        File file = new File(mediaStorageDir.getPath() + File.separator + "image.jpg");
        return file;
    }

    // helper method that fires an intent to the loginActivity
    protected void jumpToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}