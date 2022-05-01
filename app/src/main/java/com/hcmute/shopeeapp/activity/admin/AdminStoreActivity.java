package com.hcmute.shopeeapp.activity.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hcmute.shopeeapp.R;
import com.hcmute.shopeeapp.dto.StoreDTO;
import com.hcmute.shopeeapp.entity.StoreEntity;
import com.hcmute.shopeeapp.firebase.AccountFirebase;
import com.hcmute.shopeeapp.util.AuthenticationUtil;

import java.time.LocalDateTime;
import java.util.UUID;

public class AdminStoreActivity extends AppCompatActivity {


    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    TextInputEditText txtAdNameStore,
            txtAdPhoneStore,
            txtAdEmailStore,
            txtAdAddressStore,
            txtAdStatusStore;
    ImageView imgAvt;
    Button btnChoosePhoto,
            btnAdd,
            btnDelete,
            btnUpdate;

    private FirebaseAuth firebaseAuth;
    AccountFirebase accountFirebase;

    FirebaseStorage storage;
    StorageReference storageReference;
    Uri fileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_store);
    }


    public void addControl() {
        txtAdAddressStore = findViewById(R.id.txtAdAddressStore);
        txtAdEmailStore = findViewById(R.id.txtAdEmailStore);
        txtAdNameStore = findViewById(R.id.txtAdNameStore);
        txtAdPhoneStore = findViewById(R.id.txtAdPhoneStore);
        txtAdStatusStore = findViewById(R.id.txtAdStatusStore);
        imgAvt = findViewById(R.id.imgAvt);
        btnChoosePhoto = findViewById(R.id.btnStoreChoosePhoto);
        btnAdd = findViewById(R.id.btnAdAdd);
        btnDelete = findViewById(R.id.btnAdDelete);
        btnUpdate = findViewById(R.id.btnAdUpdate);

        accountFirebase = new AccountFirebase(this);
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void addEvent(){
        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String name = txtAdNameStore.getText().toString();
                String email = txtAdEmailStore.getText().toString();
                String phone = txtAdPhoneStore.getText().toString();
                if (!validate(name, phone, email)) {
                    Toast.makeText(AdminStoreActivity.this, "Nhập thông tin sai", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadImage();
            }
        });
    }


    private boolean validate(String name, String phone, String email) {
        if (name.equals("")) {
            txtAdNameStore.setError("Không được bỏ trống");
            return false;
        }
        if (phone.equals("")) {
            txtAdPhoneStore.setError("Không được bỏ trống");
            return false;
        }
        if (email.equals("")) {
            txtAdEmailStore.setError("Không được bỏ trống");
            return false;
        }
        if (fileImage == null) {
            fileImage = Uri.parse("android.resource://com.hcmute.shopeeapp/drawable/account");
        }
        return true;
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    // UploadImage method
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadImage()
    {
        String urlImage = "";
        // Code for showing progressDialog while uploading
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Admin store...");
        progressDialog.show();
        if (fileImage != null) {

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("stores/"+ UUID.randomUUID().toString());

            ref.putFile(fileImage).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    // Image uploaded successfully
                    // Dismiss dialog
                    Toast.makeText(AdminStoreActivity.this,"Image Uploaded!!",Toast.LENGTH_SHORT).show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Store");
                            String urlImage = String.valueOf(uri);
                            String phone = txtAdPhoneStore.getText().toString();
                            String address = txtAdAddressStore.getText().toString();
                            String name = txtAdNameStore.getText().toString();
                            String email = txtAdEmailStore.getText().toString();
                            String storeId = "";
                            String status = txtAdStatusStore.getText().toString();
                            while (true) {
                                if (StoreDTO.FLAG && !StoreDTO.STORE_ID.equals("")) {
                                    storeId = StoreDTO.STORE_ID;
                                    StoreDTO.FLAG = false;
                                    StoreDTO.STORE_ID = "";
                                    break;
                                }
                                if (!StoreDTO.FLAG_FAIL) {
                                    StoreDTO.FLAG_FAIL = true;
                                    progressDialog.dismiss();
                                    return;
                                }
                            }
                            String id = AuthenticationUtil.encode(LocalDateTime.now().toString());
                            StoreEntity store = new StoreEntity(id, name, phone, email, urlImage, address, status);
                            databaseReference.child(id).setValue(store);
                            progressDialog.dismiss();
                        }
                    });


                }

            })


                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(AdminStoreActivity.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(
                                UploadTask.TaskSnapshot taskSnapshot)
                        {
                            double progress
                                    = (100.0
                                    * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(
                                    "Uploaded "
                                            + (int)progress + "%");
                        }
                    });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            fileImage = data.getData();
            if (fileImage != null){
                imgAvt.setImageURI(data.getData());
            }
            else {
                System.out.println("Lỗi");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}