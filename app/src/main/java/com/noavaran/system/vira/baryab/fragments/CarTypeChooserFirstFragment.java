package com.noavaran.system.vira.baryab.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noavaran.system.vira.baryab.BaseFragment;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.adapters.CarTypeChooserAdapter;
import com.noavaran.system.vira.baryab.database.models.TruckType;
import com.noavaran.system.vira.baryab.dialogs.CarTypeChooserDialog;
import com.noavaran.system.vira.baryab.listeners.ClickListener;
import com.noavaran.system.vira.baryab.listeners.RecyclerTouchListener;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class CarTypeChooserFirstFragment extends BaseFragment {
    private View rootView;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    private List<TruckType> listTruckType;
    private RecyclerView rvCarTypeList;
    private CarTypeChooserAdapter carTypeChooserAdapter;

    public CarTypeChooserFirstFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_car_type_chooser_first, container, false);
        this.inflater = inflater;
        this.savedInstanceState = savedInstanceState;

        return this.rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews();
        initComponents();
        initRecyclerView();
        setViewsListeners();
    }

    private void findViews() {
        rvCarTypeList = (RecyclerView) this.rootView.findViewById(R.id.frCarTypeChooserFirst_rvCarTypeList);
    }

    private void initComponents() {
        listTruckType = Select.from(TruckType.class).where(Condition.prop("pid").eq("0")).list();
    }

    private void initRecyclerView() {
        carTypeChooserAdapter = new CarTypeChooserAdapter(listTruckType);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvCarTypeList.setLayoutManager(mLayoutManager);
        rvCarTypeList.addItemDecoration(new DividerItemDecoration(getActivity(), 0));
        rvCarTypeList.setItemAnimator(new DefaultItemAnimator());
        rvCarTypeList.setAdapter(carTypeChooserAdapter);
    }

    private void setViewsListeners() {
        rvCarTypeList.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvCarTypeList, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CarTypeChooserDialog.gotoSecondFragment(listTruckType.get(position).getTruckTypeId());
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        CarTypeChooserDialog.setBackButtonVisible(View.GONE);
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
