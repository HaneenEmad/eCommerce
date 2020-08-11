package com.example.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {
    private Button sellerLoginBegin;
    private EditText nameInput,phoneInput,emailInput,passwordInput,adressInput;
    private  Button registerButton;
    private FirebaseAuth mAuth;
    ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);


        mAuth=FirebaseAuth.getInstance();
        emailInput=findViewById(R.id.seller_email);
        sellerLoginBegin=findViewById(R.id.seller_already_have_account_btn);
        nameInput=findViewById(R.id.seller_name);
        phoneInput=findViewById(R.id.seller_phone);
        passwordInput=findViewById(R.id.seller_password);
        adressInput=findViewById(R.id.seller_address);
        registerButton=findViewById(R.id.seller_register_btn);
        LoadingBar=new ProgressDialog(this);


        sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerSeller();
            }



            private void registerSeller() {
                final String name=nameInput.getText().toString();
                final String phone=phoneInput.getText().toString();
                final String email=emailInput.getText().toString();
                final String password=passwordInput.getText().toString();
                final String address=adressInput.getText().toString();

                if(!name.equals("")&& !phone.equals("")&& !email.equals("")&& !password.equals("")&& !address.equals(""))
                {
                    LoadingBar.setTitle("Creating Seller Account");
                    LoadingBar.setMessage("please Wait, while we are checking the credentials.");
                    LoadingBar.setCanceledOnTouchOutside(false);
                    LoadingBar.show();


                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        final DatabaseReference rootRef;
                                        rootRef= FirebaseDatabase.getInstance().getReference();
                                        String sid=mAuth.getCurrentUser().getUid();
                                        HashMap<String,Object> sellerMap=new HashMap<>();

                                        sellerMap.put("sid",sid);
                                        sellerMap.put("Phone",phone);
                                        sellerMap.put("email",email);
                                        sellerMap.put("address",address);
                                        sellerMap.put("name",name);

                                        rootRef.child("Sellers").child(sid).updateChildren(sellerMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        LoadingBar.dismiss();
                                                        Toast.makeText(SellerRegistrationActivity.this, "you are Registered successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent=new Intent(SellerRegistrationActivity.this,SellerHomeActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                });



                                    }
                                    else{
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(SellerRegistrationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });




                }
                else {
                    Toast.makeText(SellerRegistrationActivity.this, "يا عم كمل البيانات الأول.", Toast.LENGTH_SHORT).show();
                }




            }
        });

    }
}

