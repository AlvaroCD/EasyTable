package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DetallesCuenta extends AppCompatActivity {

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    private RecyclerView mRecyclerViewDetallesCuenta;
    private DetallesCuentaAdapter mDetallesCuentaAdapter;

    private TextView mMetodoPago, mMontoPagado, mFecha;

    //Adicion de la instancia de Firebase para el uso de Cloud Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_detalles_cuenta);

        //Instanciacion del Recycler View
        mRecyclerViewDetallesCuenta = findViewById(R.id.recyclerViewDetallesCuenta);
        mRecyclerViewDetallesCuenta.setLayoutManager(new LinearLayoutManager(this));

        //mMetodoPago tambien se usara como contenedor para poner "Sin pago" cuando no se haya pagado la cuenta
        mMetodoPago = findViewById(R.id.metodoPagoDetallesCuenta);
        mMontoPagado = findViewById(R.id.montoPagadoDetallesCuenta);
        mFecha = findViewById(R.id.fechaDetallesCuenta);

        Bundle extra = getIntent().getExtras();
        String idCuenta = extra.getString("idCuenta");
        boolean metodoPago = extra.getBoolean("metodoPago");
        boolean pagado = extra.getBoolean("pagado");
        long montoPagado = extra.getLong("montoPagado");
        String fecha = extra.getString("fecha");

        //Coloca los nombres
        recyclerViewDetallesCuenta(idCuenta);

        if (pagado){
            if (metodoPago){
                mMetodoPago.setText("Método de Pago: Efectivo");
            }
            else {
                mMetodoPago.setText("Método de Pago: Tarjeta/Paypal");
            }
        }
        else {
            mMetodoPago.setText("Método de Pago: Aún no se ha pagado");
        }
        mMontoPagado.setText("Total: $" + montoPagado + " MXN");
        mFecha.setText("Fecha: "+fecha);
    }

    private void recyclerViewDetallesCuenta(String idCuenta) {
        //Consulta para obtener los datos de la BD
        Query query = db.collection("cuenta").document(idCuenta)
                .collection("usuariosPago");

        FirestoreRecyclerOptions<DetallesCuentaPojo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<DetallesCuentaPojo>()
                .setQuery(query, DetallesCuentaPojo.class).build();

        mDetallesCuentaAdapter = new DetallesCuentaAdapter(firestoreRecyclerOptions);
        mDetallesCuentaAdapter.notifyDataSetChanged();
        mRecyclerViewDetallesCuenta.setAdapter(mDetallesCuentaAdapter);
    }

    //Metodo para que cuando el usuario esté dentro de la aplicacion, la aplicación esté actualizando los datos de la misma (datos de los empleados)
    @Override
    protected void onStart() {
        super.onStart();
        mDetallesCuentaAdapter.startListening();
    }

    //Metodo para que cuando el usuario no esté dentro de la aplicacion, la aplicación deje de actualizar los datos de la misma (datos de los empleados)
    @Override
    protected void onStop() {
        super.onStop();
        mDetallesCuentaAdapter.stopListening();
    }

}