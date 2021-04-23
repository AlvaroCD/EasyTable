package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class BusquedaRestaurante extends AppCompatActivity {
    private static final String TAG = "BusquedaRestaurante";

    private EditText mRestauranteBuscado;
    private RadioGroup mCategoriaBuscada;
    private RadioButton mCategoriaBuscadaSeleccionada;
    private ImageButton mBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_restaurante);


        mRestauranteBuscado = (EditText) findViewById(R.id.buscar);
        mCategoriaBuscada = (RadioGroup) findViewById(R.id.categoriaBuscarLocalRadioGroup);
        mBuscar = (ImageButton) findViewById(R.id.buscarRestaurantesButton);

        mBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtencion del nombre y la categoria seleccionada para realizar la busqueda
                String restauranteBuscado = mRestauranteBuscado.getText().toString();
                int idCategoriaSeleccionada = mCategoriaBuscada.getCheckedRadioButtonId();
                mCategoriaBuscadaSeleccionada = findViewById(idCategoriaSeleccionada);
                String categoriaSeleccionada = mCategoriaBuscadaSeleccionada.getText().toString();
                if (!restauranteBuscado.isEmpty() || !categoriaSeleccionada.isEmpty()) {
                    Toast.makeText(BusquedaRestaurante.this, "Escribiste: " + restauranteBuscado, Toast.LENGTH_SHORT).show();
                    Toast.makeText(BusquedaRestaurante.this, "Seleccionaste: " + categoriaSeleccionada, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(BusquedaRestaurante.this, ResultadoBusquedaRestaurante.class);
                    i.putExtra("restauranteBuscado", restauranteBuscado);
                    i.putExtra("categoriaRestauranteBuscado", categoriaSeleccionada);
                    startActivity(i);
                }
                else {
                    mostrarDialogo();
                }
            }
        });
    }


    private void mostrarDialogo(){
        new AlertDialog.Builder(this)
                .setMessage("Ingresa el nombre y/o selecciona la categoria del restaurante")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


}