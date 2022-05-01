package com.hcmute.shopeeapp.activity;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.hcmute.shopeeapp.R;
import com.hcmute.shopeeapp.firebase.AccountFirebase;
import com.hcmute.shopeeapp.util.AuthenticationUtil;
import com.hcmute.shopeeapp.util.SessionManagerUtil;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference database;

    AccountFirebase accountFirebase;

    Button btnLogin;
    TextInputEditText txtEmail;
    TextInputEditText txtPassword;
    TextView btnSignup;
    Button btnForgotPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControl();
        addEvent();


    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionManagerUtil sessionManagerUtil = new SessionManagerUtil(this);
        if (sessionManagerUtil.getId() != null) {
            accountFirebase.signIn(sessionManagerUtil.getEmail()
                    , AuthenticationUtil.decode(sessionManagerUtil.getPassword()));
        }
    }

    public void addControl() {
        txtEmail = findViewById(R.id.email);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogIn);
        accountFirebase = new AccountFirebase(this);
        btnSignup = findViewById(R.id.btnSignup);
    }

    public void addEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                accountFirebase.signIn(email, password);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


}