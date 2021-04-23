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

public class RegistrarUsuario extends AppCompatActivity {

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
    private EditText mNombre, mApellidos, mTelefono, mCorreo, mUsername, mPassword;
    private Spinner mTipoUsuario;
    private Button mRegistrarseButton;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);           //Se utiliza para quitar el nombre de la aplicacion de la pantalla inicial en el celular
        setContentView(R.layout.vista_registrar_usuario);

        //Instanciación de Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        //Relacion de la parte grafica (xml) con la lógica (java)
        mNombre = (EditText) findViewById(R.id.nombreRegistrarUsuarioTxt);
        mApellidos = (EditText) findViewById(R.id.apellidoRegistrarUsuarioTxt);
        mTelefono = (EditText) findViewById(R.id.telefonoRegistrarUsuarioTxt);
        mCorreo = (EditText) findViewById(R.id.correoRegistrarUsuarioTxt);
        mUsername = (EditText) findViewById(R.id.usernameRegistrarUsuarioTxt);
        mPassword = (EditText) findViewById(R.id.passwordRegistrarUsuarioTxt);
        mTipoUsuario = (Spinner) findViewById(R.id.tipoDeUsuarioRegistrarUsuarioSpinner);
        mRegistrarseButton = (Button) findViewById(R.id.registrarseRegistrarUsuarioButton);



        //Variables que almacenaran el contenido de los EditText's y el Spinner de la parte grafica


        mRegistrarseButton.setOnClickListener(new View.OnClickListener() {          //Boton de acción para registrar un usuario
            @Override
            public void onClick(View v) {

                String nombre = mNombre.getText().toString();
                String apellidos = mApellidos.getText().toString();
                String telefono = mTelefono.getText().toString();
                String correo = mCorreo.getText().toString();
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String tipoUsuario = mTipoUsuario.getSelectedItem().toString();
                //Aqui se crea un Id con la propiedad random para prevenir que los identificadores de los usuarios se repitan
                String id = UUID.randomUUID().toString();


                if (!nombre.isEmpty() && !apellidos.isEmpty() && !telefono.isEmpty() && !correo.isEmpty() &&
                        !username.isEmpty() && !password.isEmpty()){

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
                                registerUserOnFirebaseAuth(id);
                            }
                        })
                        //Listener que indica si la creacion del usuario fue incorrecta
                        .addOnFailureListener(new OnFailureListener()   {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistrarUsuario.this, "Error", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });
            }
            else{
                    Toast.makeText(RegistrarUsuario.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
            }
            }
        });




    }

    //Se recibe el ID para posteriormente en la funcion "OnComplete" poder enviar dicho ID a la siguiente ventana (RegistrarRestaurante1)
    //y de esa manera poder ligar el ID del usuario con el restaurante
    private void registerUserOnFirebaseAuth(String id) {
        String correo = mCorreo.getText().toString();
        String password = mPassword.getText().toString();
        String tipoUsuario = mTipoUsuario.getSelectedItem().toString();
        mAuth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegistrarUsuario.this, "Usuario agregado", Toast.LENGTH_SHORT).show();
                    if (tipoUsuario.equals("Cliente")){
                        //Una vez agregado el usuario cliente se retorna a la vista Ingresar
                        startActivity(new Intent(RegistrarUsuario.this, Ingresar.class));
                        //Se finaliza la activity para evitar que el usuario regrese de nuevo a la activity del registro con todos los datos ingresados
                        finish();
                    }
                    else {
                        //En caso contrario que sea Dueño de Local se pasa a la siguiente vista para registrar el restaurante del usuario
                        Intent i = new Intent(RegistrarUsuario.this, RegistrarRestaurante1.class);
                        i.putExtra("idPropietario", id);
                        startActivity(i);
                        finish();
                    }
                }
                else {
                    Toast.makeText(RegistrarUsuario.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}