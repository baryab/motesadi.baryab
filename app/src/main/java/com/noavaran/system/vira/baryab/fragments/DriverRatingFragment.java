package com.noavaran.system.vira.baryab.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.noavaran.system.vira.baryab.BaseFragment;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomRatingBar;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;

import org.json.JSONObject;

public class DriverRatingFragment extends BaseFragment {
    private View rootView;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    private CustomTextView tvTitle;
    private ImageView ivDriverPhoto;
    private CustomTextView tvDriverName;
    private CustomTextView tvDriverCarType;
    private CustomTextView tvPlateLicenseNumber;
    private CustomTextView tvPlateLicenseIranNumber;
    private CustomRatingBar rbDriverRating;
    private ImageView ivSignature;
    private CustomTextView tvSignatureOwner;
    private CustomTextView btnConfirm;

    public DriverRatingFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_driver_rating, container, false);
        this.inflater = inflater;
        this.savedInstanceState = savedInstanceState;

        return this.rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews();
        initComponents();
        setViewsListeners();
    }

    private void findViews() {
        tvTitle = (CustomTextView) this.rootView.findViewById(R.id.frDriverRating_tvTitle);
        ivDriverPhoto = (ImageView) this.rootView.findViewById(R.id.frDriverRating_ivDriverPhoto);
        tvDriverName = (CustomTextView) this.rootView.findViewById(R.id.frDriverRating_tvDriverName);
        tvDriverCarType = (CustomTextView) this.rootView.findViewById(R.id.frDriverRating_tvDriverCarType);
        tvPlateLicenseNumber = (CustomTextView) this.rootView.findViewById(R.id.frDriverRating_tvPlateLicenseNumber);
        tvPlateLicenseIranNumber = (CustomTextView) this.rootView.findViewById(R.id.frDriverRating_tvPlateLicenseIranNumber);
        rbDriverRating = (CustomRatingBar) this.rootView.findViewById(R.id.frDriverRating_rbDriverRating);
        ivSignature = (ImageView) this.rootView.findViewById(R.id.frDriverRating_ivSignature);
        tvSignatureOwner = (CustomTextView) this.rootView.findViewById(R.id.frDriverRating_tvSignatureOwner);
        btnConfirm = (CustomTextView) this.rootView.findViewById(R.id.frDriverRating_btnConfirm);
    }

    private void initComponents() {
    }

    private void setViewsListeners() {
        btnConfirm.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }
        });
    }

    public void setData(JSONObject data) {
        tvTitle.setText(data.optString("TypeName"));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}