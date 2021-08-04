package com.example.easytable;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class VentasAdapter extends FirestoreRecyclerAdapter<VentasPojo, VentasAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private FirebaseFirestore db;

    public VentasAdapter(@NonNull FirestoreRecyclerOptions<VentasPojo> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull VentasPojo model) {
        holder.monto.setText("Monto pagado: " + model.getMontoPagar());

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        db.collection("usuario").document(model.getIdPrincipal()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint({"SetTextI18n"})
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            holder.idUsuario.setText("Usuario: "+ documentSnapshot.get("Nombre").toString());
                        }
                    }

                });

        db.collection("mesa").document(model.getMesa()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint({"SetTextI18n"})
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            holder.numMesa.setText("Numero de mesa: " + documentSnapshot.get("numeroMesa").toString());
                        }
                    }

                });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_ventas_list, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView idUsuario, numMesa, monto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idUsuario = itemView.findViewById(R.id.nombreCliente);
            numMesa = itemView.findViewById(R.id.numeroMesaUtilizada);
            monto = itemView.findViewById(R.id.CantidadPagada);


        }
    }
}
