package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Platillos extends AppCompatActivity {

    Button mAgregarPlatillo, mEliminarPlatillo, mEditarPlatillo, mListarPlatillos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_platillos);

        mAgregarPlatillo = findViewById(R.id.agregarPlatilloButton);
        mEditarPlatillo = findViewById(R.id.editarPlatilloButton);
        mEliminarPlatillo = findViewById(R.id.eliminarPlatilloButton);
        mListarPlatillos = findViewById(R.id.listadoPlatillosButton);

        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");

        mAgregarPlatillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Platillos.this, AgregarPlatillo.class);
                i.putExtra("idRestaurante", idRestaurante);
                startActivity(i);
            }
        });

        mEditarPlatillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Platillos.this, ListadoEditarPlatillo.class);
                i.putExtra("idRestaurante", idRestaurante);
                startActivity(i);
            }
        });

        mEliminarPlatillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Platillos.this, ListadoEliminarPlatillo.class);
                i.putExtra("idRestaurante", idRestaurante);
                startActivity(i);
            }
        });

        mListarPlatillos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Platillos.this, ListadoPlatillos.class);
                i.putExtra("idRestaurante", idRestaurante);
                startActivity(i);
            }
        });
    }
}