package com.example.easytable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class RestaurantesAdapter extends FirestoreRecyclerAdapter<RestaurantePojo, RestaurantesAdapter.ViewHolder> {
    private static OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RestaurantesAdapter(@NonNull FirestoreRecyclerOptions<RestaurantePojo> options) {
        super(options);
    }

    //Aqui se establecen los datos que va a tener cada uno de los elementos de nuestra vista
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull RestaurantePojo restaurante) {
        holder.descripcionLocal.setText(restaurante.getDescripcionRestaurante());
        holder.nombreLocal.setText(restaurante.getNombreLocal());
    }

    //El onCreateViewHolder de lo que se encarga es de "inflar" n cantidad de veces (donde n es la cantidad de elementos que hay en la base de datos)
    //la vista a la que se esta haciendo referencia, en este caso para mostrar los restaurantes
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vista_restaurante_list, viewGroup, false);
        return new ViewHolder(view);
    }

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView descripcionLocal;
        TextView nombreLocal;

        //Aqui se enlazan los objetos con el contenedor correspondiente del xml
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descripcionLocal = itemView.findViewById(R.id.descripcionRestaurante);
            nombreLocal = itemView.findViewById(R.id.nombreRestaurante);


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

    public void setOnItemClickListener(OnItemClickListener listener){
        RestaurantesAdapter.listener = listener;
    }
}
