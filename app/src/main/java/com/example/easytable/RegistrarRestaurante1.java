package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegistrarRestaurante1 extends AppCompatActivity {
    private static final String TAG = "RegistrarRestaurante1";

    //Creacion de las variables a relacionar con la parte grafica
    private EditText mNombreDelLocal, mDireccion, mCodigoPostal, mTelefonoLocal;
    private ImageButton mSiguienteButton;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap
    private static final String KEY_NOMBRELOCAL = "Nombre del Local";
    private static final String KEY_DIRECCION = "Direccion";
    private static final String KEY_CP = "Codigo Postal";
    private static final String KEY_TELEFONOLOCAL= "Telefono Local";
    private static final String KEY_ID= "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //Se utiliza para quitar el nombre de la aplicacion de la pantalla inicial en el celular
        setContentView(R.layout.activity_registrar_restaurante1);

        //Intanciación de las variables
        mNombreDelLocal = (EditText) findViewById(R.id.nombreRegistrarLocalTxt);
        mDireccion = (EditText) findViewById(R.id.direccionRegistrarLocalTxt);
        mCodigoPostal = (EditText) findViewById(R.id.codigoPostalRegistrarLocalTxt);
        mTelefonoLocal = (EditText) findViewById(R.id.telefonoRegistrarLocalTxt);
        mSiguienteButton = (ImageButton) findViewById(R.id.primerSiguienteRegistrarLocalButton);

        mSiguienteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Variables que almacenaran el contenido de los EditText's de la parte grafica
                String nombreDelLocal = mNombreDelLocal.getText().toString();
                String direccion = mDireccion.getText().toString();
                String codigoPostal = mCodigoPostal.getText().toString();
                String telefonoLocal = mTelefonoLocal.getText().toString();
                //Aqui se crea un Id con la propiedad random para prevenir que los identificadores de los usuarios se repitan
                String id = UUID.randomUUID().toString();

                if (!nombreDelLocal.isEmpty() && !direccion.isEmpty() && !codigoPostal.isEmpty() && !telefonoLocal.isEmpty()) {

                    //Se crea una estructura de datos HashMap para poder guardar los datos ingresados por el usuario
                    Map<String, Object> restaurante = new HashMap<>();

                    //Se ingresan los datos en la estructura HashMap llamada "user"
                    restaurante.put(KEY_NOMBRELOCAL, nombreDelLocal);
                    restaurante.put(KEY_DIRECCION, direccion);
                    restaurante.put(KEY_CP, codigoPostal);
                    restaurante.put(KEY_TELEFONOLOCAL, telefonoLocal);
                    restaurante.put(KEY_ID, id);

                    //Aqui se indica con que nombre se creará la coleccion y el ID de cada usuario en la BD
                    db.collection("restaurante").document(id).set(restaurante)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegistrarRestaurante1.this, "Primer paso completado", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegistrarRestaurante1.this, RegistrarRestaurante2.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegistrarRestaurante1.this, "Ocurrio un error en este paso", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });
                }else {
                    Toast.makeText(RegistrarRestaurante1.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}