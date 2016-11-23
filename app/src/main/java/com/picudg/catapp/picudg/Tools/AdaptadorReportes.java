package com.picudg.catapp.picudg.Tools;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Text;
import com.picudg.catapp.picudg.Modelo.Reporte;
import com.picudg.catapp.picudg.Modelo.ReporteCardView;
import com.picudg.catapp.picudg.R;

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

        public ViewHolder(View v){
            super(v);

            tvAsunto      = (TextView)  v.findViewById(R.id.TV_asuntoCard);
            tvEdificio    = (TextView)  v.findViewById(R.id.TV_edificioCard);
            tvEscuela     = (TextView)  v.findViewById(R.id.TV_escuelaCard);
            tvDescripcion = (TextView)  v.findViewById(R.id.TV_descripcionCard);
            ivPhoto       = (ImageView) v.findViewById(R.id.IV_photoCard);
            tvAutor       = (TextView)  v.findViewById(R.id.TV_autorCard);
            tvFecha       = (TextView)  v.findViewById(R.id.TV_fechaCard);

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
        ReporteCardView item = items.get(position);
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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
