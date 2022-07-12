package com.metauniversity.behindthebusiness.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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

import com.metauniversity.behindthebusiness.Activities.LoginActivity;
import com.metauniversity.behindthebusiness.Models.YelpService;
import com.metauniversity.behindthebusiness.Models.YelpSearchResult;
import com.metauniversity.behindthebusiness.Models.YelpBusiness;
import com.metauniversity.behindthebusiness.BusinessesAdapter;
import com.metauniversity.behindthebusiness.EndlessRecyclerViewScrollListener;
import com.metauniversity.behindthebusiness.R;
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
public class IndividualHomeFragment extends Fragment implements LocationChangeFragment.OnInputSelected {

    private static final String TAG = "Home Fragment";
    public static final String BASE_URL = "https://api.yelp.com/v3/";
    public static final String API_KEY = "oxPk-IUSVW11ywOYiP_f36cDTQZOzezaZ6IZdxrdwRYbcDWeR_jroSB0lfpe5fYKxQrLEk8si0QR_ndSxtc4lb9DlxJiVcqkardZIxdhGS-8Sge9-mT776v24Ai2YnYx";
    public static final int LIMIT = 25;
    private int startPosition = 0;
    private String location = "United States";
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeBusinesses;
    RecyclerView rvBusinesses;
    List<YelpBusiness> businessList;
    BusinessesAdapter adapter;
    private Toolbar topAppBar;

    public IndividualHomeFragment() {
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
        String categories = "";
        ArrayList<String> favoriteCategories = (ArrayList<String>) ParseUser.getCurrentUser().get("favoriteCategories");
        int i = 0;
        for (; i < favoriteCategories.size() - 1; i++)
            categories += favoriteCategories.get(i) + ", ";
        categories += favoriteCategories.get(i);
        Log.i("HomeFragment", "categories searched: " + categories);

        startPosition += 25;
        Call<YelpSearchResult> call = yelpService.searchBusinesses("Bearer " + API_KEY, categories, location, LIMIT, startPosition);
        call.enqueue(new Callback<YelpSearchResult>() {
            @Override
            public void onResponse
                    (Call<YelpSearchResult> call, Response<YelpSearchResult> response) {
                // checking for code 200 to confirm a successful call
                Log.i(TAG, "onResponse: " + response.code());
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
        boolean isBusiness = (boolean) ParseUser.getCurrentUser().get("isBusiness");
        ArrayList<String> favoriteCategories = (ArrayList<String>) ParseUser.getCurrentUser().get("favoriteCategories");
        if (!isBusiness) {
            int i = 0;
            for (; i < favoriteCategories.size() - 1; i++)
                categories += favoriteCategories.get(i) + ", ";
            categories += favoriteCategories.get(i);
            Log.i("HomeFragment", "categories searched: " + categories);
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
        dialog.setTargetFragment(IndividualHomeFragment.this, 1);
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
