package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListadoEditarEmpleado extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private EmpleadosAdapter mEmpleadosAdapter;



    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_listado_editar_empleado);

        //Instanciacion del Recycler View
        mRecyclerView = findViewById(R.id.recyclerViewListadoEmpleados);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Coloca los empleados
        recyclerViewEmpleados();

        //Funcion que determina que accion se realiza cuando se hace click en algun empleado
        onClickEmpleado();

    }

    private void recyclerViewEmpleados() {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("usuario");

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
                EmpleadosPojo empleado = documentSnapshot.toObject(EmpleadosPojo.class);
                Intent i = new Intent(ListadoEditarEmpleado.this, EditarEmpleado.class);
                String nombreEmpleado = documentSnapshot.get("Nombre").toString();
                String apellidoEmpleado = documentSnapshot.get("Apellidos").toString();
                String telefonoEmpleado = documentSnapshot.get("Telefono").toString();
                String correoEmpleado = documentSnapshot.get("Correo").toString();
                String usuarioEmpleado = documentSnapshot.get("Usuario").toString();
                String tipoUsuarioEmpleado = documentSnapshot.get("tipoDeUsuario").toString();
                String ID = documentSnapshot.getId();
                i.putExtra("nombre",nombreEmpleado);
                i.putExtra("apellido",apellidoEmpleado);
                i.putExtra("telefono",telefonoEmpleado);
                i.putExtra("correo",correoEmpleado);
                i.putExtra("usuario",usuarioEmpleado);
                i.putExtra("tipoUsuario", tipoUsuarioEmpleado);
                i.putExtra("id", ID);
                startActivity(i);
            }
        });

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