package com.example.easytable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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


    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_resturante);


        //Optencion del Id del local escaneado y de su nombre
        String IdRestaurante, nombreRestaurante;
        IdRestaurante = getIntent().getStringExtra("idRestaurante");
        nombreRestaurante = getIntent().getStringExtra("nombreRestaurante");


        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mImagenLocal = findViewById(R.id.imagenLocal);
        mCalificacionLocal = findViewById(R.id.calificacionLocal);
        mReservar = findViewById(R.id.reservarButton);
        mRestaurante = findViewById(R.id.nombreRestauranteVistaRestaurante);
        mTipoRestaurante = findViewById(R.id.tipoRestauranteVistaRestaurante);

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoComentarios);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        //Funcion para obtener los datos del restaurante con su ID
        colocacionInformacion(IdRestaurante);

        //Coloca los comentarios
        recycleView(nombreRestaurante);


        mReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Restaurante.this, ApartarLugarUC.class);
                startActivity(i);
            }
        });
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
            @SuppressLint({"SetTextI18n"})
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String cali =  documentSnapshot.get("calificacion").toString();
                    String usuariosCali = documentSnapshot.get("usuariosCalificacion").toString();

                    float calificacion = Float.parseFloat(cali);
                    float usuariosCalificacion =Float.parseFloat(usuariosCali);

                    float calificaciones = calificacion/usuariosCalificacion;

                    switch ((0 <= calificaciones && calificaciones < 1)?0 : (1 <= calificaciones && calificaciones < 2)?1 :
                            (2 <= calificaciones && calificaciones < 3)?2 : (3 <= calificaciones && calificaciones < 4)?3 :
                                    (4 <= calificaciones && calificaciones < 5)?4 : (calificaciones == 5)?5 : 6) {
                        case 0:
                            mCalificacionLocal.setImageResource(R.drawable.__00estrellas);
                            break;
                        case 1:
                            mCalificacionLocal.setImageResource(R.drawable.__10estrellas);
                            break;
                        case 2:
                            mCalificacionLocal.setImageResource(R.drawable.__20estrellas);
                            break;
                        case 3:
                            mCalificacionLocal.setImageResource(R.drawable.__30estrellas);
                            break;
                        case 4:
                            mCalificacionLocal.setImageResource(R.drawable.__40estrellas);
                            break;
                        case 5:
                            mCalificacionLocal.setImageResource(R.drawable.__50estrellas);
                            break;
                        case 6:
                            mCalificacionLocal.setImageResource(R.drawable.__05estrellas);
                            break;
                    }
                    mRestaurante.setText(documentSnapshot.get("nombreLocal").toString());
                    mTipoRestaurante.setText(documentSnapshot.get("tipoRestaurante").toString());
                } else {
                    Toast.makeText(Restaurante.this, "Error, restaurante no encontrado", Toast.LENGTH_SHORT).show();
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
