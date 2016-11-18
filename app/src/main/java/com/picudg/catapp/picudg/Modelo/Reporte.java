package com.picudg.catapp.picudg.Modelo;

/**
 * Created by Javi-cuc on 13/11/2016.
 */

public class Reporte {

    public String idReporte;
    public String asuntoReporte;
    public String descripcionReporte;
    public String reporteuriReporte;
    public String fk_Usuario;
    public String fk_Market;
    public String imagenUri;

    public Reporte(String ID, String Asunto, String Descripcion, String URI,
                   String fkUsuario, String fkMarket, String imgUri){

        this.idReporte          = ID;
        this.asuntoReporte      = Asunto;
        this.descripcionReporte = Descripcion;
        this.reporteuriReporte  = URI;
        this.fk_Usuario         = fkUsuario;
        this.fk_Market          = fkMarket;
        this.imagenUri          = imgUri;

    }
}
