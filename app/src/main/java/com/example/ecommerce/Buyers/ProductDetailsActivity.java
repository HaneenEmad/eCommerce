package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    //me
    private Button addToCartBtn, addOneButton, minusOneButton;
    private ImageView productImage;
    private TextView productName, productDescription, productPrice, countTextView;
    private String productId = "", state = "Normal  "; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("pid");

        addToCartBtn = findViewById(R.id.product_details_add_product_to_cart_btn);
        addOneButton = findViewById(R.id.product_details_number_add_btn);
        minusOneButton = findViewById(R.id.product_details_number_minus_btn);
        countTextView = findViewById(R.id.product_details_count_tv);
        productImage = findViewById(R.id.product_details_product_image_iv);
        productName = findViewById(R.id.product_details_product_name_tv);
        productDescription = findViewById(R.id.product_details_product_description_tv);
        productPrice = findViewById(R.id.product_details_product_price_tv);

        addOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = Integer.parseInt(countTextView.getText().toString());
                counter++;
                countTextView.setText(counter);
            }
        });

        minusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = Integer.parseInt(countTextView.getText().toString());
                counter++;
                countTextView.setText(counter);
            }
        });


        getProductDetails(productId);

        //
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state.equals("Order shipped") || state.equals("Order placed")){
                    Toast.makeText(ProductDetailsActivity.this, "you can add purchase more products, once your order shipped or confirmed.", Toast.LENGTH_LONG).show();
                }else {
                    addingToCartList();
                }
            }
        });
    }

    //
    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productId);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", countTextView.getText().toString());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this, "Added ti Cart List.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ProductDetailsActivity.this, HomeActivity.class));
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void getProductDetails(final String productId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //
    private void checkOrderState(){
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped")){
                        state = "Order shipped";

                    }
                    else if (shippingState.equals("not shipped")){
                        state = "Order placed";
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
