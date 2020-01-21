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

public class CarTypeChooserSecondFragment extends BaseFragment {
    private View rootView;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    private List<TruckType> listTruckType;
    private RecyclerView rvCarTypeList;
    private CarTypeChooserAdapter carTypeChooserAdapter;

    private int pid;

    public CarTypeChooserSecondFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_car_type_chooser_second, container, false);
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
        rvCarTypeList = (RecyclerView) this.rootView.findViewById(R.id.frCarTypeChooserSeconds_rvCarTypeList);
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
                if (listTruckType.get(position).isHasChild())
                    CarTypeChooserDialog.gotoThirdFragment(listTruckType.get(position).getTruckTypeId());
                else {
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
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

//    private void handleClickListenerForKhavar(int position) {
//        if (position == 0)
//            send("خاور، همه موارد");
//        else if (position == 1)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_KHAVAR, CarTypeChooserDialog.KEYWORD_CAR_KHAVAR_ROBAZ);
//        else if (position == 2)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_KHAVAR, CarTypeChooserDialog.KEYWORD_CAR_KHAVAR_MOSAGHAF);
//        else if (position == 3)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_KHAVAR, CarTypeChooserDialog.KEYWORD_CAR_KHAVAR_KAFI);
//        else if (position == 4)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_KHAVAR, CarTypeChooserDialog.KEYWORD_CAR_KHAVAR_TANKER);
//        else if (position == 5)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_KHAVAR, CarTypeChooserDialog.KEYWORD_CAR_KHAVAR_YAKHCHALI);
//        else if (position == 6)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_KHAVAR, CarTypeChooserDialog.KEYWORD_CAR_KHAVAR_KOMPERESI);
//
//    }
//
//    private void handleClickListenerFor911(int position) {
//        if (position == 0)
//            send("911، همه موارد");
//        else if (position == 1)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_911, CarTypeChooserDialog.KEYWORD_CAR_911_ROBAZ);
//        else if (position == 2)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_911, CarTypeChooserDialog.KEYWORD_CAR_911_MOSAGHAF);
//        else if (position == 3)
//            send("911، کفی");
//        else if (position == 4)
//            send("911، تانکر");
//        else if (position == 5)
//            send("911، یخچالی");
//        else if (position == 6)
//            send("911، کمپرسی");
//    }
//
//    private void handleClickListenerForTAK(int position) {
//        if (position == 0)
//            send("تک، همه موارد");
//        else if (position == 1)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_TAK, CarTypeChooserDialog.KEYWORD_CAR_TAK_ROBAZ);
//        else if (position == 2)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_TAK, CarTypeChooserDialog.KEYWORD_CAR_TAK_MOSAGHAF);
//        else if (position == 3)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_TAK, CarTypeChooserDialog.KEYWORD_CAR_TAK_KAFI);
//        else if (position == 4)
//            send("تک، تانکر");
//        else if (position == 5)
//            send("تک، يخچالي");
//        else if (position == 6)
//            send("تک، کمپرسي");
//    }
//
//    private void handleClickListenerForJoft(int position) {
//        if (position == 0)
//            send("جفت، همه موارد");
//        else if (position == 1)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_JOFT, CarTypeChooserDialog.KEYWORD_CAR_JOFT_ROBAZ);
//        else if (position == 2)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_JOFT, CarTypeChooserDialog.KEYWORD_CAR_JOFT_MOSAGHAF);
//        else if (position == 3)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_JOFT, CarTypeChooserDialog.KEYWORD_CAR_JOFT_KAFI);
//        else if (position == 4)
//            send("جفت، تانکر");
//        else if (position == 5)
//            send("جفت، يخچالي");
//        else if (position == 6)
//            send("جفت، کمپرسي");
//    }
//
//    private void handleClickListenerForTereli(int position) {
//        if (position == 0)
//            send("تریلی، همه موارد");
//        else if (position == 1)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_TERELI, CarTypeChooserDialog.KEYWORD_CAR_TERELI_LABEDAR);
//        else if (position == 2)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_TERELI, CarTypeChooserDialog.KEYWORD_CAR_TERELI_KAFI);
//        else if (position == 3)
//            send("تریلی، کمپرسي");
//        else if (position == 4)
//            send("تریلی، تانکر");
//        else if (position == 5)
//            send("تریلی، خودرو بر");
//    }
//
//    private void handleClickListenerForTeranzit(int position) {
//        if (position == 0)
//            send("ترانزیت، همه موارد");
//        else if (position == 1)
//            CarTypeChooserDialog.gotoThirdFragment(CarTypeChooserDialog.KEYWORD_CAR_TERANZIT, CarTypeChooserDialog.KEYWORD_CAR_TERANZIT_CHADORI);
//        else if (position == 2)
//            send("ترانزیت، يخچالي");
//    }

    // This method is used to pass selection car type's data by user to fragment's NewLoadFragment
    private void send(String carType) {
        Intent intent = new Intent("CarType");
        intent.putExtra("car_type", carType);
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
        CarTypeChooserDialog.setBackButtonVisible(View.VISIBLE);
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