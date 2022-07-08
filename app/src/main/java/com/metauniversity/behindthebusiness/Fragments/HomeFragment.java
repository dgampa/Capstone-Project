package com.metauniversity.behindthebusiness.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metauniversity.behindthebusiness.Models.YelpService;
import com.metauniversity.behindthebusiness.Models.YelpSearchResult;
import com.metauniversity.behindthebusiness.Models.YelpBusiness;
import com.metauniversity.behindthebusiness.BusinessesAdapter;
import com.metauniversity.behindthebusiness.EndlessRecyclerViewScrollListener;
import com.metauniversity.behindthebusiness.R;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "Home Fragment";
    public static final String BASE_URL = "https://api.yelp.com/v3/";
    public static final String API_KEY = "oxPk-IUSVW11ywOYiP_f36cDTQZOzezaZ6IZdxrdwRYbcDWeR_jroSB0lfpe5fYKxQrLEk8si0QR_ndSxtc4lb9DlxJiVcqkardZIxdhGS-8Sge9-mT776v24Ai2YnYx";
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeBusinesses;
    RecyclerView rvBusinesses;
    List<YelpBusiness> businessList;
    BusinessesAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                getBusinesses();
                swipeBusinesses.setRefreshing(false);
            }
        });
        // Retain an instance so that you can call 'resetState()' for fresh searches
        scrollListener = (new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextData(totalItemsCount+=20);
            }
        });
        // Adds the scroll listener to RecyclerView
        rvBusinesses.addOnScrollListener(scrollListener);
    }
    private void loadNextData(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        getBusinesses();
        //moreProgress.setVisibility(View.VISIBLE);
        Log.e(TAG, "MORE PROGRESS");
    }

    private void getBusinesses() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        YelpService yelpService = retrofit.create(YelpService.class);
        String categories = "";
        String location = "California";
        boolean isBusiness = (boolean) ParseUser.getCurrentUser().get("isBusiness");
        ArrayList<String> favoriteCategories = (ArrayList<String>) ParseUser.getCurrentUser().get("favoriteCategories");
        if (!isBusiness) {
            int i = 0;
            for (; i < favoriteCategories.size() - 1; i++)
                categories += favoriteCategories.get(i) + ", ";
            categories += favoriteCategories.get(i);
            Log.i("HomeFragment", "categories searched: " + categories);
        }
        Call<YelpSearchResult> call = yelpService.searchRestaurants("Bearer " + API_KEY, categories, location);
        call.enqueue(new Callback<YelpSearchResult>() {
            @Override
            public void onResponse(Call<YelpSearchResult> call, Response<YelpSearchResult> response) {
                // checking for code 200 to confirm a successful call
                Log.i(TAG, "onResponse: " + response.code() + "total: " + response.body().getTotal());
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
}
