package com.example.it120_proj;

public class User {

    public String userId;
    public String username;
    public String email;
    public String password;
    public int balance;

    public User(){

    }
    public User(String userId, String username, String email, String password, int balance) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }
}