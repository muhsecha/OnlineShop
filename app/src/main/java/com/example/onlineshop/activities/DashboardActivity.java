package com.example.onlineshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;

import com.example.onlineshop.fragments.CategoryFragment;
import com.example.onlineshop.fragments.ProductFragment;
import com.example.onlineshop.R;
import com.example.onlineshop.adapters.ViewPagerAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class DashboardActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    MaterialToolbar materialToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        materialToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(materialToolbar);
        getTabs();
    }

    public void getTabs(){
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                viewPagerAdapter.addFragment(ProductFragment.getInstance(),"Produk");
                viewPagerAdapter.addFragment(CategoryFragment.getInstance(),"Kategori");

                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);

                tabLayout.getTabAt(0).setIcon(R.drawable.box_white);
                tabLayout.getTabAt(1).setIcon(R.drawable.list_white);
            }
        });
    }
}