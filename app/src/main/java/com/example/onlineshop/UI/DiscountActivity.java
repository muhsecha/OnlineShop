package com.example.onlineshop.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.onlineshop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DiscountActivity extends AppCompatActivity {
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        fabAdd = findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiscountActivity.this, CreateDiscountActivity.class);
                startActivity(intent);
            }
        });
    }
}