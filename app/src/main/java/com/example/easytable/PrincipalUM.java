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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PrincipalUM extends Activity {

    private RecyclerView mRecyclerView;
    private CuentaUMAdapter mAdapter;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modelo_mesas_mesero);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoCuentasUM);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Coloca los restaurantes
        recyclerViewRestaurante();


    }

    private void recyclerViewRestaurante() {
        String idMesero = mAuth.getUid();
        //Consulta para obtener los datos de la BD
        Query query = db.collection("usuario").whereEqualTo("ID", idMesero);

        FirestoreRecyclerOptions<MeserosPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<MeserosPojo>()
                .setQuery(query, MeserosPojo.class).build();

        mAdapter = new CuentaUMAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }
}
