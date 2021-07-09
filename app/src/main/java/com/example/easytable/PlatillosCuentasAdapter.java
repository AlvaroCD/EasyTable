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

public class PlatillosCuentasAdapter extends FirestoreRecyclerAdapter <PlatillosCuentasPojo, PlatillosCuentasAdapter.ViewHolder> {
    private static PlatillosCuentasAdapter.OnItemClickListener listener;

    public PlatillosCuentasAdapter(@NonNull FirestoreRecyclerOptions<PlatillosCuentasPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PlatillosCuentasAdapter.ViewHolder holder, int position, @NonNull PlatillosCuentasPojo model) {
        holder.nombrePlatillo.setText(model.getNombrePlatillo());
        holder.cantidadPlatillos.setText(""+model.getCantidad());
        holder.precio.setText(""+model.getCosto());
    }

    @NonNull
    @Override
    public PlatillosCuentasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_platillos_cuenta_list, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cantidadPlatillos, nombrePlatillo, precio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cantidadPlatillos = itemView.findViewById(R.id.cantidadPlatillosCuenta);
            nombrePlatillo = itemView.findViewById(R.id.nombrePlatilloCuenta);
            precio = itemView.findViewById(R.id.costoPlatilloCuenta);


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

    public void setOnItemClickListener(PlatillosCuentasAdapter.OnItemClickListener listener1){
        listener = listener1;
    }

}
