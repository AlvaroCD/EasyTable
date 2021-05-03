package com.example.easytable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class OrdenPedidosAdapter extends FirestoreRecyclerAdapter<PlatilloPojo, OrdenPedidosAdapter.ViewHolder>{

    private static OrdenPedidosAdapter.OnItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    //Aqui se establecen los datos que va a tener cada uno de los elementos de nuestra vista
    public OrdenPedidosAdapter(@NonNull FirestoreRecyclerOptions<PlatilloPojo> options) {
        super(options);
    }
    //El onCreateViewHolder de lo que se encarga es de "inflar" n cantidad de veces (donde n es la cantidad de elementos que hay en la base de datos)
    //la vista a la que se esta haciendo referencia, en este caso para mostrar los platillos
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PlatilloPojo model) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_vista_orden_pedidos, parent, false);
        return new ViewHolder(view);
    }

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePlatillo;
        ImageButton eliminatPlatillo;
        EditText comentarioEspecifico;

        //Aqui se enlazan los objetos con el contenedor correspondiente del xml
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrePlatillo = itemView.findViewById(R.id.nombrePlatillo);
            eliminatPlatillo = itemView.findViewById(R.id.eliminarPlatillo);
            comentarioEspecifico = itemView.findViewById(R.id.comentarioEspecifico);

            eliminatPlatillo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            //listener.onDeleteClick(position);
                        }
                    }

                       // listener.onItemClick(getSnapshots().getSnapshot(posicion),posicion);


                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int posicion);
    }

    public void setOnItemClickListener(OrdenPedidosAdapter.OnItemClickListener listener2){
        listener = listener2;
    }
}
