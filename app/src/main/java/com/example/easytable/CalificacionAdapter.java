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

public class CalificacionAdapter extends FirestoreRecyclerAdapter<CalificacionPojo, CalificacionAdapter.ViewHolder> {
    private static CalificacionAdapter.OnItemClickListener listener;
    public CalificacionAdapter(@NonNull FirestoreRecyclerOptions<CalificacionPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CalificacionAdapter.ViewHolder holder, int position, @NonNull CalificacionPojo model) {
        holder.nombrePlatilloCalificado.setText(model.getNombrePlatillo());
    }

    @NonNull
    @Override
    public CalificacionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.modelo_calificacion_list, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePlatilloCalificado;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrePlatilloCalificado = itemView.findViewById(R.id.nombrePlatilloCalificacion);

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

    public void setOnItemClickListener(CalificacionAdapter.OnItemClickListener listener){
        CalificacionAdapter.listener = listener;
    }

}
