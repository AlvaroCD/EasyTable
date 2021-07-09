package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PrincipalUH extends AppCompatActivity {

    private Button mAsignarMesero, mApartarLugar, mMeserosTrabajando, mCuentas, mContraseña, mLogOut;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal_uh);

        mAsignarMesero = findViewById(R.id.asignarMeseroButton);
        mApartarLugar = findViewById(R.id.apartarLugarButton);
        mMeserosTrabajando = findViewById(R.id.meserosTrabajandoButtonUH);
        mCuentas = findViewById(R.id.cuentasUH);
        mContraseña = findViewById(R.id.cambiarConButtonHost);
        mLogOut = findViewById(R.id.LogOutButtonHost);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String idUsuarioLogueado = mAuth.getUid();

        DocumentReference doc = db.collection("usuario").document(idUsuarioLogueado);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String idRestaurante = value.get("IDRestReg").toString();

                mAsignarMesero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUH.this, AsignarMesero.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });

                mApartarLugar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUH.this, ApartarLugarUH.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });

                mMeserosTrabajando.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUH.this, MeserosTrabajando.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });

                mCuentas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUH.this, CuentasUH.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });

                mContraseña.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUH.this, Contrasena.class);
                        startActivity(i);                    }
                });
            }
        });

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
            }
        });

    }
}