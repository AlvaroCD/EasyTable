package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListadoQuejas extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerViewQuejas;
    private QuejasAdapter mQuejasAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_listado_quejas);

        //Instanciacion del Recycler View
        mRecyclerViewQuejas = findViewById(R.id.recyclerViewListadoQuejas);
        mRecyclerViewQuejas.setLayoutManager(new LinearLayoutManager(this));

        Bundle extra = getIntent().getExtras();
        String idDelLocal = extra.getString("idRestaurante");

        //Coloca las ordenes
        recyclerViewQuejas(idDelLocal);


    }

    private void recyclerViewQuejas(String idDelLocal) {

        //Consulta para obtener los datos de la BD
        Query query = db.collection("quejas").whereEqualTo("idDelLocal", idDelLocal);

        FirestoreRecyclerOptions<QuejasPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<QuejasPojo>()
                .setQuery(query, QuejasPojo.class).build();

        mQuejasAdapter = new QuejasAdapter(firestoreRecyclerOptions);
        mQuejasAdapter.notifyDataSetChanged();
        mRecyclerViewQuejas.setAdapter(mQuejasAdapter);

    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mQuejasAdapter.startListening();
        //mPlatillosOrdenadosAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mQuejasAdapter.stopListening();
        //mPlatillosOrdenadosAdapter.stopListening();
    }
}