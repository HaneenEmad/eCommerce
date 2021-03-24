package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.Buyers.LoginActivity;
import com.example.ecommerce.Buyers.RegisterActivity;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminRegisterActivity extends AppCompatActivity {

    Button CreateAccountbutton;
    EditText inputname,inputnumber,inputpassword;
    ProgressDialog LoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        CreateAccountbutton=findViewById(R.id.admin_register_btn);
        inputname=findViewById(R.id.admin_register_username_input);
        inputnumber=findViewById(R.id.admin_register_phone_number_input);
        inputpassword=findViewById(R.id.admin_register_password_input);
        LoadingBar=new ProgressDialog(this);

        CreateAccountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }

            private void CreateAccount() {
                String name=inputname.getText().toString();
                String password=inputpassword.getText().toString();
                String number=inputnumber.getText().toString();
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(AdminRegisterActivity.this, "Please enter Your Name... ", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password)){
                    Toast.makeText(AdminRegisterActivity.this, "Please enter Your Password... ", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(number)){
                    Toast.makeText(AdminRegisterActivity.this, "Please enter Your PhoneNumber... ", Toast.LENGTH_SHORT).show();
                }
                else{
                    LoadingBar.setTitle("Create Account");
                    LoadingBar.setMessage("please Wait, while we are checking the credentials.");
                    LoadingBar.setCanceledOnTouchOutside(false);
                    LoadingBar.show();
                    ValidatePhoneNumber(name,number,password);

                }
            }

            private void ValidatePhoneNumber(final String name, final String number, final String password) {
                final DatabaseReference RootRef;
                RootRef= FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!(dataSnapshot.child("Admins").child(number).exists()))
                        {
                            HashMap<String,Object> userdataMap=new HashMap<>();
                            userdataMap.put("Phone" ,number);
                            userdataMap.put("Password", password);
                            userdataMap.put("userName",name);
                            RootRef.child("Admins").child(number).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AdminRegisterActivity.this, "Congratulation, Your Account has been created", Toast.LENGTH_SHORT).show();
                                                LoadingBar.dismiss();
                                                Intent intent=new Intent(AdminRegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                LoadingBar.dismiss();
                                                Toast.makeText(AdminRegisterActivity.this, "NetWork Error:Please Try again after some Time... ", Toast.LENGTH_SHORT).show();

                                            }


                                        }
                                    });


                        }
                        else{
                            Toast.makeText(AdminRegisterActivity.this, "this" + number + "already exists", Toast.LENGTH_SHORT).show();
                            LoadingBar.dismiss();
                            Toast.makeText(AdminRegisterActivity.this, "Please Try again using another PhoneNumber.", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(AdminRegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });



    }


}
