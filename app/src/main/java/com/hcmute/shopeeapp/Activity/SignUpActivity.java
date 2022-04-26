package com.hcmute.shopeeapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hcmute.shopeeapp.DTO.SignUpDTO;
import com.hcmute.shopeeapp.R;
import com.hcmute.shopeeapp.entity.AccountEntity;
import com.hcmute.shopeeapp.entity.UserEntity;
import com.hcmute.shopeeapp.firebase.AccountFirebase;
import com.hcmute.shopeeapp.util.AuthenticationUtil;
import com.hcmute.shopeeapp.util.ImageUtil;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;


public class SignUpActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    TextInputEditText txtFullName
            , txtEmail
            , txtPhone
            , txtAddress
            , txtUsername
            , txtPassword
            , txtConfirmPassword;
    TextInputLayout layoutConfirmPassword;
    TextView txtSignIn;
    ImageView imgAvt;
    ChipGroup chipGroupSex;
    Button btnChoosePhoto;
    Button btnSignUp;
    ImageButton btnBack;


    private FirebaseAuth firebaseAuth;
    AccountFirebase accountFirebase;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri fileImage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        addControl();
        addEvent();








    }

    public void addControl(){
        btnBack = findViewById(R.id.btnBack);
        txtSignIn = findViewById(R.id.txtSignIn);
        txtFullName = findViewById(R.id.txtFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        layoutConfirmPassword = findViewById(R.id.layoutConfirmPassword);
        chipGroupSex = findViewById(R.id.chipGroupSex);
        imgAvt = findViewById(R.id.imgAvt);
        btnChoosePhoto = findViewById(R.id.btnChoosePhoto);
        btnSignUp = findViewById(R.id.btnSignUp);
        accountFirebase = new AccountFirebase(this);
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

   public void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

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

       btnSignUp.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.O)
           @Override
           public void onClick(View view) {
               String fullName = txtFullName.getText().toString();
               String email = txtEmail.getText().toString();
               String phone = txtPhone.getText().toString();
               String address = txtAddress.getText().toString();
               String username = txtUsername.getText().toString();
               String password = txtPassword.getText().toString();
               String confirmPassword = txtConfirmPassword.getText().toString();
               if (!validate(fullName, email, phone, address, username, password, confirmPassword)) {
                   Toast.makeText(SignUpActivity.this, "Nhập thông tin sai", Toast.LENGTH_SHORT).show();
                   return;
               }
               AccountEntity account = new AccountEntity(username, email, AuthenticationUtil.encode(password), 2, "enable");
               accountFirebase.signUp(account);
               uploadImage();

           }
       });
   }

    private boolean validate(@NotNull String fullName, String email, String phone, String address,
                             String username, String password, String confirmPassword) {
        if (fullName.equals("")) {
            txtFullName.setError("Không được bỏ trống");
            return false;
        }
        if (email.equals("")) {
            txtEmail.setError("Không được bỏ trống");
            return false;
        }
        if (phone.equals("")) {
            txtPhone.setError("Không được bỏ trống");
            return false;
        }
        if (address.equals("")) {
            txtAddress.setError("Không được bỏ trống");
            return false;
        }
        if (username.equals("")) {
            txtFullName.setError("Không được bỏ trống");
            return false;
        }
        if (password.equals("")) {
            txtPassword.setError("Không được bỏ trống");
            return false;
        }
        if (confirmPassword.equals("")) {
            txtConfirmPassword.setError("Không được bỏ trống");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            txtConfirmPassword.setError("Mật khẩu không trùng khớp");
            return false;
        }
        if (password.length() < 6) {
            txtPassword.setError("Mật khẩu phải dài hơn hoặc bằng 6 ký tự");
            return false;
        }
        if (fileImage == null) {
            fileImage = Uri.parse("android.resource://com.hcmute.shopeeapp/drawable/account");
        }
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @NotNull
    private String getSex() {
        int selected = chipGroupSex.getCheckedChipId();
        switch (selected) {
            case R.id.chipMale:
                return "Male";
            case R.id.chipFemale:
                return "Female";
            default:
                return "Others";
        }
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
        progressDialog.setTitle("Sign Up...");
        progressDialog.show();
        if (fileImage != null) {


            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());


            ref.putFile(fileImage).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    // Image uploaded successfully
                    // Dismiss dialog
                    Toast.makeText(SignUpActivity.this,"Image Uploaded!!",Toast.LENGTH_SHORT).show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                            String urlImage = String.valueOf(uri);
                            String fullName = txtFullName.getText().toString();
                            String phone = txtPhone.getText().toString();
                            String address = txtAddress.getText().toString();
                            String sex = getSex();
                            String accountId = "";
                            String status = "enable";
                            while (true) {
                                if (SignUpDTO.FLAG && !SignUpDTO.ACCOUNT_ID.equals("")) {
                                    accountId = SignUpDTO.ACCOUNT_ID;
                                    SignUpDTO.FLAG = false;
                                    SignUpDTO.ACCOUNT_ID = "";
                                    break;
                                }
                                if (!SignUpDTO.FLAG_FAIL) {
                                    SignUpDTO.FLAG_FAIL = true;
                                    progressDialog.dismiss();
                                    return;
                                }
                            }
                            String id = AuthenticationUtil.encode(LocalDateTime.now().toString());
                            UserEntity user = new UserEntity(id, accountId, fullName, sex, phone, address,urlImage, "", "", status);
                            databaseReference.child(id).setValue(user);
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
                        Toast.makeText(SignUpActivity.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
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