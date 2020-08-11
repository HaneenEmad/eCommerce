package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    //me
    private CircleImageView imageViewProfile;
    private EditText editTextFullName, editTextUserPhone, editTextAddress;
    private TextView profileChangeTextBtn, closeTextBtn, updateTextBtn;
    private Button securityQuestionBtn;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfileImageRef;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        imageViewProfile = findViewById(R.id.settings_profile_image_iv);
        editTextFullName = findViewById(R.id.settings_full_name_et);
        editTextUserPhone = findViewById(R.id.settings_phone_number_et);
        editTextAddress = findViewById(R.id.settings_address_et);
        profileChangeTextBtn = findViewById(R.id.settings_profile_image_change_btn);
        closeTextBtn = findViewById(R.id.settings_close_btn);
        updateTextBtn = findViewById(R.id.settings_update_account_btn);
        securityQuestionBtn = findViewById(R.id.settings_security_questions_btn);

        userInfoDisplay(imageViewProfile, editTextFullName, editTextUserPhone, editTextAddress);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    userInfoSaved();
                }else {
                    UpdateOnlyUserInfo();
                }
            }
        });

        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            imageViewProfile.setImageURI(imageUri);
        }else {
            Toast.makeText(this, "Error, Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void UpdateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name",editTextFullName.getText().toString());
        userMap.put("address",editTextAddress.getText().toString());
        userMap.put("PhoneNumber",editTextUserPhone.getText().toString());

        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Info update success", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(editTextFullName.getText().toString())){
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(editTextAddress.getText().toString())){
            Toast.makeText(this, "Address is mandatory", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(editTextUserPhone.getText().toString())){
            Toast.makeText(this, "Phone is mandatory", Toast.LENGTH_SHORT).show();
        }else if (checker.equals("clicked")){
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileRef = storageProfileImageRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (! task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()){
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("name",editTextFullName.getText().toString());
                                userMap.put("address",editTextAddress.getText().toString());
                                userMap.put("PhoneNumber",editTextUserPhone.getText().toString());
                                userMap.put("image", myUrl);

                                ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                Toast.makeText(SettingsActivity.this, "Profile Info update success", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else {

            Toast.makeText(this, "Image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView imageViewProfile, final EditText editTextFullName, final EditText editTextUserPhone, final EditText editTextAddress) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if (dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(imageViewProfile);

                        editTextFullName.setText(name);
                        editTextUserPhone.setText(phone);
                        editTextAddress.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
