package com.ium.example.progetto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ium.example.progetto.BookingTest.BookingTestActivity;
import com.ium.example.progetto.BookingTest.Ripetizioni;
import com.ium.example.progetto.account.LoginActivity;
import com.ium.example.progetto.account.UserLocalStore;
import com.ium.example.progetto.settings.SettingsActivity;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    UserLocalStore userLocalStore;
    public static String MYURL = "http://10.0.2.2:8080/Progetto1_war_exploded/servlet1";
    RecyclerView recyclerView;
    RequestQueue mQueue;
    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLocalStore = new UserLocalStore(this);

        recyclerView = findViewById(R.id.MainActivityRelative); //presa la visione del recycler
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        mQueue = Volley.newRequestQueue(MainActivity.this);

        boolean loginHelper = LoginActivity.LoggedIn; // mi prendo il valore del login
        if(loginHelper) {
            //INIZIO RICHIESTA RIPE PASSATE
            StringRequest request = new StringRequest(Request.Method.POST, MYURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        ArrayList<Ripetizioni> ripetizioniList = new ArrayList<>();
                        Prenotazione[] prenotazioni = gson.fromJson(response, Prenotazione[].class);
                        for(int i =0; i< prenotazioni.length; i++){
                             ripetizioniList.add(prenotazioni[i].getRip());
                        }

                        HelperAdapterMainActvity helperAdapterMainActvity = new HelperAdapterMainActvity(prenotazioni ,ripetizioniList, MainActivity.this);
                        recyclerView.setAdapter(helperAdapterMainActvity);

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // method to handle errors.
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // below line we are creating a map for
                    // storing our values in key and value pair.
                    Map<String, String> params = new HashMap<String, String>();

                    // on below line we are passing our key
                    // and value pair to our parameters.
                    params.put("action", "myPrenots");
                    return params;
                }
            };
            // below line is to make
            // a json object request.
            mQueue.add(request);
            //FINE RICHIESTA RIPE PASSATE
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    public void btnSettings_onClick(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }



    public void btnLogin_onClick(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public void btnSeeRepetitions_onClick(View view){
        Intent intent = new Intent(this, BookingTestActivity.class);
        startActivity(intent);
        finish();
    }


    //elenco di metodi per i link button
    public void linkButtonUni(View view){
        goToUrl("http://www.di.unito.it/do/home.pl");
    }

    public void linkButtonMaterialeDidattico(View view){
        goToUrl("https://informatica.i-learn.unito.it/");
    }

    public void linkButtonInformazioni(View view){
        goToUrl("http://laurea.educ.di.unito.it/index.php/studiare-informatica/contatti/");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout_btn:
                userLocalStore.setUserLoggedIn(false);
        }
    }
}


//c'e da vedere come cambiare per le ripetizioni e per le prenotazioni in fase di disdire

