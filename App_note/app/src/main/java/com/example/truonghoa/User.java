package com.example.truonghoa;

public class User {
    public String fullname;
    public String password;
    public String email;
    public String phonenumber;

    public User(String fullname, String password, String email, String phonenumber) {
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
