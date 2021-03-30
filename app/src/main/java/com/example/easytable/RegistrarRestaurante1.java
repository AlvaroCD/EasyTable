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
    private EditText mNombreDelLocal, mDireccion, mCodigoPostal, mTelefonoLocal, mNumeroMesas;
    private ImageButton mSiguienteButton;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap
    private static final String KEY_NOMBRELOCAL = "nombreLocal";
    private static final String KEY_DIRECCION = "direccion";
    private static final String KEY_CP = "cp";
    private static final String KEY_TELEFONOLOCAL= "telefonoLocal";
    private static final String KEY_ID_RESTAURANTE= "IdRestaurante";
    private static final String KEY_NUMEROMESAS = "cantidadMesas";
    private static final String KEY_ID= "IdPropietario";

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
        mNumeroMesas = (EditText) findViewById(R.id.numeroMesasRegistrarLocalTxt);
        mSiguienteButton = (ImageButton) findViewById(R.id.primerSiguienteRegistrarLocalButton);

        mSiguienteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extra = getIntent().getExtras();
                String idPropietario = extra.getString("idPropietario");

                //Variables que almacenaran el contenido de los EditText's de la parte grafica
                String nombreDelLocal = mNombreDelLocal.getText().toString();
                String direccion = mDireccion.getText().toString();
                String codigoPostal = mCodigoPostal.getText().toString();
                String telefonoLocal = mTelefonoLocal.getText().toString();
                String numeroMesas = mNumeroMesas.getText().toString();
                //Aqui se crea un Id con la propiedad random para prevenir que los identificadores de los usuarios se repitan
                String idRestaurante = UUID.randomUUID().toString();

                if (!nombreDelLocal.isEmpty() && !direccion.isEmpty() && !codigoPostal.isEmpty() && !telefonoLocal.isEmpty()) {

                    //Se crea una estructura de datos HashMap para poder guardar los datos ingresados por el usuario
                    Map<String, Object> restaurante = new HashMap<>();

                    //Se ingresan los datos en la estructura HashMap llamada "restaurante"
                    restaurante.put(KEY_NOMBRELOCAL, nombreDelLocal);
                    restaurante.put(KEY_DIRECCION, direccion);
                    restaurante.put(KEY_CP, codigoPostal);
                    restaurante.put(KEY_TELEFONOLOCAL, telefonoLocal);
                    restaurante.put(KEY_ID, idPropietario);
                    restaurante.put(KEY_NUMEROMESAS, numeroMesas);
                    restaurante.put(KEY_ID_RESTAURANTE, idRestaurante);

                    //Aqui se indica con que nombre se creará la coleccion y el ID de cada usuario en la BD
                    db.collection("restaurante").document(idRestaurante).set(restaurante)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegistrarRestaurante1.this, "Primer paso completado", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(RegistrarRestaurante1.this, RegistrarRestaurante2.class);
                                    i.putExtra("nombreLocal",nombreDelLocal);
                                    i.putExtra("direccion", direccion);
                                    i.putExtra("cp", codigoPostal);
                                    i.putExtra("telefonoLocal", telefonoLocal);
                                    i.putExtra("numeroMesas", numeroMesas);
                                    i.putExtra("idPropietario", idPropietario);
                                    i.putExtra("idRestaurante", idRestaurante);
                                    startActivity(i);
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