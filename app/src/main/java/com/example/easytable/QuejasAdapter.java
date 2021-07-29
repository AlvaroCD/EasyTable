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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class QuejasAdapter extends FirestoreRecyclerAdapter<QuejasPojo, QuejasAdapter.ViewHolder> {
    private static QuejasAdapter.OnItemClickListener listener;

    public QuejasAdapter(@NonNull FirestoreRecyclerOptions<QuejasPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuejasAdapter.ViewHolder holder, int position, @NonNull QuejasPojo model) {

        String idMesa = model.getMesa();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("mesa").document(idMesa);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String numeroMesa = value.get("numeroMesa").toString();
                holder.mesa.setText("Mesa #"+numeroMesa);
            }
        });
        holder.queja.setText(model.getQuejaCliente());
    }

    @NonNull
    @Override
    public QuejasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_quejas_list, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mesa, queja;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mesa = itemView.findViewById(R.id.numeroMesaQueja);
            queja = itemView.findViewById(R.id.quejaDelCliente);

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

    public void setOnItemClickListener(QuejasAdapter.OnItemClickListener listener1){
        listener = listener1;
    }

}
