package com.noavaran.system.vira.baryab.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomRadioButton;
import com.noavaran.system.vira.baryab.info.MoneyTypeInfo;

import java.util.List;

public class MoneyTypeAdapter extends RecyclerView.Adapter<MoneyTypeAdapter.MoneyTypeHolder> {
    private List<MoneyTypeInfo> listMoneyTypeInfo;
    private MoneyTypeInfo moneyTypeInfo;
    private View itemView;

    public int mSelectedItem = -1;

    private onItemClickListener onItemClickListener;

    public interface onItemClickListener {
        public abstract void onClick(int position);
    }

    public MoneyTypeAdapter(List<MoneyTypeInfo> listMoneyTypeInfo) {
        this.listMoneyTypeInfo = listMoneyTypeInfo;
    }

    @Override
    public MoneyTypeAdapter.MoneyTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_loading_fare_type, parent, false);

        return new MoneyTypeAdapter.MoneyTypeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MoneyTypeAdapter.MoneyTypeHolder holder, int position) {
        this.moneyTypeInfo = this.listMoneyTypeInfo.get(position);

        holder.rbLoadingFareType.setText(moneyTypeInfo.getName());

        if (moneyTypeInfo.isSelected())
            holder.rbLoadingFareType.setChecked(true);
        else
            holder.rbLoadingFareType.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return listMoneyTypeInfo.size();
    }

    public class MoneyTypeHolder extends RecyclerView.ViewHolder {
        public CustomRadioButton rbLoadingFareType;

        public MoneyTypeHolder(View view) {
            super(view);

            rbLoadingFareType = (CustomRadioButton) view.findViewById(R.id.rlLoadingFareType_rbLoadingFareTypeTon);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, listMoneyTypeInfo.size());
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