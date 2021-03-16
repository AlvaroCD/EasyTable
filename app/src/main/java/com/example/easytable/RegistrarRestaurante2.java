package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegistrarRestaurante2 extends AppCompatActivity {

    private RadioGroup mCategoria;
    private RadioButton mRadioSeleccionado;
    private ImageButton mSiguienteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_restaurante2);

        mCategoria = (RadioGroup) findViewById(R.id.categoriaRegistrarLocalRadioGroup);

        mSiguienteButton = (ImageButton) findViewById(R.id.segundoSiguienteRegistrarLocalButton);

        mSiguienteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se declara con Integer en lugar de int para poder compararlo con un null
                int idDeLaCategoriaSeleccionada = mCategoria.getCheckedRadioButtonId();
                mRadioSeleccionado = findViewById(idDeLaCategoriaSeleccionada);
                String categoriaSeleccionada = mRadioSeleccionado.getText().toString();
                Toast.makeText(RegistrarRestaurante2.this, "Seleccionaste: "+categoriaSeleccionada, Toast.LENGTH_SHORT).show();
            }
        });
    }
}







/*
        mComidaRapida = (RadioButton) findViewById(R.id.comidaRapidaRegistrarLocalRadio);
                mBuffet = (RadioButton) findViewById(R.id.buffetRegistrarLocalRadio);
                mTematico = (RadioButton) findViewById(R.id.tematicoRegistrarLocalRadio);
                mParaLlevar = (RadioButton) findViewById(R.id.paraLlevarRegistrarLocalRadio);
                mDeAuto = (RadioButton) findViewById(R.id.deAutoRegistrarLocalRadio);
                mAltaCocina = (RadioButton) findViewById(R.id.altaCocinaRegistrarLocalRadio);
                mFusion = (RadioButton) findViewById(R.id.fusionRegistrarLocalRadio);
                mComidaMexicana = (RadioButton) findViewById(R.id.comidaMexicanaRegistrarLocalRadio);
                mComidaAsitica= (RadioButton) findViewById(R.id.comidaAsiaticaRegistrarLocalRadio);
                mComidaItaliana = (RadioButton) findViewById(R.id.comidaItalianaRegistrarLocalRadio);

                mComidaRapida, mBuffet, mTematico, mParaLlevar, mDeAuto,
            mAltaCocina, mFusion, mComidaMexicana, mComidaAsitica, mComidaItaliana
 */