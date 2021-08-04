package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MetodoDePago extends AppCompatActivity {

    private Spinner mMetodoPagoSpinner;
    private Button mPagarButton;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_metodo_de_pago);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mMetodoPagoSpinner = findViewById(R.id.metodoPagoSpinner);
        mPagarButton = findViewById(R.id.pagarMetodoPagoButton);

        String idCuenta = getIntent().getStringExtra("idCuenta");
        String idRestaurante = getIntent().getStringExtra("idRestaurante");
        String idUsuario = mAuth.getUid();

        mPagarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String metodoPago = mMetodoPagoSpinner.getSelectedItem().toString();
                if (metodoPago.equals("Efectivo")){
                    DocumentReference doc = db.collection("usuario").document(idUsuario);
                    doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            String tipoUsuario = value.getString("tipoDeUsuario");
                            Intent i;
                            if (tipoUsuario.equals("Cliente")){
                                i = new Intent(MetodoDePago.this, PagoEfectivo.class);
                                i.putExtra("idRestaurante", idRestaurante);
                            }
                            else {
                                i = new Intent(MetodoDePago.this, CobrarCuentaEfectivo.class);
                            }
                            i.putExtra("idCuenta", idCuenta);
                            startActivity(i);
                        }
                    });
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