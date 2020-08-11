package com.example.ecommerce.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ecommerce.R;

public class SellerProductCategoryActivity extends AppCompatActivity {
    private ImageView tshirts,sportsTshirts,femaleDresses,sweathers;
    private  ImageView glasses,hatCaps,walletsBagsPurses,shoes;
    private  ImageView headPhoneHandfree,Laptops,watches,mobilePhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);


        tshirts=findViewById(R.id.T_shirts);
        tshirts=findViewById(R.id.T_shirts);
        sportsTshirts=findViewById(R.id.sports_t_shirts);
        femaleDresses=findViewById(R.id.female_dresses);
        sweathers=findViewById(R.id.sweathers);
        glasses=findViewById(R.id.glasses);
        hatCaps=findViewById(R.id.hats);
        walletsBagsPurses=findViewById(R.id.pursrs_bags_wallets);
        shoes=findViewById(R.id.shoes);
        headPhoneHandfree=findViewById(R.id.headphone_handfree);
        Laptops=findViewById(R.id.laptop_pc);
        watches=findViewById(R.id.watches);
        mobilePhones=findViewById(R.id.mobilepones);




        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","tShirts");
                startActivity(intent);
            }
        });
        sportsTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","sports tShirts");
                startActivity(intent);
            }
        });
        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Female Dresses");
                startActivity(intent);
            }
        });

        sweathers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Sweathers");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });
        hatCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Hat Caps");
                startActivity(intent);
            }
        });
        walletsBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Wallet Bags Purses");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });
        headPhoneHandfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","HeadPhone HandFree");
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });
        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","MobilePhones");
                startActivity(intent);
            }
        });

    }
}
