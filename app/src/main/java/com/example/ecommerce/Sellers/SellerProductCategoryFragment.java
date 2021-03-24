package com.example.ecommerce.Sellers;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ecommerce.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerProductCategoryFragment extends Fragment {

    private ImageView tshirts,sportsTshirts,femaleDresses,sweathers;
    private  ImageView glasses,hatCaps,walletsBagsPurses,shoes;
    private  ImageView headPhoneHandfree,Laptops,watches,mobilePhones;

    public SellerProductCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_product_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tshirts = view.findViewById(R.id.T_shirts);
        tshirts = view.findViewById(R.id.T_shirts);
        sportsTshirts = view.findViewById(R.id.sports_t_shirts);
        femaleDresses = view.findViewById(R.id.female_dresses);
        sweathers = view.findViewById(R.id.sweathers);
        glasses = view.findViewById(R.id.glasses);
        hatCaps = view.findViewById(R.id.hats);
        walletsBagsPurses = view.findViewById(R.id.pursrs_bags_wallets);
        shoes = view.findViewById(R.id.shoes);
        headPhoneHandfree = view.findViewById(R.id.headphone_handfree);
        Laptops = view.findViewById(R.id.laptop_pc);
        watches = view.findViewById(R.id.watches);
        mobilePhones = view.findViewById(R.id.mobilepones);




        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category","tShirts");
                startActivity(intent);
            }
        });
        sportsTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category","sports tShirts");
                startActivity(intent);
            }
        });
        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category","Female Dresses");
                startActivity(intent);
            }
        });

        sweathers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category","Sweathers");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });
        hatCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category","Hat Caps");
                startActivity(intent);
            }
        });
        walletsBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category","Wallet Bags Purses");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });
        headPhoneHandfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category","HeadPhone HandFree");
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });
        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category","MobilePhones");
                startActivity(intent);
            }
        });
    }
}
