package com.picudg.catapp.picudg.Modelo;

/**
 * Created by Javi-cuc on 18/11/2016.
 */

public class ReporteCardView {

    private String AsuntoCard;
    private String CentroEstudioCard;
    private String DescripcionCard;
    private String ImagenCard;
    private String AutorCard;

    public ReporteCardView (String Asunto, String Centro, String Descripcion,
                                 String Uri, String Autor){

        this.AsuntoCard = Asunto;
        this.CentroEstudioCard = Centro;
        this.DescripcionCard = Descripcion;
        this.ImagenCard = Uri;
        this.AutorCard = Autor;

    }

}
