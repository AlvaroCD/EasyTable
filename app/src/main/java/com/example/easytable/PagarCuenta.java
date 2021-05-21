package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PagarCuenta extends AppCompatActivity {

    //TODO: REALIZAR ESTA PARTE DE LOS PAGOS
    private TextView mMontoPagar;
    private Button mPagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_pagar_cuenta);

        mMontoPagar = findViewById(R.id.montoPagarTxt);
        mPagar = findViewById(R.id.pagarButton);


        mPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }//
}