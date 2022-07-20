package com.metauniversity.behindthebusiness.Fragments;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.metauniversity.behindthebusiness.Activities.BusinessMediaActivity;
import com.metauniversity.behindthebusiness.Activities.LoginActivity;
import com.metauniversity.behindthebusiness.BusinessUser;
import com.metauniversity.behindthebusiness.Models.YelpService;
import com.metauniversity.behindthebusiness.Models.YelpSearchResult;
import com.metauniversity.behindthebusiness.Models.YelpBusiness;
import com.metauniversity.behindthebusiness.BusinessesAdapter;
import com.metauniversity.behindthebusiness.EndlessRecyclerViewScrollListener;
import com.metauniversity.behindthebusiness.R;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessHomeFragment extends Fragment implements LocationChangeFragment.OnInputSelected{

    private static final String TAG = "Business Home Fragment";
    public static final String BASE_URL = "https://api.yelp.com/v3/";
    public static final String API_KEY = "oxPk-IUSVW11ywOYiP_f36cDTQZOzezaZ6IZdxrdwRYbcDWeR_jroSB0lfpe5fYKxQrLEk8si0QR_ndSxtc4lb9DlxJiVcqkardZIxdhGS-8Sge9-mT776v24Ai2YnYx";
    public static final int LIMIT = 25;
    private int startPosition = 0;
    private String location = "United States";
    private String category = "Food";
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeBusinesses;
    RecyclerView rvBusinesses;
    List<YelpBusiness> businessList;
    BusinessesAdapter adapter;
    private Toolbar topAppBar;

    public BusinessHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_individual_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                }, 1
        );
        topAppBar = view.findViewById(R.id.topAppBar);
        //set up the top app bar
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        searchFor(item);
                        break;
                    case R.id.logout:
                        logout();
                        break;
                    case R.id.locationChange:
                        showLocationChangeDialog();
                        break;
                }
                return true;
            }
        });
        swipeBusinesses = view.findViewById(R.id.swipeBusinesses);
        rvBusinesses = view.findViewById(R.id.rvBusinesses);
        // set up recycler view
        businessList = new ArrayList<>();
        adapter = new BusinessesAdapter(getContext(), businessList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvBusinesses.setLayoutManager(linearLayoutManager);
        rvBusinesses.setAdapter(adapter);
        // Yelp API call
        getBusinesses();
        // setup refresh listener which triggers new data loading
        swipeBusinesses.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                startPosition = 0;
                getBusinesses();
                swipeBusinesses.setRefreshing(false);
            }
        });
        // Retain an instance so that you can call 'resetState()' for fresh searches
        scrollListener = (new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreBusinesses();
            }
        });
        // Adds the scroll listener to RecyclerView
        rvBusinesses.addOnScrollListener(scrollListener);
    }

    private void loadMoreBusinesses() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        YelpService yelpService = retrofit.create(YelpService.class);
        startPosition+=25;
        ParseUser currentUser =  ParseUser.getCurrentUser();
        List<BusinessUser> businessUsers = new ArrayList<>();
        ParseQuery<BusinessUser> query = ParseQuery.getQuery(BusinessUser.class);
        query.whereEqualTo("businessUser", currentUser.getObjectId());
        try {
            businessUsers = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(businessUsers.size()>0) {
            BusinessUser businessUser = businessUsers.get(0);
            category = businessUser.getCategory();
            location = businessUser.getLocation();
        }
        Call<YelpSearchResult> call = yelpService.searchBusinesses("Bearer " + API_KEY, category, location, LIMIT, startPosition);
        call.enqueue(new Callback<YelpSearchResult>() {
            @Override
            public void onResponse(Call<YelpSearchResult> call, Response<YelpSearchResult> response) {
                // checking for code 200 to confirm a successful call
                Log.i(TAG, "onResponse: " + response.code() );
                Log.i("HomeFragment", "categories searched: " + category);
                YelpSearchResult body = response.body();
                businessList.addAll(body.getBusinesses());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<YelpSearchResult> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t);
            }
        });
    }


    public void getBusinesses() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        YelpService yelpService = retrofit.create(YelpService.class);
        String categories = "";
        //set the businesses category
        ParseUser currentUser =  ParseUser.getCurrentUser();
        List<BusinessUser> businessUsers = new ArrayList<>();
        ParseQuery<BusinessUser> query = ParseQuery.getQuery(BusinessUser.class);
        query.whereEqualTo("businessUser", currentUser.getObjectId());
        try {
            businessUsers = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(businessUsers.size()>0) {
            BusinessUser businessUser = businessUsers.get(0);
            category = businessUser.getCategory();
            location = businessUser.getLocation();
        }
        Call<YelpSearchResult> call = yelpService.searchBusinesses("Bearer " + API_KEY, categories, location, LIMIT, 0);
        call.enqueue(new Callback<YelpSearchResult>() {
            @Override
            public void onResponse(Call<YelpSearchResult> call, Response<YelpSearchResult> response) {
                // checking for code 200 to confirm a successful call
                Log.i(TAG, "onResponse: " + response.code() + "Body: " + response.body());
                YelpSearchResult body = response.body();
                businessList.addAll(body.getBusinesses());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<YelpSearchResult> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t);
            }
        });
    }
    private void showLocationChangeDialog() {
        LocationChangeFragment dialog = new LocationChangeFragment();
        dialog.setTargetFragment(BusinessHomeFragment.this, 1);
        dialog.show(getFragmentManager(), "onLocationChangeFragment");
    }

    private void searchFor(MenuItem menuItem) {
        // implement search
        Toast.makeText(getContext(), "Clicked Search", Toast.LENGTH_SHORT).show();
    }

    public void logout() {
        ParseUser.logOut();
        // explicit intent to return to login page
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void sendLocation(String input) {
        location = input;
        adapter.clear();
        getBusinesses();
    }
}
