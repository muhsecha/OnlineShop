package com.example.onlineshop.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.onlineshop.Constants;
import com.example.onlineshop.R;

public class LoginActivity extends AppCompatActivity {
    private Button btn_register,btn_login;
    private TextView tv_frgt_pass;
    private EditText et_email,et_pass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_frgt_pass = findViewById(R.id.tv_frgt_pass);
        et_email = findViewById(R.id.et_login_email);
        et_pass = findViewById(R.id.et_login_pass);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

        progressDialog = new ProgressDialog(this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString().trim();
                String password = et_pass.getText().toString().trim();

                boolean isEmpty = false;

                if (email.isEmpty()) {
                    isEmpty = true;
                    et_email.setError("Required");
                }

                if (password.isEmpty()) {
                    isEmpty = true;
                    et_pass.setError("Required");
                }

                if (!isEmpty) {
                    progressDialog.setTitle("Loading...");
                    progressDialog.show();

                    AndroidNetworking.post(Constants.API + "/login")
                            .addBodyParameter("email", email)
                            .addBodyParameter("password", password)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");

                                        if (status.equals("success")) {
                                            SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                                            sp.edit().putString("token_user", response.getString("data")).apply();

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            progressDialog.dismiss();
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
                                    Toast.makeText(LoginActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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
}