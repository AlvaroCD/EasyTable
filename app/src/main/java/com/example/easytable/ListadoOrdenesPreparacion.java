package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class ListadoOrdenesPreparacion extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private ListadoOrdenesAdapter mOrdenesAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_listado_ordenes_preparacion);

        //Instanciacion del Recycler View
        mRecyclerView = findViewById(R.id.recyclerViewListadoOrdenesPreparacion);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle extra = getIntent().getExtras();
        String idDelLocal = extra.getString("idRestaurante");

        //Coloca las ordenes
        recyclerViewOrdenes(idDelLocal);

        //Funcion que determina que accion se realiza cuando se hace click en alguna orden
        onClickOrden(idDelLocal);

    }

    private void recyclerViewOrdenes(String idDelLocal) {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("cuenta").whereEqualTo("statusPreparacion", 1)
                .whereEqualTo("idDelLocal", idDelLocal);

        FirestoreRecyclerOptions<ListadoOrdenesPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ListadoOrdenesPojo>()
                .setQuery(query, ListadoOrdenesPojo.class).build();

        mOrdenesAdapter = new ListadoOrdenesAdapter(firestoreRecyclerOptions);
        mOrdenesAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mOrdenesAdapter);
    }

    private void onClickOrden(String idDelLocal) {
        mOrdenesAdapter.setOnItemClickListener(new ListadoOrdenesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                String idCuenta = documentSnapshot.getId();
                DocumentReference documentReference = db.collection("cuenta").document(idCuenta);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        Intent i = new Intent(ListadoOrdenesPreparacion.this, ListadoPlatillosOrdenados.class);
                        //i.putExtra("idOrden", ID);
                        i.putExtra("idCuenta", idCuenta);
                        i.putExtra("idRestaurante", idDelLocal);
                        Toast.makeText(ListadoOrdenesPreparacion.this, idDelLocal, Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
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