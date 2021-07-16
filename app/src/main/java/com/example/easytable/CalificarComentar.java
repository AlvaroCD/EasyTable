package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CalificarComentar extends AppCompatActivity {

    private EditText mComentario;
    private Button mEnviar;
    private Spinner mCalificacionRestaurante;

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerViewCalificar;
    private CalificacionAdapter mCalificacionAdapter;

    private String idCuenta, idRestaurante;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static long calificacionAnterior, nuevaCalificacion, usuariosCalificacionAnterior, nuevoUsuariosCalificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_calificar_comentar);

        mComentario = findViewById(R.id.comentarioRestaurante);
        mEnviar = findViewById(R.id.enviarComentarioCalificacionesButton);
        mCalificacionRestaurante = findViewById(R.id.calificacionRestaurante);

        //Instanciacion del Recycler View
        mRecyclerViewCalificar = findViewById(R.id.recyclerViewListadoCalificados);
        mRecyclerViewCalificar.setLayoutManager(new LinearLayoutManager(this));

        idCuenta = getIntent().getStringExtra("idCuenta");
        idRestaurante = getIntent().getStringExtra("idRestaurante");

        //Coloca las ordenes
        recyclerViewPlatillosCalificados(idCuenta);

        //Funcion que determina que accion se realiza cuando se hace click en alguna orden
        onClickPlatillo();


        mEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String calificacion = mCalificacionRestaurante.getSelectedItem().toString();
                long calificacionParse = Long.parseLong(calificacion);
                db.collection("restaurante").document(idRestaurante).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                calificacionAnterior = documentSnapshot.getLong("calificacion");
                                nuevaCalificacion = (calificacionAnterior+calificacionParse);
                                usuariosCalificacionAnterior = documentSnapshot.getLong("usuariosCalificacion");
                                nuevoUsuariosCalificacion = (usuariosCalificacionAnterior+1);
                                Map <String, Object> puntuacionMap = new HashMap<>();
                                puntuacionMap.put("calificacion", nuevaCalificacion);
                                puntuacionMap.put("usuariosCalificacion", nuevoUsuariosCalificacion);

                                db.collection("restaurante").document(idRestaurante).update(puntuacionMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(CalificarComentar.this, "Calificado exitosamente", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CalificarComentar.this, "Hubo un error al enviar la calificacion", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                String comentario = mComentario.getText().toString();
                if (!comentario.equals("")){
                    String resultadoSinPalabrotas = comentario
                            .replace("puto", "****")
                            .replace("Puto", "****")
                            .replace("mierda", "****")
                            .replace("Mierda", "****")
                            .replace("perra", "****")
                            .replace("Perra", "****")
                            .replace("Puta", "****")
                            .replace("puta", "****")
                            .replace("mamon", "****")
                            .replace("mamona", "****")
                            .replace("chingadera", "****")
                            .replace("Chingadera", "****")
                            .replace("tonto", "****")
                            .replace("Tonto", "****")
                            .replace("tonta", "****")
                            .replace("Tonta", "****")
                            .replace("maldita", "****")
                            .replace("Maldita", "****")
                            .replace("zorra", "****")
                            .replace("Zorra", "****")
                            .replace("chingan", "****")
                            .replace("Chingan", "****")
                            .replace("chingado", "****")
                            .replace("Chingado", "****")
                            .replace("verga", "****")
                            .replace("Verga", "****")
                            .replace("pendejo", "****")
                            .replace("Pendejo", "****")
                            .replace("pendeja", "****")
                            .replace("Pendeja", "****")
                            .replace("estupido", "****")
                            .replace("Estupido", "****")
                            .replace("estupida", "****")
                            .replace("Estupida", "****")
                            .replace("baboso", "****")
                            .replace("Baboso", "****")
                            .replace("babosa", "****")
                            .replace("Babosa", "****");

                    Map <String, Object> comentarioMap = new HashMap<>();
                    comentarioMap.put("comentario", resultadoSinPalabrotas);
                    comentarioMap.put("idLocalComentado", idRestaurante);
                    String idComentario = UUID.randomUUID().toString();
                    db.collection("comentario").document(idComentario).set(comentarioMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(CalificarComentar.this, "Comentario colocado", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CalificarComentar.this, "Hubo un error al colocar el comentario", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(CalificarComentar.this, "No hubo comentario", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void onClickPlatillo() {
        mCalificacionAdapter.setOnItemClickListener(new CalificacionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                String nombrePlatillo = documentSnapshot.getString("nombrePlatillo");
                String idPlatillo = documentSnapshot.getId();
                Intent i = new Intent(CalificarComentar.this, PuntuarPlatillo.class);
                i.putExtra("nombrePlatillo", nombrePlatillo);
                i.putExtra("idPlatillo", idPlatillo);
                startActivity(i);
            }
        });
    }

    private void recyclerViewPlatillosCalificados(String idCuenta) {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("cuenta").document(idCuenta).collection("platillos");

        FirestoreRecyclerOptions<CalificacionPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<CalificacionPojo>()
                .setQuery(query, CalificacionPojo.class).build();

        mCalificacionAdapter = new CalificacionAdapter(firestoreRecyclerOptions);
        mCalificacionAdapter.notifyDataSetChanged();
        mRecyclerViewCalificar.setAdapter(mCalificacionAdapter);
    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mCalificacionAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mCalificacionAdapter.stopListening();
    }

}