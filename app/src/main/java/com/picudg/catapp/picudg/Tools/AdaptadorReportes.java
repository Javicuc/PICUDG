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
import com.picudg.catapp.picudg.R;

/**
 * Created by javilubz on 17/11/16.
 */

public class AdaptadorReportes extends RecyclerView.Adapter<AdaptadorReportes.ViewHolder>{

    private final Context contexto;
    private Cursor items;

    private OnItemClickListener escucha;

    interface OnItemClickListener{
        public void onClick(ViewHolder holder, int idReporte);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        //Referencias UI
        public TextView tvAsunto;
        public TextView tvEscuela;
        public TextView tvDescripcion;
        public TextView tvEdificio;
        public ImageView ivPhoto;

        public ViewHolder(View v){
            super(v);
            tvAsunto      = (TextView) v.findViewById(R.id.TV_asuntoCard);
            tvEscuela     = (TextView) v.findViewById(R.id.TV_escuelaCard);
            tvEdificio    = (TextView) v.findViewById(R.id.TV_edificioCard);
            tvDescripcion = (TextView) v.findViewById(R.id.TV_descripcionCard);
            ivPhoto       = (ImageView) v.findViewById(R.id.IV_photoCard);
        }

        @Override
        public void onClick(View view) {
            escucha.onClick(this, obtenerIdReporte(getAdapterPosition()));
        }
    }

    private int obtenerIdReporte(int adapterPosition) {
        if(items != null){
            if(items.moveToPosition(adapterPosition)){
                return items.getInt(ConsultaReportes.ID_REPORTE);
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }

    public AdaptadorReportes(Context contexto, OnItemClickListener escucha) {
        this.contexto = contexto;
        this.escucha = escucha;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reporte,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        items.moveToPosition(position);

        String s;

        //Asignacion UI
        s = items.getString(ConsultaReportes.ASUNTO);
        holder.tvAsunto.setText(s);

        s = items.getString(ConsultaReportes.ESCUELA);
        holder.tvEscuela.setText(s);

        s = items.getString(ConsultaReportes.EDIFICIO);

        s =  items.getString(ConsultaReportes.DESCRIPCION);
        holder.tvDescripcion.setText(s);

        s = items.getString(ConsultaReportes.URL);
        Glide.with(contexto)
                .load(s)
                .centerCrop()
                .into(holder.ivPhoto);
    }

    interface ConsultaReportes{
        int ID_REPORTE  = 1;
        int ASUNTO      = 2;
        int ESCUELA     = 3;
        int EDIFICIO    = 4;
        int DESCRIPCION = 5;
        int URL         = 6;
    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.getCount();
        return 0;
    }
    public void swapCursor(Cursor nvCursor) {
        if(nvCursor != null){
            items = nvCursor;
            notifyDataSetChanged();
        }
    }
    public Cursor getCursor(){
        return items;
    }
}
