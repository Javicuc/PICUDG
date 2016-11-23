package com.picudg.catapp.picudg.SQLite;
import java.util.UUID;

/**
 * Created by Javi-cuc on 13/11/2016.
 */

public class InibdPicudg {

    interface ColumnasCentroEstudio{
        String ID_CENTROESTUDIO = "ID_Centro";
        String NOMBRE           = "Nombre_Centro";
        String ACRONIMO         = "Acronimo_Centro";
        String DIRECCION        = "Direccion_Centro";
    }
    interface ColumnasContacto  {
        String ID_CONTACTO = "ID_Contacto";
        String NOMBRE      = "Nombre";
        String CORREO      = "Correo";
        String TELEFONO    = "Telefono";
        String ROL         = "Rol";
        String FK_CENTRO   = "FK_Centro";
    }
    interface ColumnasContactos_Ubicacion  {
        String ID_CONTACTOSUBICACION = "ID_ContactosUbicacion";
        String FK_UBICACION = "FK_Ubicacion";
        String FK_CONTACTO  = "FK_Contacto";
    }
    interface ColumnasCoordenadas  {
        String ID_COORDENADA = "ID_Coordenada";
        String LONGITUD      = "Longitud";
        String LATITUD       = "Latitud";
        String FK_UBICACION  = "FK_Ubicacion";
        String FK_CENTRO     = "FK_Centro";
        String FK_MARKET     = "FK_Market";
        String INSERCION     = "Insercion";
    }

    interface ColumnasMarket  {
        String ID_MARKET  = "ID_Market";
        String ICON       = "Icon";
        String TIPO       = "Tipo";
        String TITULO     = "Titulo";
    }
    interface ColumnasReporte  {
        String ID_REPORTE   = "ID_Reporte";
        String ASUNTO       = "Asunto";
        String DESCRIPCION  = "Descripcion";
        String REPORTEURI   = "ReporteUri";
        String FK_USUARIO   = "FK_Usuario";
        String FK_MARKET    = "FK_Market";
        String IMAGENURI    = "Imagen";
        String FECHA        = "Fecha";
        String UBICACION    = "Ubicacion";
    }
    interface ColumnasUbicacion  {
        String ID_UBICACION = "ID_Ubicacion";
        String NOMBRE       = "Nombre";
        String FK_CENTRO    = "FK_Centro";
    }
    interface ColumnasUsuario  {
        String ID_USUARIO = "ID_Usuario";
        String NOMBRE    = "Nombre";
        String CORREO    = "Correo";
        String CODIGO    = "Codigo";
        String FK_CENTRO = "FK_Centro";
    }

    public static  class CentroEstudio implements ColumnasCentroEstudio {
        public static String gemerarIdCentroEstudio(){
            return "CES-" + UUID.randomUUID().toString();
        }
    }
    public static class Contacto implements ColumnasContacto {
        public static String generarIdContacto(){
            return "COT-" + UUID.randomUUID().toString();
        }
    }
    public static class Contactos_Ubicacion implements  ColumnasContactos_Ubicacion {
        public static String generarIdContactos_Ubicacion() {
            return "CTU-" + UUID.randomUUID().toString();
        }
    }
    public static class Coordenadas implements ColumnasCoordenadas {
        public  static String generarIdCoordenadas(){
            return "COR-" + UUID.randomUUID().toString();
        }
    }
    public static class Market implements ColumnasMarket {
        public static String generarIdMarket(){
            return "MKT-" + UUID.randomUUID().toString();
        }
    }
    public static class Reporte implements ColumnasReporte {
        public static String generarIdReporte(){
            return "REP-" + UUID.randomUUID().toString();
        }
    }
    public static class Ubicacion implements ColumnasUbicacion {
        public static String generarIdUbicacion(){
            return "UBI-" + UUID.randomUUID().toString();
        }
    }
    public static class Usuario implements ColumnasUsuario {
        public static String geneararIdUsuario(){
            return "USU-" + UUID.randomUUID().toString();
        }
    }

    private InibdPicudg(){
    }
}
