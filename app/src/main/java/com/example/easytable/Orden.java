package com.example.easytable;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Orden extends Activity {
    private TextView mNombrePlatillo, mCostoToltal;
    private Button mOrden, mPagar, mQueja, mAnadir;
    private ImageButton mEliminarPlatillo;
    private EditText mComentarioEspecifico;
    private RecyclerView mRecyclerView;
    private PlatilloAdapter mAdapter;

    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_orden);

        String  nombrePlatillo;
        nombrePlatillo = getIntent().getStringExtra("nombrePlatillo");

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mNombrePlatillo = findViewById(R.id.nombrePlatillo);
        mCostoToltal = findViewById(R.id.costoPlatillo);
        mOrden = findViewById(R.id.ordenar);
        mPagar = findViewById(R.id.pagar);
        mQueja = findViewById(R.id.queja);
        mAnadir = findViewById(R.id.añadir);
        mEliminarPlatillo = findViewById(R.id.eliminarPlatillo);
        mComentarioEspecifico = findViewById(R.id.comentarioEspecifico);


        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoOrden);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        mNombrePlatillo.setText(nombrePlatillo);

        mAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Orden.this, MenuLocal.class);
                intent.putExtra("statusOrden", false);
                intent.putExtra("idRestaurante",nombreRestaurante);
                intent.putExtra("idMesa", idMesa);
                startActivity(intent);
            }
        });
        recycleView(nombrePlatillo);



        mQueja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Orden.this, Queja.class));
            }
        });



    }

    private void recycleView(String nombrePlatillo) {

        //Consulta para obtener los datos de la BD
        Query query = db.collection("platillos").whereEqualTo("nombrePlatillo", nombrePlatillo);

        FirestoreRecyclerOptions<PlatilloPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<PlatilloPojo>()
                .setQuery(query, PlatilloPojo.class).build();

        mAdapter = new PlatilloAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los Platillos)
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los Platillos     )
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
