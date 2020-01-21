package com.noavaran.system.vira.baryab.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.ApplicantDriversActivity;
import com.noavaran.system.vira.baryab.activities.MainActivity;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.dialogs.ConfirmDialog;
import com.noavaran.system.vira.baryab.info.ApplicantDriversInfo;
import com.noavaran.system.vira.baryab.interfaces.OnLoadMoreListener;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.DialerUtil;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

public class ApplicantDriversAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ApplicantDriversInfo> listApplicantDrivers;
    private ApplicantDriversInfo applicantDriversInfo;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int lastPosition = -1;

    public ApplicantDriversAdapter(Context context, RecyclerView recyclerView, List<ApplicantDriversInfo> listApplicantDrivers) {
        this.context = context;
        this.listApplicantDrivers = listApplicantDrivers;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.row_list_applicant_driver, parent, false);

            return new ApplicantDriversHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_item, parent, false);

            return new LoadingViewHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ApplicantDriversHolder) {
            this.applicantDriversInfo = this.listApplicantDrivers.get(position);

            ApplicantDriversHolder myLoadingsHolder = (ApplicantDriversHolder) holder;

            myLoadingsHolder.tvDriverName.setText(this.applicantDriversInfo.getDriverName());
            myLoadingsHolder.tvDriverRating.setRating(this.applicantDriversInfo.getRate());
            Picasso.with(context).load(this.applicantDriversInfo.getDriverPhoto()).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(myLoadingsHolder.ivDriverPhoto);
            myLoadingsHolder.etTruckType.setText(this.applicantDriversInfo.getTruckType());

            if (this.applicantDriversInfo.getHeight() > 0)
                myLoadingsHolder.etTruckDetails.setText("طول " + this.applicantDriversInfo.getLength() + "، عرض " + this.applicantDriversInfo.getWidth() + "، ارتفاع " + this.applicantDriversInfo.getHeight() + "");
            else
                myLoadingsHolder.etTruckDetails.setText("طول " + this.applicantDriversInfo.getLength() + "، عرض " + this.applicantDriversInfo.getWidth());

            try {
                if (!GlobalUtils.IsNullOrEmpty(this.applicantDriversInfo.getLicensePlate())) {
                    String[] strSplitedPlateLicenseNumber = this.applicantDriversInfo.getLicensePlate().split(" ");
                    myLoadingsHolder.tvPlateLicenseNumber.setText(strSplitedPlateLicenseNumber[0] + "   " + strSplitedPlateLicenseNumber[1] + "   " + strSplitedPlateLicenseNumber[2]);
                    myLoadingsHolder.tvPlateLicenseIranNumber.setText(strSplitedPlateLicenseNumber[3]);
                } else {
                    myLoadingsHolder.tvPlateLicenseNumber.setText("");
                    myLoadingsHolder.tvPlateLicenseIranNumber.setText("");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            myLoadingsHolder.etRequestTime.setText(this.applicantDriversInfo.getRequestDate());

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

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return listApplicantDrivers.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listApplicantDrivers.size();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public class ApplicantDriversHolder extends RecyclerView.ViewHolder {
        private CustomTextView tvDriverName;
        private RatingBar tvDriverRating;
        private ImageView ivDriverPhoto;
        private CustomTextView etTruckType;
        private CustomTextView etTruckDetails;
        private CustomTextView tvPlateLicenseNumber;
        private CustomTextView tvPlateLicenseIranNumber;
        private CustomTextView btnConfirm;
        private CustomTextView btnDelete;
        private CustomTextView btnContact;
        private CustomTextView etRequestTime;

        public ApplicantDriversHolder(View view) {
            super(view);

            tvDriverName = (CustomTextView) view.findViewById(R.id.rlApplicantDriver_tvDriverName);
            tvDriverRating = (RatingBar) view.findViewById(R.id.rlApplicantDriver_tvDriverRating);
            ivDriverPhoto = (ImageView) view.findViewById(R.id.rlApplicantDriver_ivDriverPhoto);
            etTruckType = (CustomTextView) view.findViewById(R.id.rlApplicantDriver_etTruckType);
            etTruckDetails = (CustomTextView) view.findViewById(R.id.rlApplicantDriver_etTruckDetails);
            tvPlateLicenseNumber = (CustomTextView) view.findViewById(R.id.rlApplicantDriver_tvPlateLicenseNumber);
            tvPlateLicenseIranNumber = (CustomTextView) view.findViewById(R.id.rlApplicantDriver_tvPlateLicenseIranNumber);
            btnConfirm = (CustomTextView) view.findViewById(R.id.rlApplicantDriver_btnConfirm);
            btnDelete = (CustomTextView) view.findViewById(R.id.rlApplicantDriver_btnDelete);
            btnContact = (CustomTextView) view.findViewById(R.id.rlApplicantDriver_btnContact);
            etRequestTime = (CustomTextView) view.findViewById(R.id.rlApplicantDriver_etRequestTime);

            btnConfirm.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
//                    showConfirmationDialogToAcceptDriver(getAdapterPosition());
                    if (getAdapterPosition() >= 0)
                        doDriverAccesptForThisLoading(getAdapterPosition());
                }
            });

            btnDelete.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {

                }
            });

            btnContact.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (getAdapterPosition() >= 0) {
                        DialerUtil.getInstance(context).openTheDialerApp(listApplicantDrivers.get(getAdapterPosition()).getPhoneNumber());

//                        Uri call = Uri.parse("tel:" + listApplicantDrivers.get(getAdapterPosition()).getPhoneNumber());
//                        Intent surf = new Intent(Intent.ACTION_CALL, call);
//                        context.startActivity(surf);
                    }
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

    private void showConfirmationDialogToAcceptDriver(final int position) {
        ConfirmDialog dialog = new ConfirmDialog(context, context.getString(R.string.app_name), "آیا مایل به اختصاص دادن این بار به راننده " + listApplicantDrivers.get(position).getDriverName() + " هستید؟");
        dialog.setOnClickListener(new ConfirmDialog.OnClickListener() {
            @Override
            public void onConfirm() {
                doDriverAccesptForThisLoading(position);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void doDriverAccesptForThisLoading(int position) {
        OkHttpHelper okHttpHelper = new OkHttpHelper(context, Configuration.API_ACCEPT_DRIVERS, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.doDriverAccesptForThisLoading(listApplicantDrivers.get(position).getShipmentId(), listApplicantDrivers.get(position).getDriverId(), new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                ((ApplicantDriversActivity) context).showDialogProgress("در حال ارسال اطلاعات به سرور");
            }

            @Override
            public void onResponse(JSONObject result) {
                ((ApplicantDriversActivity) context).showToastSuccess(" عملیات با موفقیت انجام شد");
                ((ApplicantDriversActivity) context).finish();

                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }

            @Override
            public void onRequestReject(String message) {
                ((ApplicantDriversActivity) context).dismissDialogProgress();
                ((ApplicantDriversActivity) context).showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                ((ApplicantDriversActivity) context).dismissDialogProgress();
                ((ApplicantDriversActivity) context).showToastError(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                ((ApplicantDriversActivity) context).dismissDialogProgress();
            }

            @Override
            public void onFinish() {
                ((ApplicantDriversActivity) context).dismissDialogProgress();
            }
        });
    }
}
