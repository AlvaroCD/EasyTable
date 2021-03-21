package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

public class Ingresar extends AppCompatActivity {

   private static final String TAG = "Login";

    private EditText mUsername, mPassword;
    private Button mLoginButton, mRegisterButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);           //Se utiliza para quitar el nombre de la aplicacion de la pantalla inicial en el celular
        setContentView(R.layout.vista_ingresar);

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mLoginButton = (Button) findViewById(R.id.ingresarLoginButton);
        mRegisterButton = (Button) findViewById(R.id.registrarseLoginButton);
        mUsername = (EditText) findViewById(R.id.correoUsuarioLoginTxt);
        mPassword = (EditText) findViewById(R.id.contraseñaLoginTxt);

        //Instanciación de Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()){
                    loginUser();
                }
                else {
                    Toast.makeText(Ingresar.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Ingresar.this, RegistrarUsuario.class));
            }
        });
    }

    private void loginUser(){
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(Ingresar.this, PrincipalUC.class));
                    finish();
                }
                else {
                    Toast.makeText(Ingresar.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Con este metodo y esta condicional se busca hacer que el usuario mantenga su sesion iniciada aún cuando la app ya se haya cerrado
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()!= null){
            startActivity(new Intent(Ingresar.this, PrincipalUC.class));
            finish();
        }
    }
}