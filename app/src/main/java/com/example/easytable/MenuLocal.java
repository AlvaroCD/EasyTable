package com.example.easytable;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MenuLocal extends Activity {
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

        //Optencion del Id del local escaneado y de su nombre
        String  nombreRestaurante;
        nombreRestaurante = getIntent().getStringExtra("idRestaurante");

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mNombreLocal = findViewById(R.id.nombreLocal);

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoPlatillos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        mNombreLocal.setText(nombreRestaurante);

        recycleView(nombreRestaurante);
    }

    private void recycleView(String nombreRestaurante) {

        //Consulta para obtener los datos de la BD
        Query query = db.collection("platillos").whereEqualTo("nombreDelLocal", nombreRestaurante);

        FirestoreRecyclerOptions<PlatilloPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<PlatilloPojo>()
                .setQuery(query, PlatilloPojo.class).build();

        mAdapter = new PlatilloAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los Platillos)
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los Platillos)
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}

