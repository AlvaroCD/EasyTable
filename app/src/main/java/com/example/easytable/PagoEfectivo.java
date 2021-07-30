package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class PagoEfectivo extends AppCompatActivity {

    private TextView mAvisoPagar;
    private Button mEntendidoButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_pago_efectivo);

        mAvisoPagar = findViewById(R.id.avisoPagar);
        mEntendidoButton = findViewById(R.id.entendidoButton);

        db = FirebaseFirestore.getInstance();

        String idCuenta = getIntent().getStringExtra("idCuenta");
        String idRestaurante = getIntent().getStringExtra("idRestaurante");

        DocumentReference doc = db.collection("cuenta").document(idCuenta);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String montoPagar = value.get("montoPagar").toString();
                String idMesa = value.getString("mesa");
                mAvisoPagar.setText("Paga la cantidad de: " + montoPagar + " MXN");

                mEntendidoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> actualizarStatusMesa = new HashMap<>();
                        actualizarStatusMesa.put("statusMesa", false);

                        db.collection("mesa").document(idMesa).update(actualizarStatusMesa)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(PagoEfectivo.this, "Mesa liberada", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(PagoEfectivo.this, PagoExitoso.class);
                                        i.putExtra("montoPagado", montoPagar);
                                        i.putExtra("idRestaurante", idRestaurante);
                                        i.putExtra("idCuenta", idCuenta);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PagoEfectivo.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });

    }
}