package com.example.apptaller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorConsulta2 extends RecyclerView.Adapter<AdaptadorConsulta2.ViewHolder> {

    ArrayList<Consulta2> listaConsulta2;

    public AdaptadorConsulta2(ArrayList<Consulta2> listaConsulta2) {
        this.listaConsulta2 = listaConsulta2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtAño, txtCiudad, txtMarca, txtImporteTotal;

        public ViewHolder(View v) {
            super(v);
            txtAño = v.findViewById(R.id.tv1);
            txtCiudad = v.findViewById(R.id.tv2);
            txtMarca = v.findViewById(R.id.tv3);
            txtImporteTotal = v.findViewById(R.id.tv4);
            (v.findViewById(R.id.tv5)).setVisibility(View.GONE); // Ocultar para acomodar
        }
    }

    @NonNull
    @Override
    public AdaptadorConsulta2.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_consulta, viewGroup, false);

        AdaptadorConsulta2.ViewHolder vh = new AdaptadorConsulta2.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorConsulta2.ViewHolder viewHolder, int i) {
        viewHolder.txtAño.setText(listaConsulta2.get(i).getAño());
        viewHolder.txtCiudad.setText(listaConsulta2.get(i).getCiudad());
        viewHolder.txtMarca.setText(listaConsulta2.get(i).getMarca());
        viewHolder.txtImporteTotal.setText("$" + listaConsulta2.get(i).getImporteTotal());
    }

    @Override
    public int getItemCount() {
        return listaConsulta2.size();
    }

}


