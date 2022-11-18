package com.ium.example.progetto.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ium.example.progetto.MainActivity;
import com.ium.example.progetto.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    User user;
    private RequestQueue mQueue;
    Button btn_login;
    EditText etEmail, etPassword;
    TextView tvregisterLink;
    UserLocalStore userLocalStore;
    public static boolean LoggedIn; //var per prenotare
    String User_param, Password_param;
    String MYURL = MainActivity.MYURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etPassword = findViewById(R.id.editTextTextPassword);
        btn_login = findViewById(R.id.login_btn);
        btn_login.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        mQueue = Volley.newRequestQueue(this);
    }

    public void backToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void logOut(View view) {
        LoggedIn = false;
        Toast.makeText(LoginActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
    }

    //metodo per chiudere l'attività
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if(etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Per favore riempi tutti gli spazi", Toast.LENGTH_SHORT).show();
                } else {
                    //**** parte di codice per la request alla servlet ****//
                    User_param = etEmail.getText().toString();
                    Password_param = etPassword.getText().toString();
                    Toast.makeText(LoginActivity.this, "" + User_param + " " + Password_param, Toast.LENGTH_SHORT).show();

                    mQueue = Volley.newRequestQueue(this);

                    StringRequest request = new StringRequest(Request.Method.POST, MYURL, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // on below line we are displaying a success toast message.
                            if(response.equals("user") || response.equals("admin")){
                                Toast.makeText(LoginActivity.this, "Logged In effettuato con successo", Toast.LENGTH_SHORT).show();
                                LoggedIn = true;
                                user = new User(User_param, Password_param);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Can't login : " + response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // method to handle errors.
                            Toast.makeText(LoginActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {


                            // below line we are creating a map for
                            // storing our values in key and value pair.
                            Map<String, String> params = new HashMap<String, String>();

                            // on below line we are passing our key
                            // and value pair to our parameters.
                            params.put("action", "login");
                            params.put("username", User_param);
                            params.put("password", Password_param);

                            // at last we are
                            // returning our params.
                            return params;
                        }
                    };
                    // below line is to make
                    // a json object request.
                    mQueue.add(request);
                    break;

                }
        }
    }
}
    //function to connect yout username and psw to a existing user

