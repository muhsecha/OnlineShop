package com.example.onlineshop.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.onlineshop.Constants;
import com.example.onlineshop.R;
import com.example.onlineshop.adapters.ProductCategoryAdapter;
import com.example.onlineshop.models.ProductCategory;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.onlineshop.Constants.SHARED_PREFS;
import static com.example.onlineshop.Constants.TOKEN_SHOP;

public class CategoryActivity extends AppCompatActivity {
    public static final String TAG = CategoryActivity.class.getSimpleName();
    private FloatingActionButton fabAdd;
    private RecyclerView rvCategories;
    private ArrayList<ProductCategory> listProductCategory = new ArrayList<>();
    private ProductCategoryAdapter productCategoryAdapter;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private String tokenShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        fabAdd = findViewById(R.id.fab_add);
        rvCategories = findViewById(R.id.rv_categories);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        rvCategories.setHasFixedSize(true);
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        tokenShop = sharedPreferences.getString(TOKEN_SHOP, "");

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
        productCategoryAdapter = new ProductCategoryAdapter(listProductCategory, this);
        rvCategories.setAdapter(productCategoryAdapter);
    }

    private void getProductCategories() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();

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
                                listProductCategory.clear();
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
                            Log.d(TAG, "onError errorCode : " + anError.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + anError.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + anError.getErrorDetail());
                        } else {
                            Log.d(TAG, "onError errorDetail : " + anError.getErrorDetail());
                        }
                    }
                });
    }

    public void deleteCategory(String id) {
        new AlertDialog.Builder(this)
                .setMessage("Delete ?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        AndroidNetworking.delete(Constants.API + "/product-categories/" + id)
                                .addHeaders("Authorization", "Bearer " + tokenShop)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String status = response.getString("status");
                                            String message = response.getString("message");

                                            if (status.equals("success")) {
                                                progressDialog.dismiss();

                                                Toast.makeText(CategoryActivity.this, message, Toast.LENGTH_SHORT).show();
                                                getProductCategories();
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
                                            Log.d(TAG, "onError errorCode : " + anError.getErrorCode());
                                            Log.d(TAG, "onError errorBody : " + anError.getErrorBody());
                                            Log.d(TAG, "onError errorDetail : " + anError.getErrorDetail());
                                        } else {
                                            Log.d(TAG, "onError errorDetail : " + anError.getErrorDetail());
                                        }
                                    }
                                });
                    }
                }).create().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}