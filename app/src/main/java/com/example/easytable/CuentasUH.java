package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class CuentasUH extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Button mCuentasPagadas, mCuentasSinPagar, mLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_cuentas_uh);

        mCuentasPagadas = findViewById(R.id.cuentasPagadasUH);
        mCuentasSinPagar = findViewById(R.id.cuentasSinPagarUH);

        String idUsuario = mAuth.getUid();
        DocumentReference doc = db.collection("usuario").document(idUsuario);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String idRestaurante = value.getString("IDRestReg");

                mCuentasPagadas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CuentasUH.this, MenuCuentasPyNP.class);
                        i.putExtra("idLocal", idRestaurante);
                        startActivity(i);
                    }
                });

                mCuentasSinPagar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CuentasUH.this, ListadoCuentasSinPagar.class);
                        i.putExtra("idLocal", idRestaurante);
                        startActivity(i);
                    }
                });
            }
        });
    }
}