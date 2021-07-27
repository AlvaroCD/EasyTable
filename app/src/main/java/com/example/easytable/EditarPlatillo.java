package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarPlatillo extends AppCompatActivity {

    private static final String KEY_DESCRIPCION = "descripcion";
    private static final String KEY_DISPONIBILIDAD = "disponibilidad";
    private static final String KEY_NOMBREPLATILLO = "nombrePlatillo";
    private static final String KEY_COSTO = "precio";


    private EditText mNombrePlatillo, mDescripcionPlatillo, mPrecioPlatillo;
    private Spinner mDisponibilidadPlatillo;
    private TextView mDisponibilidadActual;
    private Button mModificar;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_editar_platillo);

        mNombrePlatillo =  findViewById(R.id.nombreEditarPlatilloTxt);
        mDescripcionPlatillo = findViewById(R.id.descripcionEditarPlatilloTxt);
        mPrecioPlatillo = findViewById(R.id.precioEditarPlatilloTxt);
        mDisponibilidadActual = findViewById(R.id.disponibilidadActualPlatillo);
        mDisponibilidadPlatillo = findViewById(R.id.estadoActualizadoEditarPlatillo);
        mModificar = findViewById(R.id.modificarEditarPlatilloButton);

        db = FirebaseFirestore.getInstance();

        Bundle extra = getIntent().getExtras();
        String idPlatillo = extra.getString("idPlatillo");
        String nombre = extra.getString("nombrePlatillo");
        String descripcion = extra.getString("descripcion");
        long precio = extra.getLong("precio");
        boolean disponibilidad = extra.getBoolean("disponibilidad");

        mNombrePlatillo.setText(nombre);
        mDescripcionPlatillo.setText(descripcion);
        mPrecioPlatillo.setText(""+precio);
        if (disponibilidad){
            mDisponibilidadActual.setText("Disponibilidad: Disponible");
        }
        else {
            mDisponibilidadActual.setText("Disponibilidad: No Disponible");
        }

        mModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos(idPlatillo);
            }
        });

    }

    private void actualizarDatos(String idPlatillo) {
        String nombrePlatillo = mNombrePlatillo.getText().toString();
        String descripcionPlatillo = mDescripcionPlatillo.getText().toString();
        String precioParse = mPrecioPlatillo.getText().toString();
        long precioPlatillo = Long.parseLong(precioParse);
        boolean disponibilidadPlatillo;
        String disponibilidadParse = mDisponibilidadPlatillo.getSelectedItem().toString();
        if (disponibilidadParse.equals("Disponible")){
            disponibilidadPlatillo = true;
        }
        else {
            disponibilidadPlatillo = false;
        }

        Map<String, Object> platilloActualizado = new HashMap<>();
        platilloActualizado.put(KEY_NOMBREPLATILLO, nombrePlatillo);
        platilloActualizado.put(KEY_DESCRIPCION, descripcionPlatillo);
        platilloActualizado.put(KEY_COSTO, precioPlatillo);
        platilloActualizado.put(KEY_DISPONIBILIDAD, disponibilidadPlatillo);

        db.collection("platillos").document(idPlatillo).update(platilloActualizado)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditarPlatillo.this, "Platillo actualizado", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditarPlatillo.this, "Hubo un error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}