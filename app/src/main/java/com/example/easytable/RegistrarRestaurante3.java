package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrarRestaurante3 extends AppCompatActivity {
    private static final String TAG = "RegistrarRestaurante3";

    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap
    private static final String KEY_CATEGORIA = "Categoria del Local";
    private static final String KEY_NOMBRELOCAL = "Nombre del Local";
    private static final String KEY_DIRECCION = "Direccion";
    private static final String KEY_CP = "Codigo Postal";
    private static final String KEY_TELEFONOLOCAL= "Telefono Local";
    private static final String KEY_ID= "ID";
    private static final String KEY_DESCRIPCION = "Descripción";

    private EditText mDescripcion;
    private ImageButton mNextButton;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_restaurante3);

        mDescripcion = (EditText) findViewById(R.id.descripcionRegistrarLocalTxt);
        mNextButton = (ImageButton) findViewById(R.id.tercerSiguienteRegistrarLocalButton);



        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String descripcion = mDescripcion.getText().toString();
                
                //Recepcion de la informacion obtenida en la vista anterior
                Bundle extra = getIntent().getExtras();
                String nombreLocal = extra.getString("nombreLocal");
                String direccion = extra.getString("direccion");
                String codigoPostal = extra.getString("cp");
                String telefonoLocal = extra.getString("telefonoLocal");
                String categoria = extra.getString("categoria");
                String IDLocal = extra.getString("idRestaurante");
                
                
                //Se crea una estructura de datos HashMap para poder guardar los datos ingresados por el usuario
                Map<String, Object> restaurante = new HashMap<>();
                //Se ingresan los datos en la estructura HashMap llamada "restaurante"
                restaurante.put(KEY_CATEGORIA, categoria);
                restaurante.put(KEY_NOMBRELOCAL, nombreLocal);
                restaurante.put(KEY_DIRECCION, direccion);
                restaurante.put(KEY_CP, codigoPostal);
                restaurante.put(KEY_TELEFONOLOCAL, telefonoLocal);
                restaurante.put(KEY_ID, IDLocal);
                restaurante.put(KEY_DESCRIPCION, descripcion);

                //Aqui se indica con que nombre se creará la coleccion y el ID de cada restaurante en la BD
                db.collection("restaurante").document(IDLocal).set(restaurante)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegistrarRestaurante3.this, "Restaurante Registrado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistrarRestaurante3.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });

                Toast.makeText(RegistrarRestaurante3.this, "Llegaste al final"+IDLocal, Toast.LENGTH_SHORT).show();
            }
        });


    }
}