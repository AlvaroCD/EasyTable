package com.example.easytable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class Orden extends Activity {
    private TextView  mCostoToltal;
    private Button mOrden, mPagar, mQueja, mAnadir;
    private RecyclerView mRecyclerViewOrden, mRecyclerViewlistadoPedidos;
    private PlatilloAdapter mAdapterOrden, mAdapterListadoPedidos ;

    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_orden);

        String nombrePlatillo;
        String idCuenta;
        String nombreRestaurante;
        String idMesa;
        nombrePlatillo = getIntent().getStringExtra("nombrePlatillo");
        idCuenta = getIntent().getStringExtra("idCuenta");
        nombreRestaurante = getIntent().getStringExtra("idRestaurante");
        idMesa = getIntent().getStringExtra("idMesa");

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

        mAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Orden.this, MenuLocal.class);
                intent.putExtra("statusOrden", false);
                intent.putExtra("idRestaurante",nombreRestaurante);
                intent.putExtra("idMesa", idMesa);
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

        mAdapterOrden = new PlatilloAdapter(firestoreRecyclerOptions);
        mAdapterOrden.notifyDataSetChanged();
        mRecyclerViewOrden.setAdapter(mAdapterOrden);


    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los Platillos)
    @Override
    protected void onStart() {
        super.onStart();
        mAdapterOrden.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los Platillos     )
    @Override
    protected void onStop() {
        super.onStop();
        mAdapterOrden.stopListening();
    }
}
