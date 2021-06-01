package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PrincipalUDL extends AppCompatActivity {


    private Button mAgregarEmpleados, mEditarEmpleados, mListarEmpleados, mEliminarEmpleados, mListarCodigos, mLogOut;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal_usuario_duenolocal);

        //Instanciación de Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        mAgregarEmpleados = findViewById(R.id.agregarEmpleadosButton);
        mEditarEmpleados = findViewById(R.id.editarEmpleadosButton);
        mListarEmpleados = findViewById(R.id.listarEmpleadosButton);
        mEliminarEmpleados = findViewById(R.id.eliminarEmpleadosButton);
        mListarCodigos = findViewById(R.id.listarCodigosQR);
        mLogOut = findViewById(R.id.LogOutButton3);

        String idUsuarioLogueado = mAuth.getUid();
        DocumentReference doc = db.collection("usuario").document(idUsuarioLogueado);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String idRestaurante = value.get("IDRestReg").toString();

                mAgregarEmpleados.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUDL.this, RegistrarEmpleado.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });

                mEditarEmpleados.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUDL.this, ListadoEditarEmpleado.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });

                mListarEmpleados.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUDL.this, ListadoEmpleados.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });

                mEliminarEmpleados.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUDL.this, ListadoEliminarEmpleado.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });

                mListarCodigos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUDL.this, ListadoCodigosQR.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });
            }
        });

        //Boton para salir de la app
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
            }
        });
    }
}