package com.noavaran.system.vira.baryab.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noavaran.system.vira.baryab.BaseFragment;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.adapters.CarTypeChooserAdapter;
import com.noavaran.system.vira.baryab.database.models.TruckType;
import com.noavaran.system.vira.baryab.dialogs.CarTypeChooserDialog;
import com.noavaran.system.vira.baryab.listeners.ClickListener;
import com.noavaran.system.vira.baryab.listeners.RecyclerTouchListener;
import com.noavaran.system.vira.baryab.info.TruckTypeInfo;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class CarTypeChooserThirdFragment extends BaseFragment {
    private View rootView;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    private List<TruckType> listTruckType;
    private RecyclerView rvCarTypeList;
    private CarTypeChooserAdapter carTypeChooserAdapter;

    private int pid;

    public CarTypeChooserThirdFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_car_type_chooser_third, container, false);
        this.inflater = inflater;
        this.savedInstanceState = savedInstanceState;

        return this.rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews();
        initComponents();
        getAllArguments();
        initRecyclerView();
        setViewsListeners();
    }

    private void findViews() {
        rvCarTypeList = (RecyclerView) this.rootView.findViewById(R.id.frCarTypeChooserThird_rvCarTypeList);
    }

    private void initComponents() {

    }

    private void getAllArguments() {
        pid = getArguments().getInt("pid");

        listTruckType = Select.from(TruckType.class).where(Condition.prop("truck_Type_Id").eq(String.valueOf(pid))).list();
        CarTypeChooserDialog.setFragmentTitle(listTruckType.get(0).getFullName().replaceAll("/", " ، "));

        listTruckType = Select.from(TruckType.class).where(Condition.prop("pid").eq(String.valueOf(pid))).list();
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
                try {
                    TruckType truckType = listTruckType.get(position);
                    TruckTypeInfo truckTypeInfo = new TruckTypeInfo(truckType.getTruckTypeId(), truckType.getName(), truckType.getPid(), truckType.getFullName(), truckType.getMinLength(), truckType.getMaxLength(), truckType.getMinWidth(), truckType.getMaxWidth(), truckType.getMinHeight(), truckType.getMaxHeight(), truckType.isRoof(), truckType.isHasChild());

                    ObjectMapper mapper = new ObjectMapper();
                    String jsonInString = mapper.writeValueAsString(truckTypeInfo);

                    send(jsonInString);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    // This method is used to pass selection car type's data by user to fragment's NewLoadFragment
    private void send(String carType) {
        Intent intent = new Intent("CarType");
        intent.putExtra("car_type", carType.replaceAll("فرقی نمی کند", "همه"));
        intent.setAction("com.noavaran.system.vira.baryab.REQUEST_CARType");
        getActivity().sendBroadcast(intent);
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
