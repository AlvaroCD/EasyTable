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

public class ListadoPlatillosOrdenadosAdapter extends FirestoreRecyclerAdapter<ListadoPlatillosOrdenadosPojo, ListadoPlatillosOrdenadosAdapter.ViewHolder> {
    private static ListadoPlatillosOrdenadosAdapter.OnItemClickListener listener;

    public ListadoPlatillosOrdenadosAdapter(@NonNull FirestoreRecyclerOptions<ListadoPlatillosOrdenadosPojo> options) {
        super(options);
    }

    //Aqui se establecen los datos que va a tener cada uno de los elementos de nuestra vista
    @Override
    protected void onBindViewHolder(@NonNull ListadoPlatillosOrdenadosAdapter.ViewHolder holder, int position, @NonNull ListadoPlatillosOrdenadosPojo model) {
        String cantidadParse = String.valueOf(model.getCantidad());
        holder.cantidad.setText(cantidadParse);
        holder.nombrePlatillo.setText(model.getNombrePlatillo());
        holder.especificaciones.setText(model.getEspecificaciones());
    }


    //El onCreateViewHolder de lo que se encarga es de "inflar" n cantidad de veces (donde n es la cantidad de elementos que hay en la base de datos)
    //la vista a la que se esta haciendo referencia, en este caso para mostrar los platillos ordenados
    @NonNull
    @Override
    public ListadoPlatillosOrdenadosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_platillos_ordenados_list, parent, false);
        return new ViewHolder(view);
    }

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cantidad, nombrePlatillo, especificaciones;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cantidad = itemView.findViewById(R.id.cantidadPlatillosTxt);
            nombrePlatillo = itemView.findViewById(R.id.nombrePlatilloListadoOrden);
            especificaciones = itemView.findViewById(R.id.indicacionesPlatillo);

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

    public void setOnItemClickListener(ListadoPlatillosOrdenadosAdapter.OnItemClickListener listener){
        ListadoPlatillosOrdenadosAdapter.listener = listener;
    }
}
