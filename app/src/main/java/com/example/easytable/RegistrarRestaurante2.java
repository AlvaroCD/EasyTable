package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrarRestaurante2 extends AppCompatActivity {
    private static final String TAG = "RegistrarRestaurante2";

    private RadioGroup mCategoria;
    private RadioButton mRadioSeleccionado;
    private ImageButton mSiguienteButton;


    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap
    private static final String KEY_CATEGORIA = "Categoria del Local";
    private static final String KEY_NOMBRELOCAL = "Nombre del Local";
    private static final String KEY_DIRECCION = "Direccion";
    private static final String KEY_CP = "Codigo Postal";
    private static final String KEY_TELEFONOLOCAL= "Telefono Local";
    private static final String KEY_ID_RESTAURANTE= "ID Restaurante";
    private static final String KEY_ID_PROPIETARIO= "ID Propietario";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //Se utiliza para quitar el nombre de la aplicacion de la pantalla inicial en el celular
        setContentView(R.layout.activity_registrar_restaurante2);

        mCategoria = (RadioGroup) findViewById(R.id.categoriaRegistrarLocalRadioGroup);

        mSiguienteButton = (ImageButton) findViewById(R.id.segundoSiguienteRegistrarLocalButton);

        mSiguienteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Recepcion de la informacion obtenida en la vista anterior
                Bundle extra = getIntent().getExtras();
                String nombreLocal = extra.getString("nombreLocal");
                String direccion = extra.getString("direccion");
                String codigoPostal = extra.getString("cp");
                String telefonoLocal = extra.getString("telefonoLocal");
                String idRestaurante = extra.getString("idRestaurante");
                String idPropietario = extra.getString("idPropietario");

                int idDeLaCategoriaSeleccionada = mCategoria.getCheckedRadioButtonId();
                mRadioSeleccionado = findViewById(idDeLaCategoriaSeleccionada);
                String categoriaSeleccionada = mRadioSeleccionado.getText().toString();

                //Se crea una estructura de datos HashMap para poder guardar los datos ingresados por el usuario
                Map<String, Object> restaurante = new HashMap<>();
                //Se ingresan los datos en la estructura HashMap llamada "restaurante"
                restaurante.put(KEY_CATEGORIA, categoriaSeleccionada);
                restaurante.put(KEY_NOMBRELOCAL, nombreLocal);
                restaurante.put(KEY_DIRECCION, direccion);
                restaurante.put(KEY_CP, codigoPostal);
                restaurante.put(KEY_TELEFONOLOCAL, telefonoLocal);
                restaurante.put(KEY_ID_PROPIETARIO, idPropietario);
                restaurante.put(KEY_ID_RESTAURANTE, idRestaurante);


                //Aqui se indica con que nombre se creará la coleccion y el ID de cada restaurante en la BD
                db.collection("restaurante").document(idRestaurante).set(restaurante)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegistrarRestaurante2.this, "Seleccionaste: "+categoriaSeleccionada, Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegistrarRestaurante2.this, "Segundo paso completado", Toast.LENGTH_SHORT).show();
                                //Si la informacion se agrega correctamente se pasa a la siguiente ventana
                                Intent i = new Intent(RegistrarRestaurante2.this, RegistrarRestaurante3.class);
                                i.putExtra("categoria", categoriaSeleccionada);
                                i.putExtra("nombreLocal",nombreLocal);
                                i.putExtra("direccion", direccion);
                                i.putExtra("cp", codigoPostal);
                                i.putExtra("telefonoLocal", telefonoLocal);
                                i.putExtra("idPropietario", idPropietario);
                                i.putExtra("idRestaurante", idRestaurante);
                                startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, e.toString());
                            }
                        });



            }
        });
    }
}







/*
        mComidaRapida = (RadioButton) findViewById(R.id.comidaRapidaRegistrarLocalRadio);
                mBuffet = (RadioButton) findViewById(R.id.buffetRegistrarLocalRadio);
                mTematico = (RadioButton) findViewById(R.id.tematicoRegistrarLocalRadio);
                mParaLlevar = (RadioButton) findViewById(R.id.paraLlevarRegistrarLocalRadio);
                mDeAuto = (RadioButton) findViewById(R.id.deAutoRegistrarLocalRadio);
                mAltaCocina = (RadioButton) findViewById(R.id.altaCocinaRegistrarLocalRadio);
                mFusion = (RadioButton) findViewById(R.id.fusionRegistrarLocalRadio);
                mComidaMexicana = (RadioButton) findViewById(R.id.comidaMexicanaRegistrarLocalRadio);
                mComidaAsitica= (RadioButton) findViewById(R.id.comidaAsiaticaRegistrarLocalRadio);
                mComidaItaliana = (RadioButton) findViewById(R.id.comidaItalianaRegistrarLocalRadio);

                mComidaRapida, mBuffet, mTematico, mParaLlevar, mDeAuto,
            mAltaCocina, mFusion, mComidaMexicana, mComidaAsitica, mComidaItaliana
 */