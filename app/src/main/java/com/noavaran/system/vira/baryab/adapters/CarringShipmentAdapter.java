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

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomRatingBar;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.helpers.ActivitiesHelpers;
import com.noavaran.system.vira.baryab.info.CarringShipmentInfo;
import com.noavaran.system.vira.baryab.interfaces.OnLoadMoreListener;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.DateUtil;
import com.noavaran.system.vira.baryab.utils.DialerUtil;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;
import com.noavaran.system.vira.baryab.utils.PersianDateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarringShipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CarringShipmentInfo> listCarringShipments;
    private CarringShipmentInfo carringShipmentInfo;

    private LinearLayoutManager linearLayoutManager;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int lastPosition = -1;

    public CarringShipmentAdapter(Context context, RecyclerView recyclerView, List<CarringShipmentInfo> listCarringShipments) {
        this.context = context;
        this.listCarringShipments = listCarringShipments;

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
            View view = LayoutInflater.from(context).inflate(R.layout.row_list_carring_shipment, parent, false);

            return new CarringShipmentHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_item, parent, false);

            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CarringShipmentHolder) {
            this.carringShipmentInfo = this.listCarringShipments.get(position);

            CarringShipmentHolder carringShipmentHolder = (CarringShipmentHolder) holder;

            if (!GlobalUtils.IsNullOrEmpty(String.valueOf(this.carringShipmentInfo.getDate())))
                carringShipmentHolder.tvDate.setText(PersianDateUtil.getPersianDate(this.carringShipmentInfo.getDate()));
            else
                carringShipmentHolder.tvDate.setText(PersianDateUtil.getPersianDate(DateUtil.getCurrentDateAsString(DateUtil.DATE_FORMAT)));

            carringShipmentHolder.tvTruckType.setText(this.carringShipmentInfo.getTruckType());
            carringShipmentHolder.tvShipmentType.setText(getWeightString(this.carringShipmentInfo.getWeight()) + " " + this.carringShipmentInfo.getShipmentType());
            carringShipmentHolder.tvOrigin.setText(this.carringShipmentInfo.getOriginsName());
            carringShipmentHolder.tvDestination.setText(this.carringShipmentInfo.getDestinationName());

            Picasso.with(context).load(this.carringShipmentInfo.getDriverPhoto()).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(carringShipmentHolder.ivDriverPhoto);

            carringShipmentHolder.tvDriverName.setText(this.carringShipmentInfo.getDriverName());
            carringShipmentHolder.rbDriverRating.setScore(this.carringShipmentInfo.getDriverRating());

            try {
                if (!GlobalUtils.IsNullOrEmpty(this.carringShipmentInfo.getPlateLicenseNumber())) {
                    String[] strSplitedPlateLicenseNumber = this.carringShipmentInfo.getPlateLicenseNumber().split(" ");
                    carringShipmentHolder.tvPlateLicenseNumber.setText(strSplitedPlateLicenseNumber[0] + "   " + strSplitedPlateLicenseNumber[1] + "   " + strSplitedPlateLicenseNumber[2]);
                    carringShipmentHolder.tvPlateLicenseIranNumber.setText(strSplitedPlateLicenseNumber[3]);
                } else {
                    carringShipmentHolder.tvPlateLicenseNumber.setText("");
                    carringShipmentHolder.tvPlateLicenseIranNumber.setText("");

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
        return listCarringShipments.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listCarringShipments.size();
    }

    private String getWeightString(float weight) {
        String strWeight = "";

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

    public class CarringShipmentHolder extends RecyclerView.ViewHolder {
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
        private CustomTextView btnContactRoDriver;
        private CustomTextView btnMoreDetails;

        public CarringShipmentHolder(View view) {
            super(view);

            tvDate = (CustomTextView) view.findViewById(R.id.rlCarringShipment_tvDate);
            tvTruckType = (CustomTextView) view.findViewById(R.id.rlCarringShipment_tvTruckType);
            tvShipmentType = (CustomTextView) view.findViewById(R.id.rlCarringShipment_tvShipmentType);
            tvOrigin = (CustomTextView) view.findViewById(R.id.rlCarringShipment_tvOrigin);
            tvDestination = (CustomTextView) view.findViewById(R.id.rlCarringShipment_tvDestination);
            ivDriverPhoto = (ImageView) view.findViewById(R.id.rlCarringShipment_ivDriverPhoto);
            tvDriverName = (CustomTextView) view.findViewById(R.id.rlCarringShipment_tvDriverName);
            rbDriverRating = (CustomRatingBar) view.findViewById(R.id.rlCarringShipment_rbDriverRating);
            tvPlateLicenseNumber = (CustomTextView) view.findViewById(R.id.rlCarringShipment_tvPlateLicenseNumber);
            tvPlateLicenseIranNumber = (CustomTextView) view.findViewById(R.id.rlCarringShipment_tvPlateLicenseIranNumber);
            btnContactRoDriver = (CustomTextView) view.findViewById(R.id.rlCarringShipment_btnContactRoDriver);
            btnMoreDetails = (CustomTextView) view.findViewById(R.id.rlCarringShipment_btnMoreDetails);

            btnContactRoDriver.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (getAdapterPosition() >= 0) {
                        DialerUtil.getInstance(context).openTheDialerApp(listCarringShipments.get(getAdapterPosition()).getDriverPhoneNumber());

//                        Uri call = Uri.parse("tel:" + listCarringShipments.get(getAdapterPosition()).getDriverPhoneNumber());
//                        Intent surf = new Intent(Intent.ACTION_CALL, call);
//                        context.startActivity(surf);
                    }
                }
            });

            btnMoreDetails.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (getAdapterPosition() >= 0)
                        ActivitiesHelpers.getInstance(context).gotoActivityCarryingShipmentDetail(listCarringShipments.get(getAdapterPosition()).getShipmentId());
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
}