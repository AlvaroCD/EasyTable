package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListadoEliminarRecursos extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private RecursosAdapter mRecursosAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_listado_eliminar_recursos);

        //Instanciacion del Recycler View
        mRecyclerView = findViewById(R.id.recyclerViewListadoEliminarRecursos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Coloca los recursos
        recyclerViewRecursos();

        //Funcion que determina que accion se realiza cuando se hace click en algun recurso
        onClickRecurso();
    }

    private void recyclerViewRecursos() {
        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");
        //Consulta para obtener los datos de la BD
        Query query = db.collection("listaDeRecursos").whereEqualTo("idDelLocal", idRestaurante);

        FirestoreRecyclerOptions<RecursosPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<RecursosPojo>()
                .setQuery(query, RecursosPojo.class).build();

        mRecursosAdapter = new RecursosAdapter(firestoreRecyclerOptions);
        mRecursosAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mRecursosAdapter);
    }

    private void onClickRecurso() {
        mRecursosAdapter.setOnItemClickListener(new RecursosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                String ID = documentSnapshot.getId();
                mostrarDialogo(ID);
            }
        });
    }

    private void mostrarDialogo(String ID) {
        new AlertDialog.Builder(this)
                .setMessage("¿Seguro que quieres eliminar este ingrediente?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("listaDeRecursos").document(ID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ListadoEliminarRecursos.this, "Recurso eliminado", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ListadoEliminarRecursos.this, "Error al eliminar recurso", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Mensaje", "Se cancelo la accion");
                    }
                })
                .show();
    }


    public void onStart() {
        super.onStart();
        mRecursosAdapter.startListening();
    }

    public void onStop() {
        super.onStop();
        mRecursosAdapter.startListening();
    }

}