package com.example.apptaller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorConsulta3 extends RecyclerView.Adapter<AdaptadorConsulta3.ViewHolder> {

    ArrayList<Consulta3> listaConsulta3;

    public AdaptadorConsulta3(ArrayList<Consulta3> listaConsulta3) {
        this.listaConsulta3 = listaConsulta3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtRFC, txtNombre;

        public ViewHolder(View v) {
            super(v);
            txtRFC = v.findViewById(R.id.tv1);
            txtNombre = v.findViewById(R.id.tv2);
            (v.findViewById(R.id.tv3)).setVisibility(View.GONE);
            (v.findViewById(R.id.tv4)).setVisibility(View.GONE);
            (v.findViewById(R.id.tv5)).setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public AdaptadorConsulta3.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_consulta, viewGroup, false);

        AdaptadorConsulta3.ViewHolder vh = new AdaptadorConsulta3.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorConsulta3.ViewHolder viewHolder, int i) {
        viewHolder.txtRFC.setText(listaConsulta3.get(i).getRfc());
        viewHolder.txtNombre.setText(listaConsulta3.get(i).getNombre());
    }

    @Override
    public int getItemCount() {
        return listaConsulta3.size();
    }


}
