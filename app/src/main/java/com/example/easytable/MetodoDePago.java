package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MetodoDePago extends AppCompatActivity {

    private Spinner mMetodoPagoSpinner;
    private Button mPagarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_metodo_de_pago);

        mMetodoPagoSpinner = findViewById(R.id.metodoPagoSpinner);
        mPagarButton = findViewById(R.id.pagarMetodoPagoButton);

        String idCuenta = getIntent().getStringExtra("idCuenta");
        String idRestaurante = getIntent().getStringExtra("idRestaurante");

        mPagarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String metodoPago = mMetodoPagoSpinner.getSelectedItem().toString();
                if (metodoPago.equals("Efectivo")){
                    Intent i = new Intent(MetodoDePago.this, PagoEfectivo.class);
                    i.putExtra("idCuenta", idCuenta);
                    startActivity(i);
                } else {
                    Intent o = new Intent(MetodoDePago.this, PagarCuenta.class);
                    o.putExtra("idCuenta", idCuenta);
                    o.putExtra("idRestaurante", idRestaurante);
                    startActivity(o);
                }
            }
        });
    }
}