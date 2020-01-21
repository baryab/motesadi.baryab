package com.noavaran.system.vira.baryab.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomRadioButton;
import com.noavaran.system.vira.baryab.info.LoadingFareTypeInfo;

import java.util.List;

public class LoadingFareTypeAdapter extends RecyclerView.Adapter<LoadingFareTypeAdapter.LoadingFareTypeHolder> {
    private List<LoadingFareTypeInfo> listLoadingFareTypeInfo;
    private LoadingFareTypeInfo loadingFareTypeInfo;
    private View itemView;

    public int mSelectedItem = 0;

    private onItemClickListener onItemClickListener;
    public interface onItemClickListener {
        public abstract void onClick(int position);
    }

    public LoadingFareTypeAdapter(List<LoadingFareTypeInfo> listLoadingFareTypeInfo) {
        this.listLoadingFareTypeInfo = listLoadingFareTypeInfo;
    }

    @Override
    public LoadingFareTypeAdapter.LoadingFareTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_loading_fare_type, parent, false);

        return new LoadingFareTypeAdapter.LoadingFareTypeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LoadingFareTypeAdapter.LoadingFareTypeHolder holder, int position) {
        this.loadingFareTypeInfo = this.listLoadingFareTypeInfo.get(position);
        holder.rbLoadingFareType.setText(loadingFareTypeInfo.getName());
        holder.rbLoadingFareType.setChecked(position == mSelectedItem);
    }

    @Override
    public int getItemCount() {
        return listLoadingFareTypeInfo.size();
    }

    public class LoadingFareTypeHolder extends RecyclerView.ViewHolder {
        public CustomRadioButton rbLoadingFareType;

        public LoadingFareTypeHolder(View view) {
            super(view);

            rbLoadingFareType = (CustomRadioButton) view.findViewById(R.id.rlLoadingFareType_rbLoadingFareTypeTon);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, listLoadingFareTypeInfo.size());
                    onItemClickListener.onClick(getAdapterPosition());
                }
            };

            rbLoadingFareType.setOnClickListener(clickListener);
        }
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
