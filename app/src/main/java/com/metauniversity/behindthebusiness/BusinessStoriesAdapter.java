package com.metauniversity.behindthebusiness;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.metauniversity.behindthebusiness.Activities.FullPageActivity;
import com.parse.ParseFile;

import java.util.List;

public class BusinessStoriesAdapter extends RecyclerView.Adapter<BusinessStoriesAdapter.ImageViewHolder> {

    Context context;
    List<UserPost> businessStories;
    rvBusinessStoriesClickListener clickListener;

    public BusinessStoriesAdapter(Context context, List<UserPost> businessStories, rvBusinessStoriesClickListener clickListener){
        this.context = context;
        this.businessStories = businessStories;
        this.clickListener = clickListener;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        UserPost currentPost = businessStories.get(position);
        holder.bind(currentPost);
        Toast.makeText(context, currentPost.getBusiness(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return businessStories.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivStoryPic;
        ProgressBar progressBar;
        public ImageViewHolder(View itemView) {
            super(itemView);
            ivStoryPic = itemView.findViewById(R.id.ivStoryPic);
            progressBar = itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           clickListener.onCLick(view, getAdapterPosition());
        }

        public void bind(UserPost currentPost) {
            ParseFile image = currentPost.getImage();
            if(image != null) {
                Glide.with(context).load(image.getUrl()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(ivStoryPic);
            }
        }
    }
}
