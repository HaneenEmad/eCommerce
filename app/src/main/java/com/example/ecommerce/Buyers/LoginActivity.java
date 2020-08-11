package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminHomeActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    EditText inputnumber, inputpassword;
    Button loginButton;

    private TextView AdminLink, NotAdminLink, forgotPasswordLink;
    ProgressDialog LoadingBar;
    String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //me
        forgotPasswordLink = findViewById(R.id.forgot_password_link);

        inputnumber = findViewById(R.id.log_in_phone_number_input);
        inputpassword = findViewById(R.id.log_in_password_input);
        loginButton = findViewById(R.id.login_btn);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        chkBoxRememberMe = findViewById(R.id.remmember_chkbx);
        LoadingBar = new ProgressDialog(this);
        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginUser();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";

            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
            }
        });

        //me
        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

    }

    private void LoginUser() {
        String password=inputpassword.getText().toString();
        String number=inputnumber.getText().toString();
        if (TextUtils.isEmpty(number)){
            Toast.makeText(LoginActivity.this, "Please enter Your Number... ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Please enter Your Password... ", Toast.LENGTH_SHORT).show();
        }
        else{
            LoadingBar.setTitle("Login Account");
            LoadingBar.setMessage("please Wait, while we are checking the credentials.");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();
            AllowAccessToAccount(number,password);
        }
    }

    private void AllowAccessToAccount(final String number, final String password) {

        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.userPhoneKey,number);
            Paper.book().write(Prevalent.userPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(number).exists()) {
                    Users usersData = dataSnapshot.child(parentDbName).child(number).getValue(Users.class);

                    if (usersData.getPhone() != null && usersData.getPhone().equals(number)) {
                        if (usersData.getPassword().equals(password)) {
                            if (parentDbName.equals("Admins")) { // new meeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
                                Toast.makeText(LoginActivity.this, " Welcome يا عم الأدمن ,you are logged in successfully", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            } else if (parentDbName.equals("Users")) {
                                Toast.makeText(LoginActivity.this, "log in successfully", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);

                            } else {
                                LoadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Password is not Correct", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Account with this " + number + "do not exists ", Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
