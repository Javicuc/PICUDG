package com.picudg.catapp.picudg.Modelo;

/**
 * Created by Javi-cuc on 18/11/2016.
 */

public class ReporteCardView {

    public String AsuntoCard;
    public String CentroEstudioCard;
    public String DescripcionCard;
    public String ImagenCard;
    public String AutorCard;

    public ReporteCardView (String Asunto, String Centro, String Descripcion,
                                 String Uri, String Autor){

        this.AsuntoCard = Asunto;
        this.CentroEstudioCard = Centro;
        this.DescripcionCard = Descripcion;
        this.ImagenCard = Uri;
        this.AutorCard = Autor;

    }

}
