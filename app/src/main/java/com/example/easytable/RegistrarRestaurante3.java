package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.UUID;

public class RegistrarRestaurante3 extends AppCompatActivity {
    private static final String TAG = "RegistrarRestaurante3";

    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap para el restaurante
    private static final String KEY_CATEGORIA = "tipoRestaurante";
    private static final String KEY_NOMBRELOCAL = "nombreLocal";
    private static final String KEY_DIRECCION = "direccion";
    private static final String KEY_CP = "cp";
    private static final String KEY_TELEFONOLOCAL= "telefonoLocal";
    private static final String KEY_NUMEROMESAS = "cantidadMesas";
    private static final String KEY_ID_RESTAURANTE= "IdRestaurante";
    private static final String KEY_ID_PROPIETARIO= "IdPropietario";
    private static final String KEY_DESCRIPCION = "descripcionRestaurante";
    private static final String KEY_CALIFICACION = "calificacion";
    private static final String KEY_USUARIOSCALIFICACION = "usuariosCalificacion";


    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap para el usuario
    private static final String KEY_NOMBRE = "Nombre";
    private static final String KEY_APELLIDO = "Apellidos";
    private static final String KEY_TELEFONO = "Telefono";
    private static final String KEY_CORREO = "Correo";
    private static final String KEY_USUARIO = "Usuario";
    private static final String KEY_PASSWORD = "Contraseña";
    private static final String KEY_USERTYPE = "tipoDeUsuario";
    private static final String KEY_ID= "ID";
    private static final String KEY_ID_REST_REG= "IDRestReg";

    //Creacion de las KEYS necesarias para ingresar los datos dentro de la estructura HashMap para las mesas
    private static final String KEY_LOCAL = "idDelLocal";
    private static final String KEY_ID_MESA = "idMesa";
    private static final String KEY_NOMBRE_DEL_LOCAL = "nombreDelLocal";
    private static final String KEY_NUMERO_MESA = "numeroMesa";
    private static final String KEY_STATUS_MESA = "statusMesa";





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
                String numeroMesas = extra.getString("numeroMesas");
                //se parsea la variable numeroMesas de String a int para agregarla como tal en la BD (como int)
                int numeroMesasInt = Integer.parseInt(numeroMesas);
                String idRestaurante = extra.getString("idRestaurante");
                String idPropietario = extra.getString("idPropietario");
                
                if (!descripcion.isEmpty()){
                    //Se crea una estructura de datos HashMap para poder guardar los datos ingresados por el usuario
                    Map<String, Object> restaurante = new HashMap<>();
                    //Se ingresan los datos en la estructura HashMap llamada "restaurante"
                    restaurante.put(KEY_CATEGORIA, categoria);
                    restaurante.put(KEY_NOMBRELOCAL, nombreLocal);
                    restaurante.put(KEY_DIRECCION, direccion);
                    restaurante.put(KEY_CP, codigoPostal);
                    restaurante.put(KEY_TELEFONOLOCAL, telefonoLocal);
                    restaurante.put(KEY_NUMEROMESAS, numeroMesasInt);
                    restaurante.put(KEY_ID_RESTAURANTE, idRestaurante);
                    restaurante.put(KEY_ID_PROPIETARIO, idPropietario);
                    restaurante.put(KEY_DESCRIPCION, descripcion);
                    restaurante.put(KEY_CALIFICACION, 5);
                    restaurante.put(KEY_USUARIOSCALIFICACION, 1);

                    //Aqui se indica con que nombre se creará la coleccion y el ID de cada restaurante en la BD
                    //(se usa el ID generado anteriormente para agregar la informacion al mismo restaurante)
                    db.collection("restaurante").document(idRestaurante).set(restaurante)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegistrarRestaurante3.this, "Restaurante Registrado", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(RegistrarRestaurante3.this, PrincipalUDL.class);
                                    i.putExtra("idRestaurante", idRestaurante);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegistrarRestaurante3.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });


                    //____________________________________________________________________________-
                    for (int i = 1; i<=numeroMesasInt; i++) {
                        //Id unico para cada mesa creada
                        String idMesa = UUID.randomUUID().toString();
                        Map<String, Object> creacionMesas = new HashMap<>();
                        creacionMesas.put(KEY_LOCAL, idRestaurante);
                        creacionMesas.put(KEY_ID_MESA, idMesa);
                        creacionMesas.put(KEY_NOMBRE_DEL_LOCAL, nombreLocal);
                        creacionMesas.put(KEY_NUMERO_MESA, i);//variable del ciclo
                        creacionMesas.put(KEY_STATUS_MESA, false);

                        db.collection("mesa").document(idMesa).set(creacionMesas)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Toast.makeText(RegistrarRestaurante3.this, "Mesas creadas", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegistrarRestaurante3.this, "Error al crear las mesas", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    //______________________________________________________________________________


                    DocumentReference doc = db.collection("usuario").document(idPropietario);
                    doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            String nombre = value.get(KEY_NOMBRE).toString();
                            String apellidos = value.get(KEY_APELLIDO).toString();
                            String telefono = value.get(KEY_TELEFONO).toString();
                            String correo = value.get(KEY_CORREO).toString();
                            String username = value.get(KEY_USUARIO).toString();
                            String password = value.get(KEY_PASSWORD).toString();
                            String tipoUsuario = value.get(KEY_USERTYPE).toString();



                            Map<String, Object> user = new HashMap<>();
                            //Se ingresan los datos en la estructura HashMap llamada "user"
                            user.put(KEY_NOMBRE, nombre);
                            user.put(KEY_APELLIDO, apellidos);
                            user.put(KEY_TELEFONO, telefono);
                            user.put(KEY_CORREO, correo);
                            user.put(KEY_USUARIO, username);
                            user.put(KEY_PASSWORD, password);
                            user.put(KEY_USERTYPE, tipoUsuario);
                            user.put(KEY_ID, idPropietario);
                            user.put(KEY_ID_REST_REG, idRestaurante);

                            //Aqui se indica con que nombre se creará la coleccion y el ID de cada usuario en la BD
                            db.collection("usuario").document(idPropietario).set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegistrarRestaurante3.this, "Hubo un error al guardar el ID del Restaurante", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });


                }else{
                    Toast.makeText(RegistrarRestaurante3.this, "Escribe una descripcion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}