package com.example.easytable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.util.Util;

import java.io.File;
import java.net.URI;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class PrincipalUC extends Activity implements ZXingScannerView.ResultHandler{

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private ImageButton _ImagenQR;
    private ImageView  _ImagenL1, _ImagenL2, _ImagenL3, _ImagenL4;
    private TextView _local1, _local2, _local3, _local4, _dlocal1, _dlocal2, _dlocal3, _dlocal4;
    private Button mLogOut;
    private ZXingScannerView mScannerView;
    private FirebaseAuth mAuth;
    DatabaseReference mRootReference;


    //Vinculacion de la actividad con el layout
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal_usuario_cliente);
        //Inica la referencia en el nodo principal de fire base
        mRootReference = FirebaseDatabase.getInstance().getReference();

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        _ImagenL1 =  findViewById(R.id.imagen1);
        _ImagenL2 =  findViewById(R.id.imagen2);
        _ImagenL3 =  findViewById(R.id.imagen3);
        _ImagenL4 =  findViewById(R.id.imagen4);
        _ImagenQR = findViewById(R.id.codigoQR);
        _local1 = findViewById(R.id.Nrestaurante1);
        _local2 = findViewById(R.id.Nrestaurante2);
        _local3 = findViewById(R.id.Nrestaurante3);
        _local4 = findViewById(R.id.Nrestaurante4);
        _dlocal1 = findViewById(R.id.Drestaurante1);
        _dlocal2 = findViewById(R.id.Drestaurante2);
        _dlocal3 = findViewById(R.id.Drestaurante3);
        _dlocal4 = findViewById(R.id.Drestaurante4);
        mLogOut = findViewById(R.id.LogOutButton);


        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }

        //Boton de codigoQR
        _ImagenQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerView = new ZXingScannerView(PrincipalUC.this);
                setContentView(mScannerView);
                mScannerView.setResultHandler(PrincipalUC.this);
                mScannerView.startCamera();
            }
        });

        /*//Boton para salir de la app
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(PrincipalUC.this, Ingresar.class));
                finish();
            }
        });*/

        ExtraccionFireBase();

    }

    private void ExtraccionFireBase() {
        //Regresa los registros dentro de restaurante
        mRootReference.child("restaurante").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    //Regresa lops campos de cada restaurante
                    mRootReference.child("restaurante").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            RestaurantePojo restaurantePojo = snapshot.getValue(RestaurantePojo.class);
                            String nombre = restaurantePojo.getNombreLocal();
                            String descripcion = restaurantePojo.getDescripcionRestaurante();
                            String direccion = restaurantePojo.getDireccion();
                            String tipo = restaurantePojo.getTipoRestaurante();
                            String rfc = restaurantePojo.getRfc();
                            int mesas = restaurantePojo.getCantidadMesas();
                            _local1.setText(nombre);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //Metodo que realiza la busqueda
    private void doMySearch(String query) {
        _local1.setText(query);
    }


    //Metodo para desifrar codigo QR
    @Override
    public void handleResult(com.google.zxing.Result result) {
        Log.v("HandleResult", result.getText());
        //Aparece el texto del codigo QR en un dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resultado Scan");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //Permite seguir escaneando despues de la primera vez
        mScannerView.resumeCameraPreview(this);

    }
}