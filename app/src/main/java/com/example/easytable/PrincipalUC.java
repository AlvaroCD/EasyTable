package com.example.easytable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class PrincipalUC extends Activity implements ZXingScannerView.ResultHandler{

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private RestaurantesAdapter mAdapter;
    private ImageButton ImagenQR, mSearch;
    private Button mReservacion, mLogOut;


    //Objetos para utilizar las dependencias
    private ZXingScannerView mScannerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    //Vinculacion de la actividad con el layout
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.vista_principal_usuario_cliente);

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        ImagenQR = findViewById(R.id.codigoQR);
        mLogOut = findViewById(R.id.LogOutButton2);
        mSearch = findViewById(R.id.lupa);
        mReservacion = findViewById(R.id.reservacionCliente);

        //Instanciación de Firebase Authentication y de Firebase Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

            //Instanciacion del Recycler View
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoRestaurantes);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Coloca los restaurantes
        recyclerViewRestaurante();

        //Funcion que determina que accion se realiza cuando se hace click en algun restaurante
        onClickRestaurante();

        String idUsuario = mAuth.getUid();
        cancelacionAutomatica(idUsuario);

        //Boton de codigoQR
        ImagenQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerView = new ZXingScannerView(PrincipalUC.this);
                setContentView(mScannerView);
                mScannerView.setResultHandler(PrincipalUC.this);
                mScannerView.startCamera();
            }
        });


        //Boton de busqueda
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalUC.this, BusquedaRestaurante.class));
            }
        });

        mReservacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PrincipalUC.this, ReservacionCliente.class);

                startActivity(i);
            }
        });

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
            }
        });
    }

    private void cancelacionAutomatica(String idUsuario) {
        Calendar obtencionDeHora = Calendar.getInstance();
        String minutos;
        if (obtencionDeHora.get(Calendar.MINUTE)<9){
            minutos = "0"+obtencionDeHora.get(Calendar.MINUTE);
        }
        else {
            minutos = ""+obtencionDeHora.get(Calendar.MINUTE);
        }
        String horaActual = obtencionDeHora.get(Calendar.HOUR_OF_DAY)+":"+minutos;
        Toast.makeText(this, horaActual, Toast.LENGTH_SHORT).show();
        db.collection("reservaciones").document(idUsuario).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String horaReservacion = documentSnapshot.getString("hora");
                        if (horaActual.equals(horaReservacion)){
                            Timer timer = new Timer();
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    db.collection("reservaciones").document(idUsuario).delete();
                                    Map<String, Object> consecuenciasDeCancelacion = new HashMap<>();
                                    consecuenciasDeCancelacion.put("Adeudo", true);
                                    consecuenciasDeCancelacion.put("Reserva", false);
                                    db.collection("usuario").document(idUsuario).update(consecuenciasDeCancelacion);
                                }
                            };
                            //Temporizador de 10 minutos
                            timer.schedule(timerTask, 600000);
                        }
                    }
                });

    }

    private void recyclerViewRestaurante() {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("restaurante");

        FirestoreRecyclerOptions<RestaurantePojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<RestaurantePojo>()
                .setQuery(query, RestaurantePojo.class).build();

        mAdapter = new RestaurantesAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void onClickRestaurante() {
        mAdapter.setOnItemClickListener(new RestaurantesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                RestaurantePojo restaurante = documentSnapshot.toObject(RestaurantePojo.class);
                String id = documentSnapshot.getId();
                String nombreRestaurante = documentSnapshot.get("nombreLocal").toString();
                Intent i = new Intent(PrincipalUC.this, Restaurante.class);
                i.putExtra("idRestaurante",id);
                i.putExtra("nombreRestaurante", nombreRestaurante);
                startActivity(i);
            }
        });
    }


    //Metodo para desifrar codigo QR
    @Override
    public void handleResult(com.google.zxing.Result result) {

        String dato = result.getText();

        final DocumentReference doc = db.collection("mesa").document(dato);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String idDelLocal = value.get("idDelLocal").toString();
                String nombreRestaurante = value.get("nombreDelLocal").toString();
                String idMesa = value.getId().toString();
                boolean status = value.getBoolean("statusMesa");

                String idOrden = UUID.randomUUID().toString();


                //Envio de informacion a la vista MenuLocal
                Intent Restaurante = new Intent(PrincipalUC.this, MenuLocal.class);

                Restaurante.putExtra("idRestaurante",idDelLocal);
                Restaurante.putExtra("nombreDelLocal", nombreRestaurante);
                Restaurante.putExtra("idMesa", idMesa);
                Restaurante.putExtra("estado", status);
                Restaurante.putExtra("montoPagar", 0);
                Restaurante.putExtra("idOrden", idOrden);

                startActivity(Restaurante);
                finish();
            }
        });
    }


    //Metodo para que que  cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los restaurantes)
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los restaurantes)
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}