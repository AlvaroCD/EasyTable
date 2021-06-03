package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListadoOrdenes extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private ListadoOrdenesAdapter mOrdenesAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_listado_ordenes);

        //Instanciacion del Recycler View
        mRecyclerView = findViewById(R.id.recyclerViewListadoOrdenes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Coloca las ordenes
        recyclerViewOrdenes();
    }

    private void recyclerViewOrdenes() {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("orden");

        FirestoreRecyclerOptions<ListadoOrdenesPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ListadoOrdenesPojo>()
                .setQuery(query, ListadoOrdenesPojo.class).build();

        mOrdenesAdapter = new ListadoOrdenesAdapter(firestoreRecyclerOptions);
        mOrdenesAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mOrdenesAdapter);
    }


    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mOrdenesAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mOrdenesAdapter.stopListening();
    }

}