package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PrincipalUA extends AppCompatActivity {

    private Button mAgregarPlatillo, mLocal, mRecursos, mMeserosTrabajando, mLogOut;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal_ua);

        //Relación con el XML
        mAgregarPlatillo = findViewById(R.id.agregarPlatilloButton);
        mLocal = findViewById(R.id.localButton);
        mRecursos = findViewById(R.id.recursosButton);
        mMeserosTrabajando = findViewById(R.id.meserosTrabajandoButton);
        mLogOut = findViewById(R.id.LogOutButtonAdmin);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        String idUsuarioLogueado = mAuth.getUid();
        DocumentReference doc = db.collection("usuario").document(idUsuarioLogueado);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String idRestaurante = value.get("IDRestReg").toString();
                mAgregarPlatillo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUA.this, AgregarPlatillo.class);
                        startActivity(i);
                    }
                });

                mLocal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PrincipalUA.this, "Local", Toast.LENGTH_SHORT).show();
                    }
                });

                mRecursos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUA.this, Recursos.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                        Toast.makeText(PrincipalUA.this, "Listado Recursos", Toast.LENGTH_SHORT).show();
                    }
                });

                mMeserosTrabajando.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PrincipalUA.this, "Meseros Trabajando", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                Toast.makeText(PrincipalUA.this, "Cerrar Sesion", Toast.LENGTH_SHORT).show();
            }
        });

    }

}