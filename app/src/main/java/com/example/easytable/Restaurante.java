package com.example.easytable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Restaurante extends Activity {
    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerView;
    private ComentarioAdapter mAdapter;
    private ImageView mImagenLocal, mCalificacionLocal;
    private Button mReservar, mCancelar;
    private TextView mRestaurante, mTipoRestaurante, mDireccionRestaurante;


    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_resturante);


        //Obtencion del Id del local escaneado y de su nombre
        String IdRestaurante, nombreRestaurante;
        IdRestaurante = getIntent().getStringExtra("idRestaurante");
        nombreRestaurante = getIntent().getStringExtra("nombreRestaurante");


        //Relacion e inicialización de las variables con los identificadores (id's) de la parte grafica (xml)
        mImagenLocal = findViewById(R.id.imagenLocal);
        mCalificacionLocal = findViewById(R.id.calificacionLocal);
        mReservar = findViewById(R.id.reservar);
        mCancelar =findViewById(R.id.cancelar);
        mRestaurante = findViewById(R.id.nombreRestauranteVistaRestaurante);
        mTipoRestaurante = findViewById(R.id.tipoRestauranteVistaRestaurante);
        mDireccionRestaurante = findViewById(R.id.direcionRestauranteVistaRestaurante);

        //Instanciacion del Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListadoComentarios);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Funcion para obtener los datos del restaurante con su ID
        colocacionInformacion(IdRestaurante);

        //Coloca los comentarios
        recycleView(nombreRestaurante);



        String idLogueado = mAuth.getUid();

        cancelar(IdRestaurante, idLogueado);

        botonReservar(IdRestaurante, nombreRestaurante, idLogueado);

        mCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(Restaurante.this)
                        .setTitle("Cancelacion")
                        .setMessage("Estas seguro que deseas cancelar, tendras una multa de $25 MNX")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Query query = db.collection("reservacion")
                                        .whereEqualTo("idUsuario", idLogueado);

                                db.collection("reservaciones").document(query.toString()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Modificacion en datos del usuario para que vuelva a reservar
                                                Map<String, Object> editarReservacion = new HashMap<>();

                                                editarReservacion.put("Adeudo", true);
                                                editarReservacion.put("Reserva", false);

                                                db.collection("usuario").document(idLogueado).update(editarReservacion)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                            }
                                                        });

                                                Toast.makeText(Restaurante.this, "Cancelacion realizada", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Restaurante.this, "Error al eliminar empleado", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    private void botonReservar(String idRestaurante, String nombreRestaurante, String idLogueado) {
        mReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                DocumentReference doc = db.collection("usuario").document(idLogueado);
                doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        boolean adeudo = value.getBoolean("Adeudo");
                        boolean reserva = value.getBoolean("Reserva");
                        String usuario = value.getString("Usuario");

                        if (!reserva) {
                            if (!adeudo) {
                                //Consulta para obtener que mesas estan disponibles en el local seleccionado
                                Query query = db.collection("mesa").whereEqualTo("nombreDelLocal", nombreRestaurante)
                                        .whereEqualTo("statusMesa", false);


                                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        //Si la consulta contiene un elemento o mas, es posible reservar
                                        if (queryDocumentSnapshots.size() >= 1) {
                                            Intent intent = new Intent(Restaurante.this, ApartarLugar.class);
                                            intent.putExtra("usuario",usuario);
                                            intent.putExtra("idRestaurante", idRestaurante);
                                            intent.putExtra("idLogueado", idLogueado);
                                            startActivity(intent);
                                        }
                                        //De lo contrario aparecera un mensaje que indica que no disponibilidad
                                        else {
                                            new AlertDialog.Builder(Restaurante.this)
                                                    .setMessage("No hay mesas disponibles")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    .show();
                                        }
                                    }
                                });
                            } else {
                                new AlertDialog.Builder(Restaurante.this)
                                        .setTitle("Cuota pendiente")
                                        .setMessage("Realiza el pago de tu adeudo")
                                        .setPositiveButton("Pagar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
//TODO: Aqui se debe de poder ingresar a paypal para pagar los $25
                                                Intent i = new Intent(Restaurante.this, PagarAdeudo.class);
                                                i.putExtra("idRestaurante", idRestaurante);
                                                startActivity(i);
                                            }
                                        })
                                        .show();
                            }
                        }else {
                            Toast.makeText(Restaurante.this, "Ya tienes una reservacion hecha", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }
        });
    }

    private void cancelar(String idRestaurante, String idUsuario) {

        //Consulta para obtener si hay reservaciones que aun se puedan cancelar
        Query query = db.collection("reservaciones").whereEqualTo("idUsuario", idUsuario)
                .whereEqualTo("statusReservacion", 1);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //Si la consulta contiene un elemento o mas, es posible reservar
                if (queryDocumentSnapshots.size() >= 1) {
                    mReservar.setVisibility(View.INVISIBLE);
                    mCancelar.setVisibility(View.VISIBLE);
                }
                //De lo contrario aparecera un mensaje que indica que no disponibilidad
                else {

                    mReservar.setVisibility(View.VISIBLE);
                    mCancelar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void recycleView(String nombreRestaurante) {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("comentario").whereEqualTo("nombreDelLocalComentado", nombreRestaurante);


        FirestoreRecyclerOptions<ComentarioPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                        .Builder<ComentarioPojo>()
                        .setQuery(query, ComentarioPojo.class).build();

        mAdapter = new ComentarioAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void colocacionInformacion(String mIdRestaurante){

        db.collection("restaurante").document(mIdRestaurante).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint({"SetTextI18n"})
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String cali =  documentSnapshot.get("calificacion").toString();
                    String usuariosCali = documentSnapshot.get("usuariosCalificacion").toString();

                    float calificacion = Float.parseFloat(cali);
                    float usuariosCalificacion =Float.parseFloat(usuariosCali);

                    float calificaciones = calificacion/usuariosCalificacion;

                    switch ((0 <= calificaciones && calificaciones < 1)?0 : (1 <= calificaciones && calificaciones < 2)?1 :
                            (2 <= calificaciones && calificaciones < 3)?2 : (3 <= calificaciones && calificaciones < 4)?3 :
                                    (4 <= calificaciones && calificaciones < 5)?4 : (calificaciones == 5)?5 : 6) {
                        case 0:
                            mCalificacionLocal.setImageResource(R.drawable.__00estrellas);
                            break;
                        case 1:
                            mCalificacionLocal.setImageResource(R.drawable.__10estrellas);
                            break;
                        case 2:
                            mCalificacionLocal.setImageResource(R.drawable.__20estrellas);
                            break;
                        case 3:
                            mCalificacionLocal.setImageResource(R.drawable.__30estrellas);
                            break;
                        case 4:
                            mCalificacionLocal.setImageResource(R.drawable.__40estrellas);
                            break;
                        case 5:
                            mCalificacionLocal.setImageResource(R.drawable.__50estrellas);
                            break;
                        case 6:
                            mCalificacionLocal.setImageResource(R.drawable.__05estrellas);
                            break;
                    }
                    mRestaurante.setText(documentSnapshot.get("nombreLocal").toString());
                    mTipoRestaurante.setText(documentSnapshot.get("tipoRestaurante").toString());
                    mDireccionRestaurante.setText(documentSnapshot.get("direccion").toString());
                } else {
                    Toast.makeText(Restaurante.this, "Error, restaurante no encontrado", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
