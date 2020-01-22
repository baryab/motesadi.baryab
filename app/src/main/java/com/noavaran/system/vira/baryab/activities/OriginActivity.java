package com.noavaran.system.vira.baryab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.fragments.NewLoadFragment;

public class OriginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_origin);

        initFindView();
    }

    private void initFindView() {
        findViewById(R.id.acOrigin_lyt_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FindLocationActivity.class));
            }
        });

        findViewById(R.id.acOrigin_btnBack).setOnClickListener(new View.OnClickListener() {
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
