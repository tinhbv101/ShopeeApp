package com.hcmute.shopeeapp.firebase;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmute.shopeeapp.Activity.SignUpActivity;
import com.hcmute.shopeeapp.DTO.SignUpDTO;
import com.hcmute.shopeeapp.MainActivity;
import com.hcmute.shopeeapp.entity.AccountEntity;
import com.hcmute.shopeeapp.util.AuthenticationUtil;

import java.util.ArrayList;
import java.util.List;

public class AccountFirebase {
    private DatabaseReference databaseReference;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;
    private Context context;
    private AccountEntity accountEntity1 = new AccountEntity();
    SharedPreferences sharedPreferences;



    public AccountFirebase(Context context) {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference("Account");


    }



    public void signUp(AccountEntity account) { ;
        String email = account.getEmail();
        String password = AuthenticationUtil.decode(account.getPassword());
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            databaseReference.child(user.getUid()).setValue(account);
                            SignUpDTO.ACCOUNT_ID = user.getUid();
                            SignUpDTO.FLAG = true;
                            Toast.makeText(context, "Create account successful", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            SignUpDTO.FLAG_FAIL = false;
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                    }
                });
    }

    public void updatePassword(String newPassword) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User password updated.");
                    Toast.makeText(context, "Reset password successful", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signIn(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                    //--------------------------
                    sharedPreferences = context.getSharedPreferences("preferenceFile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String id = firebaseAuth.getCurrentUser().getUid();
                    editor.putString("id", id);
                    editor.putString("email", email);
                    editor.putString("password", AuthenticationUtil.encode(password));
                    editor.commit();
                    //----------------------------
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }





}
