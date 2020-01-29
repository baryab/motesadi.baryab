package com.noavaran.system.vira.baryab.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.noavaran.system.vira.baryab.R;

public class RoutingActivity extends AppCompatActivity {
    private RecyclerView rv_LoadingProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routing);


        initFindView();
        initComponents();
    }

    private void initComponents() {

    }


    private void initFindView() {
        rv_LoadingProvince = findViewById(R.id.acRouting_rcSelectState);

        findViewById(R.id.acRouting_lyt_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FindLocationActivity.class));
            }
        });

        findViewById(R.id.acRouting_btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }
}
