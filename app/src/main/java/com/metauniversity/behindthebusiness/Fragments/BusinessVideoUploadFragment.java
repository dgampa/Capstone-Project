package com.metauniversity.behindthebusiness.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metauniversity.behindthebusiness.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusinessVideoUploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusinessVideoUploadFragment extends Fragment {


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
    }
}