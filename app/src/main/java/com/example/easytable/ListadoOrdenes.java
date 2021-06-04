package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class ListadoOrdenes extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerViewOrdenes, mRecyclerViewPlatillosOrdenados;
    private ListadoOrdenesAdapter mOrdenesAdapter;
    //private ListadoPlatillosOrdenadosAdapter mPlatillosOrdenadosAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_listado_ordenes);

        //Instanciacion del Recycler View
        mRecyclerViewOrdenes = findViewById(R.id.recyclerViewListadoOrdenes);
        mRecyclerViewOrdenes.setLayoutManager(new LinearLayoutManager(this));

        //TODO: REPLANTEAR COMO ES QUE SE VAN A MOSTRAR LOS PLATILLOS ORDENADOS EN EL LISTADO
//        mRecyclerViewPlatillosOrdenados = findViewById(R.id.recyclerViewListadoPlatillosOrdenados);
//        mRecyclerViewPlatillosOrdenados.setLayoutManager(new LinearLayoutManager(this));


        //Coloca las ordenes
        recyclerViewOrdenes();

        //Coloca los platillos
        //recyclerViewPlatillosOrdenados();

        //Funcion que determina que accion se realiza cuando se hace click en alguna orden
        onClickOrden();
    }

//    private void recyclerViewPlatillosOrdenados() {
//        //Consulta para obtener los datos de la BD
//        Query query = db.collection("orden").whereEqualTo("mesa", "p5LATAuaqXza4cIKVn1W");
//
//        FirestoreRecyclerOptions<ListadoPlatillosOrdenadosPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ListadoPlatillosOrdenadosPojo>()
//                .setQuery(query, ListadoPlatillosOrdenadosPojo.class).build();
//
//        mPlatillosOrdenadosAdapter = new ListadoPlatillosOrdenadosAdapter(firestoreRecyclerOptions);
//        mPlatillosOrdenadosAdapter.notifyDataSetChanged();
//        mRecyclerViewPlatillosOrdenados.setAdapter(mPlatillosOrdenadosAdapter);
//    }


    private void recyclerViewOrdenes() {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("orden").whereEqualTo("statusPreparacion", 0);

        FirestoreRecyclerOptions<ListadoOrdenesPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ListadoOrdenesPojo>()
                .setQuery(query, ListadoOrdenesPojo.class).build();

        mOrdenesAdapter = new ListadoOrdenesAdapter(firestoreRecyclerOptions);
        mOrdenesAdapter.notifyDataSetChanged();
        mRecyclerViewOrdenes.setAdapter(mOrdenesAdapter);
    }

    private void onClickOrden() {
        mOrdenesAdapter.setOnItemClickListener(new ListadoOrdenesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                String ID = documentSnapshot.getId();
                mostrarDialogo(ID);
            }
        });
    }

    private void mostrarDialogo(String id) {
        new AlertDialog.Builder(this)
                .setTitle("¿Iniciar la preparación?")
                .setMessage("Se le avisará a la mesa seleccionada que se está preparando su orden")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map <String, Object> preparacion = new HashMap<>();
                        preparacion.put("statusPreparacion", 1);

                        db.collection("orden").document(id).update(preparacion)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ListadoOrdenes.this, "En preparacion", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ListadoOrdenes.this, "Error al comenzar la preparacion", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Mensaje", "Se cancelo la accion");
                    }
                })
                .show();
    }


    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mOrdenesAdapter.startListening();
        //mPlatillosOrdenadosAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mOrdenesAdapter.stopListening();
        //mPlatillosOrdenadosAdapter.stopListening();
    }

}