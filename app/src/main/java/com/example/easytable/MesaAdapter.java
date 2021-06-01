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

public class MesaAdapter extends FirestoreRecyclerAdapter<MesaPojo, MesaAdapter.ViewHolder> {
    private static MesaAdapter.OnItemClickListener listener;

    public MesaAdapter(@NonNull FirestoreRecyclerOptions<MesaPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull MesaPojo model) {
        holder.numeroMesa.setText("Mesa " + model.getNumeroMesa());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_mesas_list, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView numeroMesa;
        TextView tipoDeUsuario;

        //Aqui se enlazan los objetos con el contenedor correspondiente del xml
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroMesa = itemView.findViewById(R.id.numeroMesas);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicion = getAdapterPosition();
                    if (posicion != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(posicion), posicion);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int posicion);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        MesaAdapter.listener = (OnItemClickListener) listener;
    }

}
