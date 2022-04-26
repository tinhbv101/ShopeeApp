package com.hcmute.shopeeapp.entity;

public class AccountEntity {
    private String id;
    private String username;
    private String email;
    private String password;
    private Integer roleID;
    private String status;

    public AccountEntity() {
    }
    public AccountEntity(String username, String email, String password, Integer roleID, String status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleID = roleID;
        this.status = status;
    }
    public AccountEntity(String id, String username, String email, String password, Integer roleID, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleID = roleID;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
