package com.example.easytable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class CalificarLocal extends Activity {

    Button mFinal, mSiguiente;
    TextView mText;
    EditText comida, local, comentario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calificar_local);

     mFinal = findViewById(R.id.Continuar);
     mSiguiente = findViewById(R.id.Calificar);
     mText = findViewById(R.id.comida);
     comida = findViewById(R.id.text1);
     local = findViewById(R.id.calificacionLocalFinal);
     comentario = findViewById(R.id.comentarioLocal);

     mSiguiente.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             mText.setText("Tortitas de Papa");
             comida.setText("");

         }
     });


    mFinal.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(CalificarLocal.this, PrincipalUC.class));
        }
    });

    }
}
