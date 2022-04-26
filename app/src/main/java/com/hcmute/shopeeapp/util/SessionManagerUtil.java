package com.hcmute.shopeeapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagerUtil {
    private String id;
    private String email;
    private String password;
    private Context context;
    private SharedPreferences sharedPreferences;

    public SessionManagerUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("preferenceFile", Context.MODE_PRIVATE);
    }

    public String getId(){
        sharedPreferences = context.getSharedPreferences("preferenceFile", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("id")) {
            this.email = sharedPreferences.getString("id", "");
        }
        return this.email;
    }
    public String getEmail(){
        sharedPreferences = context.getSharedPreferences("preferenceFile", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("email")) {
            this.email = sharedPreferences.getString("email", "");
        }
        return this.email;
    }
    public String getPassword(){

        if (sharedPreferences.contains("password")) {
            this.password = sharedPreferences.getString("password", "");
        }
        return this.password;
    }
}
