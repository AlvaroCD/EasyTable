package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrincipalUCO extends AppCompatActivity {

    private Button mOrdenes, mOrdenesTerminadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal_uco);

        mOrdenes = findViewById(R.id.ordenesButton);
        mOrdenesTerminadas = findViewById(R.id.ordenesTerminadasButton);

        mOrdenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PrincipalUCO.this, ListadoOrdenes.class);
                startActivity(i);
            }
        });

        mOrdenesTerminadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}