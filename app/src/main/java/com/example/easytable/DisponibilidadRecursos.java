package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DisponibilidadRecursos extends AppCompatActivity {

    TextView mNombreIngrediente, mDisponibilidad;
    Button mDisponible, mNoDisponible;

    private FirebaseFirestore db;

    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap
    private static final String KEY_DISPONIBILIDAD = "disponibilidad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_disponibilidad_recursos);

        mNombreIngrediente = findViewById(R.id.nombreIngrediente);
        mDisponibilidad = findViewById(R.id.disponibilidadIngrediente);
        mDisponible = findViewById(R.id.disponibleButton);
        mNoDisponible = findViewById(R.id.agotadoButton);

        db = FirebaseFirestore.getInstance();

        //Obtencion de la informacion del ingrediente
        Bundle extra = getIntent().getExtras();
        String nombreIngredienteRecibido = extra.getString("nombreIngrediente");
        Boolean disponibilidad = extra.getBoolean("disponibilidad");
        String id = extra.getString("id");

        //Colocacion de la informacion del ingrediente para su visualizacion
        mNombreIngrediente.setText(nombreIngredienteRecibido);
        if (disponibilidad.equals(true)){
            mDisponibilidad.setText("Disponible");
        } else {
            mDisponibilidad.setText("No Disponible");
        }

        mDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> nuevaDisponibilidad = new HashMap<>();
                nuevaDisponibilidad.put(KEY_DISPONIBILIDAD, true);
                db.collection("listaDeRecursos").document(id).update(nuevaDisponibilidad)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DisponibilidadRecursos.this, "Actualizado", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DisponibilidadRecursos.this, "No actualizado", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        mNoDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> nuevaDisponibilidad = new HashMap<>();
                nuevaDisponibilidad.put(KEY_DISPONIBILIDAD, false);
                db.collection("listaDeRecursos").document(id).update(nuevaDisponibilidad)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DisponibilidadRecursos.this, "Actualizado", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DisponibilidadRecursos.this, "No actualizado", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}