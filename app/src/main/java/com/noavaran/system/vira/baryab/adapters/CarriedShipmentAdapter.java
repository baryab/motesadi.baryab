package com.noavaran.system.vira.baryab.adapters;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.MainActivity;
import com.noavaran.system.vira.baryab.customviews.CustomRatingBar;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.dialogs.ConfirmDialog;
import com.noavaran.system.vira.baryab.enums.ShipmentEditTypeEnum;
import com.noavaran.system.vira.baryab.helpers.ActivitiesHelpers;
import com.noavaran.system.vira.baryab.info.CarriedShipmentInfo;
import com.noavaran.system.vira.baryab.interfaces.OnLoadMoreListener;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.DateUtil;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;
import com.noavaran.system.vira.baryab.utils.PersianDateUtil;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

public class CarriedShipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CarriedShipmentInfo> listCarriedShipments;
    private CarriedShipmentInfo carriedShipmentInfo;

    private LinearLayoutManager linearLayoutManager;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int lastPosition = -1;

    public CarriedShipmentAdapter(Context context, RecyclerView recyclerView, List<CarriedShipmentInfo> listCarriedShipments) {
        this.context = context;
        this.listCarriedShipments = listCarriedShipments;

        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    if (linearLayoutManager == null)
                        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }

                        isLoading = true;
                    }
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_list_carried_shipment, parent, false);

            return new CarriedShipmentHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_item, parent, false);

            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CarriedShipmentHolder) {
            this.carriedShipmentInfo = this.listCarriedShipments.get(position);

            CarriedShipmentHolder carriedShipmentHolder = (CarriedShipmentHolder) holder;

            if (!GlobalUtils.IsNullOrEmpty(String.valueOf(this.carriedShipmentInfo.getDate())))
                carriedShipmentHolder.tvDate.setText(PersianDateUtil.getPersianDate(this.carriedShipmentInfo.getDate()));
            else
                carriedShipmentHolder.tvDate.setText(PersianDateUtil.getPersianDate(DateUtil.getCurrentDateAsString(DateUtil.DATE_FORMAT)));

            carriedShipmentHolder.tvTruckType.setText(this.carriedShipmentInfo.getTruckType());
            carriedShipmentHolder.tvShipmentType.setText(getWeightString(this.carriedShipmentInfo.getWeight()) + " " + this.carriedShipmentInfo.getShipmentType());
            carriedShipmentHolder.tvOrigin.setText(this.carriedShipmentInfo.getOriginsName());
            carriedShipmentHolder.tvDestination.setText(this.carriedShipmentInfo.getDestinationName());

            Picasso.with(context).load(this.carriedShipmentInfo.getDriverPhoto()).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(carriedShipmentHolder.ivDriverPhoto);

            carriedShipmentHolder.tvDriverName.setText(this.carriedShipmentInfo.getDriverName());
            carriedShipmentHolder.rbDriverRating.setScore(this.carriedShipmentInfo.getDriverRating());

            try {
                if (!GlobalUtils.IsNullOrEmpty(this.carriedShipmentInfo.getPlateLicenseNumber())) {
                    String[] strSplitedPlateLicenseNumber = this.carriedShipmentInfo.getPlateLicenseNumber().split(" ");
                    carriedShipmentHolder.tvPlateLicenseNumber.setText(strSplitedPlateLicenseNumber[0] + "   " + strSplitedPlateLicenseNumber[1] + "   " + strSplitedPlateLicenseNumber[2]);
                    carriedShipmentHolder.tvPlateLicenseIranNumber.setText(strSplitedPlateLicenseNumber[3]);
                } else {
                    carriedShipmentHolder.tvPlateLicenseNumber.setText("");
                    carriedShipmentHolder.tvPlateLicenseIranNumber.setText("");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

//            loadAnimation(holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    private void loadAnimation(RecyclerView.ViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return listCarriedShipments.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listCarriedShipments.size();
    }

    private String getWeightString(float weight) {
        String strWeight = "";
//
//        float ton = weight / 1000;
//        float kilo = weight % 1000;
//
//        if (ton > 0) {
//            if (kilo > 0) {
//                strWeight = ton + " تن " + kilo + " کیلوگرم ";
//            } else {
//                strWeight = ton + " تن ";
//            }
//        } else {
//            strWeight = kilo + " کیلوگرم ";
//        }

        strWeight = weight + " تن ";

        return strWeight;
    }

    public class CarriedShipmentHolder extends RecyclerView.ViewHolder {
        private CustomTextView tvDate;
        private CustomTextView tvTruckType;
        private CustomTextView tvShipmentType;
        private CustomTextView tvOrigin;
        private CustomTextView tvDestination;
        private ImageView ivDriverPhoto;
        private CustomTextView tvDriverName;
        private CustomRatingBar rbDriverRating;
        private CustomTextView tvPlateLicenseNumber;
        private CustomTextView tvPlateLicenseIranNumber;
        private CustomTextView btnRequestAgain;
        private CustomTextView btnMoreDetails;

        public CarriedShipmentHolder(View view) {
            super(view);

            tvDate = (CustomTextView) view.findViewById(R.id.rlCarriedShipment_tvDate);
            tvTruckType = (CustomTextView) view.findViewById(R.id.rlCarriedShipment_tvTruckType);
            tvShipmentType = (CustomTextView) view.findViewById(R.id.rlCarriedShipment_tvShipmentType);
            tvOrigin = (CustomTextView) view.findViewById(R.id.rlCarriedShipment_tvOrigin);
            tvDestination = (CustomTextView) view.findViewById(R.id.rlCarriedShipment_tvDestination);
            ivDriverPhoto = (ImageView) view.findViewById(R.id.rlCarriedShipment_ivDriverPhoto);
            tvDriverName = (CustomTextView) view.findViewById(R.id.rlCarriedShipment_tvDriverName);
            rbDriverRating = (CustomRatingBar) view.findViewById(R.id.rlCarriedShipment_rbDriverRating);
            tvPlateLicenseNumber = (CustomTextView) view.findViewById(R.id.rlCarriedShipment_tvPlateLicenseNumber);
            tvPlateLicenseIranNumber = (CustomTextView) view.findViewById(R.id.rlCarriedShipment_tvPlateLicenseIranNumber);
            btnRequestAgain = (CustomTextView) view.findViewById(R.id.rlCarriedShipment_btnRequestAgain);
            btnMoreDetails = (CustomTextView) view.findViewById(R.id.rlCarriedShipment_btnMoreDetails);

            btnRequestAgain.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (getAdapterPosition() >= 0)
                        getCarriedShipmentDetailFromServer(listCarriedShipments.get(getAdapterPosition()).getShipmentId());
                }
            });

            btnMoreDetails.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (getAdapterPosition() >= 0)
                        ActivitiesHelpers.getInstance(context).gotoActivityCarriedShipmentDetail(listCarriedShipments.get(getAdapterPosition()).getShipmentId());
                }
            });
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    private void getCarriedShipmentDetailFromServer(String shipmentId) {
        OkHttpHelper okHttpHelper = new OkHttpHelper(context, Configuration.API_GET_CARRIED_LOADING_DETAILS, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.getCarriedShipmentsDetails(shipmentId, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                ((MainActivity) context).showDialogProgress();
            }

            @Override
            public void onResponse(JSONObject result) {
                loadCarriedShipmentDetails(result);
            }

            @Override
            public void onRequestReject(String message) {
                ((MainActivity) context).dismissDialogProgress();
                ((MainActivity) context).showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                ((MainActivity) context).dismissDialogProgress();
                ((MainActivity) context).showToastError(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                ((MainActivity) context).dismissDialogProgress();
            }

            @Override
            public void onFinish() {
                ((MainActivity) context).dismissDialogProgress();
            }
        });
    }

    public void loadCarriedShipmentDetails(final JSONObject result) {
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        String shipmentDetail = result.optJSONObject("data").toString();
                        showConfirmationDialogToRequestShipmentAgain(shipmentDetail);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void showConfirmationDialogToRequestShipmentAgain(final String shipmentDetail) {
        ConfirmDialog confirmDialog = new ConfirmDialog(context, context.getString(R.string.app_name), "آیا مایل به درخواست مجدد این بار هستید؟");
        confirmDialog.setOnClickListener(new ConfirmDialog.OnClickListener() {
            @Override
            public void onConfirm() {
                ActivitiesHelpers.getInstance(context).gotoActivityEditShipment(shipmentDetail, ShipmentEditTypeEnum.requestAgain.getValue());
            }

            @Override
            public void onCancel() {

            }
        });
        confirmDialog.show();
    }
}