package com.example.easytable;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class AsignarMesero extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MesaAdapter mMesaAdapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_asignar_mesero);

        //Instanciacion del Recycler View
        mRecyclerView = findViewById(R.id.recyclerViewListadoMesasDisponibles);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Coloca las mesas
        recyclerViewMesas();
        onClickMesa();
    }

    private void recyclerViewMesas() {
        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");
        //Consulta para obtener los datos de la BD
        Query query = db.collection("mesa").whereEqualTo("idDelLocal", idRestaurante)
                .whereEqualTo("statusMesa", false);

        FirestoreRecyclerOptions<MesaPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<MesaPojo>()
                .setQuery(query, MesaPojo.class).build();

        mMesaAdapter = new MesaAdapter(firestoreRecyclerOptions);
        mMesaAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mMesaAdapter);
    }

    private void onClickMesa() {
        mMesaAdapter.setOnItemClickListener(new MesaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                MesaPojo mesa = documentSnapshot.toObject(MesaPojo.class);
                String id = documentSnapshot.getId();
                mostrarDialogo(id);

            }
        });

    }


    private void mostrarDialogo(String id) {
        new AlertDialog.Builder(this)
                .setTitle("¿Seguro?")
                .setMessage("Se asignará un mesero a la mesa seleccionada")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Insertar la accion de asignar un mesero

                        //Al asignar un mesero la mesa pasa automaticamente a estar ocupada, por lo que se actualiza el campo
                        //de la disponibilidad de la mesa
                        Map<String, Object> disponibilidadActualizada = new HashMap<>();
                        disponibilidadActualizada.put("statusMesa", true);
                        db.collection("mesa").document(id).update(disponibilidadActualizada)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AsignarMesero.this, "Good", Toast.LENGTH_SHORT).show();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AsignarMesero.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        Toast.makeText(AsignarMesero.this, "Mesero asignado", Toast.LENGTH_SHORT).show();
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
    protected void onStart(){
        super.onStart();
        mMesaAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mMesaAdapter.stopListening();
    }


}