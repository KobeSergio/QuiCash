package com.example.it120_proj;

public class Transaction {

    public String transactionID;
    public String userID;
    public String action;
    public String date;
    public String description;

    public Transaction(){

    }
    public Transaction(String transactionID, String userID, String action, String date, String description) {
        this.transactionID = transactionID;
        this.userID = userID;
        this.action = action;
        this.date = date;
        this.description = description;
    }

}
