package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Ventas extends AppCompatActivity {

    private TextView mTotalVentas;
    private FirebaseFirestore db;
    private RecyclerView mRecyclerView;
    private VentasAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_ventas);

        mTotalVentas = findViewById(R.id.totalVentas);
        db = FirebaseFirestore.getInstance();

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoVentasx);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");

        recyclerViewVentas(idRestaurante);

        Query doc = db.collection("cuenta").whereEqualTo("idDelLocal", idRestaurante);
        doc.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        DocumentReference docRef = db.collection("cuenta").document(document.getId());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists())
                                        Toast.makeText(Ventas.this, document.getId()+ "    :->           "
                                                +document.get("montoPagar").toString(), Toast.LENGTH_SHORT).show();
                                    else {
                                    }

                                } else {
                                }

                            }
                        });
                    }
                }
            }
        });
    }


    private void recyclerViewVentas(String idRestaurante) {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("cuenta").whereEqualTo("pagado", true).whereEqualTo("idDelLocal", idRestaurante);

        FirestoreRecyclerOptions<VentasPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<VentasPojo>()
                .setQuery(query, VentasPojo.class).build();

        mAdapter = new VentasAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }


    //Metodo para que que  cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los restaurantes)
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