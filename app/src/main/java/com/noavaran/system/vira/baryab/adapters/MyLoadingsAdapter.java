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
import android.widget.RelativeLayout;

import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.helpers.ActivitiesHelpers;
import com.noavaran.system.vira.baryab.info.MyLoadingInfo;
import com.noavaran.system.vira.baryab.interfaces.OnLoadMoreListener;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.DateUtil;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;
import com.noavaran.system.vira.baryab.utils.NumberCommafy;
import com.noavaran.system.vira.baryab.utils.PersianDateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyLoadingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MyLoadingInfo> listMyLoading;
    private MyLoadingInfo myLoadingInfo;

    private LinearLayoutManager linearLayoutManager;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int lastPosition = -1;

    public MyLoadingsAdapter(Context context, RecyclerView recyclerView, List<MyLoadingInfo> listMyLoading) {
        this.context = context;
        this.listMyLoading = listMyLoading;

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
            View view = LayoutInflater.from(context).inflate(R.layout.row_list_my_loadings, parent, false);

            return new MyLoadingsHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyLoadingsHolder) {
            this.myLoadingInfo = this.listMyLoading.get(position);

            MyLoadingsHolder myLoadingsHolder = (MyLoadingsHolder) holder;

            Picasso.with(context).load(Configuration.BASE_IMAGE_URL + this.myLoadingInfo.getPic()).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(myLoadingsHolder.ivLoadingImage);
            myLoadingsHolder.tvDriverCount.setText(this.myLoadingInfo.getDriverCount() + "");
            myLoadingsHolder.tvCarType.setText(this.myLoadingInfo.getTruckType());
            myLoadingsHolder.tvLoadingType.setText(this.myLoadingInfo.getShipmentType());
            myLoadingsHolder.tvLoadingFareType.setText(this.myLoadingInfo.getMoneyType());

            if (this.myLoadingInfo.getMoneyType().equals("صافی") || this.myLoadingInfo.getMoneyType().equals("تنی")) {
                myLoadingsHolder.tvLoadingFare.setVisibility(View.VISIBLE);
                myLoadingsHolder.tvLoadingFare.setText("(" + NumberCommafy.decimalFormatCommafy(String.valueOf(this.myLoadingInfo.getMoney())) + " تومان " + ")");
            } else {
                myLoadingsHolder.tvLoadingFare.setVisibility(View.GONE);
                myLoadingsHolder.tvLoadingFare.setText("(" + NumberCommafy.decimalFormatCommafy(String.valueOf(this.myLoadingInfo.getMoney())) + " تومان " + ")");
            }

            if (!GlobalUtils.IsNullOrEmpty(this.myLoadingInfo.getLoadDate()))
                myLoadingsHolder.tvDate.setText(PersianDateUtil.getPersianDate(DateUtil.getDateFromDateString(this.myLoadingInfo.getLoadDate(), DateUtil.DATE_FORMAT).getTime()));
            else
                myLoadingsHolder.tvDate.setText(PersianDateUtil.getPersianDate(DateUtil.getDateFromDateString(DateUtil.getCurrentDateAsString(DateUtil.DATE_FORMAT), DateUtil.DATE_FORMAT).getTime()));

            myLoadingsHolder.tvOrigin.setText(this.myLoadingInfo.getMabdaName());
            myLoadingsHolder.tvDestination.setText(this.myLoadingInfo.getMaghsadName());

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
        return listMyLoading.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listMyLoading.size();
    }

    public class MyLoadingsHolder extends RecyclerView.ViewHolder {
        private RelativeLayout container;
        private ImageView ivLoadingImage;
        private CustomTextView tvDriverCount;
        private CustomTextView tvCarType;
        private CustomTextView tvLoadingType;
        private CustomTextView tvLoadingFareType;
        private CustomTextView tvLoadingFare;
        private CustomTextView tvDate;
        private CustomTextView tvOrigin;
        private CustomTextView tvDestination;

        public MyLoadingsHolder(View view) {
            super(view);

            container = view.findViewById(R.id.rlMyLodaing_container);
            ivLoadingImage = view.findViewById(R.id.rlMyLodaing_ivLoadingImage);
            tvDriverCount = view.findViewById(R.id.rlMyLodaing_tvDriverCount);
            tvCarType = view.findViewById(R.id.rlMyLodaing_tvCarType);
            tvLoadingType = view.findViewById(R.id.rlMyLodaing_tvLoadingType);
            tvLoadingFareType = view.findViewById(R.id.rlMyLodaing_tvLoadingFareType);
            tvLoadingFare = view.findViewById(R.id.rlMyLodaing_tvLoadingFare);
            tvDate = view.findViewById(R.id.rlMyLodaing_tvDate);
            tvOrigin = view.findViewById(R.id.rlMyLodaing_tvOrigin);
            tvDestination = view.findViewById(R.id.rlMyLodaing_tvDestination);

            container.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (getAdapterPosition() >= 0)
                        ActivitiesHelpers.getInstance(context).gotoMyLoadingDetailsActivity(listMyLoading.get(getAdapterPosition()).getId());
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