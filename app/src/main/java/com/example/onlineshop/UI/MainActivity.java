package com.example.onlineshop.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshop.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    
    ImageView iv_logout;
    CircleImageView img_profile;
    TextView tv_username,tv_email;
    CardView cd_product,cd_profile,cd_trans,cd_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_logout = findViewById(R.id.iv_logout);
        img_profile = findViewById(R.id.img_profile);
        tv_username = findViewById(R.id.tv_profile_name);
        tv_email = findViewById(R.id.tv_profile_email);
        cd_product = findViewById(R.id.cd_product);
        cd_profile = findViewById(R.id.cd_profile);
        cd_trans = findViewById(R.id.cd_trans);
        cd_setting = findViewById(R.id.cd_setting);

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Image", Toast.LENGTH_SHORT).show();
            }
        });

        cd_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cd_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cd_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cd_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));
            }
        });
    }
}