package com.example.onlineshop.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        String tokenUser = sp.getString("token_user", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tokenUser.isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, CreateShopActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 500);

    }
}
