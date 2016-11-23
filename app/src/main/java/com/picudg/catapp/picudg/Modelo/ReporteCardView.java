package com.picudg.catapp.picudg.Modelo;

/**
 * Created by Javi-cuc on 18/11/2016.
 */

public class ReporteCardView {

    public String AsuntoCard;
    public String CentroEstudioCard;
    public String EdificioCard;
    public String DescripcionCard;
    public String ImagenCard;
    public String AutorCard;
    public String FechaCard;

    public ReporteCardView (String Asunto, String Centro,String Edificio, String Descripcion,
                                 String Uri, String Autor, String Fecha){

        this.AsuntoCard        = Asunto;
        this.CentroEstudioCard = Centro;
        this.EdificioCard      = Edificio;
        this.DescripcionCard   = Descripcion;
        this.ImagenCard        = Uri;
        this.AutorCard         = Autor;
        this.FechaCard         = Fecha;

    }

}
