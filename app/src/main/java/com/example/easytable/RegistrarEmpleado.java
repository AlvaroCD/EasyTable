package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegistrarEmpleado extends AppCompatActivity {

    private static final String TAG = "RegistrarUsuario";
    private FirebaseAuth mAuth;

    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap
    private static final String KEY_NOMBRE = "Nombre";
    private static final String KEY_APELLIDO = "Apellidos";
    private static final String KEY_TELEFONO = "Telefono";
    private static final String KEY_CORREO = "Correo";
    private static final String KEY_USUARIO = "Usuario";
    private static final String KEY_PASSWORD = "Contraseña";
    private static final String KEY_USERTYPE = "tipoDeUsuario";
    private static final String KEY_ID= "ID";


    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private EditText mNombreEmpleado, mApellidosEmpleado, mTelefonoEmpleado, mCorreoEmpleado, mUsernameEmpleado, mPasswordEmpleado;
    private Spinner mTipoUsuarioEmpleado;
    private Button mRegistrarEmpleadoButton;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_registrar_empleado);

        //Instanciación de Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        //Relacion de la parte grafica (xml) con la lógica (java)
        mNombreEmpleado = (EditText) findViewById(R.id.nombreRegistrarEmpleadoTxt);
        mApellidosEmpleado = (EditText) findViewById(R.id.apellidoRegistrarEmpleadoTxt);
        mTelefonoEmpleado = (EditText) findViewById(R.id.telefonoRegistrarEmpleadoTxt);
        mCorreoEmpleado = (EditText) findViewById(R.id.correoRegistrarEmpleadoTxt);
        mUsernameEmpleado = (EditText) findViewById(R.id.usernameRegistrarEmpleadoTxt);
        mPasswordEmpleado = (EditText) findViewById(R.id.passwordRegistrarEmpleadoTxt);
        mTipoUsuarioEmpleado = (Spinner) findViewById(R.id.tipoDeUsuarioRegistrarEmpleadoSpinner);
        mRegistrarEmpleadoButton = (Button) findViewById(R.id.registrarseRegistrarEmpleadoButton);



        //Variables que almacenaran el contenido de los EditText's y el Spinner de la parte grafica


        mRegistrarEmpleadoButton.setOnClickListener(new View.OnClickListener() {          //Boton de acción para registrar un usuario
            @Override
            public void onClick(View v) {

                String nombre = mNombreEmpleado.getText().toString();
                String apellidos = mApellidosEmpleado.getText().toString();
                String telefono = mTelefonoEmpleado.getText().toString();
                String correo = mCorreoEmpleado.getText().toString();
                String username = mUsernameEmpleado.getText().toString();
                String password = mPasswordEmpleado.getText().toString();
                String tipoUsuario = mTipoUsuarioEmpleado.getSelectedItem().toString();


                if (!nombre.isEmpty() && !apellidos.isEmpty() && !telefono.isEmpty() && !correo.isEmpty() &&
                        !username.isEmpty() && !password.isEmpty()){

                    mAuth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Aqui se obtiene el ID con el que se creo el perfil de firebase auth para asociarlo al perfil en la base de datos (firestore)
                                String id = mAuth.getUid();
                                //Se crea una estructura de datos HashMap para poder guardar los datos ingresados por el usuario
                                Map<String, Object> user = new HashMap<>();
                                //Se ingresan los datos en la estructura HashMap llamada "user"
                                user.put(KEY_NOMBRE, nombre);
                                user.put(KEY_APELLIDO, apellidos);
                                user.put(KEY_TELEFONO, telefono);
                                user.put(KEY_CORREO, correo);
                                user.put(KEY_USUARIO, username);
                                user.put(KEY_PASSWORD, password);
                                user.put(KEY_USERTYPE, tipoUsuario);
                                user.put(KEY_ID, id);

                                //Aqui se indica con que nombre se creará la coleccion y el ID de cada usuario en la BD
                                db.collection("usuario").document(id).set(user)

                                        //Listener que indica si la creacion del usuario fue correcta (es similar a un try-catch)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                startActivity(new Intent(RegistrarEmpleado.this, PrincipalUDL.class));
                                                finish();
                                            }
                                        })
                                        //Listener que indica si la creacion del usuario fue incorrecta
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegistrarEmpleado.this, "Error", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, e.toString());
                                            }
                                        });

                                Toast.makeText(RegistrarEmpleado.this, "Empleado agregado", Toast.LENGTH_SHORT).show();
                                //Una vez agregado el usuario dueño del local se retorna a la vista Principal de dueño del local
                                startActivity(new Intent(RegistrarEmpleado.this, PrincipalUDL.class));
                                //Se finaliza la activity para evitar que el usuario regrese de nuevo a la activity del registro con todos los datos ingresados
                                finish();
                            }
                            else {
                                Toast.makeText(RegistrarEmpleado.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegistrarEmpleado.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}