package com.api.report.user;

public class User {

    private String email;
    private String lastLogin;
    private String roles;

    public User(String email){
        this.email = email;
    }
    public User(){}

    public String getEmail() {
        return email;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
