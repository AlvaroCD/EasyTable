package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CuentasUH extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerViewCuentas;
    private CuentasAdapter mCuentasAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_cuentas_uh);

        //Instanciacion del Recycler View
        mRecyclerViewCuentas = findViewById(R.id.recyclerViewRecibirCuentasUH);
        mRecyclerViewCuentas.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();

        String idDelLocal = getIntent().getStringExtra("idRestaurante");


        recyclerViewCuentas(idDelLocal);

        //Funcion que determina que accion se realiza cuando se hace click en alguna cuenta
        onClickCuenta();

    }

    private void onClickCuenta() {
        mCuentasAdapter.setOnItemClickListener(new CuentasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                Intent i = new Intent(CuentasUH.this, DetallesCuenta.class);
                String idCuenta = documentSnapshot.getId();
                boolean metodoPago = documentSnapshot.getBoolean("efectivo");
                long montoPagado =  documentSnapshot.getLong("montoPagar");
                String fecha = documentSnapshot.getString("fecha");
                i.putExtra("idCuenta", idCuenta);
                i.putExtra("metodoPago", metodoPago);
                i.putExtra("montoPagado", montoPagado);
                i.putExtra("fecha", fecha);
                startActivity(i);
            }
        });
    }

    private void recyclerViewCuentas(String idDelLocal) {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("cuenta")
                .whereEqualTo("idDelLocal", idDelLocal);

        FirestoreRecyclerOptions<CuentasPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<CuentasPojo>()
                .setQuery(query, CuentasPojo.class).build();

        mCuentasAdapter = new CuentasAdapter(firestoreRecyclerOptions);
        mCuentasAdapter.notifyDataSetChanged();
        mRecyclerViewCuentas.setAdapter(mCuentasAdapter);
    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mCuentasAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mCuentasAdapter.stopListening();
    }

}