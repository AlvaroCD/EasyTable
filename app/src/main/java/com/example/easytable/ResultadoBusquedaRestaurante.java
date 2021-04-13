package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ResultadoBusquedaRestaurante extends AppCompatActivity {

    private RecyclerView mRecyclerResultadoBusqueda;

    private RestaurantesAdapter mAdapter;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busqueda_restaurante);


        mRecyclerResultadoBusqueda = findViewById(R.id.recyclerViewRestauranteEncontrado);
        mRecyclerResultadoBusqueda.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        Bundle extra = getIntent().getExtras();
        String nombreLocalBuscado = extra.getString("restauranteBuscado");
        String categoriaLocalBuscado = extra.getString("categoriaRestauranteBuscado");

        //Coloca los restaurante
        recyclerViewRestauranteEncontrado(nombreLocalBuscado, categoriaLocalBuscado);

    }


    private void recyclerViewRestauranteEncontrado(String nombreLocalBuscado, String categoriaLocalBuscado){
        //Consulta para obtener los datos de la BD
        Query query = db.collection("restaurante").whereEqualTo("nombreLocal", nombreLocalBuscado)
                .whereEqualTo("tipoRestaurante", categoriaLocalBuscado);

        FirestoreRecyclerOptions<RestaurantePojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<RestaurantePojo>()
                .setQuery(query, RestaurantePojo.class).build();

        mAdapter = new RestaurantesAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerResultadoBusqueda.setAdapter(mAdapter);
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