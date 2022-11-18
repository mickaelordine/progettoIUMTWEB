package com.ium.example.progetto.account;

public class User {
    String email, password;

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public void clear(){
        this.email = null;
        this.password = null;
    }

}
