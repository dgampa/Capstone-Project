package com.metauniversity.behindthebusiness.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.metauniversity.behindthebusiness.R;
import com.metauniversity.behindthebusiness.UserPost;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class CustomerReviewActivity extends AppCompatActivity {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 43;
    private File photoFile;
    private String photoFileName = "photo.jpg";
    private float rating;
    TextView tvBusinessName;
    ImageButton ibExit;
    Button btnCapture;
    ImageView ivPost;
    RatingBar ratingBar;
    Button btnSubmitRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_review);
        getSupportActionBar().hide();
        tvBusinessName = findViewById(R.id.tvBusinessName);
        ibExit = findViewById(R.id.ibExit);
        btnCapture = findViewById(R.id.btnCapture);
        ivPost = findViewById(R.id.ivPost);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmitRating = findViewById(R.id.btnSubmitRating);
        // set Business Name here
        // tvBusiness.setText(businessName);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });
        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // return to the last fragment
                finish();
            }
        });
        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRating();
            }
        });
    }

    private void saveRating() {
        rating = ratingBar.getRating();
        try {
            savePost(ParseUser.getCurrentUser(), photoFile, rating);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void launchCamera() {
        // fire an implicit intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);
        //wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(CustomerReviewActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            //Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // load the taken image into a preview
                ivPost.setImageBitmap(takenImage);
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // helper method to return the file for a photo stored on disk given the fileName
    private File getPhotoFileUri(String photoFileName) {
        // we don't need to request external read/write runtime permissions
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Cusomer Review Activity");
        // create the storage directory if it does not exit
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("Customer Review Activity", "failed to create directory");
        }
        // return the file target for the photo based on the file name
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);
        return file;
    }

    private void savePost(ParseUser currentUser, File photoFile, Number rating) throws ParseException {
        UserPost post = new UserPost();
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.setRating(rating);
        post.save();
//        post.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e!=null){
//                    Log.e("CustomerReviewActivity", "Error while saving", e);
//                }
//                Log.i("CustomerReviewActivity", "Post save was successful!");
//            }
//        });
    }
}