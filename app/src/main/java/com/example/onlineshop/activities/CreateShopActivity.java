package com.example.onlineshop.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.onlineshop.Constants;
import com.example.onlineshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateShopActivity extends AppCompatActivity {
    private SmartMaterialSpinner spCity;
    private List<String> cityList;
    private ProgressDialog progressDialog;
    private EditText etName, etLink;
    private Button btnSubmit;
    private String city = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        spCity = findViewById(R.id.sp_city);
        etName = findViewById(R.id.et_name);
        etLink = findViewById(R.id.et_link);
        btnSubmit = findViewById(R.id.btn_submit);

        progressDialog = new ProgressDialog(this);
        getCity();

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                city = cityList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String link = etLink.getText().toString().trim();

                boolean isEmpty = false;

                if (name.isEmpty()) {
                    isEmpty = true;
                    etName.setError("Required");
                }

                if (link.isEmpty()) {
                    isEmpty = true;
                    etLink.setError("Required");
                }

                if (city == null) {
                    Toast.makeText(CreateShopActivity.this, "Required address", Toast.LENGTH_SHORT).show();
                }

                if (!isEmpty && city != null) {
                    progressDialog.setTitle("Loading...");
                    progressDialog.show();

                    SharedPreferences sp = getSharedPreferences("online_shop", MODE_PRIVATE);
                    String tokenUser = sp.getString("token_user", "");

                    AndroidNetworking.post(Constants.API + "/shops")
                            .addBodyParameter("name", name)
                            .addBodyParameter("link", link)
                            .addBodyParameter("city", city)
                            .addHeaders("Authorization", "Bearer " + tokenUser)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");

                                        if (status.equals("success")) {
                                            JSONObject data = response.getJSONObject("data");
                                            String id = data.getString("id");
                                            changeShop(id);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(CreateShopActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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
        });
    }

    public void getCity() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        cityList = new ArrayList<>();

        AndroidNetworking.get("https://api.rajaongkir.com/starter/city")
                .addHeaders("key", "25c806b53d47f38a8327d29cda61b0df")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rajaongkir = response.getJSONObject("rajaongkir");
                            JSONArray results = rajaongkir.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject item = results.getJSONObject(i);
                                cityList.add(item.getString("city_name"));
                            }

                            spCity.setItem(cityList);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CreateShopActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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

                                Intent intent = new Intent(getApplicationContext(), ChangeShopActivity.class);
                                startActivity(intent);
                                finish();

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CreateShopActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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