package com.example.easytable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CuentasAdapter extends FirestoreRecyclerAdapter<CuentasPojo, CuentasAdapter.ViewHolder> {
    private static CuentasAdapter.OnItemClickListener listener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CuentasAdapter(@NonNull FirestoreRecyclerOptions<CuentasPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CuentasAdapter.ViewHolder holder, int position, @NonNull CuentasPojo model) {
        String idMesa = model.getMesa();
        DocumentReference doc = db.collection("mesa").document(idMesa);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String numeroMesa = value.get("numeroMesa").toString();
                holder.numeroMesa.setText("Mesa #"+numeroMesa);
            }
        });
        if (model.isPagado()){
            holder.statusPago.setText("Pagado");
        }
        else {
            holder.statusPago.setText("No Pagado");
        }
    }

    @NonNull
    @Override
    public CuentasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_cuentas_list, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView numeroMesa, statusPago;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroMesa = itemView.findViewById(R.id.numeroMesaCuentas);
            statusPago = itemView.findViewById(R.id.statusPagoCuentas);

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

    public void setOnItemClickListener(CuentasAdapter.OnItemClickListener listener){
        CuentasAdapter.listener = listener;
    }

}
