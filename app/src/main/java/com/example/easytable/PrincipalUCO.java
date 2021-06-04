package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class PrincipalUCO extends AppCompatActivity {

    private Button mOrdenes,mOrdenesPreparacion, mOrdenesTerminadas, mLogOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal_uco);

        mOrdenes = findViewById(R.id.ordenesButton);
        mOrdenesTerminadas = findViewById(R.id.ordenesTerminadasButton);
        mOrdenesPreparacion = findViewById(R.id.ordenesPreparacionButton);
        mLogOut = findViewById(R.id.LogOutButtonCocinero);

        mAuth = FirebaseAuth.getInstance();

        mOrdenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PrincipalUCO.this, ListadoOrdenes.class);
                startActivity(i);
            }
        });

        mOrdenesPreparacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PrincipalUCO.this, ListadoOrdenesPreparacion.class);
                startActivity(i);
            }
        });

        mOrdenesTerminadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PrincipalUCO.this, ListadoOrdenesTerminadas.class);
                startActivity(i);
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