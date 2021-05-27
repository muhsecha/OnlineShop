package com.example.onlineshop.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.onlineshop.Constants;
import com.example.onlineshop.R;
import com.example.onlineshop.adapters.DiscountAdapter;
import com.example.onlineshop.models.Discount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.onlineshop.Constants.SHARED_PREFS;
import static com.example.onlineshop.Constants.TOKEN_SHOP;
import static com.example.onlineshop.Constants.TOKEN_USER;

public class DiscountActivity extends AppCompatActivity {
    public static final String TAG = DiscountActivity.class.getSimpleName();
    private FloatingActionButton fabAdd;
    private RecyclerView rvDiscounts;
    private DiscountAdapter discountAdapter;
    private final ArrayList<Discount> listDiscount = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        fabAdd = findViewById(R.id.fab_add);
        rvDiscounts = findViewById(R.id.rv_discounts);

        progressDialog = new ProgressDialog(this);
        rvDiscounts.setHasFixedSize(true);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiscountActivity.this, CreateDiscountActivity.class);
                startActivity(intent);
            }
        });

        showDiscounts();
        getDiscounts();
    }

    private void showDiscounts() {
        rvDiscounts.setLayoutManager(new LinearLayoutManager(this));
        discountAdapter = new DiscountAdapter(listDiscount);
        rvDiscounts.setAdapter(discountAdapter);
    }

    private void getDiscounts() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String tokenShop = sharedPreferences.getString(TOKEN_SHOP, "");

        AndroidNetworking.get(Constants.API + "/discounts")
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

                                    Discount discount = new Discount();
                                    discount.setId(item.getString("id"));
                                    discount.setName(item.getString("name"));
                                    discount.setValue(item.getString("value"));
                                    listDiscount.add(discount);
                                }

                                discountAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DiscountActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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