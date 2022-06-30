package com.metauniversity.behindthebusiness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.metauniversity.behindthebusiness.Models.YelpBusiness;

import java.util.List;

public class BusinessesAdapter extends RecyclerView.Adapter<BusinessesAdapter.ViewHolder> {
    Context context;
    List<YelpBusiness> businessList;

    public BusinessesAdapter(Context context, List<YelpBusiness> businessList) {
        this.context = context;
        this.businessList = businessList;
    }


    @NonNull
    @Override
    public BusinessesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_business, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessesAdapter.ViewHolder holder, int position) {
        YelpBusiness business = businessList.get(position);
        holder.bind(business);
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }
    // helper methods for SwipeRefreshLayout
    public void clear(){
        businessList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<YelpBusiness> list){
        businessList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvBusinessName;
        TextView tvDescription;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBusinessName = itemView.findViewById(R.id.tvBusinessName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(YelpBusiness business){
            tvBusinessName.setText(business.getName());
            tvDescription.setText(" "+business.getRating());
            Glide.with(context).load(business.getImageUrl()).into(ivProfileImage);

            if(!business.getImageUrl().equals("none")){
                Glide.with(context).load(business.getImageUrl()).into(ivProfileImage);
            }
            else{
                ivProfileImage.setVisibility(View.GONE);
            }
        }
    }
}
