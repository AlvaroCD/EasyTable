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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class ListadoOrdenesAdapter extends FirestoreRecyclerAdapter<ListadoOrdenesPojo, ListadoOrdenesAdapter.ViewHolder> {

    private static ListadoOrdenesAdapter.OnItemClickListener listener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ListadoOrdenesAdapter(@NonNull FirestoreRecyclerOptions<ListadoOrdenesPojo> options) {
        super(options);
    }
    //Aqui se establecen los datos que va a tener cada uno de los elementos de nuestra vista
    @Override
    protected void onBindViewHolder(@NonNull ListadoOrdenesAdapter.ViewHolder holder, int position, @NonNull ListadoOrdenesPojo model) {

        if (model.getStatusPreparacion() == 0) {
            holder.statusPreparacion.setText("Sin Preparar");
        } else if (model.getStatusPreparacion() == 1) {
            holder.statusPreparacion.setText("En Preparación");
        } else if (model.getStatusPreparacion() == 2) {
            holder.statusPreparacion.setText("Pendiente de Entrega");
        }
        String idMesa = model.getMesa();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("mesa").document(idMesa);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String numeroMesa = value.get("numeroMesa").toString();
                holder.idMesa.setText("Mesa #"+numeroMesa);
            }
        });
    }

    //El onCreateViewHolder de lo que se encarga es de "inflar" n cantidad de veces (donde n es la cantidad de elementos que hay en la base de datos)
    //la vista a la que se esta haciendo referencia, en este caso para mostrar los restaurantes
    @NonNull
    @Override
    public ListadoOrdenesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_ordenes_list, parent, false);
        return new ViewHolder(view);
    }

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView idMesa, statusPreparacion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idMesa = itemView.findViewById(R.id.numeroMesaListadoOrdenes);
            statusPreparacion = itemView.findViewById(R.id.estadoPreparacion);

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

    public void setOnItemClickListener(ListadoOrdenesAdapter.OnItemClickListener listener){
        ListadoOrdenesAdapter.listener = listener;
    }

}
