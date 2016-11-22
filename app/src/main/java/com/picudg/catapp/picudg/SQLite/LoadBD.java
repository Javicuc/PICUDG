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
            String ubicacion1 = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_ALFA", centro1));
            String ubicacion2 = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_beta", centro1));
            String ubicacion3 = datos.insertarUbicacion(new Ubicacion(null,  "Biblioteca", centro1));
            String ubicacion4 = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_X", centro1));
            String ubicacion5 = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_U", centro1));
            String ubicacion6 = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_V1", centro1));
            String ubicacion7 = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_V2", centro1));
            String ubicacion8 = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_O", centro1));
            String ubicacion9 = datos.insertarUbicacion(new Ubicacion(null,  "EDIF_T", centro1));
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

            /** INSERTAR CONTACTOS **/
            String contacto1 = datos.insertarContacto(new Contacto(null, "Jorge Alberto Calvillo Vargas", "javier.jalr7@gmail.com", "3310086200",
                    "Coodinador de Servicios Generales", centro1));
            String contacto2 = datos.insertarContacto(new Contacto(null, "Edwin Francisco Ruiz Martínez", "javier_jalr@hotmail.com", "3310086201",
                    "Jefe de Unidad de Mantenimiento", centro1));
            String contacto3 = datos.insertarContacto(new Contacto(null, "Ricardo Fernando Sánchez Hernández", "javier.lopez@alumnos.udg.mx", "3310086202",
                    "Jefe de Unidad de Adquisición y Suministros", centro1));
            String contacto4 = datos.insertarContacto(new Contacto(null, "Javier Alejandro Lopez Rangel", "javimex51@gmail.com", "3310086244",
                    "Jefe de Unidad de Computación", centro2));

            /** INSERTAR MARKETS **/
            String market1 = datos.insertarMarket(new Market(null,null,"Reporte","Socket Mal Puesto"));
            String market2 = datos.insertarMarket(new Market(null,null,"Reporte","Fuga De Agua"));
            /** INSERTAR REPORTES **/
            String reporte1 = datos.insertarReportes(new Reporte(null,"Socket Mal Puesto","El socket esta mal puesto",null,usuario1,market1,null));
            String reporte2 = datos.insertarReportes(new Reporte(null,"Fuga De Agua","Hay una fuga de agua en el X",null,usuario1,market2,null));


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

            /** INSERTAR COORDENADAS CUCEI Y EDIFICIOS **/
            insertarCoordenadasCucei(datos,centro1);
            insertarCoordenadasAlfa(datos,ubicacion1);
            insertarCoordenadasBeta(datos,ubicacion2);
            insertarBibliotecaCucei(datos,ubicacion3);
            insertarCoordenadasX(datos,ubicacion4);
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
            insertarCoordenadasMatuteRemus(datos,ubicacion27);
            insertarCoordenadasEnriqueDiaz(datos,ubicacion28);
            insertarCoordenadasAlatorre(datos,ubicacion29);
            insertarCoordenadasAntonioRodriguez(datos,ubicacion30);

            /** INSERTA MARKET DE EJEMPLO **/
            datos.insertarCoordenadas(new Coordenadas(null, -103.325045, 20.656525,null,null,market1,1));
            datos.insertarCoordenadas(new Coordenadas(null, -103.327255, 20.658288,null,null,market2,2));

            datos.eliminarMarket(market2);

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

    private void insertarCoordenadasAntonioRodriguez(OperacionesBaseDatos datos, String ubicacion30) {

    }

    private void insertarCoordenadasAlatorre(OperacionesBaseDatos datos, String ubicacion29) {
    }

    private void insertarCoordenadasEnriqueDiaz(OperacionesBaseDatos datos, String ubicacion28) {
    }

    private void insertarCoordenadasMatuteRemus(OperacionesBaseDatos datos, String ubicacion27) {
    }

    private void insertarCoordenadasQ(OperacionesBaseDatos datos, String ubicacion26) {
    }

    private void insertarCoordenadasG(OperacionesBaseDatos datos, String ubicacion25) {
    }

    private void insertarCoordenadasZ2(OperacionesBaseDatos datos, String ubicacion24) {
    }

    private void insertarCoordenadasZ1(OperacionesBaseDatos datos, String ubicacion23) {
    }

    private void insertarCoordenadasL(OperacionesBaseDatos datos, String ubicacion22) {
    }

    private void insertarCoordenadasM(OperacionesBaseDatos datos, String ubicacion21) {
    }

    private void insertarCoordenadasN(OperacionesBaseDatos datos, String ubicacion20) {
    }

    private void insertarCoordenadasJ(OperacionesBaseDatos datos, String ubicacion19) {
    }

    private void insertarCoordenadasD(OperacionesBaseDatos datos, String ubicacion18) {
    }

    private void insertarCoordenadasC(OperacionesBaseDatos datos, String ubicacion17) {
    }

    private void insertarCoordenadasB(OperacionesBaseDatos datos, String ubicacion16) {
    }

    private void insertarCoordenadasA(OperacionesBaseDatos datos, String ubicacion15) {
    }

    private void insertarCoordenadasE(OperacionesBaseDatos datos, String ubicacion14) {
    }

    private void insertarCoordenadasW(OperacionesBaseDatos datos, String ubicacion13) {
    }

    private void insertarCoordenadasS(OperacionesBaseDatos datos, String ubicacion12) {
    }

    private void insertarCoordenadasGama(OperacionesBaseDatos datos, String ubicacion11) {
    }

    private void insertarCoordenadasR(OperacionesBaseDatos datos, String ubicacion10) {
    }

    private void insertarCoordenadasT(OperacionesBaseDatos datos, String ubicacion9) {
    }

    private void insertarCoordenadasO(OperacionesBaseDatos datos, String ubicacion8) {
    }

    private void insertarCoordenadasV2(OperacionesBaseDatos datos, String ubicacion7) {
    }

    private void insertarCoordenadasU(OperacionesBaseDatos datos, String ubicacion5) {
        datos.insertarCoordenadas(new Coordenadas(null,-103.325806,20.658159,ubicacion5,null,null,1));
        datos.insertarCoordenadas(new Coordenadas(null,-103.325806,20.658058, ubicacion5,null,null,2));
        datos.insertarCoordenadas(new Coordenadas(null,-103.325107,20.658061,  ubicacion5,null,null,3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.325119,20.658135,  ubicacion5,null,null,4));
        datos.insertarCoordenadas(new Coordenadas(null,-103.325806,20.658159,ubicacion5,null,null,5));
    }

    private void insertarCoordenadasX(OperacionesBaseDatos datos, String ubicacion4) {
        datos.insertarCoordenadas(new Coordenadas(null, -103.327240, 20.658208,ubicacion4, null, null, 1));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327255, 20.658288,ubicacion4, null, null, 2));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326683,20.658320,ubicacion4, null, null, 3));
        datos.insertarCoordenadas(new Coordenadas(null, -103.326687,20.658243,ubicacion4, null, null, 4));
        datos.insertarCoordenadas(new Coordenadas(null, -103.327240, 20.658208,ubicacion4, null, null, 5));
    }

    private void insertarCoordenadasV1(OperacionesBaseDatos datos, String ubicacion6) {
    }

    private void insertarCoordenadasBeta(OperacionesBaseDatos datos, String ubicacion2) {
    }

    private void insertarCoordenadasAlfa(OperacionesBaseDatos datos, String ubicacion1) {

    }
    public void insertarCoordenadasCucei(OperacionesBaseDatos datos, String centro1){
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
    public void insertarBibliotecaCucei(OperacionesBaseDatos datos, String ubicacion3) {
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
}

