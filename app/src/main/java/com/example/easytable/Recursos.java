package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Recursos extends AppCompatActivity {

    private Button mAgregarIngrediente, mEliminarIngrediente, mListadoRecursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_recursos);

        mAgregarIngrediente = findViewById(R.id.agregarIngredienteButton);
        mEliminarIngrediente = findViewById(R.id.eliminarIngredienteButton);
        mListadoRecursos = findViewById(R.id.listadoRecursosButton);

        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");

        mAgregarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Recursos.this, AgregarIngrediente.class);
                startActivity(i);
            }
        });


        mEliminarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Recursos.this, ListadoEliminarRecursos.class);
                i.putExtra("idRestaurante", idRestaurante);
                startActivity(i);
            }
        });


        mListadoRecursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Recursos.this, ListadoRecursos.class);
                i.putExtra("idRestaurante", idRestaurante);
                startActivity(i);
            }
        });
    }
}