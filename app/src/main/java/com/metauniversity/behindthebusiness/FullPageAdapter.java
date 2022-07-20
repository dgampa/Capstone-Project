package com.metauniversity.behindthebusiness;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;

import java.util.ArrayList;

public class FullPageAdapter extends PagerAdapter {

    Context context;
    ArrayList<UserPost> businessStories;
    LayoutInflater inflater;

    public FullPageAdapter(Context context, ArrayList<UserPost> businessStories) {
        this.context = context;
        this.businessStories = businessStories;
    }

    @Override
    public int getCount() {
        return businessStories.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_full_image, null);
        ImageButton btnExitPage = view.findViewById(R.id.btnExitPage);
        ImageView ivFullImage = view.findViewById(R.id.ivFullImage);

        btnExitPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).finish();
            }
        });
        ParseFile image = businessStories.get(position).getImage();
        Glide.with(context).load(image.getUrl()).apply(new RequestOptions().centerInside()).into(ivFullImage);
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vpFullPage = (ViewPager) container;
        View view = (View) object;
        vpFullPage.removeView(view);
    }
}
