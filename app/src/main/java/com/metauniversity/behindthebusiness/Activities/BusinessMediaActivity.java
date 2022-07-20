package com.metauniversity.behindthebusiness.Activities;

import static android.os.FileUtils.copy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
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

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

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
        MovieCreator movieCreator = new MovieCreator();
        int count = businessVideoFiles.size();
        Movie[] movies = new Movie[count];
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + "video.mp4";
        for (int i = 0; i < count; i++) {
            byte[] fileBytes = new byte[0];
            File videoFile = new File(path);
            try {
                fileBytes = businessVideoFiles.get(i).getData();
                FileOutputStream outputStream = new FileOutputStream(videoFile);
                outputStream.write(fileBytes);
                movies[i] = MovieCreator.build(path);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<Track> videoTracks = new LinkedList<Track>();
        for (Movie m : movies) {
            for (Track t : m.getTracks()) {
                if (t.getHandler().equals("vide")) {
                    videoTracks.add(t);
                }
            }
        }
        Movie result = new Movie();
        if (videoTracks.size() > 0) {
            try {
                result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Container mp4file = new DefaultMp4Builder().build(result);
        FileChannel fc = null;
        try {
            fc = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + "output.mp4")).getChannel();
            mp4file.writeContainer(fc);
            fc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        vvCompilationVideo.setVideoPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + "output.mp4");
        vvCompilationVideo.start();
        deleteFile("videoFile");
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