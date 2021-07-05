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

import java.util.HashMap;
import java.util.Map;

public class PrincipalUM extends Activity {

    private RecyclerView mRecyclerView;
    private CuentaUMAdapter mAdapter;
    private Button mLogOut;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal_usuario_mesero);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoCuentasUM);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mLogOut = findViewById(R.id.LogOutButtonMesero);

        //Coloca los restaurantes
        recyclerViewRestaurante();

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mAuth.getUid();
                Map<String, Object> online = new HashMap<>();
                online.put("online", false);
                db.collection("usuario").document(id).update(online);
                mAuth.signOut();
                finish();
            }
        });

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

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
