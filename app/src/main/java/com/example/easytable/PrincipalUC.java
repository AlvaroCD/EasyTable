package com.example.easytable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class PrincipalUC extends Activity implements ZXingScannerView.ResultHandler{

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private RestaurantesAdapter mAdapter;
    private ImageButton ImagenQR, mSearch;
    private Button mLogOut;


    //Objetos para utilizar las dependencias
    private ZXingScannerView mScannerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;




    //Vinculacion de la actividad con el layout
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.vista_principal_usuario_cliente);

            Toast.makeText(this, Global.getmIdUsuario(),Toast.LENGTH_LONG ).show();

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        ImagenQR = findViewById(R.id.codigoQR);
        mLogOut = findViewById(R.id.LogOutButton2);
        mSearch = findViewById(R.id.lupa);

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

        //Boton para salir de la app
        onClickSalir();

        //Boton de busqueda
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalUC.this, BusquedaRestaurante.class));
            }
        });

    }

    private void onClickSalir() {
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(PrincipalUC.this, Ingresar.class));
                finish();
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
        final DocumentReference doc = db.collection("orden").document("4bbd719d-f9c8-42a7-8cb8-96a0061dd464");
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                List<String> platillos = (List<String>) value.get("matrizPlatillos");

                platillos.add("Chilaquil");

                Toast.makeText(PrincipalUC.this, (CharSequence) platillos.get(3), Toast.LENGTH_SHORT).show();

                Map<String, Object> platillosAgregados = new HashMap<>();
                platillosAgregados.put("matrizPlatillos", platillos);


//                db.collection("orden").document("4bbd719d-f9c8-42a7-8cb8-96a0061dd464")
//                        .set(platillosAgregados)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                            }
//                        });
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
                String idMesa = "9a66xgz2JYxwrKpSqJfn";
                //String idMesa = value.getId().toString();
                boolean status = value.getBoolean("statusMesa");

                //Envio de informacion a la vista MenuLocal
                Intent Restaurante = new Intent(PrincipalUC.this, MenuLocal.class);

                Restaurante.putExtra("idRestaurante",idDelLocal);
                Restaurante.putExtra("nombreDelLocal", nombreRestaurante);
                Restaurante.putExtra("idMesa", idMesa);
                Restaurante.putExtra("estado", status);
                Restaurante.putExtra("montoPagar", 0);

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