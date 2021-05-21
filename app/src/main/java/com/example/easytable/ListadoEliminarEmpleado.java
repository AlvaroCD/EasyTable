package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListadoEliminarEmpleado extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private EmpleadosAdapter mEmpleadosAdapter;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_listado_eliminar_empleado);

        //Instanciacion del Recycler View
        mRecyclerView = findViewById(R.id.recyclerViewListadoEmpleados);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Coloca los empleados
        recyclerViewEmpleados();

        //Funcion que determina que accion se realiza cuando se hace click en algun empleado
        onClickEmpleado();

    }

    private void recyclerViewEmpleados() {
        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");
        //Consulta para obtener los datos de la BD
        Query query = db.collection("usuario").whereEqualTo("IDRestReg", idRestaurante)
                .whereNotEqualTo("tipoDeUsuario", "Dueño del Local");

        FirestoreRecyclerOptions<EmpleadosPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<EmpleadosPojo>()
                .setQuery(query, EmpleadosPojo.class).build();

        mEmpleadosAdapter = new EmpleadosAdapter(firestoreRecyclerOptions);
        mEmpleadosAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mEmpleadosAdapter);
    }

    private void onClickEmpleado() {
        mEmpleadosAdapter.setOnItemClickListener(new EmpleadosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                String ID = documentSnapshot.getId();
                mostrarDialogo(ID);
            }
        });
    }

    private void mostrarDialogo(String ID){
        new AlertDialog.Builder(this)
                .setMessage("¿Seguro que quieres eliminar a este empleado?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("usuario").document(ID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ListadoEliminarEmpleado.this, "Empleado eliminado", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ListadoEliminarEmpleado.this, "Error al eliminar empleado", Toast.LENGTH_SHORT).show();
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

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mEmpleadosAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mEmpleadosAdapter.stopListening();
    }

}