package com.example.easytable;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MeseroPuntuacionAdapter extends FirestoreRecyclerAdapter<MeserosPojo, MeseroPuntuacionAdapter.ViewHolder> {


    public MeseroPuntuacionAdapter(@NonNull FirestoreRecyclerOptions<MeserosPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MeseroPuntuacionAdapter.ViewHolder holder, int position, @NonNull MeserosPojo model) {
        String idMesero = model.getID();

        //Toast.makeText(MeseroPuntuacionAdapter.this, idMesero, Toast.LENGTH_SHORT).show();

    }

    @NonNull
    @Override
    public MeseroPuntuacionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
