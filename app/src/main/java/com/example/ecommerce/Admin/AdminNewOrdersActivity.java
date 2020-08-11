package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    // me new
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.admin_new_orders_rv);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, final int position, @NonNull final AdminOrders adminOrders) {
                        adminOrdersViewHolder.userName.setText("Name : " + adminOrders.getName());
                        adminOrdersViewHolder.userPhoneNumber.setText("Phone Number : " + adminOrders.getPhone());
                        adminOrdersViewHolder.userTotalPrice.setText("Total Price =  $ : " + adminOrders.getTotalPrice());
                        adminOrdersViewHolder.userDateTime.setText("Order at : " + adminOrders.getDate() + " " + adminOrders.getTime());
                        adminOrdersViewHolder.userShippingAddress.setText("Shipping Address : " + adminOrders.getAddress() + ", " + adminOrders.getCity());

                        //Aya
                        adminOrdersViewHolder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //vid32
                                String uId = getRef(position).getKey();

                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid", uId);
                                startActivity(intent);
                            }
                        });

                        adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final CharSequence options[] = new CharSequence[]{
                                        "Yes",
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Have you Shipped this order Products ? ");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (i == 0){

                                            //vid32
                                            String uId = getRef(position).getKey();

                                            removeOrder(uId);
                                        }
                                        else {
                                            finish();
                                        }
                                    }
                                });

                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new AdminOrdersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false));
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(String uId) {
        ordersRef.child(uId).removeValue();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
        public Button showOrdersBtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name_tv);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number_tv);
            userTotalPrice = itemView.findViewById(R.id.order_total_price_tv);
            userDateTime = itemView.findViewById(R.id.order_date_time_tv);
            userShippingAddress = itemView.findViewById(R.id.order_address_city_tv);

            showOrdersBtn = itemView.findViewById(R.id.order_show_all_products_btn);
        }
    }
}
