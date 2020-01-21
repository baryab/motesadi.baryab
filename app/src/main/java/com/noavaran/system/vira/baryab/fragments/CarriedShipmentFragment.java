package com.noavaran.system.vira.baryab.fragments;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
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
import com.noavaran.system.vira.baryab.activities.MainActivity;
import com.noavaran.system.vira.baryab.adapters.CarriedShipmentAdapter;
import com.noavaran.system.vira.baryab.info.CarriedShipmentInfo;
import com.noavaran.system.vira.baryab.interfaces.OnLoadMoreListener;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarriedShipmentFragment extends BaseFragment {
    private View rootView;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    private FragmentActivity activity;

    private RelativeLayout rlNoItemFound;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvCarriedShipments;
    private CarriedShipmentAdapter adapter;

    private List<CarriedShipmentInfo> listCarriedShipment;

    private CarriedShipmentInfo carriedShipmentInfo;
    private int page = 0;

    public CarriedShipmentFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_carried_shipment, container, false);
        this.inflater = inflater;
        this.savedInstanceState = savedInstanceState;

        return this.rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = getActivity();

        findViews();
        initComponents();
        setViewsListeners();
    }

    private void findViews() {
        rlNoItemFound = (RelativeLayout) this.rootView.findViewById(R.id.frCarriedShipment_rlNoItemFound);
        swipeContainer = (SwipeRefreshLayout) this.rootView.findViewById(R.id.frCarriedShipment_swipeContainer);
        rvCarriedShipments = (RecyclerView) this.rootView.findViewById(R.id.frCarriedShipment_rvMyLoadings);
    }

    private void initComponents() {
        listCarriedShipment = new ArrayList<>();

        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void setViewsListeners() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listCarriedShipment.clear();
                adapter.notifyDataSetChanged();
                page = 0;
                getCarriedShipmentsFromServer();
            }
        });

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                listCarriedShipment.add(null);
                adapter.notifyItemInserted(listCarriedShipment.size() - 1);

                //Load more data for reyclerview
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (listCarriedShipment.size() > 0) {
                            listCarriedShipment.remove(listCarriedShipment.size() - 1);
                            adapter.notifyItemRemoved(listCarriedShipment.size());
                        }

                        page++;
                        getCarriedShipmentsFromServer();
                    }
                }, 2000);
            }
        });
    }

    private void initSwipeRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initRecyclerView() {
        adapter = new CarriedShipmentAdapter(getActivity(), rvCarriedShipments, listCarriedShipment);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvCarriedShipments.setHasFixedSize(true);
        rvCarriedShipments.setLayoutManager(mLayoutManager);
        rvCarriedShipments.addItemDecoration(new DividerItemDecoration(getActivity(), 0));
        rvCarriedShipments.setItemAnimator(new DefaultItemAnimator());
        rvCarriedShipments.setAdapter(adapter);
    }

    private void getCarriedShipmentsFromServer() {
        OkHttpHelper okHttpHelper = new OkHttpHelper(getActivity(), Configuration.API_GET_CARRIED_LOADING, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.getCarriedShipments(String.valueOf(page), Configuration.LIMITATION, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                if (!swipeContainer.isRefreshing())
                    showProgressDialog();
            }

            @Override
            public void onResponse(JSONObject result) {
                prepareData(result);
            }

            @Override
            public void onRequestReject(String message) {
                if (swipeContainer.isRefreshing())
                    stopSwipeRefreshing();

                ((MainActivity) getActivity()).dismissDialogProgress();
                ((MainActivity) getActivity()).showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                if (swipeContainer.isRefreshing())
                    stopSwipeRefreshing();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setLoaded();
                    }
                });

                dismissProgressDialog();
                showErrorToast(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                if (swipeContainer.isRefreshing())
                    stopSwipeRefreshing();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setLoaded();
                    }
                });

                dismissProgressDialog();
            }

            @Override
            public void onFinish() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeContainer.isRefreshing())
                            swipeContainer.setRefreshing(false);

                        adapter.setLoaded();
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
                            rvCarriedShipments.setVisibility(View.GONE);
                            return;
                        } else {
                            rlNoItemFound.setVisibility(View.GONE);
                            rvCarriedShipments.setVisibility(View.VISIBLE);
                        }

                        if (page > 0 && jsonArray.length() <= 0)
                            page--;
                        else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String shipmentId = jsonArray.optJSONObject(i).getString("DId");
                                String driverId = jsonArray.optJSONObject(i).optJSONObject("Dfo").optJSONObject("Ufo").getString("Uid");
                                long date = GlobalUtils.IsNullOrEmpty(String.valueOf(jsonArray.optJSONObject(i).optLong("CreateDate"))) ? new Date().getTime() : jsonArray.optJSONObject(i).optLong("CreateDate");
                                String truckType = jsonArray.optJSONObject(i).optJSONObject("Tfo") == null ? "" : jsonArray.optJSONObject(i).optJSONObject("Tfo").optString("Type");
                                String shipmentType = jsonArray.optJSONObject(i).optString("ship");
                                float weight = BigDecimal.valueOf(jsonArray.optJSONObject(i).optDouble("Weight", 0)).floatValue();
                                String originsName = jsonArray.optJSONObject(i).optJSONArray("orgCity") == null ? "نامشخص" : jsonArray.optJSONObject(i).optJSONArray("orgCity").optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + jsonArray.optJSONObject(i).optJSONArray("orgCity").optJSONObject(0).optJSONObject("PlaceName").optString("city");
                                String destinationName = jsonArray.optJSONObject(i).optJSONArray("decCity") == null ? "نامشخص" : jsonArray.optJSONObject(i).optJSONArray("decCity").optJSONObject(jsonArray.optJSONObject(i).optJSONArray("decCity").length() - 1).optJSONObject("PlaceName").optString("province") + "، " + jsonArray.optJSONObject(i).optJSONArray("decCity").optJSONObject(jsonArray.optJSONObject(i).optJSONArray("decCity").length() - 1).optJSONObject("PlaceName").optString("city");
                                String driverPhoto = jsonArray.optJSONObject(i).optJSONObject("Dfo").optJSONObject("Ufo").getString("Pic");
                                String driverName = jsonArray.optJSONObject(i).optJSONObject("Dfo").optJSONObject("Ufo").getString("Name");
                                float driverRating = BigDecimal.valueOf(jsonArray.optJSONObject(i).optJSONObject("Dfo").optDouble("Rate", 0)).floatValue();
                                String plateLicenseNumber = jsonArray.optJSONObject(i).optJSONObject("Tfo") == null ? "" : jsonArray.optJSONObject(i).optJSONObject("Tfo").optString("plake");

                                listCarriedShipment.add(new CarriedShipmentInfo(shipmentId, driverId, date, truckType, shipmentType, weight, originsName, destinationName, Configuration.BASE_IMAGE_URL + driverPhoto, driverName, driverRating, plateLicenseNumber));
                            }

                            adapter.notifyDataSetChanged();
//                            getListOfDriversForRatingFromServer();
                        }

                    } else {
                        ((MainActivity) getActivity()).showToastError("message");
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

    private void showProgressDialog() {
        if (getActivity() == null)
            ((MainActivity) activity).showDialogProgress();
        else
            ((MainActivity) getActivity()).showDialogProgress();
    }

    private void dismissProgressDialog() {
        if (getActivity() == null)
            ((MainActivity) activity).dismissDialogProgress();
        else
            ((MainActivity) getActivity()).dismissDialogProgress();
    }

    private void showErrorToast(String message) {
        if (getActivity() == null)
            ((MainActivity) activity).showToastError(message);
        else
            ((MainActivity) getActivity()).showToastError(message);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            page = 0;
            listCarriedShipment.clear();
            adapter.notifyDataSetChanged();
            getCarriedShipmentsFromServer();
        }
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