package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ReservacionCliente extends AppCompatActivity {

    private TextView mNombreUsuario, mFecha, mHora, mCantidadPersonas, mStatusReservacion;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_reservacion_cliente);

        mNombreUsuario = findViewById(R.id.nombreUsuarioReservacionCliente);
        mFecha = findViewById(R.id.fechaContenedorReservacionCliente);
        mHora = findViewById(R.id.horaContenedorReservacionCliente);
        mCantidadPersonas = findViewById(R.id.cantidadPersonasContenedorReservacionCliente);
        mStatusReservacion = findViewById(R.id.statusReservacionContenedorReservacionCliente);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String id = mAuth.getUid();
        DocumentReference doc = db.collection("reservaciones").document(id);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String nombreUsuario = value.getString("usuario");
                if (nombreUsuario != null){
                    String fecha = value.getString("fecha");
                    String hora = value.getString("hora");
                    long cantidadPersonas = value.getLong("cantidadPersonas");
                    long statusReservacion = value.getLong("statusReservacion");
                    mNombreUsuario.setText(nombreUsuario);
                    mFecha.setText(fecha);
                    mHora.setText(hora);
                    mCantidadPersonas.setText(""+cantidadPersonas);
                    if (statusReservacion == 0){
                        mStatusReservacion.setText("Sin Aprobar");
                    }
                    else if (statusReservacion == 1){
                        mStatusReservacion.setText("Aprobada");
                    }
                }
                else {
                    mNombreUsuario.setText("Sin reservación");
                    mFecha.setText("Sin fecha");
                    mHora.setText("Sin hora");
                    mCantidadPersonas.setText("Sin personas");
                    mStatusReservacion.setText("N/A");
                }
            }
        });
    }
}