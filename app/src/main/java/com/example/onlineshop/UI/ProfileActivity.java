package com.example.onlineshop.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.example.onlineshop.Constants;
import com.example.onlineshop.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextView tvName, tvPhone, tvEmail, tvNameHeader, tvEmailHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvEmail = findViewById(R.id.tv_email);
        tvNameHeader = findViewById(R.id.tv_name_header);
        tvEmailHeader = findViewById(R.id.tv_email_header);

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

                            tvName.setText(name);
                            tvPhone.setText(phone);
                            tvEmail.setText(email);
                            tvNameHeader.setText(name);
                            tvEmailHeader.setText(email);

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ProfileActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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