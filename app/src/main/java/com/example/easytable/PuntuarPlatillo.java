package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
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

public class PuntuarPlatillo extends AppCompatActivity {

    private TextView mMensajePuntuar;
    private Spinner mPuntuacion;
    private Button mEnviarCalificacion;

    private FirebaseFirestore db;

    private static long calificacionAnterior, nuevaCalificacion, usuariosCalificacionAnterior, nuevoUsuariosCalificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_puntuar_platillo);

        mMensajePuntuar = findViewById(R.id.preguntaPlatillo);
        mPuntuacion = findViewById(R.id.puntuacionPlatilloSpinner);
        mEnviarCalificacion = findViewById(R.id.enviarCalificacionButton);

        db = FirebaseFirestore.getInstance();

        String nombrePlatillo = getIntent().getStringExtra("nombrePlatillo");
        mMensajePuntuar.setText("Que puntaje del 1 (terrible) al 5 (delicioso) le darias a: "+nombrePlatillo+"?");

        mEnviarCalificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idPlatillo = getIntent().getStringExtra("idPlatillo");
                String idCuenta = getIntent().getStringExtra("idCuenta");
                String puntuacion = mPuntuacion.getSelectedItem().toString();
                long puntuacionParse = Long.parseLong(puntuacion);

                db.collection("platillos").document(idPlatillo).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                calificacionAnterior = documentSnapshot.getLong("calificacion");
                                nuevaCalificacion = (calificacionAnterior+puntuacionParse);
                                usuariosCalificacionAnterior = documentSnapshot.getLong("usuariosCalificacion");
                                nuevoUsuariosCalificacion = (usuariosCalificacionAnterior+1);
                                Map <String, Object> puntuacionMap = new HashMap<>();
                                puntuacionMap.put("calificacion", nuevaCalificacion);
                                puntuacionMap.put("usuariosCalificacion", nuevoUsuariosCalificacion);

                                db.collection("platillos").document(idPlatillo).update(puntuacionMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(PuntuarPlatillo.this, nombrePlatillo+" calificado exitosamente", Toast.LENGTH_SHORT).show();
                                                db.collection("cuenta").document(idCuenta).collection("platillos")
                                                        .document(idPlatillo).delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(PuntuarPlatillo.this, "Listo", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(PuntuarPlatillo.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PuntuarPlatillo.this, "Hubo un error al enviar la calificacion", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PuntuarPlatillo.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}