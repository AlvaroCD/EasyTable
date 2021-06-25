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

public class ReservacionesAdapter extends FirestoreRecyclerAdapter <ReservacionesPojo, ReservacionesAdapter.ViewHolder>{
    private static ReservacionesAdapter.OnItemClickListener listener;

    public ReservacionesAdapter(@NonNull FirestoreRecyclerOptions<ReservacionesPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReservacionesAdapter.ViewHolder holder, int position, @NonNull ReservacionesPojo model) {
        holder.nombreUsuario.setText(model.getUsuario());
        holder.hora.setText(model.getHora());
        holder.fecha.setText(model.getFecha());
        String cantidadPersonasParse = String.valueOf(model.getCantidadPersonas());
        holder.cantidadPersonas.setText(cantidadPersonasParse);
    }


    //El onCreateViewHolder de lo que se encarga es de "inflar" n cantidad de veces (donde n es la cantidad de elementos que hay en la base de datos)
    //la vista a la que se esta haciendo referencia, en este caso para mostrar los restaurantes
    @NonNull
    @Override
    public ReservacionesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_reservaciones_list, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreUsuario, hora, fecha, cantidadPersonas;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreUsuario = itemView.findViewById(R.id.nombreUsuarioReservaciones);
            hora = itemView.findViewById(R.id.horaContenedor);
            fecha = itemView.findViewById(R.id.fechaContenedor);
            cantidadPersonas = itemView.findViewById(R.id.cantidadPersonasContenedor);

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

    public void setOnItemClickListener(ReservacionesAdapter.OnItemClickListener listener){
        ReservacionesAdapter.listener = listener;
    }

}
