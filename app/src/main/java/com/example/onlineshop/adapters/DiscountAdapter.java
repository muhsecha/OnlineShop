package com.example.onlineshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.models.Discount;

import java.util.ArrayList;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {
    private ArrayList<Discount> listDiscount;

    public DiscountAdapter(ArrayList<Discount> listDiscount) {
        this.listDiscount = listDiscount;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        Discount discount = listDiscount.get(position);
        holder.etName.setText(discount.getName());
        holder.etValue.setText(discount.getValue());
    }

    @Override
    public int getItemCount() {
        return listDiscount.size();
    }

    public class DiscountViewHolder extends RecyclerView.ViewHolder {
        EditText etName, etValue;

        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.et_name);
            etValue = itemView.findViewById(R.id.et_value);
        }
    }
}
