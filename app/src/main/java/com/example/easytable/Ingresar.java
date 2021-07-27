package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

import java.util.HashMap;
import java.util.Map;

public class Ingresar extends AppCompatActivity {

    private static final String TAG = "Login";

    private EditText mCorreo, mPassword;
    private Button mLoginButton, mRegisterButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);           //Se utiliza para quitar el nombre de la aplicacion de la pantalla inicial en el celular
        setContentView(R.layout.vista_ingresar);

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mLoginButton = (Button) findViewById(R.id.ingresarLoginButton);
        mRegisterButton = (Button) findViewById(R.id.registrarseLoginButton);
        mCorreo = (EditText) findViewById(R.id.correoUsuarioLoginTxt);
        mPassword = (EditText) findViewById(R.id.contraseñaLoginTxt);

        //Instanciación de Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = mCorreo.getText().toString();
                String password = mPassword.getText().toString();
                if (!correo.isEmpty() && !password.isEmpty()) {
                    loginUser();
                } else {
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

    private void loginUser() {
        String correo = mCorreo.getText().toString();
        String password = mPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(correo, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String id = mAuth.getUid();
                        DocumentReference doc = db.collection("usuario").document(id);
                        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                String tipoUsuario = value.get("tipoDeUsuario").toString();
                                switch (tipoUsuario) {
                                    case "Cliente":
                                        startActivity(new Intent(Ingresar.this, PrincipalUC.class));
                                        break;
                                    case "Dueño del Local":
                                        startActivity(new Intent(Ingresar.this, PrincipalUDL.class));
                                        break;
                                    case "Host":
                                        startActivity(new Intent(Ingresar.this, PrincipalUH.class));
                                        break;
                                    case "Cocinero":
                                        startActivity(new Intent(Ingresar.this, PrincipalUCO.class));
                                        break;
                                    case "Administrador":
                                        startActivity(new Intent(Ingresar.this, PrincipalUA.class));
                                        break;
                                    case "Mesero":
                                        if (mAuth.getCurrentUser() != null) {
                                            Map<String, Object> online = new HashMap<>();
                                            online.put("online", true);
                                            db.collection("usuario").document(id).update(online);
                                        }
                                        startActivity(new Intent(Ingresar.this, PrincipalUM.class));
                                        break;
                                    case "Cajero":
                                        db.collection("usuario").document(id).get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        String idRestaurante = documentSnapshot.getString("IDRestReg");
                                                        Intent i = new Intent(Ingresar.this, PrincipalUCJ.class);
                                                        i.putExtra("idLocal", idRestaurante);
                                                        startActivity(i);
                                                    }
                                                });
                                        break;
                                }
                                finish();

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Ingresar.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Con este metodo y esta condicional se busca hacer que el usuario mantenga su sesion iniciada aún cuando la app ya se haya cerrado
    /*@Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            String id = mAuth.getUid();
            //El usuario tiene una sesion iniciada
            DocumentReference doc = db.collection("usuario").document(id);
            doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    String tipoUsuario = value.get("tipoDeUsuario").toString();
                    switch (tipoUsuario) {
                        case "Cliente":
                            startActivity(new Intent(Ingresar.this, PrincipalUC.class));
                            break;
                        case "Dueño del Local":
                            startActivity(new Intent(Ingresar.this, PrincipalUDL.class));
                            break;
                        case "Host":
                            startActivity(new Intent(Ingresar.this, PrincipalUH.class));
                            break;
                        case "Cocinero":
                            startActivity(new Intent(Ingresar.this, PrincipalUCO.class));
                            break;
                        case "Administrador":
                            startActivity(new Intent(Ingresar.this, PrincipalUA.class));
                            break;
                        case "Mesero":
                            if (mAuth.getCurrentUser() != null) {
                                Map<String, Object> online = new HashMap<>();
                                online.put("online", true);
                                db.collection("usuario").document(id).update(online);
                            }
                            startActivity(new Intent(Ingresar.this, PrincipalUM.class));
                            break;
                        case "Cajero":
                            db.collection("usuario").document(id).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String idRestaurante = documentSnapshot.getString("IDRestReg");
                                            Intent i = new Intent(Ingresar.this, PrincipalUCJ.class);
                                            i.putExtra("idLocal", idRestaurante);
                                            startActivity(i);
                                        }
                                    });
                            break;
                    }
                    finish();

                }
            });
        }
    }*/

    @Override
    protected void onStop() {
        super.onStop();
    }
}