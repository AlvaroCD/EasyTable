package com.example.easytable;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private FirebaseAuth mAuth;


    //Creacion de las KEYS necesarias para ingresar los datos dentro del la estructura HashMap
    private static final String KEY_MESA = "mesa";
    private static final String KEY_MONTOPAGAR = "montoPagar";
    private static final String KEY_ID = "id";
    private static final String KEY_IDUSUARIO = "idPrincipal";
    private static final String KEY_MATRIZUSUARIOS = "matrizUsuarios";
    private static final String KEY_STATUSCUENTA = "pagado";
    private static final String KEY_METODOPAGO = "efectivo";
    private static final String KEY_FECHA = "fecha";

    private static final String KEY_ID_CUENTA = "idCuenta";

    private static final String KEY_ID_LOCAL = "idDelLocal";
    private static final String KEY_STATUSORDEN = "ordenTerminadaPedir";
    private static final String KEY_STATUSPREPARACION = "statusPreparacion";

    ArrayList<String> platillos = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_menu);


        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Obtención de los datos de la vista PrincipalUC
        String nombreRestaurante = getIntent().getStringExtra("nombreDelLocal");
        String idDelLocal = getIntent().getStringExtra("idRestaurante");
        String idMesa = getIntent().getStringExtra("idMesa");
        String idOrden = getIntent().getStringExtra("idOrden");
        boolean statusMesa = getIntent().getBooleanExtra("estado", true);

        //Obtención de los datos de la vista Orden
        boolean statusOrden = getIntent().getBooleanExtra("statusOrden", false);

        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        //Aqui se crea un Id con la propiedad random para prevenir que los identificadores de los usuarios se repitan
        String idUsuario = mAuth.getUid();
        String idCuenta = date + "-" + idUsuario + "-" + idMesa;

        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mNombreLocal = findViewById(R.id.nombreLocal);

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoPlatillos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNombreLocal.setText(nombreRestaurante);

        recycleView(idDelLocal);

        CreacionCuenta(statusMesa, statusOrden ,idMesa, idCuenta, idOrden ,date, idDelLocal, idUsuario);


        //Funcion que determina que accion se realiza cuando se hace click en algun platillo
        onClickPlatillo(idOrden, idMesa, idDelLocal, idCuenta);
    }

    private void recycleView(String idDelLocal) {

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

    private void CreacionCuenta(boolean status, boolean ordenTerminada ,String idMesa, String idCuenta,
                                String idOrden ,String date, String idDelLocal, String idUsuario) {

        //creacion de cuenta si la mesa esta vacia
        if (status){

            //Se crea una estructura de datos HashMap para poder guardar los datos de la orden
            Map<String, Object> orden = new HashMap<>();

            //Se ingresan los datos en la estructura HashMap
            orden.put(KEY_MESA, idMesa);
            orden.put(KEY_ID_LOCAL, idDelLocal);
            orden.put(KEY_STATUSORDEN, false);
            orden.put(KEY_STATUSPREPARACION, 0);
            orden.put(KEY_IDUSUARIO, idUsuario);
            orden.put(KEY_ID_CUENTA, idCuenta);
            Toast.makeText(this, idMesa, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, idDelLocal, Toast.LENGTH_SHORT).show();
            db.collection("orden").document(idOrden).set(orden)
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



            //Se crea una estructura de datos HashMap para poder guardar los datos de la cuenta
            Map<String, Object> cuenta = new HashMap<>();

            //Se ingresan los datos en la estructura HashMap
            cuenta.put(KEY_MESA, idMesa);
            cuenta.put(KEY_MONTOPAGAR, 0);
            cuenta.put(KEY_ID, idCuenta);
            cuenta.put(KEY_IDUSUARIO, idUsuario);
            cuenta.put(KEY_METODOPAGO, false);
            cuenta.put(KEY_STATUSCUENTA, false);
            cuenta.put(KEY_FECHA, date);
            cuenta.put(KEY_ID_LOCAL, idDelLocal);
            cuenta.put(KEY_MATRIZUSUARIOS, Collections.emptyList());

            db.collection("cuenta").document(idCuenta).set(cuenta)
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

            final DocumentReference statusMesaCambio = db.collection("mesa").document(idMesa);
            statusMesaCambio.update("statusMesa", false);
        }

        else{
            //si la mesa no esta vacia y aun no mandan la comanda
            if (!ordenTerminada){
                final DocumentReference doc = db.collection("orden").document(idOrden);
                doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        //platillos = (ArrayList<String>) value.;
                        List<Map<String, Object>> users = (List<Map<String, Object>>) value.get("matrizPlatillos");
                        Toast.makeText(MenuLocal.this, platillos.get(0), Toast.LENGTH_SHORT).show();
                    }
                });
                //Toast.makeText(this,"ya hay un usuario", Toast.LENGTH_LONG).show();

            }
            //si la mesa no esta vacia y ya mandanron la comanda a la cocina
            else {

            }

        }

    }

    private void onClickPlatillo(String idOrden, String idMesa, String idDelLocal, String idCuenta) {

        mAdapter.setOnItemClickListener(new PlatilloAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {

                String nombrePlatillo = documentSnapshot.get("nombrePlatillo").toString();
                String precio = documentSnapshot.get("precio").toString();
                String idPlatillo = documentSnapshot.getId();
                boolean disponibilidadPlatillo = documentSnapshot.getBoolean("disponibilidad");

                Intent i = new Intent(MenuLocal.this, Orden.class);

                i.putExtra("nombrePlatillo", nombrePlatillo);
                i.putExtra("precio", precio);
                i.putExtra("idPlatillo", idPlatillo);
                i.putExtra("idOrden", idOrden);
                i.putExtra("idRestaurante",idDelLocal);
                i.putExtra("idMesa", idMesa);
                i.putExtra("idCuenta", idCuenta);
                i.putExtra("disponibilidadPlatillo", disponibilidadPlatillo);

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

    //Metodo para que cuando el usuario no esté dentro de la a  plicacion, la aplicación deje de actualizar los datos de la misma (datos de los Platillos     )
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}

