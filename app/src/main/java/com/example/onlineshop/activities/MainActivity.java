package com.example.onlineshop.activities;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.onlineshop.Constants;
import com.example.onlineshop.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.onlineshop.Constants.SHARED_PREFS;
import static com.example.onlineshop.Constants.TOKEN_SHOP;
import static com.example.onlineshop.Constants.TOKEN_USER;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private CircleImageView img_profile;
    private TextView tv_username, tv_email;
    private CardView cd_product, cd_profile, cd_trans, cd_setting, cdDiscount,cd_kategori;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private String tokenShop, tokenUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_profile = findViewById(R.id.img_profile);
        tv_username = findViewById(R.id.tv_profile_name);
        tv_email = findViewById(R.id.tv_profile_email);
        cd_product = findViewById(R.id.cd_product);
        cd_profile = findViewById(R.id.cd_profile);
        cd_trans = findViewById(R.id.cd_trans);
        cd_setting = findViewById(R.id.cd_setting);
        cdDiscount = findViewById(R.id.cd_discount);
        cd_kategori = findViewById(R.id.cd_category);

        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        tokenUser = sharedPreferences.getString(TOKEN_USER, "");
        tokenShop = sharedPreferences.getString(TOKEN_SHOP, "");

        getUser();
        checkShop();

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Image", Toast.LENGTH_SHORT).show();
            }
        });

        cd_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
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
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
            }
        });

        cdDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DiscountActivity.class);
                startActivity(intent);
            }
        });

        cd_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CategoryActivity.class));
            }
        });

        progressDialog = new ProgressDialog(this);

        getUser();
        checkShop();
    }

    private void getUser() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();

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
                            Log.d(TAG, "onError errorCode : " + anError.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + anError.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + anError.getErrorDetail());
                        } else {
                            Log.d(TAG, "onError errorDetail : " + anError.getErrorDetail());
                        }
                    }
                });
    }

    private void checkShop() {
        if (tokenShop.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, ChangeShopActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkShop();
    }
}