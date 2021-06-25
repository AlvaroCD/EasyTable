package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Ventas extends AppCompatActivity {

    private TextView mTotalVentas;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_ventas);

        mTotalVentas = findViewById(R.id.totalVentas);
        db = FirebaseFirestore.getInstance();

        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");

        DocumentReference doc = db.collection("restaurante").document(idRestaurante);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String ventas = value.get("ventas").toString();
                mTotalVentas.setText(ventas);
            }
        });
    }
}