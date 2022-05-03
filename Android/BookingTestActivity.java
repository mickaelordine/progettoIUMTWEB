package com.ium.example.progetto.BookingTest;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.ium.example.progetto.MainActivity;
import com.ium.example.progetto.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BookingTestActivity extends AppCompatActivity {
    Spinner mySpinner;
    RecyclerView recyclerView;
    Gson gson = new Gson();
    String MYURL = MainActivity.MYURL;
    ArrayList<Ripetizioni> ripetizioniList = new ArrayList<>();
    ArrayList<Ripetizioni> filtredRep = new ArrayList<>();
    ArrayList<Ripetizioni> arrayList = new ArrayList<>();
    RequestQueue mQueue;







    //qui di seguito inseriamo i valori dei giorni nela lista dayList SPINNER
    String[] categoryDays = {"Any","LUN","MAR","MER","GIO","VEN"};


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_test_view);
        recyclerView = findViewById(R.id.recyclerView); //presa la visione del recycler
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //qui abbiamo settato i valori dello spinner
        mySpinner = findViewById(R.id.spinner2);
        mySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,categoryDays));


        mQueue = Volley.newRequestQueue(BookingTestActivity.this);


        StringRequest request = new StringRequest(Request.Method.POST,MYURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Ripetizioni[] ripetizioni = gson.fromJson(response,Ripetizioni[].class);
                    ripetizioniList.addAll(Arrays.asList(ripetizioni));
                    arrayList = ripetizioniList;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(BookingTestActivity.this, "Fail to get response in BookingTestAct = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("action", "viewPrenotabili");
                return params;
            }
        };
        // below line is to make
        // a json object request.
        mQueue.add(request);

        checkLesson();
    }



    private void checkLesson() {

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //questi clear resettano i valori degli array ogni volta ogni volta
                ripetizioniList.clear();

                String daySelected = null;
                switch (position){ //switch per vedere che giorno e'
                    case 0:
                        daySelected = "Any";
                        break;
                    case 1:
                        daySelected = "LUN";
                        break;
                    case 2:
                        daySelected = "MAR";
                        break;
                    case 3:
                        daySelected = "MER";
                        break;
                    case 4:
                        daySelected = "GIO";
                        break;
                    case 5:
                        daySelected = "VEN";
                    default:
                        break;
                }


                //QUI INIZIA LA COMPILAZIONE DI ELEMENTI NEL RECYCLERVIEW
                if(position == 0){ // se è any carichiamo tutto
                    HelperAdapter helperAdapter = new HelperAdapter(ripetizioniList,BookingTestActivity.this);
                    recyclerView.setAdapter(helperAdapter);

                } else if(position > 0 && position < categoryDays.length){ //qui abbiamo valori da Lun a Ven
                    Toast.makeText(BookingTestActivity.this, "" + ripetizioniList.toString(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i<ripetizioniList.size();i++){
                        if(ripetizioniList.get(i).getGiorno().equals(daySelected)) {
                        filtredRep.add(ripetizioniList.get(i));
                    }
                }
                    HelperAdapter helperAdapter = new HelperAdapter(filtredRep,BookingTestActivity.this);
                    recyclerView.setAdapter(helperAdapter);
                } else {
                    Toast.makeText(BookingTestActivity.this, "Selected Category doesn't exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onBackPressed(){
        finish();
    }

}

//fatto ciò implementare la possibilità di fare il login per permettere all'utente di salvare le proprie ripetizioni con un account

//tenere in locale tutte le ripetizioni salvate per determinato utente
//implementare nella home tutte le ripetizioni a cui ci si è pronotati attraverso il menu


//PROBLEMI 500 NELLA RICHIESTA