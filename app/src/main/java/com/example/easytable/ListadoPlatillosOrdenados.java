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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class ListadoPlatillosOrdenados extends AppCompatActivity {

    private Button mAccionPreparacion;
    private RecyclerView mRecyclerViewPlatillosOrdenados;
    private ListadoPlatillosOrdenadosAdapter mPlatillosOrdenadosAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_listado_platillos_ordenados);

        Bundle extra = getIntent().getExtras();
        String idOrden = extra.getString("idOrden");
        String idCuenta = extra.getString("idCuenta");
        String idDelLocal = extra.getString("idResturante");

        //Toast.makeText(this, idOrden, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, idCuenta, Toast.LENGTH_SHORT).show();

        mAccionPreparacion = findViewById(R.id.accionPreparacionButton);
        mRecyclerViewPlatillosOrdenados = findViewById(R.id.recyclerViewListadoPlatillosOrdena2);
        mRecyclerViewPlatillosOrdenados.setLayoutManager(new LinearLayoutManager(this));


        //Coloca los platillos
        recyclerViewPlatillosOrdenados(idCuenta, idDelLocal, idOrden);

        //Obtencion del estatus de la preparacion de la orden
        obtencionStatus(idOrden);



        mAccionPreparacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference =  db.collection("orden").document(idOrden);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        long statusPreparacion = (long) value.get("statusPreparacion");
                        if (statusPreparacion == 0){
                            //mAccionPreparacion.setText("Iniciar Preparación");
                            mostrarDialogo1(idOrden);
                        }
                        else if (statusPreparacion == 1){
                            //mAccionPreparacion.setText("Terminar Preparación");
                            mostrarDialogo2(idOrden);
                        }
                    }
                });
            }
        });
    }

    private void obtencionStatus(String idOrden) {
        DocumentReference documentReference =  db.collection("orden").document(idOrden);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                long statusPreparacion = (long) value.get("statusPreparacion");
                if (statusPreparacion == 0){
                    mAccionPreparacion.setText("Iniciar Preparación");
                }
                if (statusPreparacion == 1){
                    mAccionPreparacion.setText("Terminar Preparación");
                }
                if (statusPreparacion == 2){
                    startActivity(new Intent(ListadoPlatillosOrdenados.this, PrincipalUCO.class));
                    finish();
                }
            }
        });
    }


    private void mostrarDialogo1(String id) {
        new AlertDialog.Builder(this)
                .setTitle("¿Iniciar la preparación?")
                .setMessage("Se comenzará con la preparacion de la orden de la mesa seleccionada")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> preparacion = new HashMap<>();
                        preparacion.put("statusPreparacion", 1);

                        db.collection("orden").document(id).update(preparacion)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ListadoPlatillosOrdenados.this, "En preparacion", Toast.LENGTH_SHORT).show();
                                        //startActivity(new Intent(ListadoPlatillosOrdenados.this, PrincipalUCO.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ListadoPlatillosOrdenados.this, "Error al comenzar la preparacion", Toast.LENGTH_SHORT).show();
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

    private void mostrarDialogo2(String id) {
        new AlertDialog.Builder(this)
                .setTitle("¿Quieres terminar con la preparación?")
                .setMessage("Se dará por terminada la preparación de esta orden")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> preparacion = new HashMap<>();
                        preparacion.put("statusPreparacion", 2);

                        db.collection("orden").document(id).update(preparacion)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ListadoPlatillosOrdenados.this, "Preparacion terminada", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ListadoPlatillosOrdenados.this, "Error al terminar la preparacion", Toast.LENGTH_SHORT).show();
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


    private void recyclerViewPlatillosOrdenados(String idCuenta, String idDelLocal, String idOrden) {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("cuenta").document(idCuenta)
                .collection("platillos");

        FirestoreRecyclerOptions<ListadoPlatillosOrdenadosPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ListadoPlatillosOrdenadosPojo>()
                .setQuery(query, ListadoPlatillosOrdenadosPojo.class).build();

        mPlatillosOrdenadosAdapter = new ListadoPlatillosOrdenadosAdapter(firestoreRecyclerOptions);
        mPlatillosOrdenadosAdapter.notifyDataSetChanged();
        mRecyclerViewPlatillosOrdenados.setAdapter(mPlatillosOrdenadosAdapter);
    }



    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mPlatillosOrdenadosAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mPlatillosOrdenadosAdapter.stopListening();
    }
}