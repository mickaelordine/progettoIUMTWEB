package com.ium.example.progetto.BookingTest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ium.example.progetto.MainActivity;
import com.ium.example.progetto.R;
import com.ium.example.progetto.account.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//classe che inmplementa l'interfaccia dei Json all'interno delle recycler view utilizzando e mettendo i contenuti all'niterno di row_test

public class HelperAdapter extends RecyclerView.Adapter<HelperAdapter.MyViewClass> implements PopupMenu.OnMenuItemClickListener {

    private RequestQueue mQueue;
    ArrayList<String> date;
    ArrayList<String> name;
    ArrayList<String> corso;
    ArrayList<String> orario;
    Context context;
    boolean prenotato = false;
    LessonLocalStore lessonLocalStore;
    String corso_param, name_param, date_param, orario_param;
    String MYURL = MainActivity.MYURL; // url della servlet


    public HelperAdapter(ArrayList<String> date, ArrayList<String> name, ArrayList<String> corso,ArrayList<String> orario, Context context) {
        this.date = date;
        this.name = name;
        this.orario = orario;
        this.corso = corso;
        this.context = context;
        lessonLocalStore = new LessonLocalStore(context);
    }

    @NonNull
    @Override
    public MyViewClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View LessonView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_test,parent, false);
        MyViewClass myViewClass = new MyViewClass(LessonView);
        return myViewClass;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewClass holder, int position) {
        //questa parte setta i nomi nella recycler view
        holder.name.setText(name.get(position));
        holder.materia.setText(corso.get(position));
        holder.orario.setText(orario.get(position));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
    }

    //metodo per far vedere il menu se fare le ripetizioni o meno
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        boolean loginHelper = LoginActivity.LoggedIn; // mi prendo il valore del login
            switch (item.getItemId()){
                case R.id.item2: //SI
                    //crea il metodo per effettuare la prenotazione
                        if(loginHelper){
                            //se il valore è true, allora effettua la prenotazione

                            Lesson lesson = new Lesson("","","","");
                            lessonLocalStore.storeLessonData(lesson);
                            name_param = lesson.name;
                            corso_param = lesson.corso;
                            date_param = lesson.giorno;
                            orario_param = lesson.ora;

                            mQueue = Volley.newRequestQueue(context);

                            StringRequest request = new StringRequest(Request.Method.POST, MYURL, new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // on below line we are displaying a success toast message.
                                    Toast.makeText(context, "Prenotazione effettuata con Successo" + response, Toast.LENGTH_SHORT).show();

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
                                    Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    // below line we are creating a map for
                                    // storing our values in key and value pair.
                                    Map<String, String> params = new HashMap<String, String>();

                                    // on below line we are passing our key
                                    // and value pair to our parameters.
                                    params.put("action", "prenota");
                                    params.put("materia", "");
                                    params.put("prof", "");

                                    // at last we are
                                    // returning our params.
                                    return params;
                                }
                            };
                            // below line is to make
                            // a json object request.
                            mQueue.add(request);


                        } else {
                            //atrimenti, si apre la pagina per effettuare il Login
                            Toast.makeText(context, "Per prenotare, effettua prima il login", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, LoginActivity.class);
                            context.startActivity(i);
                            prenotato = false;
                        }
                return prenotato;

           case R.id.item3:   //NO
               prenotato = false;
               return prenotato;

           case R.id.item1: //VUOI PRENOTARE LA LEZIONE??
               prenotato = false;
               return prenotato;

           default:
               prenotato = false;
               return prenotato;
       }

    }

    //metodo per la visualizzazione dell ripetizioni
    public class MyViewClass extends RecyclerView.ViewHolder{


        TextView name;
        TextView materia;
        TextView orario;

        public MyViewClass(@NonNull View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            materia = (TextView) itemView.findViewById(R.id.materia);
            orario = (TextView) itemView.findViewById(R.id.orario);
        }
    }
}


//creare la servlet per verificare il corretto funzoionamento di quello che stiamo inviando

//ricontrollare nella LESSONLOCALSTRORE e nell'HELPERADAPTER di prendere i valori che ci interessano davvero, perchè avendo copiato e incollato
//ho paura che non prenda parametri sensanti.

//rienterroga con le servlet dopo aver effettuato la ripetizione