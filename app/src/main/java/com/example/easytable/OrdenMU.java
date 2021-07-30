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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class OrdenMU extends Activity {
    private ImageButton mMas, mMenos;
    private TextView mCostoToltal;
    private Button mOrden, mPagar, mQueja, mAnadir;
    private EditText mEspecificaciones, mCantidadAlimentos;
    private RecyclerView mRecyclerViewOrden, mRecyclerViewlistadoPedidos;
    private PlatilloAdapter mAdapterListadoPedidos;
    private PlatillosCuentasAdapter mAdapterPlatillos;

    private static long cantidadSumar, montoPagar, cantidadAlimentos=1;

    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_orden);

        String nombrePlatillo,  idRestaurante, idCuenta, idPlatillo, precioPlatillo;
        boolean disponibilidadPlatillo;

        nombrePlatillo = getIntent().getStringExtra("nombrePlatillo");
        idPlatillo = getIntent().getStringExtra("idPlatillo");
        precioPlatillo = getIntent().getStringExtra("precio");
        idRestaurante = getIntent().getStringExtra("idRestaurante");
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

        DocumentReference docRef = db.collection("cuenta").document(idCuenta);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String montoActual = value.get("montoPagar").toString();
                mCostoToltal.setText(montoActual);

            }
        });



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
                    Toast.makeText(OrdenMU.this, "Boton Añadir", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(OrdenMU.this, "Se agrego el platillo y el precio", Toast.LENGTH_SHORT).show();
                                    cantidadSumar = (Long.parseLong(precioPlatillo)*cantidadAlimentosParse);
                                    String pago = mCostoToltal.getText().toString();
                                    montoPagar = Long.parseLong(pago);
                                    montoPagar = (montoPagar + cantidadSumar);
                                    /*Toast.makeText(OrdenMU.this, "Se agrego el platillo y el precio", Toast.LENGTH_SHORT).show();
                                    cantidadSumar = (Long.parseLong(precioPlatillo)*cantidadAlimentosParse);
                                    montoPagar = (montoPagar + cantidadSumar);
                                    */Map<String, Object> monto = new HashMap<>();
                                    monto.put("montoPagar", (montoPagar));

                                    db.collection("cuenta").document(idCuenta).update(monto)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(OrdenMU.this, "Good", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(OrdenMU.this, "Bad", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OrdenMU.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                                }
                            });

                    Intent intent = new Intent(OrdenMU.this, MenuLocalMU.class);
                    intent.putExtra("idRestaurante",idRestaurante);
                    intent.putExtra("statusMesa", false);
                    intent.putExtra("statusOrden", false);
                    startActivity(intent);
                    finish();
                }
                /*{
                    DocumentReference docRef = db.collection("cuenta").document(idCuenta).
                            collection("platillos").document(idPlatillo);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    DocumentReference doc = db.collection("cuenta").document(idCuenta).
                                            collection("platillos").document(idPlatillo);

                                    doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            String cantidadAlimentos = mCantidadAlimentos.getText().toString();
                                            long cantidadAlimentosParse = Long.parseLong(cantidadAlimentos);
                                            String cantidadString = value.get("cantidad").toString();
                                            long cantidad = Long.parseLong(cantidadString);
                                            cantidad = cantidadAlimentosParse + cantidad;
                                            String especificaciones = mEspecificaciones.getText().toString();

                                            //colocacionDeDatosCuenta(cantidad, especificaciones,nombrePlatillo, precioPlatillo, idCuenta, idPlatillo);

                                        }
                                    });

                                } else {
                                    String cantidadAlimentos = mCantidadAlimentos.getText().toString();
                                    long cantidadAlimentosParse = Long.parseLong(cantidadAlimentos);
                                    String especificaciones = mEspecificaciones.getText().toString();

                                    //colocacionDeDatosCuenta(cantidadAlimentosParse, especificaciones,nombrePlatillo, precioPlatillo, idCuenta, idPlatillo);

                                }
                            } else {

                            }
                        }
                    });


                    Intent intent = new Intent(OrdenMU.this, MenuLocalMU.class);
                    intent.putExtra("idRestaurante",idRestaurante);
                    intent.putExtra("idCuenta", idCuenta);

                    //intent.putExtra("idMesa", idMesa);
                   // intent.putExtra("statusMesa", false);
                    //intent.putExtra("statusOrden", false);
                    startActivity(intent);
                    finish();
                }*/
            });

            mOrden.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OrdenMU.this, "Platillos Ordenados", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            mQueja.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrdenMU.this, Queja.class);
                    startActivity(intent);
                }
            });

            mPagar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrdenMU.this, MetodoDePago.class);
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
                        Toast.makeText(OrdenMU.this, "Ordena al menos 1 platillo", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(OrdenMU.this, "No puedes ordenar tantos platillos", Toast.LENGTH_SHORT).show();
                    }
                    mCantidadAlimentos.setText(""+cantidadAlimentos);
                }
            });
        }

    }

    private void colocacionDeDatosCuenta(long cantidadAlimentosParse, String especificaciones,
                                         String nombrePlatillo, String precioPlatillo, String idCuenta, String idPlatillo) {


        Map<String, Object> platillo = new HashMap<>();
        platillo.put("nombrePlatillo", nombrePlatillo);
        platillo.put("cantidad", cantidadAlimentosParse);
        if (!especificaciones.equals("")){
            platillo.put("especificaciones", especificaciones);
        } else {
            platillo.put("especificaciones", "Sin especificaciones");
        }
        platillo.put("costo", precioPlatillo);

        //String idColection  =
        db.collection("cuenta").document(idCuenta).collection("platillos")
                .document(idPlatillo).set(platillo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(OrdenMU.this, "Se agrego el platillo y el precio", Toast.LENGTH_SHORT).show();
                        cantidadSumar = (Long.parseLong(precioPlatillo)*cantidadAlimentosParse);
                        String pago = mCostoToltal.getText().toString();
                        montoPagar = Long.parseLong(pago);
                        montoPagar = (montoPagar + cantidadSumar);
                        Map<String, Object> monto = new HashMap<>();
                        monto.put("montoPagar", (montoPagar));

                        db.collection("cuenta").document(idCuenta).update(monto)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(OrdenMU.this, "Good", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(OrdenMU.this, "Bad", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrdenMU.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void recycleViewCuenta(String idCuenta) {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("cuenta").document(idCuenta).collection("platillos");
        FirestoreRecyclerOptions<PlatillosCuentasPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<PlatillosCuentasPojo>()
                .setQuery(query, PlatillosCuentasPojo.class).build();

        mAdapterPlatillos = new PlatillosCuentasAdapter(firestoreRecyclerOptions);
        mAdapterPlatillos.notifyDataSetChanged();
        mRecyclerViewOrden.setAdapter(mAdapterPlatillos);
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

