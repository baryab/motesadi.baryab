package com.noavaran.system.vira.baryab.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.noavaran.system.vira.baryab.BaseFragment;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.SPreferences;
import com.noavaran.system.vira.baryab.activities.MainActivity;
import com.noavaran.system.vira.baryab.adapters.MyLoadingsAdapter;
import com.noavaran.system.vira.baryab.info.MyLoadingInfo;
import com.noavaran.system.vira.baryab.interfaces.OnLoadMoreListener;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyLoadingsFragment extends BaseFragment {
    private View rootView;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    private RelativeLayout rlNoItemFound;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvMyLoadings;
    private MyLoadingsAdapter myLoadingsAdapter;
    private List<MyLoadingInfo> listMyLoading;
    private MyLoadingInfo myLoadingInfo;

    private int page = 0;

    public MyLoadingsFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_my_loadings, container, false);
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
//        getMyLoadingsFromServer();
    }

    private void findViews() {
        rlNoItemFound = (RelativeLayout) this.rootView.findViewById(R.id.frMyLoadings_rlNoItemFound);
        swipeContainer = (SwipeRefreshLayout) this.rootView.findViewById(R.id.frMyLoadings_swipeContainer);
        rvMyLoadings = (RecyclerView) this.rootView.findViewById(R.id.frMyLoadings_rvMyLoadings);
    }

    private void initComponents() {
        listMyLoading = new ArrayList<>();
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void setViewsListeners() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listMyLoading.clear();
                myLoadingsAdapter.notifyDataSetChanged();
                page = 0;
                getMyLoadingsFromServer();
            }
        });

        myLoadingsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                listMyLoading.add(null);
                myLoadingsAdapter.notifyItemInserted(listMyLoading.size() - 1);

                //Load more data for reyclerview
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (listMyLoading.size() > 0) {
                            listMyLoading.remove(listMyLoading.size() - 1);
                            myLoadingsAdapter.notifyItemRemoved(listMyLoading.size());
                        }

                        page++;
                        getMyLoadingsFromServer();
                    }
                }, 2000);
            }
        });
    }

    private void initSwipeRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initRecyclerView() {
        myLoadingsAdapter = new MyLoadingsAdapter(getActivity(), rvMyLoadings, listMyLoading);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvMyLoadings.setHasFixedSize(true);
        rvMyLoadings.setLayoutManager(mLayoutManager);
        rvMyLoadings.addItemDecoration(new DividerItemDecoration(getActivity(), 0));
        rvMyLoadings.setItemAnimator(new DefaultItemAnimator());
        rvMyLoadings.setAdapter(myLoadingsAdapter);
    }

    private void getMyLoadingsFromServer() {
        OkHttpHelper okHttpHelper = new OkHttpHelper(getActivity(), Configuration.API_GET_LOADING, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.getMyLoadings(String.valueOf(page), Configuration.LIMITATION, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                if (!swipeContainer.isRefreshing() && !myLoadingsAdapter.isLoading())
                    ((MainActivity) getActivity()).showDialogProgress();
            }

            @Override
            public void onResponse(JSONObject result) {
                prepareData(result);
            }

            @Override
            public void onRequestReject(String message) {
                stopSwipeRefreshing();
                ((MainActivity) getActivity()).dismissDialogProgress();
                ((MainActivity) getActivity()).showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                stopSwipeRefreshing();
                ((MainActivity) getActivity()).dismissDialogProgress();
                ((MainActivity) getActivity()).showToastError(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                stopSwipeRefreshing();
                ((MainActivity) getActivity()).dismissDialogProgress();
            }

            @Override
            public void onFinish() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeContainer.setRefreshing(false);
                        myLoadingsAdapter.setLoaded();
                        ((MainActivity) getActivity()).dismissDialogProgress();
                    }
                });
            }
        });
    }

    private void prepareData(final JSONObject result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        JSONArray jsonArray = result.getJSONArray("data");

                        if (page == 0 && (jsonArray == null || jsonArray.length() <= 0)) {
                            rlNoItemFound.setVisibility(View.VISIBLE);
                            rvMyLoadings.setVisibility(View.GONE);
                            return;
                        } else {
                            rlNoItemFound.setVisibility(View.GONE);
                            rvMyLoadings.setVisibility(View.VISIBLE);
                        }

                        if (page > 0 && jsonArray.length() <= 0)
                            page--;
                        else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String id = jsonArray.optJSONObject(i).getString("Id");
                                String moneyType = jsonArray.optJSONObject(i).optString("MoneyType");
                                String truckType = jsonArray.optJSONObject(i).optString("TruckType").replace("/", " ، ");

                                String mabdaName;
                                if (jsonArray.optJSONObject(i).optJSONArray("MabdaName").optJSONObject(0) != null)
                                    mabdaName = jsonArray.optJSONObject(i).optJSONArray("MabdaName").optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + jsonArray.optJSONObject(i).optJSONArray("MabdaName").optJSONObject(0).optJSONObject("PlaceName").optString("city");
                                else
                                    mabdaName = "نامشخص";

                                String maghsadName;
                                if (jsonArray.optJSONObject(i).optJSONArray("MaghsadName").optJSONObject(jsonArray.optJSONObject(i).optJSONArray("MaghsadName").length() - 1) != null)
                                    maghsadName = jsonArray.optJSONObject(i).optJSONArray("MaghsadName").optJSONObject(jsonArray.optJSONObject(i).optJSONArray("MaghsadName").length() - 1).optJSONObject("PlaceName").optString("province") + "، " + jsonArray.optJSONObject(i).optJSONArray("MaghsadName").optJSONObject(jsonArray.optJSONObject(i).optJSONArray("MaghsadName").length() - 1).optJSONObject("PlaceName").optString("city");
                                else
                                    maghsadName = "نامشخص";

                                String shipmentType = jsonArray.optJSONObject(i).optString("ShipmentType");
                                String pic = jsonArray.optJSONObject(i).optString("PicPath");
                                int driverCount = jsonArray.optJSONObject(i).optInt("DriverCnt", 0);
                                int money = jsonArray.optJSONObject(i).optInt("Money", 0);
                                String loadDate = jsonArray.optJSONObject(i).optString("loadDate");
                                listMyLoading.add(new MyLoadingInfo(id, moneyType, truckType, mabdaName, maghsadName, shipmentType, pic, driverCount, money, loadDate));
                            }

                            myLoadingsAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ((MainActivity) getActivity()).showToastError(result.getString("message"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void stopSwipeRefreshing() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (listMyLoading != null) {
                page = 0;
                listMyLoading.clear();
                myLoadingsAdapter.notifyDataSetChanged();
            }
            getMyLoadingsFromServer();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && requestCode == Configuration.REQUEST_CODE_REFRESH_DATA) {
            getMyLoadingsFromServer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SPreferences.getInstance(getActivity()).hasRefresh()) {
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