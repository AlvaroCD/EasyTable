package com.example.easytable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Orden extends Activity {
    private TextView  mCostoToltal;
    private Button mOrden, mPagar, mQueja, mAnadir;
    private RecyclerView mRecyclerViewOrden, mRecyclerViewlistadoPedidos;
    private PlatilloAdapter mAdapterOrden, mAdapterListadoPedidos ;

    private static float cantidadSumar, montoPagar;

    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_orden);

        String nombrePlatillo, idOrden, idRestaurante, idMesa, idCuenta, idPlatillo, precioPlatillo;

        nombrePlatillo = getIntent().getStringExtra("nombrePlatillo");
        idPlatillo = getIntent().getStringExtra("idPlatillo");
        precioPlatillo = getIntent().getStringExtra("precio");
        idOrden = getIntent().getStringExtra("idOrden");
        idRestaurante = getIntent().getStringExtra("idRestaurante");
        idMesa = getIntent().getStringExtra("idMesa");
        idCuenta = getIntent().getStringExtra("idCuenta");

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mCostoToltal = findViewById(R.id.costoPlatillo);
        mOrden = findViewById(R.id.ordenar);
        mPagar = findViewById(R.id.pagar);
        mQueja = findViewById(R.id.queja);
        mAnadir = findViewById(R.id.añadir);


        //Instanciacion del Recycler View
        mRecyclerViewOrden = (RecyclerView) findViewById(R.id.recyclerViewListadoOrden);
        mRecyclerViewOrden.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerViewlistadoPedidos = findViewById(R.id.recyclerViewListadoPedidos);
        mRecyclerViewlistadoPedidos.setLayoutManager(new LinearLayoutManager(this));

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        recycleViewOrden(nombrePlatillo);


        //Query query = db.collection("orden").document(idOrden)
        mAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> platillo = new HashMap<>();
                platillo.put("nombrePlatillo", nombrePlatillo);
                db.collection("orden").document(idOrden).collection("platillos").document(idPlatillo).set(platillo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                cantidadSumar = Float.parseFloat(precioPlatillo);
                                montoPagar = (montoPagar + cantidadSumar);
                                Map<String, Object> monto = new HashMap<>();
                                monto.put("montoPagar", (montoPagar));
                                db.collection("Orden").document(idOrden).update(monto)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Orden.this, "Good", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Orden.this, "Bad", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Orden.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                            }
                        });

                Intent intent = new Intent(Orden.this, MenuLocal.class);
                intent.putExtra("idRestaurante",idRestaurante);
                intent.putExtra("idMesa", idMesa);
                intent.putExtra("statusMesa", false);
                intent.putExtra("statusOrden", false);
                intent.putExtra("idOrden", idOrden);
                startActivity(intent);
                finish();
            }
        });

        mOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mQueja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Orden.this, Queja.class);
                startActivity(intent);
            }
        });

        mPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Orden.this, PagarCuenta.class);
                intent.putExtra("idCuenta", idCuenta);
                startActivity(intent);
            }
        });
    }

    private void recycleViewOrden(String nombrePlatillo) {

        //Consulta para obtener los datos de la BD
        Query query = db.collection("platillos").whereEqualTo("nombrePlatillo", nombrePlatillo);

        FirestoreRecyclerOptions<PlatilloPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<PlatilloPojo>()
                .setQuery(query, PlatilloPojo.class).build();

        mAdapterListadoPedidos = new PlatilloAdapter(firestoreRecyclerOptions);
        mAdapterListadoPedidos.notifyDataSetChanged();
        mRecyclerViewlistadoPedidos.setAdapter(mAdapterListadoPedidos);
    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los Platillos)
    @Override
    protected void onStart() {
        super.onStart();
        mAdapterListadoPedidos.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los Platillos     )
    @Override
    protected void onStop() {
        super.onStop();
        mAdapterListadoPedidos.stopListening();
    }
}
