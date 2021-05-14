package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarEmpleado extends AppCompatActivity {

    private EditText mNombreEditado, mApellidoEditado, mTelefonoEditado, mCorreoEditado, mUsuarioEditado;
    private TextView mPuestoActual;
    private Spinner mTipoUsuarioEditado;
    private Button mModificarButton;

    private FirebaseFirestore db;

    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap
    private static final String KEY_NOMBRE = "Nombre";
    private static final String KEY_APELLIDO = "Apellidos";
    private static final String KEY_TELEFONO = "Telefono";
    private static final String KEY_CORREO= "Correo";
    private static final String KEY_USUARIO= "Usuario";
    private static final String KEY_TIPOEMPLEADO = "tipoDeUsuario";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);           //Se utiliza para quitar el nombre de la aplicacion de la pantalla inicial en el celular
        setContentView(R.layout.vista_editar_empleado);

        db = FirebaseFirestore.getInstance();

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        //EditText
        mNombreEditado = findViewById(R.id.nombreEditarEmpleadoTxt);
        mApellidoEditado = findViewById(R.id.apellidoEditarEmpleadoTxt);
        mTelefonoEditado = findViewById(R.id.telefonoEditarEmpleadoTxt);
        mUsuarioEditado = findViewById(R.id.usernameEditarEmpleadoTxt);
        mCorreoEditado = findViewById(R.id.correoEditarEmpleadoTxt);

        //TextView
        mPuestoActual = findViewById(R.id.puestoActualEditarEmpleado);

        //Spinner
        mTipoUsuarioEditado = findViewById(R.id.tipoDeUsuarioEditarEmpleadoSpinner);

        //Button
        mModificarButton = findViewById(R.id.modificarEditarEmpleadoButton);

        Bundle extra = getIntent().getExtras();
        String nombre = extra.getString("nombre");
        String apellido = extra.getString("apellido");
        String telefono = extra.getString("telefono");
        String correo = extra.getString("correo");
        String usuario = extra.getString("usuario");
        String tipoUsuario = extra.getString("tipoUsuario");
        String id = extra.getString("id");


        //Aqui se colocan los datos que tiene el empleado hasta el momento para que el dueño del local pueda verlos
        mNombreEditado.setText(nombre);
        mApellidoEditado.setText(apellido);
        mTelefonoEditado.setText(telefono);
        mCorreoEditado.setText(correo);
        mUsuarioEditado.setText(usuario);
        mPuestoActual.setText("Puesto actual: "+tipoUsuario);


        mModificarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos(id);
            }
        });

    }

    private void actualizarDatos(String id){
        String nombreEditado = mNombreEditado.getText().toString();
        String apellidoEditado = mApellidoEditado.getText().toString();
        String telefonoEditado = mTelefonoEditado.getText().toString();
        String correoEditado = mCorreoEditado.getText().toString();
        String usuarioEditado = mUsuarioEditado.getText().toString();
        String tipoUsuarioEditado = mTipoUsuarioEditado.getSelectedItem().toString();

        if (!nombreEditado.isEmpty() && !apellidoEditado.isEmpty() && !telefonoEditado.isEmpty() && !correoEditado.isEmpty()
                && !usuarioEditado.isEmpty() && !tipoUsuarioEditado.isEmpty()){

            Map <String, Object> informacionEmpleado = new HashMap<>();
            informacionEmpleado.put(KEY_NOMBRE, nombreEditado);
            informacionEmpleado.put(KEY_APELLIDO, apellidoEditado);
            informacionEmpleado.put(KEY_TELEFONO, telefonoEditado);
            informacionEmpleado.put(KEY_CORREO, correoEditado);
            informacionEmpleado.put(KEY_USUARIO, usuarioEditado);
            informacionEmpleado.put(KEY_TIPOEMPLEADO, tipoUsuarioEditado);

            db.collection("usuario").document(id).update(informacionEmpleado)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditarEmpleado.this, "Usuario modificado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditarEmpleado.this, "Hubo un error al modificar al usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(EditarEmpleado.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
        }

    }
}