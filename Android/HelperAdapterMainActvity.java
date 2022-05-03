package com.ium.example.progetto;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.ium.example.progetto.BookingTest.Ripetizioni;
import com.ium.example.progetto.account.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelperAdapterMainActvity  extends RecyclerView.Adapter<HelperAdapterMainActvity.MyViewClass> implements PopupMenu.OnMenuItemClickListener{
    private RequestQueue mQueue;
    ArrayList<Ripetizioni> ripetizioni;
    Prenotazione[] prenotazioni;
    Context context;
    boolean prenotato = false;
    String MYURL = MainActivity.MYURL; // url della servlet
    int ID;
    Gson gson = new Gson();


    public HelperAdapterMainActvity(Prenotazione[] prenotazioni,ArrayList<Ripetizioni> ripetizioniArrayList,Context context) {
        this.prenotazioni = prenotazioni;
        this.ripetizioni = ripetizioniArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HelperAdapterMainActvity.MyViewClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View LessonView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_test,parent, false);
        HelperAdapterMainActvity.MyViewClass myViewClass = new HelperAdapterMainActvity.MyViewClass(LessonView);
        return myViewClass;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewClass holder, int position) {
        //questa parte setta i nomi nella recycler view
        holder.name.setText(ripetizioni.get(position).getProf());
        holder.materia.setText(ripetizioni.get(position).getCorso());
        holder.orario.setText(String.valueOf(ripetizioni.get(position).getOra()));
        holder.giorno.setText(String.valueOf(ripetizioni.get(position).getGiorno()));
        holder.stato.setText(ripetizioni.get(position).stato());



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
        popupMenu.inflate(R.menu.popup_menu_main);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return ripetizioni.size();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item){
        boolean loginHelper = LoginActivity.LoggedIn; // mi prendo il valore del login
        switch (item.getItemId()){
            case R.id.item5: //SI
                if(loginHelper){
                    mQueue = Volley.newRequestQueue(context);
                    StringRequest request = new StringRequest(Request.Method.POST,MYURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                                ripetizioni.remove(ID); //tolgo da arraylist ripe
                                prenotazioni[ID] = null; //tolgo prneotazioni quella scelta
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error){
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
                            params.put("action", "disdici");
                            params.put("pdd", gson.toJson(prenotazioni[ID]));
                            return params;
                        }
                    };
                    // below line is to make
                    // a json object request.
                    mQueue.add(request);
                }
                return prenotato;

            case R.id.item6:   //NO
                prenotato = false;
                return prenotato;

            case R.id.item4: //VUOI disdire LA LEZIONE??
                prenotato = false;
                return prenotato;

            default:
                prenotato = false;
                return prenotato;
        }

    }

    /*public void onClick_setDone(View view){
        mQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST,MYURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                    ripetizioni.remove(ID); //tolgo da arraylist ripe
                    prenotazioni[ID] = null; //tolgo prneotazioni quella scelta
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
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
                params.put("action", "disdici");
                params.put("pdd", gson.toJson(prenotazioni[ID]));
                return params;
            }
        };
        // below line is to make
        // a json object request.
        mQueue.add(request);
    }*/

    //metodo per la visualizzazione dell ripetizioni
    public class MyViewClass extends RecyclerView.ViewHolder{
        TextView name;
        TextView materia;
        TextView orario;
        TextView giorno;
        TextView stato;
        TextView setDone;

        public MyViewClass(@NonNull View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            materia = (TextView) itemView.findViewById(R.id.materia);
            orario = (TextView) itemView.findViewById(R.id.orario);
            giorno = (TextView) itemView.findViewById(R.id.giorno);
            stato = (TextView) itemView.findViewById(R.id.stato);
            setDone = (TextView) itemView.findViewById(R.id.setDone);
        }
    }
}

