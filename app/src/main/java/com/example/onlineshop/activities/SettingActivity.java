package com.example.onlineshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.R;

public class SettingActivity extends AppCompatActivity {
    private LinearLayout ln_info, ln_ganti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ln_info = findViewById(R.id.ln_info);
        ln_ganti = findViewById(R.id.ln_ganti);

        ln_ganti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChangeShopActivity.class));
            }
        });

        ln_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ShopInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}