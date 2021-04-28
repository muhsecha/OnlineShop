package com.example.onlineshop.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshop.R;

public class LoginActivity extends AppCompatActivity {

    Button btn_register,btn_login;
    TextView tv_frgt_pass;
    EditText et_email,et_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_frgt_pass = (TextView) findViewById(R.id.tv_frgt_pass);
        et_email = (EditText) findViewById(R.id.et_login_email);
        et_pass = (EditText) findViewById(R.id.et_login_pass);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
//                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
            }
        });
    }
}