package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AgregarPlatillo extends AppCompatActivity {

    private EditText mNombrePlatillo, mCostoPlatillo, mDescripcionPlatillo;
    private Button mRegistrarPlatillo;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap
    private static final String KEY_DESCRIPCION = "descripcion";
    private static final String KEY_DISPONIBILIDAD = "disponibilidad";
    private static final String KEY_IDLOCAL = "idDelLocal";
    private static final String KEY_NOMBREPLATILLO = "nombrePlatillo";
    private static final String KEY_COSTO = "precio";
    private static final String KEY_CALIFICACION = "calificacion";
    private static final String KEY_IDPLATILLO = "idPlatillo";
    private static final String KEY_USUARIOS_CAL = "usuariosCalificacion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_agregar_platillo);

        mNombrePlatillo = findViewById(R.id.nombrePlatilloAgregarPlatillo);
        mCostoPlatillo = findViewById(R.id.costoPlatilloAgregarPlatillo);
        mDescripcionPlatillo = findViewById(R.id.descripcionPlatilloAgregarPlatillo);
        mRegistrarPlatillo = findViewById(R.id.registrarPlatilloAgregarPlatillo);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mRegistrarPlatillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombrePlatillo = mNombrePlatillo.getText().toString();
                String costo = mCostoPlatillo.getText().toString();
                int costoPlatillo = Integer.parseInt(costo);
                String descripcionPlatillo = mDescripcionPlatillo.getText().toString();
                //Aqui se crea un Id con la propiedad random para prevenir que los identificadores de los platillos se repitan
                String id = UUID.randomUUID().toString();
                String idUsuarioLogueado = mAuth.getUid();

                DocumentReference doc = db.collection("usuario").document(idUsuarioLogueado);
                doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String idLocal = value.get("IDRestReg").toString();
                        if (!nombrePlatillo.isEmpty() && !costo.isEmpty() && !descripcionPlatillo.isEmpty()) {
                            //Creacion del HashMap
                            Map<String, Object> platillo = new HashMap<>();
                            platillo.put(KEY_NOMBREPLATILLO, nombrePlatillo);
                            platillo.put(KEY_COSTO, costoPlatillo);
                            platillo.put(KEY_DESCRIPCION, descripcionPlatillo);
                            //Siempre que se agregue un nuevo platillo se tomará como que esta disponible
                            platillo.put(KEY_DISPONIBILIDAD, true);
                            platillo.put(KEY_IDLOCAL, idLocal);
                            platillo.put(KEY_IDPLATILLO, id);
                            //Todos los platillos recien creados tendran una calificacion inicial de 3 estrellas
                            platillo.put(KEY_CALIFICACION, 3);
                            platillo.put(KEY_USUARIOS_CAL, 1);



                            //Aqui se indica con que nombre se creará la coleccion y el ID de cada usuario en la BD
                            db.collection("platillos").document(id).set(platillo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AgregarPlatillo.this, "Platillo agregado", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AgregarPlatillo.this, "Error al agregar el platillo", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(AgregarPlatillo.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}