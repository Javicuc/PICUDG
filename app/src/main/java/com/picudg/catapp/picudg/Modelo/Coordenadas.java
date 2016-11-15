package com.picudg.catapp.picudg.Modelo;

/**
 * Created by Javi-cuc on 13/11/2016.
 */

public class Coordenadas {

    public String idCoordenada;
    public double Longitud;
    public double Latitud;
    public String fk_Ubicacion;
    public String fk_Centro;
    public String fk_Market;

    public Coordenadas(String ID, double Long, double Lat, String fkUbicacion,
                       String fkCentro, String fkMarket){

        this.idCoordenada = ID;
        this.Longitud = Long;
        this.Latitud = Lat;
        this.fk_Ubicacion = fkUbicacion;
        this.fk_Centro = fkCentro;
        this.fk_Market = fkMarket;

    }

}
