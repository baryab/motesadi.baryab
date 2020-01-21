package com.noavaran.system.vira.baryab.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.database.models.TruckType;

import java.util.List;

public class CarTypeChooserAdapter extends RecyclerView.Adapter<CarTypeChooserAdapter.CarTypeHolder> {
    private List<TruckType> listTruckType;
    private TruckType truckType;
    private View itemView;

    public CarTypeChooserAdapter(List<TruckType> listTruckType) {
        this.listTruckType = listTruckType;
    }

    @Override
    public CarTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_car_type, parent, false);

        return new CarTypeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarTypeHolder holder, int position) {
        this.truckType = this.listTruckType.get(position);
        holder.text.setText(truckType.getName());

        if (truckType.isHasChild())
            holder.icon.setVisibility(View.VISIBLE);
        else
            holder.icon.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listTruckType.size();
    }

    public class CarTypeHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView icon;

        public CarTypeHolder(View view) {
            super(view);

            text = (TextView) view.findViewById(R.id.rlCarType_tvText);
            icon = (TextView) view.findViewById(R.id.rlCarType_tIcon);
        }
    }
}