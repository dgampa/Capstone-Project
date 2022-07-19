package com.metauniversity.behindthebusiness;

import android.app.ProgressDialog;
import android.os.AsyncTask;


import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class MergeVideos extends AsyncTask<String, Integer, String> {
    private String workingPath;
    private ArrayList<String> videosToMerge;
    private final int count = videosToMerge.size();
    private ProgressDialog progressDialog;

    private MergeVideos(String workingPath, ArrayList<String> videosToMerge){
        this.workingPath = workingPath;
        this.videosToMerge = videosToMerge;
    }

    @Override
    protected String doInBackground(String... strings) {
        Movie[] inMovies = new Movie[count];
        for(int i = 0; i<count; i++) {
            File file = new File(workingPath, videosToMerge.get(i));
            if(file.exists()){
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    FileChannel fc = fis.getChannel();
                    inMovies[i] = MovieCreator.build(String.valueOf(fc));
                    fis.close();
                    fc.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    return null;
    }
}
