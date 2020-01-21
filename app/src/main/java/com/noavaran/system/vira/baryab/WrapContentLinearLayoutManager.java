package com.noavaran.system.vira.baryab;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

public class WrapContentLinearLayoutManager extends LinearLayoutManager {
    private Context context;

    public WrapContentLinearLayoutManager(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.d("probe", "meet a IOOBE in RecyclerView");
        }
    }
}