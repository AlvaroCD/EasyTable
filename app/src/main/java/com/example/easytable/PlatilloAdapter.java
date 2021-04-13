package com.example.easytable;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import static android.graphics.Color.*;
import static android.graphics.Color.RED;

public class PlatilloAdapter extends FirestoreRecyclerAdapter<PlatilloPojo, PlatilloAdapter.ViewHolder> {
    private static PlatilloAdapter.OnItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    //Aqui se establecen los datos que va a tener cada uno de los elementos de nuestra vista
    public PlatilloAdapter(@NonNull FirestoreRecyclerOptions<PlatilloPojo> options) {
        super(options);
    }

    //El onCreateViewHolder de lo que se encarga es de "inflar" n cantidad de veces (donde n es la cantidad de elementos que hay en la base de datos)
    //la vista a la que se esta haciendo referencia, en este caso para mostrar los platillos
    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PlatilloPojo model) {
        holder.nombrePlatillo.setText(model.getNombrePlatillo());
        holder.costo.setText("$ " + model.getPrecio());
        holder.descripcion.setText(model.getDescripcion());
        if (model.isDisponibilidad()) {
            holder.disponibilidad.setText("DISPONIBLE");
            holder.disponibilidad.setTextColor(Color.parseColor("#008F39"));
        }
        else {
            holder.disponibilidad.setText("NO DISPONIBLE");
            holder.disponibilidad.setTextColor(RED);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_vista_menu, parent, false);
        return new ViewHolder(view);
    }

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    public class ViewHolder extends RecyclerView.ViewHolder {
    TextView nombrePlatillo, descripcion, costo, disponibilidad;
    ImageView calificicionPlatillo;

        //Aqui se enlazan los objetos con el contenedor correspondiente del xml
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        nombrePlatillo = itemView.findViewById(R.id.nombrePlatillo);
        descripcion = itemView.findViewById(R.id.descripcionPlatillo);
        costo = itemView.findViewById(R.id.costoPlatillo);
        disponibilidad = itemView.findViewById(R.id.disponibilidad);
        calificicionPlatillo = itemView.findViewById(R.id.puntuacionPlatillo);

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

    public void setOnItemClickListener(PlatilloAdapter.OnItemClickListener listener1){
        listener = (OnItemClickListener) listener1;
    }
}
