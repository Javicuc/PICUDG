package com.picudg.catapp.picudg.Modelo;

/**
 * Created by Javi-cuc on 13/11/2016.
 */

public class Market {

    public String idMarket;
    public String iconMarket;
    public String tipoMarket;
    public String tituloMarket;

    public Market(String ID, String Icon, String Tipo, String Titulo){
        this.idMarket = ID;
        this.iconMarket = Icon;
        this.tipoMarket = Tipo;
        this.tituloMarket = Titulo;
    }
}
