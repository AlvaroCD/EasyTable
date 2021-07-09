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

public class ApartarLugarUH extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerViewReservaciones;
    private ReservacionesAdapter mReservacionesAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_apartar_lugar_uh);

        //Instanciacion del Recycler View
        mRecyclerViewReservaciones = findViewById(R.id.recyclerViewListadoSolicitudesReservacion);
        mRecyclerViewReservaciones.setLayoutManager(new LinearLayoutManager(this));

        Bundle extra = getIntent().getExtras();
        String idDelLocal = extra.getString("idRestaurante");

        //Coloca las ordenes
        recyclerViewReservaciones(idDelLocal);

        //Funcion que determina que accion se realiza cuando se hace click en alguna orden
        onClickReservacion(idDelLocal);
    }

    private void recyclerViewReservaciones(String idDelLocal) {
        //Consulta para obtener los datos de la BD

        Query query = db.collection("reservaciones")
                .whereEqualTo("statusReservacion", 0)
                .whereEqualTo("idLocalReservado", idDelLocal);


        FirestoreRecyclerOptions<ReservacionesPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ReservacionesPojo>()
                .setQuery(query, ReservacionesPojo.class).build();

        mReservacionesAdapter = new ReservacionesAdapter(firestoreRecyclerOptions);
        mReservacionesAdapter.notifyDataSetChanged();
        mRecyclerViewReservaciones.setAdapter(mReservacionesAdapter);
    }


    private void onClickReservacion(String idDelLocal) {
        mReservacionesAdapter.setOnItemClickListener(new ReservacionesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                String ID = documentSnapshot.getId();
                mostrarDialogo(ID);
            }

        });
    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mReservacionesAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mReservacionesAdapter.stopListening();
    }

    private void mostrarDialogo(String id) {
        new AlertDialog.Builder(this)
                .setTitle("¿Seguro?")
                .setMessage("Deseas confirmar esta solicitud de reservación?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> reservacion = new HashMap<>();
                        reservacion.put("statusReservacion", 1);

                        db.collection("reservaciones").document(id).update(reservacion)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ApartarLugarUH.this, "Entendido", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ApartarLugarUH.this, "Error al aceptar la reservacion", Toast.LENGTH_SHORT).show();
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

}