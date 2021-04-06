package com.example.easytable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Restaurante extends Activity {
    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private ComentarioAdapter mAdapter;
    private ImageView mImagenLocal, mCalificacionLocal;
    private Button mReservar;
    private TextView mRestaurante, mTipoRestaurante;
    String IdRestaurante;

    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_resturante);

        //Optencion del Id del local escaneado
        IdRestaurante = getIntent().getStringExtra("idRestaurante");

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mImagenLocal = findViewById(R.id.imagenLocal);
        mCalificacionLocal = findViewById(R.id.calificacionLocal);
        mReservar = findViewById(R.id.reservar);
        mRestaurante = findViewById(R.id.nombreRestauranteVistaRestaurante);
        mTipoRestaurante = findViewById(R.id.tipoRestauranteVistaRestaurante);

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoComentarios);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        colocacionInformacion(IdRestaurante);

        String nombreRestaurante = mRestaurante.getText().toString();
        Toast.makeText(getApplicationContext(),nombreRestaurante, Toast.LENGTH_LONG).show();

        //Coloca los comentarios
        recycleView(nombreRestaurante);

    }

    private void recycleView(String nombreRestaurante) {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("comentario").whereEqualTo("nombreDelLocalComentado", nombreRestaurante);


        FirestoreRecyclerOptions<ComentarioPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                        .Builder<ComentarioPojo>()
                        .setQuery(query, ComentarioPojo.class).build();

        mAdapter = new ComentarioAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void colocacionInformacion(String mIdRestaurante){

        db.collection("restaurante").document(mIdRestaurante).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mRestaurante.setText(documentSnapshot.get("nombreLocal").toString());
                    mTipoRestaurante.setText(documentSnapshot.get("tipoRestaurante").toString());
                } else {
                }
            }
        });
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
}
