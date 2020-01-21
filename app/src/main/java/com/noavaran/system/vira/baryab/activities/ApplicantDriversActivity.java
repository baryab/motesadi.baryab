package com.noavaran.system.vira.baryab.activities;

import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.adapters.ApplicantDriversAdapter;
import com.noavaran.system.vira.baryab.info.ApplicantDriversInfo;
import com.noavaran.system.vira.baryab.interfaces.OnLoadMoreListener;
import com.noavaran.system.vira.baryab.utils.PersianDateUtil;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ApplicantDriversActivity extends BaseActivity {
    private RelativeLayout rlNoItemFound;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvApplicantDrivers;
    private ApplicantDriversAdapter adapter;
    private List<ApplicantDriversInfo> listApplicantDrivers;

    private ApplicantDriversInfo applicantDriversInfo;

    private String shipmentId;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_drivers);

        findViews();
        initComponents();
        setViewsListeners();
        getIntents();
        getApplicantDriversFromServer(shipmentId);
    }

    private void findViews() {
        rlNoItemFound = (RelativeLayout) findViewById(R.id.acApplicantDrivers_rlNoItemFound);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.acApplicantDrivers_swipeContainer);
        rvApplicantDrivers = (RecyclerView) findViewById(R.id.acApplicantDrivers_rvMyLoadings);
    }

    private void initComponents() {
        listApplicantDrivers = new ArrayList<>();
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void setViewsListeners() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listApplicantDrivers.clear();
                        adapter.notifyDataSetChanged();
                        page = 0;
                        getApplicantDriversFromServer(shipmentId);
                    }
                }, 1500);
            }
        });

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                listApplicantDrivers.add(null);
                adapter.notifyItemInserted(listApplicantDrivers.size() - 1);

                //Load more data for reyclerview
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (listApplicantDrivers.size() > 0) {
                            listApplicantDrivers.remove(listApplicantDrivers.size() - 1);
                            adapter.notifyItemRemoved(listApplicantDrivers.size());
                        }

                        page++;
                        getApplicantDriversFromServer(shipmentId);
                    }
                }, 2000);
            }
        });
    }

    private void initSwipeRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void getIntents() {
        if (getIntent().getExtras() != null) {
            shipmentId = getIntent().getExtras().getString("shipmentId");
        }
    }

    private void initRecyclerView() {
        adapter = new ApplicantDriversAdapter(ApplicantDriversActivity.this, rvApplicantDrivers, listApplicantDrivers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ApplicantDriversActivity.this.getApplicationContext());
        rvApplicantDrivers.setHasFixedSize(true);
        rvApplicantDrivers.setLayoutManager(mLayoutManager);
        rvApplicantDrivers.addItemDecoration(new DividerItemDecoration(ApplicantDriversActivity.this, 0));
        rvApplicantDrivers.setItemAnimator(new DefaultItemAnimator());
        rvApplicantDrivers.setAdapter(adapter);
    }

    public void getApplicantDriversFromServer(String shipmentId) {
        OkHttpHelper okHttpHelper = new OkHttpHelper(ApplicantDriversActivity.this, Configuration.API_LOADING_DRIVERS, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.getApplicantDrivers(String.valueOf(page), Configuration.LIMITATION, shipmentId, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                if (!swipeContainer.isRefreshing() && !adapter.isLoading())
                    showDialogProgress();
            }

            @Override
            public void onResponse(JSONObject result) {
                prepareData(result);
            }

            @Override
            public void onRequestReject(String message) {
                stopSwipeRefreshing();
                dismissDialogProgress();
                showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                stopSwipeRefreshing();
                dismissDialogProgress();
                showToastError(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                stopSwipeRefreshing();
                dismissDialogProgress();
            }

            @Override
            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeContainer.setRefreshing(false);
                        adapter.setLoaded();
                        dismissDialogProgress();
                    }
                });
            }
        });
    }

    private void prepareData(final JSONObject result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        JSONArray jsonArray = result.getJSONArray("data");

                        if (page == 0 && (jsonArray == null || jsonArray.length() <= 0)) {
                            rlNoItemFound.setVisibility(View.VISIBLE);
                            rvApplicantDrivers.setVisibility(View.GONE);
                            return;
                        } else {
                            rlNoItemFound.setVisibility(View.GONE);
                            rvApplicantDrivers.setVisibility(View.VISIBLE);
                        }

                        if (page > 0 && jsonArray.length() <= 0)
                            page--;
                        else {
                            System.out.println(result);
                            for (int i = 0; i < result.optJSONArray("data").length(); i++) {
                                String userid = result.optJSONArray("data").optJSONObject(i).optString("Userid");
                                String fullname = result.optJSONArray("data").optJSONObject(i).optString("Fullname");
                                String picPath = result.optJSONArray("data").optJSONObject(i).optString("picPath");
                                int rate = result.optJSONArray("data").optJSONObject(i).optInt("rate", 0);
                                String phoneNumber = result.optJSONArray("data").optJSONObject(i).optString("phoneNumber");
                                String truckType = result.optJSONArray("data").optJSONObject(i).optString("truckType");
                                float minlength = BigDecimal.valueOf(result.optJSONArray("data").optJSONObject(i).optDouble("minlength", 0)).floatValue();
                                float minwidth = BigDecimal.valueOf(result.optJSONArray("data").optJSONObject(i).optDouble("minwidth", 0)).floatValue();
                                float minheight = BigDecimal.valueOf(result.optJSONArray("data").optJSONObject(i).optDouble("minheight", 0)).floatValue();
                                float maxlength = BigDecimal.valueOf(result.optJSONArray("data").optJSONObject(i).optDouble("maxlength", 0)).floatValue();
                                float maxwidth = BigDecimal.valueOf(result.optJSONArray("data").optJSONObject(i).optDouble("maxwidth", 0)).floatValue();
                                float maxheight = BigDecimal.valueOf(result.optJSONArray("data").optJSONObject(i).optDouble("maxheight", 0)).floatValue();
                                String licensePlate = result.optJSONArray("data").optJSONObject(i).optString("licensePlate");
                                long requestDate = result.optJSONArray("data").optJSONObject(i).optLong("RequestDate", 0);

                                listApplicantDrivers.add(new ApplicantDriversInfo(userid, shipmentId, fullname, Configuration.BASE_IMAGE_URL + picPath, rate, phoneNumber, truckType, minlength, minwidth, minheight, licensePlate, PersianDateUtil.getPersianDate(requestDate)));
                            }
//
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void stopSwipeRefreshing() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
            }
        });
    }
}