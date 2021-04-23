package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ResultadoBusquedaRestaurante extends AppCompatActivity {

    private RecyclerView mRecyclerResultadoBusqueda, mRecyclerResultadoBusqueda2;

    private RestaurantesAdapter mAdapter, mAdapter2;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busqueda_restaurante);


        mRecyclerResultadoBusqueda = findViewById(R.id.recyclerViewRestauranteEncontradoNombre);
        mRecyclerResultadoBusqueda.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerResultadoBusqueda2 = findViewById(R.id.recyclerViewRestauranteEncontradoTipoRestaurante);
        mRecyclerResultadoBusqueda2.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        Bundle extra = getIntent().getExtras();
        String nombreLocalBuscado = extra.getString("restauranteBuscado");
        String categoriaLocalBuscado = extra.getString("categoriaRestauranteBuscado");

        //Coloca los restaurante
        recyclerViewRestauranteEncontrado(nombreLocalBuscado, categoriaLocalBuscado);

    }


    private void recyclerViewRestauranteEncontrado(String nombreLocalBuscado, String categoriaLocalBuscado){
        //Consulta para obtener los datos de la BD
        Query query = db.collection("restaurante").whereEqualTo("nombreLocal", nombreLocalBuscado);

        FirestoreRecyclerOptions<RestaurantePojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<RestaurantePojo>()
                .setQuery(query, RestaurantePojo.class).build();

        mAdapter = new RestaurantesAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerResultadoBusqueda.setAdapter(mAdapter);

        Query query2 = db.collection("restaurante").whereEqualTo("tipoRestaurante", categoriaLocalBuscado);

        FirestoreRecyclerOptions<RestaurantePojo> firestoreRecyclerOptions2 = new FirestoreRecyclerOptions.Builder<RestaurantePojo>()
                .setQuery(query2, RestaurantePojo.class).build();



        mAdapter2 = new RestaurantesAdapter(firestoreRecyclerOptions2);
        mAdapter2.notifyDataSetChanged();
        mRecyclerResultadoBusqueda2.setAdapter(mAdapter2);

    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los restaurantes)
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
        mAdapter2.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los restaurantes)
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
        mAdapter2.stopListening();
    }

}