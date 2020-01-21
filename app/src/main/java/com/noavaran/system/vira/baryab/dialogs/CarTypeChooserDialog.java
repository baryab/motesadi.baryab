package com.noavaran.system.vira.baryab.dialogs;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.fragments.CarTypeChooserFirstFragment;
import com.noavaran.system.vira.baryab.fragments.CarTypeChooserSecondFragment;
import com.noavaran.system.vira.baryab.fragments.CarTypeChooserThirdFragment;

public class CarTypeChooserDialog extends DialogFragment {
    private static FragmentTransaction fragmentTransaction;
    private static CustomTextView tvFragmentName;
    private static CustomTextView btnBack;
    private CustomTextView btnCancel;

    private static CarTypeChooserFirstFragment carTypeChooserFirstFragment;
    private static CarTypeChooserSecondFragment carTypeChooserSecondFragment;
    private static CarTypeChooserThirdFragment carTypeChooserThirdFragment;
    private static FragmentManager fragmentManager;
    private static Fragment currentFragment;

    private boolean isBroadcastRequestCarTypeRegistered = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View rootView = inflater.inflate(R.layout.dialog_car_type_chooser, container, false);

        fragmentManager = getChildFragmentManager();

        findViews(rootView);
        initComponents();
        setViewsListeners();

        replaceFragment(carTypeChooserFirstFragment);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void findViews(View rootView) {
        tvFragmentName = (CustomTextView) rootView.findViewById(R.id.dgCarTypeChooser_tvFragmentName);
        btnBack = (CustomTextView) rootView.findViewById(R.id.dgCarTypeChooser_btnBack);
        btnCancel = (CustomTextView) rootView.findViewById(R.id.dgCarTypeChooser_btnCancel);
    }

    private void initComponents() {
        carTypeChooserFirstFragment = new CarTypeChooserFirstFragment();
        carTypeChooserSecondFragment = new CarTypeChooserSecondFragment();
        carTypeChooserThirdFragment = new CarTypeChooserThirdFragment();

        getDialog().setTitle("انتخاب نوع ماشین");

        isBroadcastRequestCarTypeRegistered = true;
        getActivity().registerReceiver(broadcastRequestCarType, new IntentFilter("com.noavaran.system.vira.baryab.REQUEST_CARType"));
    }

    private void setViewsListeners() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment instanceof CarTypeChooserSecondFragment) {
                    replaceFragment(carTypeChooserFirstFragment);
                    currentFragment = carTypeChooserFirstFragment;
                } else if (currentFragment instanceof CarTypeChooserThirdFragment) {
                    replaceFragment(carTypeChooserSecondFragment);
                    currentFragment = carTypeChooserSecondFragment;
                }
            }
        });
    }

    public static void setBackButtonVisible(int status) {
        btnBack.setVisibility(status);
    }

    public static void setFragmentTitle(String title) {
        tvFragmentName.setText("( " + title + " )");
    }

    public static void gotoSecondFragment(int pid) {
        Bundle bundle = new Bundle();
        bundle.putInt("pid", pid);
        carTypeChooserSecondFragment.setArguments(bundle);
        replaceFragment(carTypeChooserSecondFragment);

        currentFragment = carTypeChooserSecondFragment;
    }

    public static void gotoThirdFragment(int pid) {
        Bundle bundle = new Bundle();
        bundle.putInt("pid", pid);
        carTypeChooserThirdFragment.setArguments(bundle);
        replaceFragment(carTypeChooserThirdFragment);

        currentFragment = carTypeChooserThirdFragment;
    }

    private static void replaceFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dgCarTypeChooser_flContainers, fragment);
        fragmentTransaction.commit();
    }

    BroadcastReceiver broadcastRequestCarType = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                dismiss();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (isBroadcastRequestCarTypeRegistered) {
            isBroadcastRequestCarTypeRegistered = false;
            getActivity().unregisterReceiver(broadcastRequestCarType);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isBroadcastRequestCarTypeRegistered) {
            isBroadcastRequestCarTypeRegistered = false;
            getActivity().unregisterReceiver(broadcastRequestCarType);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
