package com.noavaran.system.vira.baryab.fragments;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noavaran.system.vira.baryab.BaseFragment;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.SPreferences;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;

public class CarriedLoadingFragment extends BaseFragment {
    private View rootView;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    private FragmentTransaction fragmentTransaction;

    private CustomTextView btnCarryingShipments;
    private CustomTextView btnCarriedShipments;

    private CarringShipmentFragment carringShipmentFragment;
    private CarriedShipmentFragment carriedShipmentFragment;
    private Fragment currentFragment;

    public CarriedLoadingFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_carried_loading, container, false);
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

        replaceFragment(carriedShipmentFragment);
    }

    private void findViews() {
        btnCarryingShipments = (CustomTextView) rootView.findViewById(R.id.frCarriedLoading_btnCarryingShipments);
        btnCarriedShipments = (CustomTextView) rootView.findViewById(R.id.frCarriedLoading_btnCarriedShipments);
    }

    private void initComponents() {
        carringShipmentFragment = new CarringShipmentFragment();
        carriedShipmentFragment = new CarriedShipmentFragment();
    }

    private void setViewsListeners() {
        btnCarryingShipments.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                btnCarryingShipments.setBackgroundResource(R.drawable.bottom_selected);
                btnCarriedShipments.setBackgroundResource(R.drawable.bottom_unselected);

                replaceFragment(carringShipmentFragment);
                setUserVisibleHint(true);
            }
        });

        btnCarriedShipments.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                btnCarriedShipments.setBackgroundResource(R.drawable.bottom_selected);
                btnCarryingShipments.setBackgroundResource(R.drawable.bottom_unselected);

                replaceFragment(carriedShipmentFragment);
                setUserVisibleHint(true);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frCarriedLoading_flContainers, fragment);
        fragmentTransaction.commit();

        currentFragment = fragment;
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentFragment instanceof CarriedShipmentFragment)
                    carriedShipmentFragment.setUserVisibleHint(isVisibleToUser);
                else if (currentFragment instanceof CarringShipmentFragment)
                    carringShipmentFragment.setUserVisibleHint(isVisibleToUser);
            }
        }, 500);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean hasRefresh = SPreferences.getInstance(getActivity()).hasRefresh();
        if (hasRefresh) {
            SPreferences.getInstance(getActivity()).setRefresh(false);
            setUserVisibleHint(true);
        }
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