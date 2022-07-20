package com.metauniversity.behindthebusiness.Fragments;

import static android.app.Activity.RESULT_OK;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.metauniversity.behindthebusiness.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.metauniversity.behindthebusiness.BusinessVideo;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessVideoUploadFragment extends Fragment {

    private Button btnVideoCapture;

    private VideoView vvDailyVideo;
    private Button btnSubmitVideo;
    Uri videoUri;

    public BusinessVideoUploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_videos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                }, 1
        );
        btnVideoCapture = view.findViewById(R.id.btnCaptureVideo);
        vvDailyVideo = view.findViewById(R.id.vvDailyVideo);
        btnSubmitVideo = view.findViewById(R.id.btnSubmitVideo);
        btnVideoCapture = view.findViewById(R.id.btnCaptureVideo);
        btnVideoCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 2);
                startActivityForResult(intent, 1);
            }
        });
        btnSubmitVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoUri != null) {
                    Log.i("info", "" + videoUri);
                    byte[] bytes = convertVideoToBytes(videoUri);
                    //now lets try and add this uri file to the parse server in a parsefile.
                    ParseFile parseVideoFile = new ParseFile("video.mp4", bytes);
                    parseVideoFile.saveInBackground();
                    BusinessVideo businessVideo = new BusinessVideo();
                    businessVideo.setUser(ParseUser.getCurrentUser());
                    businessVideo.setVideo(parseVideoFile);
                    businessVideo.saveInBackground();
                } else {
                    Toast.makeText(getContext(), "No Video to save", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            VideoView videoView = new VideoView(getContext());
            videoView.setVideoURI(data.getData());
            videoView.start();
            builder.setView(videoView).show();
            ParseObject businessVideo = new ParseObject("BusinessVideo");
            businessVideo.put("user", ParseUser.getCurrentUser());
            if (data.getData() != null) {
                Log.i("info", "" + data.getData());
                byte[] bytes = convertVideoToBytes(data.getData());
                //now lets try and add this uri file to the parse server in a parsefile.
                ParseFile parseVideoFile = new ParseFile ("vid", convertVideoToBytes(Uri.parse(data.getDataString())));
                businessVideo.put("video", parseVideoFile);
                try {
                    businessVideo.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "No Video to save", Toast.LENGTH_LONG).show();
            }
        }

    }

    public byte[] convertVideoToBytes(Uri videoUri) {
        byte[] videoBytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(getRealPathFromURI(getContext(), videoUri)));

            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);

            videoBytes = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoBytes;
    }
    // this is to convert the videoUri to byte[] arrays.

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Video.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            videoUri = data.getData();
            vvDailyVideo.setVideoURI(videoUri);
            vvDailyVideo.start();
        }

    }

    private byte[] convertVideoToBytes(Uri data) {
        byte[] videoBytes = null;
        String realPath = getRealPathFromUri(getContext(), videoUri);
        File inputFile = new File(realPath);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            FileInputStream fis = new FileInputStream(inputFile);
            byte[] buf = new byte[(int) inputFile.length()];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);

            videoBytes = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoBytes;
    }

    public static String getRealPathFromUri(Context ctx, Uri uri) {
        String[] filePathColumn = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = ctx.getContentResolver().query(uri, filePathColumn,
                null, null, null);
        String videoPath = "";
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            videoPath = cursor.getString(columnIndex);
            cursor.close();
        }
        return videoPath;
    }

}