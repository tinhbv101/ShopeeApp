package com.hcmute.shopeeapp.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public class AuthenticationUtil {
    public static String encode(@NotNull String plainString) {
        return Base64.getEncoder().withoutPadding().encodeToString(plainString.getBytes());
    }


    @NotNull
    public static String decode(String encodedString) {
        if (null == encodedString)
            return null;
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }
}
