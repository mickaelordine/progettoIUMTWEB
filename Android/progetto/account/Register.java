package com.ium.example.progetto.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ium.example.progetto.R;

public class Register extends AppCompatActivity implements View.OnClickListener{
    Button btn_register;
    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        etPassword = (EditText) findViewById(R.id.editTextTextPassword);
        btn_register = (Button) findViewById(R.id.register_btn);

        btn_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                //create a request to add this user to the DB

                User registeredData = new User(email,password);
                break;
        }
    }
}