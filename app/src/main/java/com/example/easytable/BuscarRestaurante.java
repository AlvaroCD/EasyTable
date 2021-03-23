package com.example.easytable;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BuscarRestaurante extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vista_principal_usuario_cliente);
        
    }
}
