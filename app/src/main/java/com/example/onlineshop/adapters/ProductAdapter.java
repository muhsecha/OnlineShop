package com.example.onlineshop.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.Constants;
import com.example.onlineshop.R;
import com.example.onlineshop.activities.EditProductActivity;
import com.example.onlineshop.models.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final ArrayList<Product> listProduct;

    public ProductAdapter(ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = listProduct.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());

        if (!product.getImage().equals("null")) {
            Glide.with(holder.itemView.getContext())
                    .load(Constants.STORAGE + product.getImage())
                    .into(holder.ivProduct);
        }

        if (!product.getProductCategoryId().equals("null")) {
            holder.tvCategory.setText(product.getNameCategory());
        } else {
            holder.tvCategory.setVisibility(View.GONE);
        }

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), EditProductActivity.class);
                intent.putExtra("Item Data", listProduct.get(holder.getAdapterPosition()));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvCategory;
        ImageView ivProduct, ivEdit;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            ivProduct = itemView.findViewById(R.id.iv_product);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            tvCategory = itemView.findViewById(R.id.tv_category);
        }
    }
}
