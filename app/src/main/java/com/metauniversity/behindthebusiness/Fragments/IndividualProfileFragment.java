package com.metauniversity.behindthebusiness.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metauniversity.behindthebusiness.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndividualProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndividualProfileFragment extends Fragment {


    public IndividualProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_individual_profile, container, false);
    }
}