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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ium.example.progetto.MainActivity;
import com.ium.example.progetto.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingTestActivity extends AppCompatActivity {
    Spinner mySpinner;
    RecyclerView recyclerView;
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> materia = new ArrayList<>();
    ArrayList<String> orario = new ArrayList<>();
    Gson gson = new Gson();
    JSONObject repJson = new JSONObject();
    String MYURL = MainActivity.MYURL;
    private RequestQueue mQueue;



    //qui di seguito inseriamo i valori dei giorni nela lista dayList SPINNER
    String[] categoryDays = {"Any","Lunedi","Martedi","Mercoledi","Giovedi","Venerdi"};


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

        checkLesson();
    }

    private void checkLesson() {

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //questi clear resettano i valori degli array ogni volta ogni volta
                date.clear();
                name.clear();
                materia.clear();
                orario.clear();

                /*
                FARE LA RICHIESTA DELLE RIPE E USARE LA RESPONSE COME JSONobject da usare per il JsonArray per fetchare le ripe
                */

                mQueue = Volley.newRequestQueue(BookingTestActivity.this);

                StringRequest request = new StringRequest(Request.Method.POST, MYURL, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            repJson = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BookingTestActivity.this, "Error while parsing from string to Json", Toast.LENGTH_SHORT).show();

                        }
                        // on below line we are displaying a success toast message.
                        try {
                            // on below line we are parsing the response
                            // to json object to extract data from it.
                            JSONObject respObj = new JSONObject(response);

                            // on below line we are setting this string s to our text view.
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // method to handle errors.
                        Toast.makeText(BookingTestActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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

                /*
                * FINE COLLEGAMENTO CON SERVLET
                * */


                String daySelected = null;
                switch (position){ //switch per vedere che giorno e'
                    case 0:
                        daySelected = "Any";
                        break;
                    case 1:
                        daySelected = "Lunedi";
                        break;
                    case 2:
                        daySelected = "Martedi";
                        break;
                    case 3:
                        daySelected = "Mercoledi";
                        break;
                    case 4:
                        daySelected = "Giovedi";
                        break;
                    case 5:
                        daySelected = "Venerdi";
                    default:
                        break;
                }

                //QUI INIZIA LA COMPILAZIONE DI ELEMENTI NEL RECYCLERVIEW

                if(position == 0){ // se è any carichiamo tutto
                    try {

                        //JSONObject jsonObject = new JSONObject(JsonDataFromAsset()); //metodo che prende il json dal file corsi.json
                        JSONArray jsonArray=repJson.getJSONArray("ripetizioni");
                        for (int i = 0; i<jsonArray.length();i++){//ciclo su name e materia per prendere tutti i valori che ho nel json
                            JSONObject lessonData = jsonArray.getJSONObject(i);
                                date.add(lessonData.getString("giorno"));
                                name.add(lessonData.getString("prof"));
                                materia.add(lessonData.getString("corso"));
                                orario.add(lessonData.getString("ora"));
                            }
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }

                    HelperAdapter helperAdapter = new HelperAdapter(date,name,materia,orario,BookingTestActivity.this);
                    recyclerView.setAdapter(helperAdapter);

                } else if(position > 0 && position < categoryDays.length){ //qui abbiamo valori da Lun a Ven
                    try {
                        //JSONObject jsonObject = new JSONObject(JsonDataFromAsset()); //metodo che prende il json dal file corsi.json
                        JSONArray jsonArray=repJson.getJSONArray("corsi");
                        for (int i = 0; i<jsonArray.length();i++){   //ciclo su name e materia per prendere tutti i valori che ho nel json
                            JSONObject userData = jsonArray.getJSONObject(i);
                            System.out.println(userData.getString("giorno"));
                            if(userData.getString("giorno").equals(daySelected)) {
                                date.add(userData.getString("giorno"));
                                name.add(userData.getString("prof"));
                                materia.add(userData.getString("corso"));
                                orario.add(userData.getString("ora"));
                            }
                        }

                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                } else {
                    Toast.makeText(BookingTestActivity.this, "Selected Category doesn't exist!", Toast.LENGTH_SHORT).show();

                }
                HelperAdapter helperAdapter = new HelperAdapter(date,name,materia,orario,BookingTestActivity.this);
                recyclerView.setAdapter(helperAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private String JsonDataFromAsset() throws IOException {
        String json=null;
        try {
            InputStream inputStream = getAssets().open("corsi.json");
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData, "UTF-8");
        } catch (IOException e){
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public void onBackPressed(){
        finish();
    }

}

//fatto ciò implementare la possibilità di fare il login per permettere all'utente di salvare le proprie ripetizioni con un account

//tenere in locale tutte le ripetizioni salvate per determinato utente
//implementare nella home tutte le ripetizioni a cui ci si è pronotati attraverso il menu