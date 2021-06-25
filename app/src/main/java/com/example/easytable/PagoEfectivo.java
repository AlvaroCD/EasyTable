package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

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

        DocumentReference doc = db.collection("cuenta").document(idCuenta);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String montoPagar = value.get("montoPagar").toString();
                mAvisoPagar.setText("Paga la cantidad de: "+montoPagar+" MXN");
            }
        });

        mEntendidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PagoEfectivo.this, PrincipalUC.class));
                finish();
            }
        });
    }
}