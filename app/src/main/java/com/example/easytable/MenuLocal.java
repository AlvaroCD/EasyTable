package com.example.easytable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuLocal extends Activity {
    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private PlatilloAdapter mAdapter;
    private TextView mNombreLocal;
    boolean status;
    private String nombreRestaurante;


    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;


    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap
    private static final String KEY_MESA = "mesa";
    private static final String KEY_MONTOPAGAR = "montoPagar";
    private static final String KEY_ID = "id";
    private static final String KEY_IDUSUARIO = "idPrincipal";
    private static final String KEY_USUARIO = "usuarios";
    private static final String KEY_MATRIZUSUARIOS = "matriz";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_menu);

        //Optencion del Id del local escaneado y de su nombre
        String  idMesa;
        boolean status;
        idMesa = getIntent().getStringExtra("idRestaurante");
        status = getIntent().getBooleanExtra("estado",true);
        //Aqui se crea un Id con la propiedad random para prevenir que los identificadores de los usuarios se repitan
        String idCuenta = UUID.randomUUID().toString();


        //creacion de cuenta si la mesa esta vacia
        if (status){
           /* Map<String, Object> city = new HashMap<>();
            city.put("name", "Los Angeles");
            city.put("state", "CA");
            city.put("country", "USA");

            db.collection("cuenta").document("LA")
                    .set(city)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });*/

            //Se crea una estructura de datos HashMap para poder guardar los datos ingresados por el usuario

            Map<String, Object> cuenta = new HashMap<>();

            //Se ingresan los datos en la estructura HashMap llamada "user"
            cuenta.put(KEY_MESA, idMesa);
            cuenta.put(KEY_MONTOPAGAR, 0);
            cuenta.put(KEY_ID, idCuenta);

            //sub raiz, donde se guardan los usuarios que participan en la cuenta
            Map<String, Object> usuarios = new HashMap<>();

            //usuarios.put(KEY_IDUSUARIO, idUsuario);
            usuarios.put(KEY_MATRIZUSUARIOS, Collections.emptyList());

            cuenta.put(KEY_USUARIO, usuarios);
            db.collection("cuenta").document(idCuenta).collection("usuarios").document(idCuenta).set(cuenta)

                    //Listener que indica si la creacion del usuario fue correcta (es similar a un try-catch)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    //Listener que indica si la creacion del usuario fue incorrecta
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MenuLocal.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });


        }else {
            Toast.makeText(this,"ya hay un usuario", Toast.LENGTH_LONG).show();
        }

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mNombreLocal = findViewById(R.id.nombreLocal);

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoPlatillos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        mNombreLocal.setText(nombreRestaurante);

        recycleView(nombreRestaurante);

        //Funcion que determina que accion se realiza cuando se hace click en algun platillo
        onClickPlatillo();
    }

    private void recycleView(String nombreRestaurante) {

        //Consulta para obtener los datos de la BD
        Query query = db.collection("platillos").whereEqualTo("nombreDelLocal", nombreRestaurante);

        FirestoreRecyclerOptions<PlatilloPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<PlatilloPojo>()
                .setQuery(query, PlatilloPojo.class).build();

        mAdapter = new PlatilloAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void onClickPlatillo() {
        mAdapter.setOnItemClickListener(new PlatilloAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                PlatilloPojo platillo = documentSnapshot.toObject(PlatilloPojo.class);
                String id = documentSnapshot.getId();
                String nombrePlatillo = documentSnapshot.get("nombrePlatillo").toString();
                Intent i = new Intent(MenuLocal.this, Orden.class);
                i.putExtra("idPlatillo",id);
                i.putExtra("nombrePlatillo", nombrePlatillo);
                startActivity(i);
            }
        });
    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los Platillos)
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los Platillos     )
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}

