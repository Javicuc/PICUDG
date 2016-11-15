package com.picudg.catapp.picudg.Modelo;

/**
 * Created by Javi-cuc on 13/11/2016.
 */

public class Usuario {

    public String idUsuario;
    public String nombreUsuario;
    public String correoUsuario;
    public String codigoUsuario;
    public String fk_Centro;

    public Usuario(String ID, String Nombre, String Correo, String Codigo, String FK){
        this.idUsuario = ID;
        this.nombreUsuario = Nombre;
        this.correoUsuario = Correo;
        this.codigoUsuario = Codigo;
        this.fk_Centro = FK;
    }

}
