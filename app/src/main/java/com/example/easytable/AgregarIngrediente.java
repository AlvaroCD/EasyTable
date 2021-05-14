package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class AgregarIngrediente extends AppCompatActivity {

    private EditText mNombreIngrediente;
    private Button mAgregarIngrediente;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap
    private static final String KEY_DISPONIBILIDAD = "disponibilidad";
    private static final String KEY_NOMBREINGREDIENTE = "nombreDeIngrediente";
    private static final String KEY_IDLOCAL = "idDelLocal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_agregar_ingrediente);


        mNombreIngrediente = findViewById(R.id.nombreIngredienteTxt);
        mAgregarIngrediente = findViewById(R.id.agregarIngredienteButtonAgregarIngrediente);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mAgregarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreIngrediente = mNombreIngrediente.getText().toString();
                //Siempre por defecto cuando se agrega un nuevo ingrediente se tomará como que esta disponible
                Boolean disponibilidad = true;
                //Aqui se crea un Id con la propiedad random para prevenir que los identificadores de los usuarios se repitan
                String id = UUID.randomUUID().toString();
                String idUsuarioLogueado = mAuth.getUid();

                DocumentReference doc = db.collection("usuario").document(idUsuarioLogueado);
                doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String idLocal = value.get("IDRestReg").toString();
                        if (!nombreIngrediente.isEmpty()){
                            //Creacion del HashMap
                            Map<String, Object> ingrediente = new HashMap<>();
                            ingrediente.put(KEY_DISPONIBILIDAD, disponibilidad);
                            ingrediente.put(KEY_NOMBREINGREDIENTE, nombreIngrediente);
                            ingrediente.put(KEY_IDLOCAL, idLocal);

                            //Aqui se indica con que nombre se creará la coleccion y el ID de cada usuario en la BD
                            db.collection("listaDeRecursos").document(id).set(ingrediente)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AgregarIngrediente.this, "Ingrediente agregado", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AgregarIngrediente.this, "Error al agregar el ingrediente", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
            }
        });

    }
}