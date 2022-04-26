package com.hcmute.shopeeapp.entity;

import android.graphics.Bitmap;

import com.hcmute.shopeeapp.R;

public class UserEntity {
    private static final int DEFAULT_AVT_FEMALE = R.drawable.female_profile;
    private static final int DEFAULT_AVT_MALE = R.drawable.male_profile;
    private static final int DEFAULT_AVT_UNDEFINED = R.drawable.cat_profile;


    private String id;
    private String accountId;
    private String fullname;
    private String sex;
    private String phone;
    private String address;
    private String avatar;
    private String facebook;
    private String zalo;
    private String status;

    public UserEntity() {
    }

    public UserEntity(String id, String accountId, String fullname, String sex, String phone, String address, String avatar, String facebook, String zalo, String status) {
        this.id = id;
        this.accountId = accountId;
        this.fullname = fullname;
        this.sex = sex;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.facebook = facebook;
        this.zalo = zalo;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getZalo() {
        return zalo;
    }

    public void setZalo(String zalo) {
        this.zalo = zalo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
