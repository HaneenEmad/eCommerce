package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.Sellers.SellerHomeActivity;
import com.example.ecommerce.Sellers.SellerRegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private TextView sellerbegin;
    private Button joinNowButton, loginButton;
    ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sellerbegin=findViewById(R.id.seller_begin);
        joinNowButton = (Button) findViewById(R.id.main_join_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        LoadingBar = new ProgressDialog(this);
        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        sellerbegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.userPasswordKey);


        if (UserPhoneKey != "" && UserPasswordKey != "") {

            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                AllowAccess(UserPhoneKey, UserPasswordKey);

                LoadingBar.setTitle("Already Logged in");
                LoadingBar.setMessage("please Wait........");
                LoadingBar.setCanceledOnTouchOutside(false);
                LoadingBar.show();


            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }
    }


    private void AllowAccess(final String number, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(number).exists()) {
                    Users usersData = dataSnapshot.child("Users").child(number).getValue(Users.class);

                    if (usersData.getPhone() != null && usersData.getPhone().equals(number)) {
                        if (usersData.getPassword().equals(password)) {
                            Toast.makeText(MainActivity.this, "Please Wait,you are already logged in ", Toast.LENGTH_SHORT).show();
                            LoadingBar.dismiss();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            LoadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is not Correct", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Account with this " + number + "do not exists ", Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
