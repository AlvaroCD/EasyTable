package com.example.easytable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MenuLocalMU extends Activity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private PlatilloAdapter mAdapter;
    private TextView mNombreLocal;

    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_menu);

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        //Obtención de los datos de la vista PrincipalUC
        String idCuenta = getIntent().getStringExtra("idCuenta");
        String idDelLocal = getIntent().getStringExtra("idRestaurante");

        //String idMesa = getIntent().getStringExtra("idMesa");
        //String idOrden = getIntent().getStringExtra("idOrden");


        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mNombreLocal = findViewById(R.id.nombreLocal);

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoPlatillos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));




        DocumentReference docRef = db.collection("restaurante").document(idDelLocal);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                String nombreRestaurante = value.get("nombreLocal").toString();
                mNombreLocal.setText(nombreRestaurante);

            }
        });

        recycleView(idDelLocal);

        //Funcion que determina que accion se realiza cuando se hace click en algun platillo
        onClickPlatillo(idDelLocal, idCuenta);
        //onClickPlatillo(idDelLocal, idCuenta, idOrden, idMesa);
    }
    private void recycleView(String idDelLocal) {

        //Consulta para obtener los datos de la BD
        Query query = db.collection("platillos").whereEqualTo("idDelLocal", idDelLocal);

        FirestoreRecyclerOptions<PlatilloPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<PlatilloPojo>()
                .setQuery(query, PlatilloPojo.class).build();

        mAdapter = new PlatilloAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void onClickPlatillo(String idDelLocal, String idCuenta) {
        //private void onClickPlatillo(String idDelLocal, String idCuenta, String idOrden, String idMesa) {

        mAdapter.setOnItemClickListener(new PlatilloAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {

                String nombrePlatillo = documentSnapshot.get("nombrePlatillo").toString();
                String precio = documentSnapshot.get("precio").toString();
                String idPlatillo = documentSnapshot.getId();
                boolean disponibilidadPlatillo = documentSnapshot.getBoolean("disponibilidad");


                Intent i = new Intent(MenuLocalMU.this, OrdenMU.class);

                i.putExtra("nombrePlatillo", nombrePlatillo);
                i.putExtra("precio", precio);
                i.putExtra("idPlatillo", idPlatillo);
                i.putExtra("idRestaurante",idDelLocal);
                i.putExtra("idCuenta", idCuenta);
                i.putExtra("disponibilidadPlatillo", disponibilidadPlatillo);
               //i.putExtra("idOrden", idOrden);
               //i.putExtra("idMesa", idMesa);
                //Toast.makeText(MenuLocalMU.this, idCuenta, Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
    }


    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los Platillos)
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la a  plicacion, la aplicación deje de actualizar los datos de la misma (datos de los Platillos     )
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}


