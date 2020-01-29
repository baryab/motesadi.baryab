package com.noavaran.system.vira.baryab.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.database.models.ProvinceType;

import org.w3c.dom.Text;

import java.util.List;

public class ProvinceTypeAdapter extends RecyclerView.Adapter<ProvinceTypeAdapter.MyViewHolder> {
    private Context context;
    private List<ProvinceType> provinceType;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Text txtTitle;
        LinearLayout lyMain;

        public MyViewHolder( View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txt_title);
            lyMain = view.findViewById(R.id.layout_main);
        }
    }

    public ProvinceTypeAdapter(Context context, List<ProvinceType> provinceTypes){
        this.context = context;
        this.provinceType = provinceTypes;

    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_province_type, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        final ProvinceType province = provinceType.get(position);
        holder.txtTitle.setData(province.getCityName());
        holder.lyMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, province.getCityName(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return provinceType.size();
    }


}
