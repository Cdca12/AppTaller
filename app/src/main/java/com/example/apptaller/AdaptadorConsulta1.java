package com.example.apptaller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorConsulta1 extends RecyclerView.Adapter<AdaptadorConsulta1.ViewHolder> {

    ArrayList<Consulta1> listaConsulta1;

    public AdaptadorConsulta1(ArrayList<Consulta1> listaConsulta1) {
        this.listaConsulta1 = listaConsulta1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCiudad, txtIngresoTotal, txtIngresoMenor, txtIngresoMayor, txtIngresoPromedio;

        public ViewHolder(View v) {
            super(v);
            txtCiudad = v.findViewById(R.id.tv1);
            txtIngresoTotal = v.findViewById(R.id.tv2);
            txtIngresoMenor = v.findViewById(R.id.tv3);
            txtIngresoMayor = v.findViewById(R.id.tv4);
            txtIngresoPromedio = v.findViewById(R.id.tv5);

            // textView.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_consulta, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.txtCiudad.setText(listaConsulta1.get(i).getCiudad());
        viewHolder.txtIngresoTotal.setText("$" + listaConsulta1.get(i).getIngresoTotal());
        viewHolder.txtIngresoMenor.setText("$" + listaConsulta1.get(i).getIngresoMenor());
        viewHolder.txtIngresoMayor.setText("$" + listaConsulta1.get(i).getIngresoMayor());
        viewHolder.txtIngresoPromedio.setText("$" + listaConsulta1.get(i).getIngresoPromedio());
    }

    @Override
    public int getItemCount() {
        return listaConsulta1.size();
    }


}
