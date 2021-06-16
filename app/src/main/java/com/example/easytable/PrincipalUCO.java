package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PrincipalUCO extends AppCompatActivity {

    private Button mOrdenes,mOrdenesPreparacion, mOrdenesTerminadas, mLogOut;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal_uco);

        mOrdenes = findViewById(R.id.ordenesButton);
        mOrdenesTerminadas = findViewById(R.id.ordenesTerminadasButton);
        mOrdenesPreparacion = findViewById(R.id.ordenesPreparacionButton);
        mLogOut = findViewById(R.id.LogOutButtonCocinero);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        String idUsuarioLogueado = mAuth.getUid();
        DocumentReference doc = db.collection("usuario").document(idUsuarioLogueado);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String idRestaurante = value.get("IDRestReg").toString();

                mOrdenes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUCO.this, ListadoOrdenes.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });

                mOrdenesPreparacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUCO.this, ListadoOrdenesPreparacion.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });

                mOrdenesTerminadas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUCO.this, ListadoOrdenesTerminadas.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });
            }
        });


        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
            }
        });
    }
}