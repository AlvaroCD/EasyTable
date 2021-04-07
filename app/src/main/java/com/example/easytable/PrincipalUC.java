package com.example.easytable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.util.Util;

import java.io.File;
import java.net.URI;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class PrincipalUC extends Activity implements ZXingScannerView.ResultHandler{

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private RestaurantesAdapter mAdapter;
    private ImageButton ImagenQR;
    private Button mLogOut;


    //Objetos para utilizar las dependencias
    private ZXingScannerView mScannerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    //Vinculacion de la actividad con el layout
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal_usuario_cliente);


        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        ImagenQR = findViewById(R.id.codigoQR);
        mLogOut = findViewById(R.id.LogOutButton2);

        //Instanciación de Firebase Authentication y de Firebase Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoRestaurantes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Consulta para obtener los datos de la BD
        Query query = db.collection("restaurante");

        FirestoreRecyclerOptions<RestaurantePojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<RestaurantePojo>()
                .setQuery(query, RestaurantePojo.class).build();

        mAdapter = new RestaurantesAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);


        //Funcion que determina que accion se realiza cuando se hace click en algun restaurante
        mAdapter.setOnItemClickListener(new RestaurantesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                RestaurantePojo restaurante = documentSnapshot.toObject(RestaurantePojo.class);
                String id = documentSnapshot.getId();
                String nombreRestaurante = documentSnapshot.get("nombreLocal").toString();
                Intent i = new Intent(PrincipalUC.this, Restaurante.class);
                i.putExtra("idRestaurante",id);
                i.putExtra("nombreRestaurante", nombreRestaurante);
                startActivity(i);
                Toast.makeText(PrincipalUC.this, "Posicion "+posicion + "ID:" + id, Toast.LENGTH_SHORT).show();
            }
        });




        //Boton de codigoQR
        ImagenQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerView = new ZXingScannerView(PrincipalUC.this);
                setContentView(mScannerView);
                mScannerView.setResultHandler(PrincipalUC.this);
                mScannerView.startCamera();
            }
        });

        //Boton para salir de la app
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(PrincipalUC.this, Ingresar.class));
                finish();
            }
        });

    }


    //Metodo para desifrar codigo QR
    @Override
    public void handleResult(com.google.zxing.Result result) {
        Log.v("HandleResult", result.getText());
        //Aparece el texto del codigo QR en un dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Resultado Scan");
//        builder.setMessage(result.getText());
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
        //Envio de informacion a la vista Restaurante
        String dato = result.getText();

        Intent Restaurante = new Intent(PrincipalUC.this, Restaurante.class);
        Restaurante.putExtra("idRestaurante",dato);
        startActivity(Restaurante);
        finish();


        //Permite seguir escaneando despues de la primera vez
//        mScannerView.resumeCameraPreview(this);

    }


    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los restaurantes)
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los restaurantes)
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }




    //Funcion que hace una llamada a un menu creado para poder realizar la busqueda de los restaurantes
    public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buscador, menu);
        return true;
    }


}