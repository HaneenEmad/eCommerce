package com.example.ecommerce.Buyers;

import android.content.Intent;
import android.os.Bundle;

import com.example.ecommerce.Admin.AdminMaintainProductsActivity;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //me
    private String type = "";

    private AppBarConfiguration mAppBarConfiguration;

    private DatabaseReference productsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Aya
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            type = getIntent().getExtras().get("Admin").toString();
        }


        //me
        recyclerView = findViewById(R.id.home_activity_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        //me
        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! type.equals("Admin")){
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                }
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.seller_bottom_nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        //me
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.nav_header_user_name_tv);
        CircleImageView profileImageView = headerView.findViewById(R.id.nav_header_profile_image_iv);

        if (!type.equals("Admin")){
            userNameTextView.setText(Prevalent.currentOnlineUser.getUserName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery, R.id.nav_search, R.id.nav_categories,
                R.id.nav_settings, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.buyers_nav_host_fragment);


        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //me
//        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull NavController controller,
//                                             @NonNull NavDestination destination, @Nullable Bundle arguments)
//            {
//                if (destination.getId() == R.id.nav_gallery){
//                    if (! type.equals("Admin")){
//                        startActivity(new Intent(HomeActivity.this, CartActivity.class));
//                    }
//
//                }
//
//                //Aya
//                if (destination.getId() == R.id.nav_search){
//                    startActivity(new Intent(HomeActivity.this, SearchActivity.class));
//                }
//
//                if (destination.getId() == R.id.nav_categories){
//                }
//
//                if (destination.getId() == R.id.nav_settings){
//                    if (! type.equals("Admin")){
//                        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
//                        startActivity(intent);
//                    }
//                }
//
//                if (destination.getId() == R.id.nav_logout){
//                    if (! type.equals("Admin")){
//                        Paper.book().destroy();
//
//                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            }
//        });

    }

    //me
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>().setQuery(productsRef.orderByChild("productState").equalTo("Approved"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> productAdapter = new
                FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                        productViewHolder.textViewProductName.setText(products.getPname());
                        productViewHolder.textViewProductDescription.setText(products.getDescription());
                        productViewHolder.textViewProductPrice.setText("Price = " + products.getPrice() + "$");
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageViewProduct);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (type.equals("Admin")){
                                    Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", products.getPid());
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", products.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(productAdapter);
        productAdapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.buyers_nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
         if (item.getItemId() == R.id.nav_gallery){
            if (! type.equals("Admin")){
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }

        }

        //Aya
        if (item.getItemId() == R.id.nav_search){
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));
        }

        if (item.getItemId() == R.id.nav_categories){
        }

        if (item.getItemId() == R.id.nav_settings){
            if (! type.equals("Admin")){
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        }

        if (item.getItemId() == R.id.nav_logout){
            if (! type.equals("Admin")){
                Paper.book().destroy();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
        return true;
    }
}
