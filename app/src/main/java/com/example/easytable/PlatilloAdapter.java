package com.example.easytable;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.graphics.Color.RED;

public class PlatilloAdapter extends FirestoreRecyclerAdapter<PlatilloPojo, PlatilloAdapter.ViewHolder> {
    private static PlatilloAdapter.OnItemClickListener listener;
    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;

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

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        db.collection("platillos").document(model.getIdPlatillo()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint({"SetTextI18n"})
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String cali =  documentSnapshot.get("calificacion").toString();
                            String usuariosCali = documentSnapshot.get("usuariosCalificacion").toString();

                            float calificacion = Float.parseFloat(cali);
                            float usuariosCalificacion =Float.parseFloat(usuariosCali);

                            float calificaciones = calificacion/usuariosCalificacion;

                            switch ((0 <= calificaciones && calificaciones < 1)?0 : (1 <= calificaciones && calificaciones < 2)?1 :
                                    (2 <= calificaciones && calificaciones < 3)?2 : (3 <= calificaciones && calificaciones < 4)?3 :
                                            (4 <= calificaciones && calificaciones < 5)?4 : (calificaciones == 5)?5 : 6) {
                                case 0:
                                    holder.calificicionPlatillo.setImageResource(R.drawable.__00estrellas);
                                    break;
                                case 1:
                                    holder.calificicionPlatillo.setImageResource(R.drawable.__10estrellas);
                                    break;
                                case 2:
                                    holder.calificicionPlatillo.setImageResource(R.drawable.__20estrellas);
                                    break;
                                case 3:
                                    holder.calificicionPlatillo.setImageResource(R.drawable.__30estrellas);
                                    break;
                                case 4:
                                    holder.calificicionPlatillo.setImageResource(R.drawable.__40estrellas);
                                    break;
                                case 5:
                                    holder.calificicionPlatillo.setImageResource(R.drawable.__50estrellas);
                                    break;
                                case 6:
                                    holder.calificicionPlatillo.setImageResource(R.drawable.__05estrellas);
                                    break;
                            }
                        }
                    }
                });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui se crea una vista la cual será la encargada de renderizar cada una de las vistas de los restaurantes en la pantalla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_menu_list, parent, false);
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
        listener = listener1;
    }
}
