package com.example.easytable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ComentarioAdapter extends FirestoreRecyclerAdapter<ComentarioPojo, ComentarioAdapter.ViewHolder> {

    public ComentarioAdapter(@NonNull FirestoreRecyclerOptions<ComentarioPojo> options) {
        super(options);
    }
    //Aqui se establecen los datos que va a tener cada uno de los elementos de nuestra vista
    @Override
    protected void onBindViewHolder(@NonNull ComentarioAdapter.ViewHolder holder, int position, @NonNull ComentarioPojo model) {
        holder.comentario.setText(model.getComentario());
    }

    //El onCreateViewHolder de lo que se encarga es de "inflar" n cantidad de veces (donde n es la cantidad de elementos que hay en la base de datos)
    //la vista a la que se esta haciendo referencia, en este caso para mostrar los restaurantes
    @NonNull
    @Override
    public ComentarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_vista_restaurante, parent, false);
        return new ViewHolder(view);
    }

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView comentario;
        //Aqui se enlazan los objetos con el contenedor correspondiente del xml
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comentario = itemView.findViewById(R.id.ComentarioUsuario);
        }
    }
}
