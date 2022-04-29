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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ium.example.progetto.MainActivity;
import com.ium.example.progetto.R;
import com.ium.example.progetto.account.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//classe che inmplementa l'interfaccia dei Json all'interno delle recycler view utilizzando e mettendo i contenuti all'niterno di row_test

public class HelperAdapter extends RecyclerView.Adapter<HelperAdapter.MyViewClass> implements PopupMenu.OnMenuItemClickListener {

    private RequestQueue mQueue;
    ArrayList<Ripetizioni> ripetizioni;
    Context context;
    boolean prenotato = false;
    LessonLocalStore lessonLocalStore;
    String MYURL = MainActivity.MYURL; // url della servlet
    int ID;
    Gson gson = new Gson();


    public HelperAdapter(ArrayList<Ripetizioni> ripetizioniArrayList,Context context) {
        this.ripetizioni = ripetizioniArrayList;
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
        holder.name.setText(ripetizioni.get(position).getProf());
        holder.materia.setText(ripetizioni.get(position).getCorso());
        holder.orario.setText(String.valueOf(ripetizioni.get(position).getOra()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = holder.getBindingAdapterPosition(); //qui prendo la posizione dell'item premuto nella recyclerview
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
        return ripetizioni.size();
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

                            //MANDA LA REQUESTPOST PER LA RIPETIZIONE

                            mQueue = Volley.newRequestQueue(context);


                            StringRequest request = new StringRequest(Request.Method.POST,MYURL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error){
                                    // method to handle errors.
                                    Toast.makeText(context, "Fail while Booking = " + error, Toast.LENGTH_SHORT).show();
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
                                    params.put("rdp", gson.toJson(ripetizioni.get(ID)));
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

