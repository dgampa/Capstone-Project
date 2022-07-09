package com.metauniversity.behindthebusiness.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.metauniversity.behindthebusiness.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationChangeFragment extends DialogFragment {

    public interface OnInputSelected {
        void sendLocation(String location);
    }

    public OnInputSelected onInputSelected;
    private EditText etLocation;
    private Button btnSubmitLocation;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onInputSelected = (OnInputSelected) getTargetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_change, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etLocation = view.findViewById(R.id.etLocation);
        btnSubmitLocation = view.findViewById(R.id.btnSubmitLocation);

        // onClick for individual user sign up
        btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = etLocation.getText().toString();
                if (!location.equals("")) {
                    onInputSelected.sendLocation(location);
                    getDialog().dismiss();
                }
            }
        });
    }

}