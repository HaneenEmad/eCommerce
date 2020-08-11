package com.example.ecommerce.Sellers;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity {

    // me
    private String categoryName, description, price, pname, saveCurrentDate, saveCurrentTime;
    private Button addNewProductButton;
    private ImageView inputProductImage;
    private TextInputEditText inputProductName, inputProductDescription, inputProductPrice;
    private static final int galleryPick = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUrl;
    private  StorageReference productImagesRef;
    private DatabaseReference productRef, sellersRef;
    private ProgressDialog loadingBar;
    private String sName, sPhone, sAddress, sEmail, sId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        categoryName = getIntent().getExtras().get("category").toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        //me
        sellersRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        loadingBar = new ProgressDialog(this);

        addNewProductButton = findViewById(R.id.admin_add_new_product_add_btn);
        inputProductImage = findViewById(R.id.admin_add_new_product_select_image_iv);
        inputProductName = findViewById(R.id.admin_add_new_product_product_name_et);
        inputProductDescription = findViewById(R.id.admin_add_new_product_product_description_et);
        inputProductPrice = findViewById(R.id.admin_add_new_product_product_price_et);

        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });

        //me
        sellersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            sName = dataSnapshot.child("name").getValue().toString();
                            sPhone = dataSnapshot.child("phone").getValue().toString();
                            sAddress = dataSnapshot.child("address").getValue().toString();
                            sId = dataSnapshot.child("sid").getValue().toString();
                            sEmail = dataSnapshot.child("email").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();

        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryPick && requestCode == RESULT_OK && data != null){
            imageUri = data.getData();
            inputProductImage.setImageURI(imageUri);
        }
    }

    private void validateProductData() {
        pname = inputProductName.getText().toString();
        description = inputProductDescription.getText().toString();
        price = inputProductPrice.getText().toString();

        if (imageUri == null){
            Toast.makeText(this, "Image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pname)){
            Toast.makeText(this, "Please enter product name...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Please enter product description...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price)){
            Toast.makeText(this, "Please enter product price...", Toast.LENGTH_SHORT).show();
        }
        else {
            storeProductInformation();
        }


    }

    private void storeProductInformation() {
        Calendar calendar = Calendar.getInstance();

        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Please wait, while we are adding your new product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errorMessage = e.toString();
                loadingBar.dismiss();

                Toast.makeText(SellerAddNewProductActivity.this, "Error : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductActivity.this, "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (! task.isSuccessful()){
                    throw task.getException();
                }

                downloadImageUrl = filePath.getDownloadUrl().toString();
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){

                    downloadImageUrl = task.getResult().toString();

                    Toast.makeText(SellerAddNewProductActivity.this, "got Image url successfully", Toast.LENGTH_SHORT).show();

                    saveProductInfo();
                }
            }
        });


    }

    private void saveProductInfo() {
        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("category", categoryName);
        productMap.put("description", description);
        productMap.put("pname", pname);
        productMap.put("image", imageUri);
        productMap.put("price", price);

        //me
        productMap.put("sellerName", sName);
        productMap.put("sellerAddress", sAddress);
        productMap.put("sellerPhone", sPhone);
        productMap.put("sId", sId);
        productMap.put("sellerEmail",sEmail);
        productMap.put("productState", "Not Approved");

        productRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    startActivity(new Intent(SellerAddNewProductActivity.this, SellerHomeActivity.class));

                    loadingBar.dismiss();
                    Toast.makeText(SellerAddNewProductActivity.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
                }else {
                    loadingBar.dismiss();
                    Toast.makeText(SellerAddNewProductActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
