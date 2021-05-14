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

public class EmpleadosAdapter extends FirestoreRecyclerAdapter<EmpleadosPojo, EmpleadosAdapter.ViewHolder> {
    private static EmpleadosAdapter.OnItemClickListener listener;

    public EmpleadosAdapter(@NonNull FirestoreRecyclerOptions<EmpleadosPojo> options) {
        super(options);
    }

    //Aqui se establecen los datos que va a tener cada uno de los elementos de nuestra vista
    @Override
    protected void onBindViewHolder(@NonNull EmpleadosAdapter.ViewHolder holder, int position, @NonNull EmpleadosPojo empleado) {
        holder.nombreEmpleado.setText(empleado.getNombre());
        holder.tipoDeUsuario.setText(empleado.getTipoDeUsuario());
    }

    //El onCreateViewHolder de lo que se encarga es de "inflar" n cantidad de veces (donde n es la cantidad de elementos que hay en la base de datos)
    //la vista a la que se esta haciendo referencia, en este caso para mostrar los restaurantes
    @NonNull
    @Override
    public EmpleadosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.modelo_empleados_list, viewGroup, false);

        return new ViewHolder(view);
    }

    //Creacion de los objetos que se relacionaran con las ID's de los elementos graficos del xml
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreEmpleado;
        TextView tipoDeUsuario;

        //Aqui se enlazan los objetos con el contenedor correspondiente del xml
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreEmpleado = itemView.findViewById(R.id.nombreEmpleado);
            tipoDeUsuario = itemView.findViewById(R.id.tipoDeEmpleado);

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
        EmpleadosAdapter.listener = listener;
    }

}
