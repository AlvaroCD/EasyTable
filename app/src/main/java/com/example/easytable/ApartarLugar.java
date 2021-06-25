package com.example.easytable;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class ApartarLugar extends Activity {

    private Button mHora;
    private ImageButton mAceptar, mRetroceder;
    private EditText mPersonas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_apartar_lugar);

        mHora = findViewById(R.id.horaButton);
        mPersonas = findViewById(R.id.cantidadPersonasApartarLugarUC);
        mAceptar = findViewById(R.id.hacerReservacionButton);


    }
}
