package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class PrincipalUCJ extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerViewCuentas;
    private CuentasAdapter mCuentasAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button mLogOut;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal_ucj);

        //Instanciacion del Recycler View
        mRecyclerViewCuentas = findViewById(R.id.recyclerViewRecibirCuentas);
        mRecyclerViewCuentas.setLayoutManager(new LinearLayoutManager(this));
        mLogOut = findViewById(R.id.LogOutButtonCajero);
        mAuth = FirebaseAuth.getInstance();

        String idDelLocal = getIntent().getStringExtra("idLocal");


        recyclerViewCuentas(idDelLocal);

        //Funcion que determina que accion se realiza cuando se hace click en alguna cuenta
        onClickCuenta();




        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
            }
        });

    }

    private void onClickCuenta() {
        mCuentasAdapter.setOnItemClickListener(new CuentasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                Intent i = new Intent(PrincipalUCJ.this, DetallesCuenta.class);
                String idCuenta = documentSnapshot.getId();
                boolean metodoPago = documentSnapshot.getBoolean("efectivo");
                long montoPagado =  documentSnapshot.getLong("montoPagar");
                i.putExtra("idCuenta", idCuenta);
                i.putExtra("metodoPago", metodoPago);
                i.putExtra("montoPagado", montoPagado);
                startActivity(i);
                Toast.makeText(PrincipalUCJ.this, idCuenta, Toast.LENGTH_SHORT).show();
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