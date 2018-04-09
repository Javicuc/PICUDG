package com.picudg.catapp.picudg.Tools;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.picudg.catapp.picudg.Modelo.ReporteCardView;
import com.picudg.catapp.picudg.R;
import com.picudg.catapp.picudg.ReporteFullView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by javilubz on 17/11/16.
 */

public class AdaptadorReportes extends RecyclerView.Adapter<AdaptadorReportes.ViewHolder>{

    private Context contexto;
    private List<ReporteCardView> items;
    //private Cursor items;

    public AdaptadorReportes(List<ReporteCardView> items, Context contexto){
        this.contexto = contexto;
        this.items    = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Referencias UI
        public TextView  tvAsunto;
        public TextView  tvEscuela;
        public TextView  tvDescripcion;
        public TextView  tvEdificio;
        public ImageView ivPhoto;
        public TextView  tvAutor;
        public TextView  tvFecha;
        public Button    btVerMas;

        public ViewHolder(View v){
            super(v);

            tvAsunto      = (TextView)  v.findViewById(R.id.TV_asuntoCard);
            tvEdificio    = (TextView)  v.findViewById(R.id.TV_edificioCard);
            tvEscuela     = (TextView)  v.findViewById(R.id.TV_escuelaCard);
            tvDescripcion = (TextView)  v.findViewById(R.id.TV_descripcionCard);
            ivPhoto       = (ImageView) v.findViewById(R.id.IV_photoCard);
            tvAutor       = (TextView)  v.findViewById(R.id.TV_autorCard);
            tvFecha       = (TextView)  v.findViewById(R.id.TV_fechaCard);
            btVerMas      = (Button)    v.findViewById(R.id.BT_vermasCard);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reporte,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ReporteCardView item = items.get(position);
        holder.itemView.setTag(item);

        //Asignacion UI
        holder.tvAsunto.setText(item.AsuntoCard);

        holder.tvEscuela.setText(item.CentroEstudioCard);

        holder.tvEdificio.setText(item.EdificioCard);

        holder.tvDescripcion.setText(item.DescripcionCard);

        holder.tvAutor.setText(item.AutorCard);

        holder.tvFecha.setText(item.FechaCard);

        Glide.with(contexto)
                .load(item.ImagenCard)
                .centerCrop()
                .into(holder.ivPhoto);

        holder.btVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(contexto, "Reporte: " + item.AsuntoCard, Toast.LENGTH_SHORT).show();
                Intent fullView = new Intent(contexto, ReporteFullView.class);
                fullView.putExtra("Reporte", item);
                contexto.startActivity(fullView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
