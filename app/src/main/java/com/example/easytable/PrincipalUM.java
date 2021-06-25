package com.example.easytable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PrincipalUM extends Activity {

    private RecyclerView mRecyclerView;
    private RestaurantesAdapter mAdapter;
    private ImageButton  mIniciarCuenta;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modelo_mesas_mesero);


//        mIniciarCuenta.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PrincipalUM.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
        db = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.recyclerViewListadoCuentasUM);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Coloca los restaurantes
        recyclerViewRestaurante();


    }

    private void recyclerViewRestaurante() {
        //Consulta para obtener los datos de la BD
       /* Query query = db.collection("usuario");

        FirestoreRecyclerOptions<MeserosPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<MeserosPojo>()
                .setQuery(query, MeserosPojo.class).build();

        mAdapter = new RestaurantesAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);*/
    }
}
