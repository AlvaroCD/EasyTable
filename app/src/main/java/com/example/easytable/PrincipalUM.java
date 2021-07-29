package com.example.easytable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class PrincipalUM extends Activity {

    private RecyclerView mRecyclerView;
    private CuentaUMAdapter mAdapter;
    private Button mOrdenesTerminadas, mLogOut;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PrincipalUM.this));

        mOrdenesTerminadas = findViewById(R.id.ordenesTerminadasMeseroButton);
        mLogOut = findViewById(R.id.LogOutButtonMesero);

        //Coloca los restaurantes
        recyclerViewRestaurante();

        onClickCuenta();

        String idUsuarioLogueado = mAuth.getUid();
        DocumentReference doc = db.collection("usuario").document(idUsuarioLogueado);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String idRestaurante = value.get("IDRestReg").toString();

                mOrdenesTerminadas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PrincipalUM.this, ListadoOrdenesTerminadas.class);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });

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
        });

    }

    private void onClickCuenta() {
        mAdapter.setOnItemClickListener(new CuentaUMAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                String idUsuario = mAuth.getUid();
                DocumentReference doc = db.collection("usuario").document(idUsuario);
                doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String cuenta = value.getString("Cuenta");
                        String idRestaurante = value.getString("IDRestReg");
                        Intent i = new Intent(PrincipalUM.this, MenuLocalMU.class);
                        i.putExtra("idCuenta", cuenta);
                        i.putExtra("idRestaurante", idRestaurante);
                        startActivity(i);
                    }
                });
            }
        });
    }


    private void recyclerViewRestaurante() {
        String idMesero = mAuth.getUid();
        Toast.makeText(PrincipalUM.this, idMesero, Toast.LENGTH_LONG).show();
        //Consulta para obtener los datos de la BD
        Query query = db.collection("usuario").whereEqualTo("ID", idMesero);

        FirestoreRecyclerOptions<MeserosPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<MeserosPojo>()
                .setQuery(query, MeserosPojo.class).build();

        mAdapter = new CuentaUMAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
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
