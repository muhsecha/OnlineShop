package com.example.onlineshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.onlineshop.Constants;
import com.example.onlineshop.R;
import com.example.onlineshop.adapters.ShopAdapter;
import com.example.onlineshop.models.Shop;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChangeShopActivity extends AppCompatActivity {
    private FloatingActionButton btnAdd;
    private ShopAdapter shopAdapter;
    private RecyclerView rvShops;
    private ArrayList<Shop> listShop = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_shop);

        btnAdd = findViewById(R.id.btn_add);
        rvShops = findViewById(R.id.rv_shops);

        rvShops.setHasFixedSize(true);
        progressDialog = new ProgressDialog(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeShopActivity.this, CreateShopActivity.class);
                startActivity(intent);
            }
        });

        showShops();
        getShopActive();
    }

    private void showShops() {
        rvShops.setLayoutManager(new LinearLayoutManager(this));
        shopAdapter = new ShopAdapter(listShop);
        rvShops.setAdapter(shopAdapter);

        shopAdapter.setOnItemClickCallback(new ShopAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Shop data) {
                changeShop(data.getId());
            }
        });
    }

    private void getShops(String id) {
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        SharedPreferences sp = getSharedPreferences("online_shop", MODE_PRIVATE);
        String tokenUser = sp.getString("token_user", "");

        AndroidNetworking.get(Constants.API + "/shops")
                .addHeaders("Authorization", "Bearer " + tokenUser)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {
                                listShop.clear();
                                JSONArray data = response.getJSONArray("data");

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject item = data.getJSONObject(i);

                                    Shop shop = new Shop();
                                    shop.setId(item.getString("id"));
                                    shop.setName(item.getString("name"));
                                    shop.setActive(id.equals(item.getString("id")));
                                    listShop.add(shop);
                                }

                                shopAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ChangeShopActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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

    private void changeShop(String id) {
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        SharedPreferences sp = getSharedPreferences("online_shop", MODE_PRIVATE);
        String tokenUser = sp.getString("token_user", "");

        AndroidNetworking.post(Constants.API + "/login-shop")
                .addBodyParameter("shop_id", id)
                .addHeaders("Authorization", "Bearer " + tokenUser)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {
                                SharedPreferences sp = getSharedPreferences("online_shop", MODE_PRIVATE);
                                sp.edit().putString("token_shop", response.getString("data")).apply();

                                getShopActive();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ChangeShopActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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

    private void getShopActive() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        SharedPreferences sp = getSharedPreferences("online_shop", MODE_PRIVATE);
        String tokenShop = sp.getString("token_shop", "");

        AndroidNetworking.get(Constants.API + "/auth-decode")
                .addHeaders("Authorization", "Bearer " + tokenShop)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String id = response.getString("id");
                            getShops(id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401) {
                            getShops("null");
                        } else {
                            Toast.makeText(ChangeShopActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            if (anError.getErrorCode() != 0) {
                                Log.d("TAG", "onError errorCode : " + anError.getErrorCode());
                                Log.d("TAG", "onError errorBody : " + anError.getErrorBody());
                                Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                            } else {
                                Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                            }
                        }
                    }
                });
    }
}