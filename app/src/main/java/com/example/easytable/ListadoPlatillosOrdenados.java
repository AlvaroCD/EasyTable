//package com.example.easytable;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//
//public class ListadoPlatillosOrdenados extends AppCompatActivity {
//
//    private RecyclerView mRecyclerViewPlatillosOrdenados;
//    private ListadoPlatillosOrdenadosAdapter mPlatillosOrdenadosAdapter;
//
//    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
//    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.vista_listado_platillos_ordenados);
//
//        mRecyclerViewPlatillosOrdenados = findViewById(R.id.recyclerViewListadoPlatillosOrdenados);
//        mRecyclerViewPlatillosOrdenados.setLayoutManager(new LinearLayoutManager(this));
//
//        //Coloca los platillos
//        recyclerViewPlatillosOrdenados();
//    }
//
//    private void recyclerViewPlatillosOrdenados() {
//        //Consulta para obtener los datos de la BD
//        Query query = db.collection("orden").whereEqualTo("mesa", "p5LATAuaqXza4cIKVn1W");
//
//        FirestoreRecyclerOptions<ListadoPlatillosOrdenadosPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ListadoPlatillosOrdenadosPojo>()
//                .setQuery(query, ListadoPlatillosOrdenadosPojo.class).build();
//
//        mPlatillosOrdenadosAdapter = new ListadoPlatillosOrdenadosAdapter(firestoreRecyclerOptions);
//        mPlatillosOrdenadosAdapter.notifyDataSetChanged();
//        mRecyclerViewPlatillosOrdenados.setAdapter(mPlatillosOrdenadosAdapter);
//    }
//
//    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mPlatillosOrdenadosAdapter.startListening();
//    }
//
//    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mPlatillosOrdenadosAdapter.stopListening();
//    }
//}