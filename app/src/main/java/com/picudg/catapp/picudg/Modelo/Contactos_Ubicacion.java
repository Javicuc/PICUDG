package com.picudg.catapp.picudg.Modelo;

/**
 * Created by Javi-cuc on 13/11/2016.
 */

public class Contactos_Ubicacion {

    public String idContactosUbicacion;
    public String fk_Ubicacion;
    public String fk_Contacto;

    public Contactos_Ubicacion(String id, String fkUbicacion, String fkContacto){
        this.idContactosUbicacion = id;
        this.fk_Ubicacion = fkUbicacion;
        this.fk_Contacto = fkContacto;
    }
}
