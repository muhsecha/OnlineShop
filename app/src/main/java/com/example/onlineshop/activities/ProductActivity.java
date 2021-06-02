package com.example.onlineshop.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.onlineshop.Constants;
import com.example.onlineshop.R;
import com.example.onlineshop.adapters.ProductAdapter;
import com.example.onlineshop.models.Product;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.onlineshop.Constants.SHARED_PREFS;
import static com.example.onlineshop.Constants.TOKEN_SHOP;

public class ProductActivity extends AppCompatActivity {
    public static final String TAG = ProductActivity.class.getSimpleName();
    private FloatingActionButton fabAdd;
    private RecyclerView rvProducts;
    private final ArrayList<Product> listProduct = new ArrayList<>();
    private ProductAdapter productAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        MaterialToolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fabAdd = findViewById(R.id.fab_add);
        rvProducts = findViewById(R.id.rv_products);

        rvProducts.setHasFixedSize(true);
        progressDialog = new ProgressDialog(this);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, CreateProductActivity.class);
                startActivity(intent);
            }
        });

        showProducts();
        getProducts();
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


    private void showProducts() {
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(listProduct);
        rvProducts.setAdapter(productAdapter);
    }



    private void getProducts() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String tokenShop = sharedPreferences.getString(TOKEN_SHOP, "");

        AndroidNetworking.get(Constants.API + "/products")
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

                                    Product product = new Product();
                                    product.setId(item.getString("id"));
                                    product.setName(item.getString("name"));
                                    product.setDesc(item.getString("desc"));
                                    product.setPrice(item.getString("price"));
                                    product.setStock(item.getString("stock"));
                                    product.setImage(item.getString("image"));

                                    String categoryId = item.getString("product_category_id");
                                    product.setProductCategoryId(categoryId);

                                    if (!categoryId.equals("null")) {
                                        JSONObject category = item.getJSONObject("category");
                                        product.setNameCategory(category.getString("name"));
                                    }

                                    if (item.getInt("show") != 0) {
                                        product.setShow(true);
                                    }

                                    listProduct.add(product);
                                }

                                productAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ProductActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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
}