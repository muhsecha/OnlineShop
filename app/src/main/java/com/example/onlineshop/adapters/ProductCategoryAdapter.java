package com.example.onlineshop.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.activities.CategoryActivity;
import com.example.onlineshop.activities.EditCategoryActivity;
import com.example.onlineshop.models.ProductCategory;

import java.util.ArrayList;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ProductCategoryViewHolder> {
    private ArrayList<ProductCategory> listProductCategory;
    private CategoryActivity categoryActivity;

    public ProductCategoryAdapter(ArrayList<ProductCategory> listProductCategory, Activity categoryActivity) {
        this.listProductCategory = listProductCategory;
        this.categoryActivity = (CategoryActivity) categoryActivity;
    }

    @NonNull
    @Override
    public ProductCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category, parent, false);
        return new ProductCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryViewHolder holder, int position) {
        ProductCategory productCategory = listProductCategory.get(position);
        holder.tvName.setText(productCategory.getName());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), EditCategoryActivity.class);
                intent.putExtra("Item Data", listProductCategory.get(holder.getAdapterPosition()));
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryActivity.deleteCategory(productCategory.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProductCategory.size();
    }

    public class ProductCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivEdit;
        ImageView ivDelete;

        public ProductCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
