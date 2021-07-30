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

public class CobrarCuentaEfectivo extends AppCompatActivity {

    private TextView mAvisoCobrar;
    private Button mCobrar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_cobrar_cuenta);

        db = FirebaseFirestore.getInstance();

        mAvisoCobrar = findViewById(R.id.avisoCobrar);
        mCobrar = findViewById(R.id.cobroButton);

        String idCuenta = getIntent().getStringExtra("idCuenta");
        DocumentReference doc = db.collection("cuenta").document(idCuenta);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String montoPagar = value.get("montoPagar").toString();
                mAvisoCobrar.setText("Cobra la cantidad de: "+montoPagar+" MXN");
            }
        });


        mCobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> cobroRealizado = new HashMap<>();
                cobroRealizado.put("pagado", true);
                cobroRealizado.put("efectivo", true);
                db.collection("cuenta").document(idCuenta).update(cobroRealizado)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CobrarCuentaEfectivo.this, "Se registro como pagada la cuenta", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CobrarCuentaEfectivo.this, PrincipalUM.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CobrarCuentaEfectivo.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}