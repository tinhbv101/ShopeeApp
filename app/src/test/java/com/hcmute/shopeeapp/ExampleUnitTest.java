package com.hcmute.shopeeapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.hcmute.shopeeapp.entity.AccountEntity;
import com.hcmute.shopeeapp.util.AuthenticationUtil;

import java.time.LocalDateTime;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String time = LocalDateTime.now().toString();
        System.out.println(time);
        System.out.println(time);
        System.out.println(time);
        System.out.println(time);
        System.out.println(time);
        System.out.println(time);
        System.out.println(AuthenticationUtil.encode(time));
    }

}