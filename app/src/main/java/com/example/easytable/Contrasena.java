package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Contrasena extends AppCompatActivity {

    private EditText mNuevaContraseña, mConfirmaContraseña;
    private Button mCambiarContraseña;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_contrasena);

        mNuevaContraseña = findViewById(R.id.nuevaContraseña);
        mConfirmaContraseña = findViewById(R.id.confirmaContraseña);
        mCambiarContraseña = findViewById(R.id.cambiarConButton);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        
        mCambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nueva, confirmada;
                nueva = mNuevaContraseña.getText().toString();
                confirmada = mConfirmaContraseña.getText().toString();
                
                if (!nueva.isEmpty() && !confirmada.isEmpty()){
                    if (nueva.equals(confirmada)){
                        user.updatePassword(nueva)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Contrasena.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Contrasena.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Contrasena.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Contrasena.this, "Porfavor llena todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}