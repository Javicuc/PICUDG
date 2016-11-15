package com.picudg.catapp.picudg.Modelo;

/**
 * Created by Javi-cuc on 13/11/2016.
 */

public class Ubicacion {

    public String idUbicacion;
    public String nombreUbicacion;
    public String fk_Centro;

    public Ubicacion (String ID, String Nombre, String FK){
        this.idUbicacion = ID;
        this.nombreUbicacion = Nombre;
        this.fk_Centro = FK;
    }
}
