package com.example.onlineshop.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.onlineshop.Constants;
import com.example.onlineshop.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    ImageView iv_logout;
    CircleImageView img_profile;
    TextView tv_username,tv_email;
    CardView cd_product,cd_profile,cd_trans,cd_setting, cdDiscount;

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
        cdDiscount = findViewById(R.id.cd_discount);

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Apakah anda yakin ingin keluar ?")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences sp = getSharedPreferences("online_shop", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.apply();

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).create().show();
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
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(intent);
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
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        cd_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));
            }
        });

        cdDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DiscountActivity.class);
                startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(this);

        getUser();
    }

    private void getUser() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        SharedPreferences sp = getSharedPreferences("online_shop", MODE_PRIVATE);
        String tokenUser = sp.getString("token_user", "");

        AndroidNetworking.get(Constants.API + "/auth-decode")
                .addHeaders("Authorization", "Bearer " + tokenUser)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            String email = response.getString("email");
                            String phone = response.getString("phone_number");

                            tv_username.setText(name);
                            tv_email.setText(email);

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(MainActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        if (anError.getErrorCode() != 0) {
                            Log.d("TAG", "onError errorCode : " + anError.getErrorCode());
                            Log.d("TAG", "onError errorBody : " + anError.getErrorBody());
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        } else {
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    }
                });
    }
}