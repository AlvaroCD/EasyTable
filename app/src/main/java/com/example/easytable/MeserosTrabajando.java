package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MeserosTrabajando extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private MeserosAdapter mMeserosAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_meseros_trabajando);

        //Instanciacion del Recycler View
        mRecyclerView = findViewById(R.id.recyclerViewMeserosTrabajando);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Coloca los empleados
        recyclerViewMeseros();
    }

    private void recyclerViewMeseros() {
        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");
        //Consulta para obtener los datos de la BD
        Query query = db.collection("usuario").whereEqualTo("IDRestReg", idRestaurante)
                .whereEqualTo("tipoDeUsuario", "Mesero").whereEqualTo("online", true);

        FirestoreRecyclerOptions<MeserosPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<MeserosPojo>()
                .setQuery(query, MeserosPojo.class).build();

        mMeserosAdapter = new MeserosAdapter(firestoreRecyclerOptions);
        mMeserosAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mMeserosAdapter);
    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mMeserosAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mMeserosAdapter.stopListening();
    }
}