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

public class MeserosAdapter extends FirestoreRecyclerAdapter<MeserosPojo, MeserosAdapter.ViewHolder> {
    private static MeserosAdapter.OnItemClickListener listener;

    public MeserosAdapter(@NonNull FirestoreRecyclerOptions<MeserosPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MeserosAdapter.ViewHolder holder, int position, @NonNull MeserosPojo model) {
        holder.Nombre.setText(model.getNombre());
        holder.Apellidos.setText(model.getApellidos());
    }

    @NonNull
    @Override
    public MeserosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_mesero_list, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Nombre, Apellidos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Nombre = itemView.findViewById(R.id.nombreMeseroTxt);
            Apellidos = itemView.findViewById(R.id.apellidosMeseroTxt);

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

    public void setOnItemClickListener(MeserosAdapter.OnItemClickListener listener){
        MeserosAdapter.listener = listener;
    }
}
