package com.metauniversity.behindthebusiness;

import android.content.Context;
import android.content.Intent;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.metauniversity.behindthebusiness.Activities.CustomerReviewActivity;
import com.metauniversity.behindthebusiness.Activities.BusinessMediaActivity;
import com.metauniversity.behindthebusiness.Models.YelpBusinessDetails;
import com.metauniversity.behindthebusiness.Models.YelpBusiness;
import com.metauniversity.behindthebusiness.Models.YelpBusinessHours;
import com.metauniversity.behindthebusiness.Models.YelpDailyHours;
import com.metauniversity.behindthebusiness.Models.YelpService;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessesAdapter extends RecyclerView.Adapter<BusinessesAdapter.ViewHolder> {
    private static final String TAG = "Home Fragment";
    public static final String BASE_URL = "https://api.yelp.com/v3/";
    public static final String API_KEY = "oxPk-IUSVW11ywOYiP_f36cDTQZOzezaZ6IZdxrdwRYbcDWeR_jroSB0lfpe5fYKxQrLEk8si0QR_ndSxtc4lb9DlxJiVcqkardZIxdhGS-8Sge9-mT776v24Ai2YnYx";
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

    // helper method for SwipeRefreshLayout
    public void clear() {
        businessList.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvBusinessDetails;
        ConstraintLayout condensedView;
        ImageView ivProfileImage;
        TextView tvBusinessName;
        ImageView ivOpenIcon;
        ImageButton btnAddPic;
        RatingBar rbBusinessRating;
        ConstraintLayout expandableView;
        TextView tvLocationTitle;
        TextView tvLocation;
        TextView tvHoursOfOperationTitle;
        TextView tvHoursOfOperation;
        ImageView ivFacebookIcon;
        TextView tvFacebookHandle;
        ImageView ivInstagramIcon;
        TextView tvInstaHandle;
        TextView tvNextPage;
        ImageButton ibNext;

        String businessURL;

        public ViewHolder(@NonNull View itemView)  {
            super(itemView);
            cvBusinessDetails = itemView.findViewById(R.id.cvBusinessDetails);
            condensedView = itemView.findViewById(R.id.condensedView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBusinessName = itemView.findViewById(R.id.tvBusinessName);
            ivOpenIcon = itemView.findViewById(R.id.ivOpenIcon);
            btnAddPic = itemView.findViewById(R.id.btnAddPic);
            rbBusinessRating = itemView.findViewById(R.id.rbBusinessRating);
            expandableView = itemView.findViewById(R.id.expandableView);
            tvLocationTitle = itemView.findViewById(R.id.tvLocationTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvHoursOfOperationTitle = itemView.findViewById(R.id.tvHoursOfOperationTitle);
            tvHoursOfOperation = itemView.findViewById(R.id.tvHoursOfOperation);
            ivFacebookIcon = itemView.findViewById(R.id.ivFacebookIcon);
            tvFacebookHandle = itemView.findViewById(R.id.tvFacebookHandle);
            ivInstagramIcon = itemView.findViewById(R.id.ivInstagramIcon);
            tvInstaHandle = itemView.findViewById(R.id.tvInstaHandle);
            tvNextPage = itemView.findViewById(R.id.tvNextPage);
            ibNext = itemView.findViewById(R.id.ibNext);
        }

        public void bind(YelpBusiness business) {
            expandableView.setVisibility(View.GONE);
            String businessName = business.getName();
            tvBusinessName.setText(businessName);
            // set business URL
            businessURL = business.getBusinessURL();
            Glide.with(context).asBitmap().load(business.getImageUrl()).into(ivProfileImage);

            if (!business.getImageUrl().equals("none")) {
                Glide.with(context).load(business.getImageUrl()).into(ivProfileImage);
            } else {
                ivProfileImage.setVisibility(View.GONE);
            }
            // tell user to hold longer to add review
            btnAddPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "Press longer to add a review for the business!", Toast.LENGTH_SHORT).show();
                }
            });
            // set add review
            btnAddPic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    goToReviewActivity();
                    return true;
                }
            });
            cvBusinessDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMore(view, businessName);
                }
            });
            ibNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, BusinessMediaActivity.class);
                    intent.putExtra("businessName", tvBusinessName.getText().toString());
                    intent.putExtra("businessURL", businessURL);
                    context.startActivity(intent);
                }
            });
            float rating = (float) business.getRating();
            rbBusinessRating.setRating(rating);
            rbBusinessRating.setIsIndicator(true);
            tvLocation.setText(business.getLocation().formattedLocation());
            String businessID = business.getId();
            setBusinessDetails(businessID);

        }
        private void goToReviewActivity() {
            Intent intent = new Intent(context, CustomerReviewActivity.class);
            intent.putExtra("businessName", tvBusinessName.getText().toString());
            intent.putExtra("URL", businessURL);
            context.startActivity(intent);
        }

        private void showMore(View view, String businessName) {
            if (expandableView.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cvBusinessDetails, new AutoTransition());
                expandableView.setVisibility(View.VISIBLE);
                List<BusinessUser> businessUsers = new ArrayList<>();
                ParseQuery<BusinessUser> query = ParseQuery.getQuery(BusinessUser.class);
                query.whereEqualTo("businessName", businessName);
                try {
                    businessUsers = query.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (businessUsers.size() == 0) {
                    ivFacebookIcon.setVisibility(View.GONE);
                    ivInstagramIcon.setVisibility(View.GONE);
                    return;
                }
                BusinessUser businessUser = businessUsers.get(0);
                setSocials(businessUser);
            } else {
                TransitionManager.beginDelayedTransition(cvBusinessDetails, new AutoTransition());
                expandableView.setVisibility(View.GONE);
            }
        }

        private void setSocials(BusinessUser businessUser) {
            if (businessUser.getSocialFB() != null)
                tvFacebookHandle.setText("@" + businessUser.getSocialFB());
            else
                ivFacebookIcon.setVisibility(View.GONE);
            if (businessUser.getSocialInstagram() != null)
                tvInstaHandle.setText("@" + businessUser.getSocialInstagram());
            else
                ivInstagramIcon.setVisibility(View.GONE);
        }

        private void setBusinessDetails(String businessID) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            YelpService yelpService = retrofit.create(YelpService.class);
            Call<YelpBusinessDetails> call = yelpService.searchForDetails("Bearer " + API_KEY, businessID);
            call.enqueue(new Callback<YelpBusinessDetails>() {
                @Override
                public void onResponse(Call<YelpBusinessDetails> call, Response<YelpBusinessDetails> response) {
                    // checking for code 200 to confirm a successful call
                    Log.i(TAG, "onResponseDetails: " + response.code());
                    YelpBusinessDetails businessDetails = response.body();
                    if (businessDetails == null || businessDetails.getHours() == null) {
                        tvHoursOfOperation.setText("No Hours Available \n");
                    } else {
                        // set business hours of operation
                        YelpBusinessHours businessHours = businessDetails.getHours().get(0);
                        List<YelpDailyHours> dailyHoursList = businessHours.getDailyHours();
                        tvHoursOfOperation.setText(getFormattedHours(dailyHoursList));
                        // set is the business open
                        if (businessHours.getOpen())
                            ivOpenIcon.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<YelpBusinessDetails> call, Throwable t) {
                    Log.i(TAG, "onFailureDetails: " + t);
                }
            });
        }

        private String getFormattedHours(List<YelpDailyHours> dailyHoursList) {
            String formattedHours = "";
            boolean isMondayDuplicate = false;
            boolean isTuesdayDuplicate = false;
            boolean isWednesdayDuplicate = false;
            boolean isThursdayDuplicate = false;
            boolean isFridayDuplicate = false;
            boolean isSaturdayDuplicate = false;
            boolean isSundayDuplicate = false;
            for (int i = 0; i < dailyHoursList.size(); i++) {
                YelpDailyHours dailyHours = dailyHoursList.get(i);
                if (dailyHours.getDay() == 0) {
                    if (!isMondayDuplicate) {
                        formattedHours += "Monday: ";
                    }
                    isMondayDuplicate = true;
                    formattedHours += formattedDailyHours(dailyHours) + "\n";
                }
                if (dailyHours.getDay() == 1) {
                    if (!isTuesdayDuplicate) {
                        formattedHours += "Tuesday: ";
                    }
                    isTuesdayDuplicate = true;
                    formattedHours += formattedDailyHours(dailyHours) + "\n";
                }
                if (dailyHours.getDay() == 2) {
                    if (!isWednesdayDuplicate) {
                        formattedHours += "Wednesday: ";
                    }
                    isWednesdayDuplicate = true;
                    formattedHours += formattedDailyHours(dailyHours) + "\n";
                }
                if (dailyHours.getDay() == 3) {
                    if (!isThursdayDuplicate) {
                        formattedHours += "Thursday: ";
                    }
                    isThursdayDuplicate = true;
                    formattedHours += formattedDailyHours(dailyHours) + "\n";
                }
                if (dailyHours.getDay() == 4) {
                    if (!isFridayDuplicate) {
                        formattedHours += "Friday: ";
                    }
                    isFridayDuplicate = true;
                    formattedHours += formattedDailyHours(dailyHours) + "\n";
                }
                if (dailyHours.getDay() == 5) {
                    if (!isSaturdayDuplicate) {
                        formattedHours += "Saturday: ";
                    }
                    isSaturdayDuplicate = true;
                    formattedHours += formattedDailyHours(dailyHours) + "\n";
                }
                if (dailyHours.getDay() == 6) {
                    if (!isSundayDuplicate) {
                        formattedHours += "Sunday: ";
                    }
                    isSundayDuplicate = true;
                    formattedHours += formattedDailyHours(dailyHours) + "\n";
                }
            }
            return formattedHours;
        }
    }

    private String formattedDailyHours(YelpDailyHours dailyHours) {
        String timing = "";
        boolean isPMOpening = false;
        int hourOpening = Integer.valueOf(dailyHours.getOpeningTime().substring(0, 2));
        if (hourOpening > 12) {
            isPMOpening = true;
            hourOpening -= 12;
        }
        String minutesOpening = dailyHours.getOpeningTime().substring(2);
        timing += hourOpening + ":" + minutesOpening;
        if (isPMOpening)
            timing += " pm - ";
        else
            timing += " am - ";
        boolean isPMClosing = false;
        int hourClosing = Integer.valueOf(dailyHours.getClosingTime().substring(0, 2));
        if (hourClosing > 12) {
            isPMClosing = true;
            hourClosing -= 12;
        }
        String minutesClosing = dailyHours.getClosingTime().substring(2);
        timing += hourClosing + ":" + minutesClosing;
        if (isPMClosing)
            timing += " pm";
        else
            timing += " am";
        return timing;
    }

}

