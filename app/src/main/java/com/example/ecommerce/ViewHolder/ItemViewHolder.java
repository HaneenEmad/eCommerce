package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;


public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textViewProductName, textViewProductDescription, textViewProductPrice, textViewProductStatus;
    public ImageView imageViewProduct;
    public ItemClickListener listener;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        imageViewProduct = itemView.findViewById(R.id.seller_item_iv);
        textViewProductName = itemView.findViewById(R.id.seller_item_product_name_tv);
        textViewProductDescription = itemView.findViewById(R.id.seller_item_product_description_tv);
        textViewProductPrice = itemView.findViewById(R.id.seller_item_product_price_tv);
        textViewProductStatus = itemView.findViewById(R.id.seller_item_product_state_tv);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
