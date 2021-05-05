package com.example.onlineshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.models.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private ArrayList<Product> listProduct;

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
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}
