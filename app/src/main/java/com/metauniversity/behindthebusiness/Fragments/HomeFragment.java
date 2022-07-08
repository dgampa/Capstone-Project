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
public class HomeFragment extends Fragment {

    private static final String TAG = "Home Fragment";
    public static final String BASE_URL = "https://api.yelp.com/v3/";
    public static final String API_KEY = "oxPk-IUSVW11ywOYiP_f36cDTQZOzezaZ6IZdxrdwRYbcDWeR_jroSB0lfpe5fYKxQrLEk8si0QR_ndSxtc4lb9DlxJiVcqkardZIxdhGS-8Sge9-mT776v24Ai2YnYx";
    SwipeRefreshLayout swipeBusinesses;
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
        rvBusinesses.setLayoutManager(new LinearLayoutManager(getContext()));
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
    }

    private void getBusinesses() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        YelpService yelpService = retrofit.create(YelpService.class);
        String categories="";
        String location = "California";
        boolean isBusiness = (boolean) ParseUser.getCurrentUser().get("isBusiness");
        ArrayList<String> favoriteCategories = (ArrayList<String>) ParseUser.getCurrentUser().get("favoriteCategories");
        if (!isBusiness) {
            int i = 0;
            for (; i < favoriteCategories.size() - 1; i++)
                categories += favoriteCategories.get(i) + ", ";
            categories += favoriteCategories.get(i);
            Log.i("HomeFragment", "categories searched: "+categories);
        }
        Call<YelpSearchResult> call = yelpService.searchRestaurants("Bearer " + API_KEY, categories, location);
        call.enqueue(new Callback<YelpSearchResult>() {
            @Override
            public void onResponse(Call<YelpSearchResult> call, Response<YelpSearchResult> response) {
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
}
