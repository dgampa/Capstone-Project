package com.metauniversity.behindthebusiness.Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.metauniversity.behindthebusiness.Activities.BusinessSignupActivity;
import com.metauniversity.behindthebusiness.Activities.IndividualSignupActivity;
import com.metauniversity.behindthebusiness.R;

public class UserTypeFragment extends DialogFragment {

    private TextView tvUserType;
    private Button btnIndividual;
    private Button btnBusiness;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_type, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        tvUserType = (TextView) view.findViewById(R.id.tvAccountType);
        btnIndividual = (Button) view.findViewById(R.id.btnIndividual);
        btnBusiness = (Button) view.findViewById(R.id.btnBusiness);

        // onClick for individual user sign up
        btnIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), IndividualSignupActivity.class);
                startActivity(intent);
            }
        });
        // onClick for business user sign up
        btnBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BusinessSignupActivity.class);
                startActivity(intent);
            }
        });
    }
}

