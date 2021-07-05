package com.example.easytable;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import static android.graphics.Color.RED;

public class CuentaUMAdapter extends FirestoreRecyclerAdapter<MeserosPojo, CuentaUMAdapter.ViewHolder> {
    private static CuentaUMAdapter.OnItemClickListener listener;
    private FirebaseFirestore db;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CuentaUMAdapter(@NonNull FirestoreRecyclerOptions<MeserosPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull MeserosPojo model) {
        String cuenta = model.getCuenta();
        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        //Consulta para obtener que mesas estan disponibles en el local seleccionado
        DocumentReference doc = db.collection("cuenta").document(cuenta);

        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                String refMesa = value.get("mesa").toString();
                holder.nombreUsuario.setText(value.get("idPrincipal").toString());
                holder.total.setText("$"+value.get("montoPagar").toString()+" MXN");

                Boolean pagado = value.getBoolean("pagado");
                if (pagado) {
                    holder.pago.setText("PAGADO");
                    holder.pago.setTextColor(Color.parseColor("#008F39"));
                }
                else {
                    holder.pago.setText("NO PAGADO");
                    holder.pago.setTextColor(RED);
                }


                DocumentReference reference = db.collection("mesa").document(refMesa);
                reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot values, @Nullable FirebaseFirestoreException error) {
                        holder.mesa.setText("Mesa #"+values.get("numeroMesa").toString());
                    }
                });

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_mesas_mesero, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mesa, pago, nombreUsuario, total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mesa = itemView.findViewById(R.id.mesa);
            pago = itemView.findViewById(R.id.pago);
            nombreUsuario = itemView.findViewById(R.id.nombreUsuario);
            total = itemView.findViewById(R.id.Total);

            //si toca un item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicion = getAdapterPosition();
                    if (posicion != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(posicion),posicion);
                    }
                }
            });
        }


    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int posicion);
    }

    public void setOnItemClickListener(CuentaUMAdapter.OnItemClickListener listener){
        CuentaUMAdapter.listener = listener;
    }
}
