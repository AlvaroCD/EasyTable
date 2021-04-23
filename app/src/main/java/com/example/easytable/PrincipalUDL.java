package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class PrincipalUDL extends AppCompatActivity {

    private Button mAgregarEmpleados, mEditarEmpleados, mListarEmpleados, mEliminarEmpleados, mLogOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal_usuario_duenolocal);

        //Instanciación de Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        mAgregarEmpleados = findViewById(R.id.agregarEmpleadosButton);
        mEditarEmpleados = findViewById(R.id.editarEmpleadosButton);
        mListarEmpleados = findViewById(R.id.listarEmpleadosButton);
        mEliminarEmpleados = findViewById(R.id.eliminarEmpleadosButton);
        mLogOut = findViewById(R.id.LogOutButton3);

        mAgregarEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalUDL.this, RegistrarEmpleado.class));
            }
        });

        mEditarEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalUDL.this, ListadoEditarEmpleado.class));
            }
        });

        mListarEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalUDL.this, ListadoEmpleados.class));
            }
        });

        mEliminarEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalUDL.this, ListadoEliminarEmpleado.class));
            }
        });

        //Boton para salir de la app
        onClickSalir();

    }

    private void onClickSalir() {
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(PrincipalUDL.this, Ingresar.class));
                finish();
            }
        });
    }

}