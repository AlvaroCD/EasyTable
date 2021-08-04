package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MenuCuentasPyNP extends AppCompatActivity {

    private Button mPagadoEfectivo, mPagadoDigital;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_menu_cuentas_p_y_np);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mPagadoEfectivo = findViewById(R.id.cuentasPagadasEfectivo);
        mPagadoDigital = findViewById(R.id.cuentasPagadasPaypal);

        String idUsuario = mAuth.getUid();

        DocumentReference doc = db.collection("usuario").document(idUsuario);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String idRestaurante = value.getString("IDRestReg");

                mPagadoEfectivo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuCuentasPyNP.this, ListadoCuentasPE.class);
                        i.putExtra("idLocal", idRestaurante);
                        startActivity(i);
                    }
                });

                mPagadoDigital.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuCuentasPyNP.this, ListadoCuentasPD.class);
                        i.putExtra("idLocal", idRestaurante);
                        startActivity(i);
                    }
                });

            }
        });
    }
}