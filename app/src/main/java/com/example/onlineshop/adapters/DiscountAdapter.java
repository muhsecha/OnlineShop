package com.example.onlineshop.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.activities.EditDiscountActivity;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_discount, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        Discount discount = listDiscount.get(position);
        holder.tvName.setText(discount.getName());
        holder.tvValue.setText(discount.getValue());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), EditDiscountActivity.class);
                intent.putExtra("Item Data", listDiscount.get(holder.getAdapterPosition()));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDiscount.size();
    }

    public class DiscountViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvValue;
        ImageView ivEdit;

        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvValue = itemView.findViewById(R.id.tv_value);
            ivEdit = itemView.findViewById(R.id.iv_edit);
        }
    }
}
