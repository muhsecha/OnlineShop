package com.example.onlineshop.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.onlineshop.R;

public class SettingActivity extends AppCompatActivity {

    LinearLayout ln_info,ln_ganti,ln_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ln_info = findViewById(R.id.ln_info);
        ln_ganti = findViewById(R.id.ln_ganti);
        ln_logout = findViewById(R.id.ln_logout);

        ln_ganti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ChangeShopActivity.class));
            }
        });
    }
}