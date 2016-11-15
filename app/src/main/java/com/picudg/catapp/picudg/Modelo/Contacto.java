package com.picudg.catapp.picudg.Modelo;

/**
 * Created by Javi-cuc on 13/11/2016.
 */

public class Contacto {

    public String idContacto;
    public String nombreContacto;
    public String correoContacto;
    public String telefonoContacto;
    public String rolContacto;
    public String fk_Centro;

    public Contacto(String ID,String Nombre, String Correo, String Tel, String Rol, String FK){
        this.idContacto = ID;
        this.nombreContacto = Nombre;
        this.correoContacto = Correo;
        this.telefonoContacto = Tel;
        this.rolContacto = Rol;
        this.fk_Centro = FK;
    }

}
