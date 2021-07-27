package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AsignarMesero extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MesaAdapter mMesaAdapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_asignar_mesero);

        //Instanciacion del Recycler View
        mRecyclerView = findViewById(R.id.recyclerViewListadoMesasDisponibles);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Coloca las mesas
        recyclerViewMesas();
        onClickMesa();
    }

    private void recyclerViewMesas() {
        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");
        //Consulta para obtener los datos de la BD
        Query query = db.collection("mesa").whereEqualTo("idDelLocal", idRestaurante)
                .whereEqualTo("statusMesa", false);

        FirestoreRecyclerOptions<MesaPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<MesaPojo>()
                .setQuery(query, MesaPojo.class).build();

        mMesaAdapter = new MesaAdapter(firestoreRecyclerOptions);
        mMesaAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mMesaAdapter);
    }

    private void onClickMesa() {
        mMesaAdapter.setOnItemClickListener(new MesaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                MesaPojo mesa = documentSnapshot.toObject(MesaPojo.class);
                String id = documentSnapshot.getId();
                mostrarDialogo(id);

            }
        });

    }


    private void mostrarDialogo(String id) {
        Bundle extra = getIntent().getExtras();
        String idRestaurante = extra.getString("idRestaurante");
        AlertDialog.Builder ventana = new AlertDialog.Builder(this);
                ventana.setTitle("Ingresa el nombre del cliente");

                final EditText ET_NOMBRE = new EditText(this);
                ventana.setView(ET_NOMBRE);

                ventana.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String nombre = ET_NOMBRE.getText().toString().trim();
                        if (nombre.length() == 0) {
                            nombre = "host";
                        }


                        //Insertar la accion de asignar un mesero
                        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                        //Aqui se crea un Id con la propiedad random para prevenir que los identificadores de los usuarios se repitan
                        String idCuenta = date + "-" + nombre + "-" + id;

                        String idOrden = UUID.randomUUID().toString();

                        //Se crea una estructura de datos HashMap para poder guardar los datos de la orden
                        Map<String, Object> orden = new HashMap<>();

                        //Se ingresan los datos en la estructura HashMap
                        orden.put("mesa", id);
                        orden.put("idDelLocal", idRestaurante);
                        orden.put("ordenTerminadaPedir", false);
                        orden.put("statusPreparacion", 0);
                        orden.put("idPincipal", Global.getmIdUsuario());
                        orden.put("idCuenta", idCuenta);
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
                                        Toast.makeText(AsignarMesero.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });



                        //Se crea una estructura de datos HashMap para poder guardar los datos de la cuenta
                        Map<String, Object> cuenta = new HashMap<>();

                        //Se ingresan los datos en la estructura HashMap
                        cuenta.put("mesa", id);
                        cuenta.put("montoPagar", 0);
                        cuenta.put("id", "10-07-2021-"+ nombre+"-mesa");
                        cuenta.put("idPrincipal", nombre);
                        cuenta.put("pagado", false);
                        cuenta.put("efectivo", false);
                        cuenta.put("fecha", "10-07-2021");
                        cuenta.put("idDelLocal", idRestaurante);

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
                                        Toast.makeText(AsignarMesero.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });



                        colocacionCuenta(idCuenta);





                        //Al asignar un mesero la mesa pasa automaticamente a estar ocupada, por lo que se actualiza el campo
                        //de la disponibilidad de la mesa
                        Map<String, Object> disponibilidadActualizada = new HashMap<>();
                        disponibilidadActualizada.put("statusMesa", true);
                        db.collection("mesa").document(id).update(disponibilidadActualizada)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AsignarMesero.this, "Good", Toast.LENGTH_SHORT).show();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AsignarMesero.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        Toast.makeText(AsignarMesero.this, "Mesero asignado", Toast.LENGTH_SHORT).show();
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

    private void colocacionCuenta(String idCuenta){

        Map<String, Object> disponibilidadActualizada = new HashMap<>();
        disponibilidadActualizada.put("Cuenta", idCuenta);
        db.collection("usuario").document("SYk8clanzOSPOpE2AMNNDfF6q4i1").update(disponibilidadActualizada)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AsignarMesero.this, "Good", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AsignarMesero.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart(){
        super.onStart();
        mMesaAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mMesaAdapter.stopListening();
    }


}