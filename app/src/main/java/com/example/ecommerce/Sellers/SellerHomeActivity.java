package com.example.ecommerce.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Buyers.HomeActivity;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHomeActivity extends AppCompatActivity {

    //me

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home2);
        BottomNavigationView navView = findViewById(R.id.seller_bottom_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.seller_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //me
        unverifiedProducts = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.seller_home_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_home){

                }
                //Aya
                if (destination.getId() == R.id.navigation_add){
                    startActivity(new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class));
                }

                if (destination.getId() == R.id.navigation_logout){
                    startActivity(new Intent(SellerHomeActivity.this, HomeActivity.class));
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unverifiedProducts.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i, @NonNull final Products products) {

                        itemViewHolder.textViewProductName.setText(products.getPname());
                        itemViewHolder.textViewProductDescription.setText(products.getDescription());
                        itemViewHolder.textViewProductStatus.setText("State : " + products.getProductState());
                        itemViewHolder.textViewProductPrice.setText("Price = " + products.getPrice() + "$");
                        Picasso.get().load(products.getImage()).into(itemViewHolder.imageViewProduct);

                        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String productId = products.getPid();

                                CharSequence options[] = new CharSequence[]{
                                        "yes",
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Do you want to Delete This Product. Are you sure ? ");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int position) {

                                        if (position == 0 ){
                                            deleteProduct(productId);
                                        }
                                        if (position == 1){

                                        }

                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false));
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void deleteProduct(String productId) {
        unverifiedProducts.child(productId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SellerHomeActivity.this, "That item has been deleted successfully.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
