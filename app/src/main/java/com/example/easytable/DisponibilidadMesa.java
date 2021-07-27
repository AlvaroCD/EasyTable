package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class DisponibilidadMesa extends AppCompatActivity {

    private TextView mNumeroMesa, mStatusMesa;
    private Button mOcupadoButton, mLibreButton;

    //Firebase
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_disponibilidad_mesa);

        mNumeroMesa = findViewById(R.id.numeroMesaLocal);
        mStatusMesa = findViewById(R.id.disponibilidadMesaLocal);
        mOcupadoButton = findViewById(R.id.mesaOcupadaButton);
        mLibreButton = findViewById(R.id.mesaDisponibleButton);
        db = FirebaseFirestore.getInstance();

        //Obtencion de la informacion del ingrediente
        Bundle extra = getIntent().getExtras();
        String idMesa = extra.getString("idMesa");
        String numeroMesa = extra.getString("numeroMesa");
        Boolean statusMesa = extra.getBoolean("statusMesa");


        //Colocacion de la informacion del ingrediente para su visualizacion
        mNumeroMesa.setText("Mesa #"+numeroMesa);
        if (statusMesa.equals(true)){
            mStatusMesa.setText("Ocupada");
        } else {
            mStatusMesa.setText("Libre");
        }

        mOcupadoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> disponibilidadActualizada = new HashMap<>();
                disponibilidadActualizada.put("statusMesa", true);
                db.collection("mesa").document(idMesa).update(disponibilidadActualizada)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DisponibilidadMesa.this, "Good", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DisponibilidadMesa.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        mLibreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> disponibilidadActualizada = new HashMap<>();
                disponibilidadActualizada.put("statusMesa", false);
                db.collection("mesa").document(idMesa).update(disponibilidadActualizada)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DisponibilidadMesa.this, "Good", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DisponibilidadMesa.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}