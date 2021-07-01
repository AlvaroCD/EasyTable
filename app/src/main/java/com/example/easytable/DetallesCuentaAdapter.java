package com.example.easytable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class DetallesCuentaAdapter extends FirestoreRecyclerAdapter<DetallesCuentaPojo, DetallesCuentaAdapter.ViewHolder> {
    private static DetallesCuentaAdapter.OnItemClickListener listener;

    public DetallesCuentaAdapter(@NonNull FirestoreRecyclerOptions<DetallesCuentaPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DetallesCuentaAdapter.ViewHolder holder, int position, @NonNull DetallesCuentaPojo model) {
        holder.nombre.setText(model.getNombrePersona());
    }

    @NonNull
    @Override
    public DetallesCuentaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_cuentas_detalles_list, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreCuenta);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicion = getAdapterPosition();
                    if (posicion != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(posicion), posicion);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int posicion);
    }

    public void setOnItemClickListener(DetallesCuentaAdapter.OnItemClickListener listener){
        DetallesCuentaAdapter.listener = listener;
    }

}
