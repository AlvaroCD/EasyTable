package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PagoExitoso extends AppCompatActivity {

    private Button mRegresar, mCalificar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private static long sumadorVentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_pago_exitoso);

        //mRegresar = findViewById(R.id.regresarInicioButton);
        mCalificar = findViewById(R.id.calificar);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String idUsuario = mAuth.getUid();
        String montoPagado = getIntent().getStringExtra("montoPagado");
        String idRestaurante = getIntent().getStringExtra("idRestaurante");
        String idCuenta = getIntent().getStringExtra("idCuenta");


        db.collection("restaurante").document(idRestaurante).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String sumadorVentasString = documentSnapshot.getString("ventas");
                        sumadorVentas = Long.parseLong(sumadorVentasString);
                        long montoPagadoParse = Long.parseLong(montoPagado);
                        sumadorVentas = montoPagadoParse+sumadorVentas;
                        sumadorVentasString = String.valueOf(sumadorVentas);
                        Map<String, Object> ventas = new HashMap<>();
                        ventas.put("ventas", sumadorVentasString);
                        db.collection("restaurante").document(idRestaurante).update(ventas);
                    }
                });

        Map<String, Object> updateCuenta = new HashMap<>();
        updateCuenta.put("pagado", true);
        updateCuenta.put("montoPagar", 0);
        db.collection("cuenta").document(idCuenta).update(updateCuenta);

        //Eliminacion de la reservacion
        db.collection("reservaciones").document(idUsuario).delete();

        //Liberacion del campo Reservacin para que pueda volver a reservar
        Map<String, Object> usuarioUpdate = new HashMap<>();
        usuarioUpdate.put("Reserva", false);
        db.collection("usuario").document(idUsuario).update(usuarioUpdate);


        mCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PagoExitoso.this, CalificarComentar.class);
                i.putExtra("idCuenta", idCuenta);
                i.putExtra("idRestaurante", idRestaurante);
                startActivity(i);
                finish();
            }
        });

//        mRegresar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(PagoExitoso.this, PrincipalUC.class));
//                finish();
//
//            }
//        });
    }
}