package com.example.onlineshop.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshop.R;

public class RegisterActivity extends AppCompatActivity {

    Button btn_register,btn_login;
    EditText et_name,et_phone,et_email,et_pass;

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

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "Register success", Toast.LENGTH_SHORT).show();
            }
        });
    }
}