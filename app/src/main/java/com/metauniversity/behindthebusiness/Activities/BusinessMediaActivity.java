package com.metauniversity.behindthebusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metauniversity.behindthebusiness.BusinessUser;
import com.metauniversity.behindthebusiness.BusinessStoriesAdapter;
import com.metauniversity.behindthebusiness.rvBusinessStoriesClickListener;
import com.metauniversity.behindthebusiness.UserPost;
import com.metauniversity.behindthebusiness.BusinessVideo;
import com.metauniversity.behindthebusiness.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusinessMediaActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

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
    int incrementer = 0;
    ParseFile video;
    Uri uri;

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
        mediaController = new MediaController(this);
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
    }

    private void playVideo() {
        video = businessVideos.get(incrementer).getVideo();
        uri = Uri.parse(video.getUrl());
        vvCompilationVideo.setVideoURI(uri);
        vvCompilationVideo.setMediaController(mediaController);
        mediaController.setAnchorView(vvCompilationVideo);
        vvCompilationVideo.setOnCompletionListener(this);
        vvCompilationVideo.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            if (incrementer < businessVideos.size()) {
                incrementer++;
            } else {
                incrementer = 0;
                return;
            }
            video = businessVideos.get(incrementer).getVideo();
            uri = Uri.parse(video.getUrl());
            mediaPlayer.setDataSource(video.getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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