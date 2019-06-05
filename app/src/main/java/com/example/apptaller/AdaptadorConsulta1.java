package com.example.apptaller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorConsulta1 extends RecyclerView.Adapter<AdaptadorConsulta1.ViewHolder> {

    ArrayList<Consulta1> lista;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_consulta, viewGroup, false);


        // Aquí podemos definir tamaños, márgenes, paddings
        // ...

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    public AdaptadorConsulta1( ArrayList<Consulta1> lista){
        this.lista=lista;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(lista.get(i).getPrimeraColumna());


    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // en este ejemplo cada elemento consta solo de un título
        public TextView textView;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public TextView textView5;
        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.tv1);

            textView.setVisibility(View.GONE);
            // completar
        }

    }




}
