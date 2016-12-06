package com.picudg.catapp.picudg.SQLite;

import android.content.Context;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.util.Log;

import com.picudg.catapp.picudg.Modelo.CentroEstudio;
import com.picudg.catapp.picudg.Modelo.Contacto;
import com.picudg.catapp.picudg.Modelo.Contactos_Ubicacion;
import com.picudg.catapp.picudg.Modelo.Coordenadas;
import com.picudg.catapp.picudg.Modelo.Market;
import com.picudg.catapp.picudg.Modelo.Reporte;
import com.picudg.catapp.picudg.Modelo.Ubicacion;
import com.picudg.catapp.picudg.Modelo.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by javilubz on 17/11/16.
 */

public class LoadBD extends AsyncTask<Void, Void, Void> {

    private OperacionesBaseDatos datos;

    public LoadBD(Context context) {
        this.datos = OperacionesBaseDatos
                .obtenerInstancia(context);
    }
    @Override
    protected Void doInBackground(Void... params) {
        // [INSERCIONES]
        try {

            datos.getDb().beginTransaction();

            /** INSERTAR CENTROS DE ESTUDIO **/
            String centro1 = datos.insertarCentroEstudio(new CentroEstudio(null, "Centro Universitario de Ciencias Exactas e Ingenierias", "CUCEI",
                    "Blvd. Marcelino García Barragán 1421, Ciudad Universitaria, 44430 Guadalajara, JAL"));
            String centro2 = datos.insertarCentroEstudio(new CentroEstudio(null, "Centro Universitario de Ciencias Economico Administrativas", "CUCEA",
                    "Av. José Parres Arias 74, Núcleo Universitario Los Belenes, 45100 Zapopan, Jal."));

            /** INSERTAR USUARIOS **/
            String usuario1 = datos.insertarUsuario(new Usuario(null, "Javier Alejandro López Rangel", "javier.jalr7@gmail.com", "215256109", centro1));
            String usuario2 = datos.insertarUsuario(new Usuario(null, "Oscar Brandon Ramos Sanches", "javier_jalrTMP@hotmail.com", "215254110", centro1));

            /** INSERTAR UBICACIONES **/
            String ubicacion1  = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_ALFA", centro1));
            String ubicacion2  = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_beta", centro1));
            String ubicacion3  = datos.insertarUbicacion(new Ubicacion(null,  "Biblioteca", centro1));
            String ubicacion4  = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_X", centro1));
            String ubicacion5  = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_U", centro1));
            String ubicacion6  = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_V1", centro1));
            String ubicacion7  = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_V2", centro1));
            String ubicacion8  = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_O", centro1));
            String ubicacion9  = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_T", centro1));
            String ubicacion10 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_R", centro1));
            String ubicacion11 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_GAMA", centro1));
            String ubicacion12 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_S", centro1));
            String ubicacion13 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_W", centro1));
            String ubicacion14 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_E", centro1));
            String ubicacion15 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_A", centro1));
            String ubicacion16 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_B", centro1));
            String ubicacion17 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_C", centro1));
            String ubicacion18 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_D", centro1));
            String ubicacion19 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_J", centro1));
            String ubicacion20 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_N", centro1));
            String ubicacion21 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_M", centro1));
            String ubicacion22 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_L", centro1));
            String ubicacion23 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_Z1", centro1));
            String ubicacion24 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_Z2", centro1));
            String ubicacion25 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_G", centro1));
            String ubicacion26 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_Q", centro1));
            String ubicacion27 = datos.insertarUbicacion(new Ubicacion(null, "Auditorio Matute Remus", centro1));
            String ubicacion28 = datos.insertarUbicacion(new Ubicacion(null, "Auditorio Enrique Díaz de León", centro1));
            String ubicacion29 = datos.insertarUbicacion(new Ubicacion(null, "Auditorio Antonio Alatorre", centro1));
            String ubicacion30 = datos.insertarUbicacion(new Ubicacion(null, "Auditorio Antonio Rodríguez", centro1));
            String ubicacion31 = datos.insertarUbicacion(new Ubicacion(null, "Auditorio Central Cucea", centro2));
            String ubicacion32 = datos.insertarUbicacion(new Ubicacion(null, "EDIFICIO_P", centro1));

            /** INSERTAR CONTACTOS **/
            String contacto1 = datos.insertarContacto(new Contacto(null, "Jorge Alberto Calvillo Vargas", "javier.jalr7+Contacto1@gmail.com", "3310086200",
                    "Coodinador de Servicios Generales", centro1));
            String contacto2 = datos.insertarContacto(new Contacto(null, "Edwin Francisco Ruiz Martínez", "javier.jalr7+Contacto2@gmail.com", "3310086201",
                    "Jefe de Unidad de Mantenimiento", centro1));
            String contacto3 = datos.insertarContacto(new Contacto(null, "Ricardo Fernando Sánchez Hernández", "javier.jalr7+Contacto3@gmail.com", "3310086202",
                    "Jefe de Unidad de Adquisición y Suministros", centro1));
            String contacto4 = datos.insertarContacto(new Contacto(null, "Javier Alejandro Lopez Rangel", "javimex51@gmail.com", "3310086244",
                    "Jefe de Unidad de Computación", centro2));

            /** INSERTAR MARKERS **/
            String market1 = datos.insertarMarket(new Market(null,null,"Reporte","Socket Mal Puesto"));
            String market2 = datos.insertarMarket(new Market(null,null,"Reporte","Fuga De Agua"));
            /** INSERTAR REPORTES **/
            Date date = new Date();
            SimpleDateFormat ft =
                    new SimpleDateFormat ("yyyy/MM/dd, hh:mm:ss");
            datos.insertarReportes(new Reporte(null,"Socket Mal Puesto!","El socket que esta ubicado, en la fila pegada a la ventana del escritorio del profesor" +
                    " esta mal puesto, por lo tanto no permite la carga de dispositivos electronicos.", null,usuario1,market1,null,ft.format(date).toString(),"EDIF_ALFA"));
            datos.insertarReportes(new Reporte(null,"Fuga De Agua","Hay una fuga de agua en el X. En el baño de los hombres hay una pequeña fuga de agua en" +
                    "uno de los lavamanos, se necesita con urgencia una reparacion.", null,usuario2,market2,null,ft.format(date).toString(),"EDIF_X"));


            /** INSERTA MARKER DE EJEMPLO **/
            datos.insertarCoordenadas(new Coordenadas(null, -103.325045, 20.656525,null,null,market1,1));
            datos.insertarCoordenadas(new Coordenadas(null, -103.327255, 20.658288,null,null,market2,2));

            /** RELACIONAR UBICACIONES CON SUS RESPECTIVOS CONTACTOS **/
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion1, contacto1));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion2, contacto2));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion3, contacto3));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion4, contacto1));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion5, contacto2));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion6, contacto3));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion7, contacto1));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion8, contacto2));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion9, contacto3));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion10, contacto1));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion11, contacto2));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion12, contacto3));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion13, contacto1));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion14, contacto2));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion15, contacto3));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion16, contacto1));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion17, contacto2));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion18, contacto3));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion19, contacto1));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion20, contacto2));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion21, contacto3));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion22, contacto1));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion23, contacto2));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion24, contacto3));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion25, contacto1));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion26, contacto2));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion27, contacto3));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion28, contacto1));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion29, contacto2));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion30, contacto3));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion31, contacto1));
            datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion32, contacto2));

            /** INSERTAR COORDENADAS CUCEI Y EDIFICIOS **/
            insertarCoordenadasCucei(datos,centro1);
            insertarCoordenadasCucea(datos,centro2);
            /*
            insertarCoordenadasAlfa(datos,ubicacion1);
            insertarCoordenadasBeta(datos,ubicacion2);
            */
            insertarBibliotecaCucei(datos,ubicacion3);
            insertarCoordenadasX(datos,ubicacion4);
            /*
            insertarCoordenadasU(datos,ubicacion5);
            insertarCoordenadasV1(datos,ubicacion6);
            insertarCoordenadasV2(datos,ubicacion7);
            insertarCoordenadasO(datos,ubicacion8);
            insertarCoordenadasT(datos,ubicacion9);
            insertarCoordenadasR(datos,ubicacion10);
            insertarCoordenadasGama(datos,ubicacion11);
            insertarCoordenadasS(datos,ubicacion12);
            insertarCoordenadasW(datos,ubicacion13);
            insertarCoordenadasE(datos,ubicacion14);
            insertarCoordenadasA(datos,ubicacion15);
            insertarCoordenadasB(datos,ubicacion16);
            insertarCoordenadasC(datos,ubicacion17);
            insertarCoordenadasD(datos,ubicacion18);
            insertarCoordenadasJ(datos,ubicacion19);
            insertarCoordenadasN(datos,ubicacion20);
            insertarCoordenadasM(datos,ubicacion21);
            insertarCoordenadasL(datos,ubicacion22);
            insertarCoordenadasZ1(datos,ubicacion23);
            insertarCoordenadasZ2(datos,ubicacion24);
            insertarCoordenadasG(datos,ubicacion25);
            insertarCoordenadasQ(datos,ubicacion26);
            insertarCoordenadasP(datos,ubicacion32);
            insertarCoordenadasMatuteRemus(datos,ubicacion27);
            insertarCoordenadasEnriqueDiaz(datos,ubicacion28);
            insertarCoordenadasAlatorre(datos,ubicacion29);
            insertarCoordenadasAntonioRodriguez(datos,ubicacion30);
            */
            insertarAuditorioCentralCucea(datos,ubicacion31);


            // Eliminación Contacto
            datos.eliminarContacto(contacto4);

            // Actualización Ubicacion
            datos.actualizarUbicacion(new Ubicacion(ubicacion2,"EDIF_BETA",centro1));

            datos.getDb().setTransactionSuccessful();
        } finally {
            datos.getDb().endTransaction();
        }

        // [QUERIES]
        /*
        Log.i("query1: ","obtenerCentroEstudio");
        DatabaseUtils.dumpCursor(datos.obtenerCentroEstudio());
        Log.i("query2: ","obtenerUsuarios");
        DatabaseUtils.dumpCursor(datos.obtenerUsuarios());
        Log.i("query3: ","obtenerCentroUbicaciones");
        DatabaseUtils.dumpCursor(datos.obtenerUbicaciones());
        Log.i("query4: ","obtenerContactos");
        DatabaseUtils.dumpCursor(datos.obtenerContactos());
        Log.i("query5: ","obtenerContactosUbicaciones");
        DatabaseUtils.dumpCursor(datos.obtenerContactosUbicaciones());
        Log.i("query6: ","obtenerContactosUbicacionesNombre");
        DatabaseUtils.dumpCursor(datos.obtenerContactosUbicacionNombre("EDIF_ALFA"));
        Log.i("query7: ","obtenerCoordenadas");
        DatabaseUtils.dumpCursor(datos.obtenerCoordenadas());
        Log.i("query8: ","obtenerReportes");
        DatabaseUtils.dumpCursor(datos.obtenerReportes());
        Log.i("query9: ","obtenerMarkets");
        DatabaseUtils.dumpCursor(datos.obtenerMarket());
        Log.i("query10: ","obtenerReportesMarketCoordenadas");
        DatabaseUtils.dumpCursor(datos.obtenerReportesMarketCoordenadas("Javier Alejandro López Rangel"));
        */
        datos.getDb().close();
        return null;
    }

    private void insertarCoordenadasP(OperacionesBaseDatos datos, String ubicacion32) {

    }
    private void insertarAuditorioCentralCucea(OperacionesBaseDatos datos, String ubicacion31) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.380854, 20.741718,  ubicacion31, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.380915, 20.741517,  ubicacion31, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.380513, 20.741414,  ubicacion31, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.380450, 20.741609,  ubicacion31, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.380854, 20.741718,  ubicacion31, null, null, 5));
    }
    private void insertarCoordenadasQ(OperacionesBaseDatos    datos, String ubicacion26) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.325426, 20.657655,  ubicacion26, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325419, 20.657551,  ubicacion26, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324435, 20.657617,  ubicacion26, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324443, 20.657720,  ubicacion26, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325426, 20.657655,  ubicacion26, null, null, 5));

    }
    private void insertarCoordenadasG(OperacionesBaseDatos    datos, String ubicacion25) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.327289, 20.655913,  ubicacion25, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327285, 20.655860,  ubicacion25, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327297, 20.655859,  ubicacion25, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327295, 20.655836,  ubicacion25, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327284, 20.655836,  ubicacion25, null, null, 5));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327276, 20.655772,  ubicacion25, null, null, 6));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326710, 20.655812,  ubicacion25, null, null, 7));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326717, 20.655899,  ubicacion25, null, null, 8));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326703, 20.655901,  ubicacion25, null, null, 9));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326705, 20.655924,  ubicacion25, null, null, 10));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326718, 20.655922,  ubicacion25, null, null, 11));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326722, 20.655951,  ubicacion25, null, null, 12));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326722, 20.655951,  ubicacion25, null, null, 13));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326791, 20.656009,  ubicacion25, null, null, 14));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326903, 20.656003,  ubicacion25, null, null, 15));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326901, 20.655993,  ubicacion25, null, null, 16));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327106, 20.655979,  ubicacion25, null, null, 17));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327104, 20.655928,  ubicacion25, null, null, 18));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327289, 20.655913,  ubicacion25, null, null, 19));

    }
    private void insertarCoordenadasZ2(OperacionesBaseDatos   datos, String ubicacion24) {
        /*
        datos.insertarCoordenadas(new Coordenadas(null, -103.327921, 20.657427,  ubicacion24, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327787, 20.657410,  ubicacion24, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327957, 20.657171,  ubicacion24, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327820, 20.657156,  ubicacion24, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327921, 20.657427,  ubicacion24, null, null, 5));
        */
    }
    private void insertarCoordenadasZ1(OperacionesBaseDatos   datos, String ubicacion23) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.327773, 20.657119,  ubicacion23, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327654, 20.657105,  ubicacion23, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327608, 20.657383,  ubicacion23, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327730, 20.657397,  ubicacion23, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327773, 20.657119,  ubicacion23, null, null, 5));


    }
    private void insertarCoordenadasL(OperacionesBaseDatos    datos, String ubicacion22) {

        datos.insertarCoordenadas(new Coordenadas(null, -103.325485, 20.656875,  ubicacion22, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325469, 20.656628,  ubicacion22, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324936, 20.656660,  ubicacion22, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324952, 20.656904,  ubicacion22, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325052, 20.656901,  ubicacion22, null, null, 5));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325058, 20.656991,  ubicacion22, null, null, 6));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325302, 20.656977,  ubicacion22, null, null, 7));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325297, 20.656886,  ubicacion22, null, null, 8));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325485, 20.656875,  ubicacion22, null, null, 9));

    }
    private void insertarCoordenadasM(OperacionesBaseDatos    datos, String ubicacion21) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.325919, 20.656570,  ubicacion21, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326423, 20.656534,  ubicacion21, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326442, 20.656728,  ubicacion21, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325932, 20.656766,  ubicacion21, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325919, 20.656570,  ubicacion21, null, null, 5));

    }
    private void insertarCoordenadasN(OperacionesBaseDatos    datos, String ubicacion20) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.325942, 20.656867,  ubicacion20, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326449, 20.656832,  ubicacion20, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326463, 20.657018,  ubicacion20, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325957, 20.657052,  ubicacion20, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325942, 20.656867,  ubicacion20, null, null, 5));

    }
    private void insertarCoordenadasJ(OperacionesBaseDatos    datos, String ubicacion19) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.326464, 20.656158,  ubicacion19, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326469, 20.656254,  ubicacion19, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326366, 20.656264,  ubicacion19, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326366, 20.656246,  ubicacion19, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326257, 20.656254,  ubicacion19, null, null, 5));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326253, 20.656243,  ubicacion19, null, null, 6));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326137, 20.656251,  ubicacion19, null, null, 7));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326098, 20.656256,  ubicacion19, null, null, 8));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325883, 20.656270,  ubicacion19, null, null, 9));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325847, 20.656273,  ubicacion19, null, null, 10));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325735, 20.656280,  ubicacion19, null, null, 11));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325731, 20.656217,  ubicacion19, null, null, 12));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325744, 20.656216,  ubicacion19, null, null, 13));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325745, 20.656209,  ubicacion19, null, null, 14));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325825, 20.656203,  ubicacion19, null, null, 15));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325825, 20.656190,  ubicacion19, null, null, 16));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325880, 20.656186,  ubicacion19, null, null, 17));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326083, 20.656174,  ubicacion19, null, null, 18));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326150, 20.656167,  ubicacion19, null, null, 19));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326151, 20.656179,  ubicacion19, null, null, 20));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326332, 20.656167,  ubicacion19, null, null, 21));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326335, 20.656198,  ubicacion19, null, null, 22));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326358, 20.656198,  ubicacion19, null, null, 23));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326356, 20.656163,  ubicacion19, null, null, 24));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326464, 20.656158,  ubicacion19, null, null, 25));

    }
    private void insertarCoordenadasD(OperacionesBaseDatos    datos, String ubicacion18) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.325583, 20.654476,  ubicacion18, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325559, 20.654594,  ubicacion18, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325054, 20.654506,  ubicacion18, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325075, 20.654387,  ubicacion18, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325583, 20.654476,  ubicacion18, null, null, 5));

    }
    private void insertarCoordenadasC(OperacionesBaseDatos    datos, String ubicacion17) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.325285, 20.654198,  ubicacion17, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325264, 20.654301,  ubicacion17, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324871, 20.654237,  ubicacion17, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324892, 20.654132,  ubicacion17, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325285, 20.654198,  ubicacion17, null, null, 5));

    }
    private void insertarCoordenadasB(OperacionesBaseDatos    datos, String ubicacion16) {
        /*
        datos.insertarCoordenadas(new Coordenadas(null, -103.325106, 20.654088,  ubicacion16, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324700, 20.654017,  ubicacion16, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325123, 20.653976,  ubicacion16, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324719, 20.653908,  ubicacion16, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325106, 20.654088,  ubicacion16, null, null, 5));
        */
    }
    private void insertarCoordenadasA(OperacionesBaseDatos    datos, String ubicacion15) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.326105, 20.654209,  ubicacion15, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325784, 20.654411,  ubicacion15, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325646, 20.654196,  ubicacion15, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325953, 20.653998,  ubicacion15, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326105, 20.654209,  ubicacion15, null, null, 5));

    }
    private void insertarCoordenadasE(OperacionesBaseDatos    datos, String ubicacion14) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.326108, 20.655533,  ubicacion14, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326116, 20.655529,  ubicacion14, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326844, 20.655473,  ubicacion14, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326848, 20.655413,  ubicacion14, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326968, 20.655417,  ubicacion14, null, null, 5));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326968, 20.655465,  ubicacion14, null, null, 6));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327185, 20.655449,  ubicacion14, null, null, 7));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327198, 20.655557,  ubicacion14, null, null, 8));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326091, 20.655652,  ubicacion14, null, null, 9));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326108, 20.655533,  ubicacion14, null, null, 10));

    }
    private void insertarCoordenadasW(OperacionesBaseDatos    datos, String ubicacion13) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.327266, 20.658116,  ubicacion13, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327240, 20.658112,  ubicacion13, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326682, 20.658144,  ubicacion13, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326674, 20.658037,  ubicacion13, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327257, 20.657985,  ubicacion13, null, null, 5));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327266, 20.658116,  ubicacion13, null, null, 6));
    }
    private void insertarCoordenadasS(OperacionesBaseDatos    datos, String ubicacion12) {
        /*
        datos.insertarCoordenadas(new Coordenadas(null, -103.326491, 20.657539,  ubicacion12, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326010, 20.657570,  ubicacion12, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326500, 20.657714,  ubicacion12, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326246, 20.657735,  ubicacion12, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326240, 20.657660,  ubicacion12, null, null, 5));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326006, 20.657675,  ubicacion12, null, null, 6));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326491, 20.657539,  ubicacion12, null, null, 7));
        */
    }
    private void insertarCoordenadasGama(OperacionesBaseDatos datos, String ubicacion11) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.324951, 20.656323,  ubicacion11, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324961, 20.656376,  ubicacion11, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324940, 20.656383,  ubicacion11, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324947, 20.656429,  ubicacion11, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324975, 20.656427,  ubicacion11, null, null, 5));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324988, 20.656522,  ubicacion11, null, null, 6));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324967, 20.656540,  ubicacion11, null, null, 7));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324971, 20.656545,  ubicacion11, null, null, 8));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324930, 20.656592,  ubicacion11, null, null, 9));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324921, 20.656588,  ubicacion11, null, null, 10));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324891, 20.656621,  ubicacion11, null, null, 11));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324896, 20.656624,  ubicacion11, null, null, 12));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324873, 20.656650,  ubicacion11, null, null, 13));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324805, 20.656595,  ubicacion11, null, null, 14));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324831, 20.656568,  ubicacion11, null, null, 15));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324824, 20.656562,  ubicacion11, null, null, 16));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324865, 20.656517,  ubicacion11, null, null, 17));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324832, 20.656489,  ubicacion11, null, null, 18));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324830, 20.656438,  ubicacion11, null, null, 19));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324888, 20.656433,  ubicacion11, null, null, 20));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324877, 20.656326,  ubicacion11, null, null, 21));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324951, 20.656323,  ubicacion11, null, null, 22));

    }
    private void insertarCoordenadasR(OperacionesBaseDatos    datos, String ubicacion10) {
        /*
        datos.insertarCoordenadas(new Coordenadas(null, -103.325833, 20.657590,  ubicacion10, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325845, 20.657711,  ubicacion10, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325837, 20.657593,  ubicacion10, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325511, 20.657618,  ubicacion10, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325833, 20.657590,  ubicacion10, null, null, 5));
        */
    }
    private void insertarCoordenadasT(OperacionesBaseDatos    datos, String ubicacion9) {
        /*
        datos.insertarCoordenadas(new Coordenadas(null, -103.326080, 20.657462,  ubicacion9, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325170, 20.657938,  ubicacion9, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325162, 20.657856,  ubicacion9, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325803, 20.657842,  ubicacion9, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326080, 20.657462,  ubicacion9, null, null, 5));
        */
    }
    private void insertarCoordenadasO(OperacionesBaseDatos    datos, String ubicacion8) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.326053, 20.657139,  ubicacion8, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326467, 20.657153,  ubicacion8, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326490, 20.657427,  ubicacion8, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326080, 20.657462,  ubicacion8, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326053, 20.657139,  ubicacion8, null, null, 5));

    }
    private void insertarCoordenadasV2(OperacionesBaseDatos   datos, String ubicacion7) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.326560, 20.658142,  ubicacion7, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326547, 20.658032,  ubicacion7, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325932, 20.658069,  ubicacion7, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325942, 20.658177,  ubicacion7, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326560, 20.658142,  ubicacion7, null, null, 5));

    }
    private void insertarCoordenadasU(OperacionesBaseDatos    datos, String ubicacion5) {
        datos.insertarCoordenadas(new Coordenadas(null,-103.325806,20.658159,ubicacion5,null,null,1));
        datos.insertarCoordenadas(new Coordenadas(null,-103.325806,20.658058, ubicacion5,null,null,2));
        datos.insertarCoordenadas(new Coordenadas(null,-103.325107,20.658061,  ubicacion5,null,null,3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325119,20.658135,  ubicacion5,null,null,4));
        datos.insertarCoordenadas(new Coordenadas(null,-103.325806,20.658159,ubicacion5,null,null,5));
    }
    private void insertarCoordenadasX(OperacionesBaseDatos    datos, String ubicacion4) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.327240, 20.658208,ubicacion4, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327255, 20.658288,ubicacion4, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326683,20.658320,ubicacion4, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326687,20.658243,ubicacion4, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327240, 20.658208,ubicacion4, null, null, 5));
    }
    private void insertarCoordenadasV1(OperacionesBaseDatos   datos, String ubicacion6) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.326575, 20.658337,  ubicacion6, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326564, 20.658240,  ubicacion6, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325947, 20.658284,  ubicacion6, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325955, 20.658376,  ubicacion6, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326575, 20.658337,  ubicacion6, null, null, 5));

    }
    private void insertarCoordenadasBeta(OperacionesBaseDatos datos, String ubicacion2) {

        datos.insertarCoordenadas(new Coordenadas(null, -103.325425, 20.656168,  ubicacion2, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325043, 20.656184,  ubicacion2, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325055, 20.656295,  ubicacion2, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325436, 20.656268,  ubicacion2, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325425, 20.656168,  ubicacion2, null, null, 5));

    }
    private void insertarCoordenadasAlfa(OperacionesBaseDatos datos, String ubicacion1) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.325427, 20.656497,  ubicacion1, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325045, 20.656525,  ubicacion1, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325037, 20.656413,  ubicacion1, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325419, 20.656386,  ubicacion1, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325427, 20.656497,  ubicacion1, null, null, 5));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325427, 20.656497,  ubicacion1, null, null, 6));

    }
    private void insertarCoordenadasCucei(OperacionesBaseDatos       datos, String centro1){
        /** INSERTAR COORDENADAS                  ID,  Longitud,   Latitud, fkUbi,fkCentro,fkMarket **/
        /** INSERTAR COORDENADAS DE CUCEI **/
        datos.insertarCoordenadas(new Coordenadas(null, -103.326595, 20.656397, null, centro1, null,1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327721, 20.655883, null, centro1, null,2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327721, 20.655883, null, centro1, null,3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325795, 20.653345, null, centro1, null,4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324238, 20.654013, null, centro1, null,5));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324711, 20.655633, null, centro1, null,6));
        datos.insertarCoordenadas(new Coordenadas(null, -103.323984, 20.656062, null, centro1, null,7));
        datos.insertarCoordenadas(new Coordenadas(null, -103.32418,  20.659559, null, centro1, null,8));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324616, 20.659792, null, centro1, null,9));
        datos.insertarCoordenadas(new Coordenadas(null, -103.324515, 20.658278, null, centro1, null,10));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325909, 20.658169, null, centro1, null,11));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325929, 20.658384, null, centro1, null,12));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326397, 20.658399, null, centro1, null,13));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326466, 20.659354, null, centro1, null,14));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327018, 20.659757, null, centro1, null,15));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327352, 20.659329, null, centro1, null,16));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327261, 20.657806, null, centro1, null,17));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327666, 20.657616, null, centro1, null,18));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327535, 20.657027, null, centro1, null,19));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327385, 20.657037, null, centro1, null,20));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327376, 20.656939, null, centro1, null,21));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326632, 20.656987, null, centro1, null,22));
    }
    private void insertarBibliotecaCucei(OperacionesBaseDatos        datos, String ubicacion3) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.325218, 20.654837,ubicacion3, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325218, 20.654837,ubicacion3, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325470, 20.655045,ubicacion3, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325469, 20.655060,ubicacion3, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325508, 20.655069,ubicacion3, null, null, 5));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325564, 20.655032,ubicacion3, null, null, 6));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325644, 20.655045,ubicacion3, null, null, 7));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325662, 20.654952,ubicacion3, null, null, 8));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325683, 20.654937,ubicacion3, null, null, 9));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325710, 20.654798,ubicacion3, null, null, 10));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325672, 20.654743,ubicacion3, null, null, 11));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325617, 20.654735,ubicacion3, null, null, 12));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325627, 20.654679,ubicacion3, null, null, 13));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325604, 20.654645,ubicacion3, null, null, 14));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325551, 20.654638,ubicacion3, null, null, 15));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325552, 20.654625,ubicacion3, null, null, 16));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325346, 20.654589,ubicacion3, null, null, 17));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325332, 20.654653,ubicacion3, null, null, 18));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325178, 20.654624,ubicacion3, null, null, 19));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325154, 20.654589,ubicacion3, null, null, 20));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325095, 20.654582,ubicacion3, null, null, 21));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325082, 20.654642,ubicacion3, null, null, 22));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325186, 20.654660,ubicacion3, null, null, 23));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325159, 20.654822,ubicacion3, null, null, 24));
    }
    private void insertarCoordenadasCucea(OperacionesBaseDatos       datos, String centro2) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.382824, 20.739913, null, centro2, null,1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.378672, 20.738659, null, centro2, null,2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.377277, 20.744789, null, centro2, null,3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.381579, 20.744227, null, centro2, null,4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.382824, 20.739913, null, centro2, null,5));
    }
    private void insertarCoordenadasAlatorre(OperacionesBaseDatos    datos, String ubicacion29) {

        datos.insertarCoordenadas(new Coordenadas(null, -103.326053, 20.657139,  ubicacion29, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326467, 20.657153,  ubicacion29, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326490, 20.657427,  ubicacion29, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326080, 20.657462,  ubicacion29, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326053, 20.657139,  ubicacion29, null, null, 5));

    }
    private void insertarCoordenadasEnriqueDiaz(OperacionesBaseDatos datos, String ubicacion28) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.325879, 20.654053,  ubicacion28, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325718, 20.654158,  ubicacion28, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325588, 20.653983,  ubicacion28, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325743, 20.653880,  ubicacion28, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325879, 20.654053,  ubicacion28, null, null, 5));

    }
    private void insertarCoordenadasMatuteRemus(OperacionesBaseDatos datos, String ubicacion27) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.325580, 20.656728,  ubicacion27, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325717, 20.656715,  ubicacion27, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325723, 20.656751,  ubicacion27, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325715, 20.656753,  ubicacion27, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325710, 20.656797,  ubicacion27, null, null, 5));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325743, 20.656979,  ubicacion27, null, null, 6));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325729, 20.656981,  ubicacion27, null, null, 7));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325731, 20.657013,  ubicacion27, null, null, 8));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325648, 20.657019,  ubicacion27, null, null, 9));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325644, 20.656987,  ubicacion27, null, null, 10));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325599, 20.656988,  ubicacion27, null, null, 11));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325615, 20.656807,  ubicacion27, null, null, 12));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325600, 20.656759,  ubicacion27, null, null, 13));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325583, 20.656759,  ubicacion27, null, null, 14));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325580, 20.656728,  ubicacion27, null, null, 15));
    }
    private void insertarCoordenadasAntonioRodriguez(OperacionesBaseDatos datos, String ubicacion30) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.327182, 20.655507,  ubicacion30, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327198, 20.655560,  ubicacion30, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326980, 20.655574,  ubicacion30, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326974, 20.655511,  ubicacion30, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327182, 20.655507,  ubicacion30, null, null, 5));

    }

}