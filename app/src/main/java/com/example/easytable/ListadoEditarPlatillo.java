package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListadoEditarPlatillo extends AppCompatActivity {

    private FirebaseFirestore db;

    private RecyclerView mRecyclerView;
    private PlatilloAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_listado_editar_platillo);

        db = FirebaseFirestore.getInstance();

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoPlatillos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String idDelLocal = getIntent().getStringExtra("idRestaurante");

        recyclerView(idDelLocal);

        onClickPlatillo();

    }

    private void onClickPlatillo() {
        mAdapter.setOnItemClickListener(new PlatilloAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                String id = documentSnapshot.getId();
                String nombre = documentSnapshot.getString("nombrePlatillo");
                String descripcion = documentSnapshot.getString("descripcion");
                long precio = documentSnapshot.getLong("precio");
                boolean disponibilidad = documentSnapshot.getBoolean("disponibilidad");
                Intent i = new Intent(ListadoEditarPlatillo.this, EditarPlatillo.class);
                i.putExtra("idPlatillo", id);
                i.putExtra("nombrePlatillo", nombre);
                i.putExtra("descripcion", descripcion);
                i.putExtra("precio", precio);
                i.putExtra("disponibilidad", disponibilidad);
                startActivity(i);
            }
        });
    }

    public void recyclerView(String idDelLocal) {

        //Consulta para obtener los datos de la BD
        Query query = db.collection("platillos").whereEqualTo("idDelLocal", idDelLocal);

        FirestoreRecyclerOptions<PlatilloPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<PlatilloPojo>()
                .setQuery(query, PlatilloPojo.class).build();

        mAdapter = new PlatilloAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        Toast.makeText(this, idDelLocal, Toast.LENGTH_SHORT).show();
    }


    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los Platillos)
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la a  plicacion, la aplicación deje de actualizar los datos de la misma (datos de los Platillos     )
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

}