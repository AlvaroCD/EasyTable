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

public class LocalAdapter extends FirestoreRecyclerAdapter <LocalPojo, LocalAdapter.ViewHolder> {
    private static LocalAdapter.OnItemClickListener listener;

    public LocalAdapter(@NonNull FirestoreRecyclerOptions<LocalPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LocalAdapter.ViewHolder holder, int position, @NonNull LocalPojo model) {
        holder.numeroMesa.setText("Mesa: "+model.getNumeroMesa());
        if (model.isStatusMesa()){
            holder.disponibilidadMesa.setText("Ocupada");
        }
        else {
            holder.disponibilidadMesa.setText("Libre");
        }
    }

    @NonNull
    @Override
    public LocalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_local_list, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView numeroMesa, disponibilidadMesa;

        //Aqui se enlazan los objetos con el contenedor correspondiente del xml
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroMesa = itemView.findViewById(R.id.numeroDeLaMesa);
            disponibilidadMesa = itemView.findViewById(R.id.disponibilidadMesa);

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

    public void setOnItemClickListener(LocalAdapter.OnItemClickListener listener){
        LocalAdapter.listener = (LocalAdapter.OnItemClickListener) listener;
    }

}
