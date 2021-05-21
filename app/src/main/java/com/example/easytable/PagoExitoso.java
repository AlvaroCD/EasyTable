package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PagoExitoso extends AppCompatActivity {

    Button mRegresar;

    //TODO: REALIZAR ESTA PARTE DE PAGOS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_pago_exitoso);

        mRegresar = findViewById(R.id.regresarInicioButton);

        mRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PagoExitoso.this, PrincipalUC.class));
                finish();
            }
        });
    }
}