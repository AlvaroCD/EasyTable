package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Local extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerViewMesasLocal;
    private LocalAdapter mMesasLocalAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_local);

        //Instanciacion del Recycler View
        mRecyclerViewMesasLocal = findViewById(R.id.recyclerViewListadoMesasLocal);
        mRecyclerViewMesasLocal.setLayoutManager(new LinearLayoutManager(this));

        Bundle extra = getIntent().getExtras();
        String idDelLocal = extra.getString("idRestaurante");

        //Coloca las ordenes
        recyclerViewLocal(idDelLocal);

        //Funcion que determina que accion se realiza cuando se hace click en alguna orden
        onClick(idDelLocal);



    }

    private void onClick(String idDelLocal) {
        mMesasLocalAdapter.setOnItemClickListener(new LocalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                String id = documentSnapshot.getId();
                String numeroMesa = documentSnapshot.get("numeroMesa").toString();
                Boolean disponibilidad = documentSnapshot.getBoolean("statusMesa");
                Intent i = new Intent(Local.this, DisponibilidadMesa.class);
                i.putExtra("idMesa", id);
                i.putExtra("numeroMesa", numeroMesa);
                i.putExtra("statusMesa", disponibilidad);
                startActivity(i);
            }
        });
    }

    private void recyclerViewLocal(String idDelLocal) {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("mesa").whereEqualTo("idDelLocal", idDelLocal);

        FirestoreRecyclerOptions<LocalPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<LocalPojo>()
                .setQuery(query, LocalPojo.class).build();

        mMesasLocalAdapter = new LocalAdapter(firestoreRecyclerOptions);
        mMesasLocalAdapter.notifyDataSetChanged();
        mRecyclerViewMesasLocal.setAdapter(mMesasLocalAdapter);
    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mMesasLocalAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mMesasLocalAdapter.stopListening();
    }
}