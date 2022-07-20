package com.metauniversity.behindthebusiness.Activities;

import static android.os.FileUtils.copy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metauniversity.behindthebusiness.BusinessUser;
import com.metauniversity.behindthebusiness.BusinessStoriesAdapter;
import com.metauniversity.behindthebusiness.rvBusinessStoriesClickListener;
import com.metauniversity.behindthebusiness.UserPost;
import com.metauniversity.behindthebusiness.BusinessVideo;
import com.metauniversity.behindthebusiness.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusinessMediaActivity extends AppCompatActivity {

    private TextView tvDescriptionTitle;
    private TextView tvDescription;
    private RecyclerView rvBusinessStories;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvYelpWebsite;
    private VideoView vvCompilationVideo;
    private Button btnPlayVideo;
    private MediaController mediaController;
    BusinessStoriesAdapter adapter;
    ArrayList<UserPost> businessStories;
    String businessName;
    BusinessUser currentBusiness;
    List<BusinessVideo> businessVideos;
    List<ParseFile> businessVideoFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_media);
        tvDescriptionTitle = (TextView) findViewById(R.id.tvDescriptionTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        rvBusinessStories = (RecyclerView) findViewById(R.id.rvBusinessStories);
        tvYelpWebsite = (TextView) findViewById(R.id.tvYelpWebsite);
        layoutManager = new LinearLayoutManager(this);
        vvCompilationVideo = (VideoView) findViewById(R.id.vvBusinessCompilationVideo);
        btnPlayVideo = (Button) findViewById(R.id.btnPlayVideo);
        rvBusinessStories.setHasFixedSize(true);
        rvBusinessStories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Intent intent = getIntent();
        businessName = intent.getStringExtra("businessName");
        String businessURL = intent.getStringExtra("businessURL");
        ParseUser currentUser = ParseUser.getCurrentUser();
        List<BusinessUser> businessUsers = new ArrayList<>();
        ParseQuery<BusinessUser> query = ParseQuery.getQuery(BusinessUser.class);
        query.whereEqualTo("businessName", businessName);
        try {
            businessUsers = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (businessUsers.size() > 0) {
            currentBusiness = businessUsers.get(0);
            tvDescription.setText(currentBusiness.getDescription());
        } else {
            tvDescriptionTitle.setVisibility(View.GONE);
            tvDescription.setVisibility(View.GONE);
        }
        if (currentBusiness != null) {
            findVideo();
        } else {
            btnPlayVideo.setVisibility(View.GONE);
            vvCompilationVideo.setVisibility(View.GONE);
        }
        tvYelpWebsite.setText(businessURL);
        btnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
            }
        });
        businessStories = new ArrayList<UserPost>();
        final rvBusinessStoriesClickListener listener = new rvBusinessStoriesClickListener() {
            @Override
            public void onCLick(View view, int position) {
                // open full screen activity with image clicked
                Intent intent = new Intent(getApplicationContext(), FullPageActivity.class);
                intent.putParcelableArrayListExtra("BusinessStories", businessStories);
                intent.putExtra("Position", position);
                startActivity(intent);
            }
        };
        adapter = new BusinessStoriesAdapter(this, businessStories, listener);
        rvBusinessStories.setAdapter(adapter);
        getBusinessStories();
    }

    private void findVideo() {
        businessVideos = new ArrayList<>();
        ParseQuery<BusinessVideo> query = ParseQuery.getQuery(BusinessVideo.class);
        query.whereEqualTo("user", currentBusiness.getUser());
        try {
            businessVideos = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (businessVideos.size() > 0) {
            for (BusinessVideo businessVideo : businessVideos) {
                businessVideoFiles.add(businessVideo.getVideo());
            }
        }
    }

    private void playVideo() {
        Mp4ParserWrapper mp4ParserWrapper = new Mp4ParserWrapper();
        String finalPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + 0 + "video.mp4";
        byte[] fileBytes1 = new byte[0];
        File videoFile1 = new File(finalPath);
        try {
            fileBytes1 = businessVideoFiles.get(0).getData();
            FileOutputStream outputStream1 = new FileOutputStream(videoFile1);
            outputStream1.write(fileBytes1);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < businessVideoFiles.size(); i++) {
            String additionalPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + i + "video.mp4";
            try {
                byte[] fileBytes2 = businessVideoFiles.get(i).getData();
                File videoFile2 = new File(additionalPath);
                FileOutputStream outputStream2 = new FileOutputStream(videoFile2);
                outputStream2.write(fileBytes2);
                mp4ParserWrapper.append(videoFile1.getAbsolutePath(), videoFile2.getAbsolutePath());
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        vvCompilationVideo.setVideoPath(finalPath);
        vvCompilationVideo.start();
    }

    private void getBusinessStories() {
        ParseQuery<UserPost> query = ParseQuery.getQuery(UserPost.class);
        query.whereEqualTo("businessFor", businessName);
        query.findInBackground(new FindCallback<UserPost>() {
            @Override
            public void done(List<UserPost> objects, ParseException e) {
                if (e != null) {
                    Log.e("Business Media Activity", "Issue with getting user posts", e);
                    return;
                }
                businessStories.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}