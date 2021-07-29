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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Orden extends Activity {
    private ImageButton mMas, mMenos;
    private TextView  mCostoToltal;
    private Button mOrden, mPagar, mQueja, mAnadir;
    private EditText mEspecificaciones, mCantidadAlimentos;
    private RecyclerView mRecyclerViewOrden, mRecyclerViewlistadoPedidos;
    private PlatilloAdapter mAdapterListadoPedidos;
    private PlatillosCuentasAdapter mAdapterPlatillos;

    private static long cantidadSumar, montoPagar, cantidadAlimentos=1;

    private FirebaseAuth mAuth;

    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_orden);

        String nombrePlatillo, idOrden, idRestaurante, idMesa, idCuenta, idPlatillo, precioPlatillo;
        boolean disponibilidadPlatillo;

        mAuth = FirebaseAuth.getInstance();

        String idUsuario = mAuth.getUid();

        nombrePlatillo = getIntent().getStringExtra("nombrePlatillo");
        idPlatillo = getIntent().getStringExtra("idPlatillo");
        precioPlatillo = getIntent().getStringExtra("precio");
        idOrden = getIntent().getStringExtra("idOrden");
        idRestaurante = getIntent().getStringExtra("idRestaurante");
        idMesa = getIntent().getStringExtra("idMesa");
        idCuenta = getIntent().getStringExtra("idCuenta");
        disponibilidadPlatillo = getIntent().getBooleanExtra("disponibilidadPlatillo", true);


        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mCostoToltal = findViewById(R.id.montoAPagar);
        mOrden = findViewById(R.id.ordenar);
        mPagar = findViewById(R.id.pagar);
        mQueja = findViewById(R.id.queja);
        mAnadir = findViewById(R.id.añadir);
        mEspecificaciones = findViewById(R.id.especificaciones);
        mCantidadAlimentos = findViewById(R.id.cantidadAlimentos);
        mMas = findViewById(R.id.mas);
        mMenos = findViewById(R.id.menos);


        //Instanciacion del Recycler View
        mRecyclerViewOrden = (RecyclerView) findViewById(R.id.recyclerViewListadoOrden);
        mRecyclerViewOrden.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerViewlistadoPedidos = findViewById(R.id.recyclerViewListadoPedidos);
        mRecyclerViewlistadoPedidos.setLayoutManager(new LinearLayoutManager(this));

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();


        mCostoToltal.setText(montoPagar+ " MXN");
        mCantidadAlimentos.setText(""+cantidadAlimentos);

        recycleViewOrden(nombrePlatillo);

        recycleViewCuenta(idCuenta);

        if (!disponibilidadPlatillo){
            mOrden.setEnabled(false);
            mAnadir.setEnabled(false);
            mQueja.setEnabled(false);
            mPagar.setEnabled(false);
        }
        else {

            mAnadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Orden.this, "Boton Añadir", Toast.LENGTH_SHORT).show();
                    String cantidadAlimentos = mCantidadAlimentos.getText().toString();
                    long cantidadAlimentosParse = Long.parseLong(cantidadAlimentos);
                    String especificaciones = mEspecificaciones.getText().toString();
                    Map<String, Object> platillo = new HashMap<>();
                    platillo.put("nombrePlatillo", nombrePlatillo);
                    platillo.put("cantidad", cantidadAlimentosParse);
                    if (!especificaciones.equals("")){
                        platillo.put("especificaciones", especificaciones);
                    } else {
                        platillo.put("especificaciones", "Sin especificaciones");
                    }
                    platillo.put("costo", precioPlatillo);
                    db.collection("cuenta").document(idCuenta).collection("platillos").document(idPlatillo).set(platillo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Orden.this, "Se agrego el platillo y el precio", Toast.LENGTH_SHORT).show();
                                    cantidadSumar = (Long.parseLong(precioPlatillo)*cantidadAlimentosParse);
                                    montoPagar = (montoPagar + cantidadSumar);
                                    Map<String, Object> monto = new HashMap<>();
                                    monto.put("montoPagar", (montoPagar));

                                    db.collection("cuenta").document(idCuenta).update(monto)
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

                    db.collection("usuario").document(idUsuario).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String tipoUsuario = documentSnapshot.getString("tipoDeUsuario");
                                    Intent intent;
                                    if (tipoUsuario.equals("Cliente")){
                                        intent = new Intent(Orden.this, MenuLocal.class);
                                    }
                                    else {
                                        intent = new Intent(Orden.this, MenuLocalMU.class);
                                    }
                                    intent.putExtra("idRestaurante",idRestaurante);
                                    intent.putExtra("idMesa", idMesa);
                                    intent.putExtra("statusMesa", false);
                                    intent.putExtra("statusOrden", false);
                                    intent.putExtra("idOrden", idOrden);
                                    intent.putExtra("idCuenta", idCuenta);
                                    startActivity(intent);
                                }
                            });
                    finish();
                }
            });

            mOrden.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Orden.this, "Platillos Ordenados", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            mQueja.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Orden.this, Queja.class);
                    intent.putExtra("idMesa", idMesa);
                    intent.putExtra("idRestaurante", idRestaurante);
                    startActivity(intent);
                }
            });

            mPagar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Orden.this, MetodoDePago.class);
                    intent.putExtra("idCuenta", idCuenta);
                    intent.putExtra("idRestaurante", idRestaurante);
                    startActivity(intent);
                }
            });

            mMenos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cantidadAlimentos>1){
                        cantidadAlimentos = cantidadAlimentos-1;
                    }
                    else {
                        Toast.makeText(Orden.this, "Ordena al menos 1 platillo", Toast.LENGTH_SHORT).show();
                    }
                    mCantidadAlimentos.setText(""+cantidadAlimentos);
                }
            });

            mMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cantidadAlimentos<30){
                        cantidadAlimentos = cantidadAlimentos+1;
                    }
                    else {
                        Toast.makeText(Orden.this, "No puedes ordenar tantos platillos", Toast.LENGTH_SHORT).show();
                    }
                    mCantidadAlimentos.setText(""+cantidadAlimentos);
                }
            });
        }

    }

    private void recycleViewCuenta(String idCuenta) {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("cuenta").document(idCuenta).collection("platillos");
        if (query != null) {
            FirestoreRecyclerOptions<PlatillosCuentasPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                    .Builder<PlatillosCuentasPojo>()
                    .setQuery(query, PlatillosCuentasPojo.class).build();

            mAdapterPlatillos = new PlatillosCuentasAdapter(firestoreRecyclerOptions);
            mAdapterPlatillos.notifyDataSetChanged();
            mRecyclerViewOrden.setAdapter(mAdapterPlatillos);
        }
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
        mAdapterPlatillos.startListening();
        mAdapterListadoPedidos.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los Platillos     )
    @Override
    protected void onStop() {
        super.onStop();
        mAdapterPlatillos.stopListening();
        mAdapterListadoPedidos.stopListening();
    }
}
