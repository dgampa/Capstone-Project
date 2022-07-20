package com.metauniversity.behindthebusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.metauniversity.behindthebusiness.BusinessUser;
import com.metauniversity.behindthebusiness.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class BusinessSignup2Activity extends AppCompatActivity {
    //    private Button btnSelectPhoto;
//    private ImageView ivProfilePic;
//    private File imageFile;
    private EditText etDescription;
    private TextInputLayout tilCategory;
    private AutoCompleteTextView actCategory;
    private ArrayList<String> arrayListCategories;
    private ArrayAdapter<String> arrayAdapterCategories;
    private EditText etSocialFb;
    private EditText etSocialInstagram;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup2);

//        btnSelectPhoto = (Button) findViewById(R.id.btnSelectPhoto);
//        ivProfilePic = (ImageView) findViewById(R.id.ivProfileImage);
        etDescription = (EditText) findViewById(R.id.etDescription);
        tilCategory = (TextInputLayout) findViewById(R.id.tilCategory);
        actCategory = (AutoCompleteTextView) findViewById(R.id.actCategory);
        etSocialFb = (EditText) findViewById(R.id.etSocialFacebook);
        etSocialInstagram = (EditText) findViewById(R.id.etSocialInstagram);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

//        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectImage();
//            }
//        });

        arrayListCategories = new ArrayList<>();
        arrayListCategories.add("Restaurants");
        arrayListCategories.add("Food");
        arrayListCategories.add("Home Services");
        arrayListCategories.add("Hotels and Travel");
        arrayListCategories.add("Local Flavor");
        arrayListCategories.add("Nightlife");
        arrayListCategories.add("Event Planning and Services");
        arrayListCategories.add("Pets");

        arrayAdapterCategories = new ArrayAdapter<>(getApplicationContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, arrayListCategories);
        actCategory.setAdapter(arrayAdapterCategories);
        actCategory.setThreshold(1);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBusinessUser();
                jumpToLogin();
                Toast.makeText(BusinessSignup2Activity.this, "Account Created! Please Sign in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBusinessUser() {
        BusinessUser businessUser = new BusinessUser();
        businessUser.setBusinessName(ParseUser.getCurrentUser().get("Name").toString());
        businessUser.setUser(ParseUser.getCurrentUser());
        businessUser.setDescription(etDescription.getText().toString());
        businessUser.setCategory(tilCategory.getEditText().toString());
        businessUser.setSocialFB(etSocialFb.getText().toString());
        businessUser.setSocialInstagram(etSocialInstagram.getText().toString());
//      businessUser.setProfileImage(new ParseFile(imageFile));
//      Saves the new object.
        businessUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //Save was done
                    Log.i("Business Signup 2 Activity", "Business User was successfully created");
                } else {
                    //Something went wrong
                    Log.e("Business Signup 2 Activity", e.getMessage());
                    Toast.makeText(BusinessSignup2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ParseUser.getCurrentUser().put("businessUser", businessUser);
    }
    // helper method that fires an intent to the loginActivity
    protected void jumpToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

//    private void selectImage() {
//        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessSignup2Activity.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Choose from Gallery")) {
//                    Intent i = new Intent();
//                    i.setType("image/*");
//                    i.setAction(Intent.ACTION_GET_CONTENT);
//                    launchSomeActivity.launch(i);
//                } else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//
//    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//        if (result.getResultCode() == Activity.RESULT_OK) {
//            Intent data = result.getData();
//            if (data != null && data.getData() != null) {
//                imageFile = getPhotoFileUri("image.jpg");
//                Uri selectedImageUri = data.getData();
//                Bitmap selectedImageBitmap = null;
//                try {
//                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                ivProfilePic.setImageBitmap(selectedImageBitmap);
//            }
//        }
//    });

//    private File getPhotoFileUri(String photoFileName) {
//        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "BusinessSignup2Activity");
//        // create the storage directory if it does not exit
//        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
//            Log.d("BusinessSignup2Activity", "failed to create directory");
//        }
//        // return the file target for the photo based on the file name
//        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);
//        return file;
//    }
}