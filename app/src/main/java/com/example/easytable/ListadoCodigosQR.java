package com.example.easytable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListadoCodigosQR extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MesaAdapter mMesaAdapter;


    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_listado_codigos_qr);
        //Instanciacion del Recycler View
        mRecyclerView = findViewById(R.id.recyclerViewListadoMesas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Coloca las mesas
        recyclerViewMesas();
        onClickMesa();
    }

    private void recyclerViewMesas() {
        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");
        //Consulta para obtener los datos de la BD
        Query query = db.collection("mesa").whereEqualTo("idDelLocal", idRestaurante);

        FirestoreRecyclerOptions<MesaPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<MesaPojo>()
                .setQuery(query, MesaPojo.class).build();

        mMesaAdapter = new MesaAdapter(firestoreRecyclerOptions);
        mMesaAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mMesaAdapter);
    }

    private void onClickMesa() {
        mMesaAdapter.setOnItemClickListener(new MesaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                MesaPojo mesa = documentSnapshot.toObject(MesaPojo.class);
                String id = documentSnapshot.getId();
                Intent i = new Intent(ListadoCodigosQR.this, GenerarQR.class);
                i.putExtra("idMesa", id);
                startActivity(i);
            }
        });

    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
        @Override
        protected void onStart(){
            super.onStart();
            mMesaAdapter.startListening();
        }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mMesaAdapter.stopListening();
    }
}

