package com.ium.example.progetto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ium.example.progetto.BookingTest.BookingTestActivity;
import com.ium.example.progetto.account.LoginActivity;
import com.ium.example.progetto.account.UserLocalStore;
import com.ium.example.progetto.settings.SettingsActivity;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;

//import com.ium.example.progetto.calendar.WeekViewActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    UserLocalStore userLocalStore;
    boolean canBook = false;
    public static String MYURL = "http://10.0.2.2:8080/Progetto1_war_exploded/servlet1";
    JSONObject repJson = new JSONObject();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLocalStore = new UserLocalStore(this);


    }

    @Override
    protected void onStart(){
        super.onStart();

        if(authenticate()){
            canBook = true;
        } else {
            canBook = false;
        }
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    public void btnSettings_onClick(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void btnLogin_onClick(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    public void btnSeeRepetitions_onClick(View view){
        Intent intent = new Intent(this, BookingTestActivity.class);
        startActivity(intent);
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


