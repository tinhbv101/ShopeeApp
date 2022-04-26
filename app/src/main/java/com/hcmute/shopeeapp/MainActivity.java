 package com.hcmute.shopeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmute.shopeeapp.Activity.LoginActivity;
import com.hcmute.shopeeapp.entity.UserEntity;
import com.hcmute.shopeeapp.util.SessionManagerUtil;

import java.net.MalformedURLException;
import java.net.URL;

 public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    EditText txtFullNameMain, txtSexMain, txtAddressMain, txtPhoneMain;
    Button btnLogoutMain;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControl();
        addEvent();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    UserEntity user = new UserEntity();
                    user = dsp.getValue(UserEntity.class);
                    SessionManagerUtil sessionManagerUtil = new SessionManagerUtil(MainActivity.this);
                    if (user.getAccountId().equals(sessionManagerUtil.getId())){
                        txtFullNameMain.setText(user.getFullname());
                        txtSexMain.setText((user.getSex()));
                        txtAddressMain.setText(user.getAddress());
                        txtPhoneMain.setText(user.getPhone());
                        Glide.with(MainActivity.this).load(user.getAvatar()).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



     void addControl(){
        imageView = findViewById(R.id.imageView);
        txtFullNameMain = findViewById(R.id.txtFullNameMain);
        txtSexMain = findViewById(R.id.txtSexMain);
        txtAddressMain = findViewById(R.id.txtAddressMain);
        txtPhoneMain = findViewById(R.id.txtPhoneMain);
        btnLogoutMain = findViewById(R.id.btnLogoutMain);

    }
    void addEvent() {
        btnLogoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("preferenceFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("id");
                editor.remove("email");
                editor.remove("password");
                editor.commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}