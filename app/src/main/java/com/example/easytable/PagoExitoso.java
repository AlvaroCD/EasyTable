package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PagoExitoso extends AppCompatActivity {

    Button mRegresar, mCalificar;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_pago_exitoso);

        mRegresar = findViewById(R.id.regresarInicioButton);
        mCalificar = findViewById(R.id.calificar);
        db = FirebaseFirestore.getInstance();

        String montoPagado = getIntent().getStringExtra("montoPagado");
        String idRestaurante = getIntent().getStringExtra("idRestaurante");

        Map <String, Object> ventas = new HashMap<>();
        ventas.put("ventas", montoPagado);

        db.collection("restaurante").document(idRestaurante).update(ventas);

        mCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PagoExitoso.this, CalificarLocal.class));
            }
        });

        mRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PagoExitoso.this, PrincipalUC.class));
                finish();
            }
        });
    }
}