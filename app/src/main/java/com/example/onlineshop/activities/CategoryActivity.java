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
import com.example.onlineshop.adapters.ProductCategoryAdapter;
import com.example.onlineshop.models.ProductCategory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    private FloatingActionButton fabAdd;
    private RecyclerView rvCategories;
    private ArrayList<ProductCategory> listProductCategory = new ArrayList<>();
    private ProductCategoryAdapter productCategoryAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        fabAdd = findViewById(R.id.fab_add);
        rvCategories = findViewById(R.id.rv_categories);

        rvCategories.setHasFixedSize(true);
        progressDialog = new ProgressDialog(this);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, CreateCategoryActivity.class);
                startActivity(intent);
            }
        });

        showProductCategories();
        getProductCategories();
    }

    private void showProductCategories() {
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        productCategoryAdapter = new ProductCategoryAdapter(listProductCategory);
        rvCategories.setAdapter(productCategoryAdapter);
    }

    private void getProductCategories() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        SharedPreferences sp = getSharedPreferences("online_shop", MODE_PRIVATE);
        String tokenShop = sp.getString("token_shop", "");

        AndroidNetworking.get(Constants.API + "/product-categories")
                .addHeaders("Authorization", "Bearer " + tokenShop)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {
                                JSONArray data = response.getJSONArray("data");

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject item = data.getJSONObject(i);

                                    ProductCategory productCategory = new ProductCategory();
                                    productCategory.setId(item.getString("id"));
                                    productCategory.setName(item.getString("name"));
                                    listProductCategory.add(productCategory);
                                }

                                productCategoryAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CategoryActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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