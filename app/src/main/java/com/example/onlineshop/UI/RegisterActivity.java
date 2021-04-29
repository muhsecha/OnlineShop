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

public class RegisterActivity extends AppCompatActivity {
    private Button btn_register,btn_login;
    private EditText et_name,et_phone,et_email,et_pass, et_confirm_pass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register = findViewById(R.id.btn_regis_register);
        btn_login = findViewById(R.id.btn_log_register);
        et_name = findViewById(R.id.et_register_name);
        et_phone = findViewById(R.id.et_register_phone);
        et_email = findViewById(R.id.et_register_email);
        et_pass = findViewById(R.id.et_register_pass);
        et_confirm_pass = findViewById(R.id.et_confirm_password);

        progressDialog = new ProgressDialog(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_name.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String phoneNumber = et_phone.getText().toString().trim();
                String password = et_pass.getText().toString().trim();
                String confirmPassword = et_confirm_pass.getText().toString().trim();

                boolean isEmpty = false;
                boolean isInvalidPassword = false;

                if (name.isEmpty()) {
                    isEmpty = true;
                    et_name.setError("Required");
                }

                if (email.isEmpty()) {
                    isEmpty = true;
                    et_email.setError("Required");
                }

                if (phoneNumber.isEmpty()) {
                    isEmpty = true;
                    et_phone.setError("Required");
                }

                if (password.isEmpty()) {
                    isEmpty = true;
                    et_pass.setError("Required");
                }

                if (confirmPassword.isEmpty()) {
                    isEmpty = true;
                    et_confirm_pass.setError("Required");
                }

                if (!password.equals(confirmPassword)) {
                    isInvalidPassword = true;
                    et_confirm_pass.setError("Password & Confirm Password don't match");
                }


                if (!isEmpty && !isInvalidPassword) {
                    progressDialog.setTitle("Loading...");
                    progressDialog.show();

                    AndroidNetworking.post(Constants.API + "/register")
                            .addBodyParameter("name", name)
                            .addBodyParameter("email", email)
                            .addBodyParameter("phone_number", phoneNumber)
                            .addBodyParameter("password", password)
                            .addBodyParameter("password_confirmation", confirmPassword)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");

                                        if (status.equals("success")) {
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
                                    Toast.makeText(RegisterActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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