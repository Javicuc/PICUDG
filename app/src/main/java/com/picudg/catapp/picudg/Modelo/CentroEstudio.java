package com.picudg.catapp.picudg.Modelo;

/**
 * Created by Javi-cuc on 13/11/2016.
 */

public class CentroEstudio {

    public String idCentroEstudio;
    public String nombreCentro;
    public String acronimoCentro;
    public String direccionCentro;

    public  CentroEstudio(String ID, String Nombre, String Acrom, String Dir){
        this.idCentroEstudio = ID;
        this.nombreCentro = Nombre;
        this.acronimoCentro = Acrom;
        this.direccionCentro = Dir;
    }
}
