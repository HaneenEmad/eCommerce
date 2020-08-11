package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check = "";
    private TextView pageTitle, titleQuestion;
    private EditText phoneNumber, question1, question2;
    private Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.reset_password_page_title_tv);
        phoneNumber = findViewById(R.id.reset_password_find_phone_number_et);
        question1 = findViewById(R.id.reset_password_question_1);
        question2 = findViewById(R.id.reset_password_question_2);
        titleQuestion = findViewById(R.id.reset_password_title_questions_tv);
        verifyBtn = findViewById(R.id.reset_password_verify_btn);

    }

    @Override
    protected void onStart() {
        super.onStart();


        phoneNumber.setVisibility(View.GONE);

        if (check.equals("settings")) {
            pageTitle.setText("Set Questions");
            titleQuestion.setText("Please set Answers for the following Security Question");
            verifyBtn.setText("Set");

            displayPreviousAnswers();

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswers();
                }
            });
        } else if (check.equals("login")) {
            phoneNumber.setVisibility(View.VISIBLE);

            //Aya
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUser();

                }
            });
        }
    }

    private void setAnswers() {
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question1.getText().toString().toLowerCase();

        if (question1.equals("") && question2.equals("")) {
            Toast.makeText(ResetPasswordActivity.this, "Please answer both questions", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);

            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "you have set security questions successfully.", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(ResetPasswordActivity.this, HomeActivity.class));
                    }
                }
            });
        }
    }

    private void displayPreviousAnswers() {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String ans1 = dataSnapshot.child("answer1").getValue().toString();
                    String ans2 = dataSnapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private void verifyUser(){
        final String phone =phoneNumber.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question1.getText().toString().toLowerCase();

        if (!phone.equals("")&&!answer1.equals("")&&!answer2.equals(""))
        {
            final DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(phone);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.exists()){
                        String mphone=dataSnapshot.child("Phone").getValue().toString();
                        if(phone.equals(mphone))
                        {
                            if (dataSnapshot.hasChild("Security Questions"))
                            {
                                String ans1 = dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                                String ans2 = dataSnapshot.child("Security Questions").child("answer2").getValue().toString();

                                if(!ans1.equals(answer1)){
                                    Toast.makeText(ResetPasswordActivity.this, "your 1st answer is wrong.", Toast.LENGTH_SHORT).show();
                                }
                                else if(!ans2.equals(answer2)){
                                    Toast.makeText(ResetPasswordActivity.this, "your 2nd answer is wrong.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this);
                                    builder.setTitle("New Password");
                                    final EditText newpassword= new EditText(ResetPasswordActivity.this);
                                    newpassword.setHint("Write Password Here......");
                                    builder.setView(newpassword);
                                    builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {
                                            if(!newpassword.getText().toString().equals(""))
                                            {
                                                ref.child("Password")
                                                        .setValue(newpassword.getText().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(ResetPasswordActivity.this, "Password change successfully", Toast.LENGTH_SHORT).show();
                                                                    Intent intent=new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                    startActivity(intent);
                                                                }}
                                                        });
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        }
                        else {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set the security question.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ResetPasswordActivity.this, "this phone number not exist.", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {


                }
            });
        }
        else{
            Toast.makeText(this, "Please complete the form", Toast.LENGTH_SHORT).show();
        }

    }
}
