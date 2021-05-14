package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class ListadoRecursos extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private RecursosAdapter mRecursosAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_listado_recursos);

        //Instanciacion del Recycler View
        mRecyclerView = findViewById(R.id.recyclerViewListadoRecursos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Coloca los recursos
        recyclerViewRecursos();

        //Funcion que determina que accion se realiza cuando se hace click en algun recurso
        onClickRecurso();
    }

    private void recyclerViewRecursos() {
        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");
        //Consulta para obtener los datos de la BD
        Query query = db.collection("listaDeRecursos").whereEqualTo("idDelLocal", idRestaurante);

        FirestoreRecyclerOptions<RecursosPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<RecursosPojo>()
                .setQuery(query, RecursosPojo.class).build();

        mRecursosAdapter = new RecursosAdapter(firestoreRecyclerOptions);
        mRecursosAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mRecursosAdapter);
    }

    private void onClickRecurso() {
        mRecursosAdapter.setOnItemClickListener(new RecursosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                Intent i = new Intent(ListadoRecursos.this, DisponibilidadRecursos.class);
                String nombreIngrediente = documentSnapshot.get("nombreDeIngrediente").toString();
                Boolean disponibilidad = documentSnapshot.getBoolean("disponibilidad");
                String ID = documentSnapshot.getId();
                i.putExtra("nombreIngrediente", nombreIngrediente);
                i.putExtra("disponibilidad", disponibilidad);
                i.putExtra("id", ID);
                startActivity(i);
            }
        });
    }


    public void onStart() {
        super.onStart();
        mRecursosAdapter.startListening();
    }

    public void onStop() {
        super.onStop();
        mRecursosAdapter.startListening();
    }

}