package com.example.onlineshop.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.onlineshop.Constants;
import com.example.onlineshop.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.onlineshop.Constants.SHARED_PREFS;
import static com.example.onlineshop.Constants.TOKEN_SHOP;

public class CreateCategoryActivity extends AppCompatActivity {
    public static final String TAG = CreateCategoryActivity.class.getSimpleName();
    private EditText etName;
    private Button btnSubmit;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        etName = findViewById(R.id.et_name);
        btnSubmit = findViewById(R.id.btn_submit);

        progressDialog = new ProgressDialog(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();

                boolean isEmpty = false;

                if (name.isEmpty()) {
                    isEmpty = true;
                    etName.setError("Required");
                }

                if (!isEmpty) {
                    progressDialog.setTitle("Loading...");
                    progressDialog.show();

                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    String tokenShop = sharedPreferences.getString(TOKEN_SHOP, "");

                    AndroidNetworking.post(Constants.API + "/product-categories")
                            .addHeaders("Authorization", "Bearer " + tokenShop)
                            .addBodyParameter("name", name)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");

                                        if (status.equals("success")) {

                                            Intent intent = new Intent(CreateCategoryActivity.this, CategoryActivity.class);



                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            startActivity(intent);

                                            progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(CreateCategoryActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
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
        });
    }
}