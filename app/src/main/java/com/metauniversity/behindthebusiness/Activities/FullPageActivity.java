package com.metauniversity.behindthebusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.metauniversity.behindthebusiness.FullPageAdapter;
import com.metauniversity.behindthebusiness.R;
import com.metauniversity.behindthebusiness.UserPost;
import com.parse.ParseFile;

import java.util.ArrayList;

public class FullPageActivity extends Activity {

    ViewPager vpFullPage;
    ArrayList<UserPost> businessStories;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_page);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            businessStories = intent.getParcelableArrayListExtra("BusinessStories");
            position = intent.getIntExtra("Position", 0);
        }
        vpFullPage = findViewById(R.id.vpFullImage);
        FullPageAdapter fullPageAdapter = new FullPageAdapter(this, businessStories);
        vpFullPage.setAdapter(fullPageAdapter);
        vpFullPage.setCurrentItem(position, true);
    }
}