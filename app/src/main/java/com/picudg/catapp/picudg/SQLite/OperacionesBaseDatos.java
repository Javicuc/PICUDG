package com.picudg.catapp.picudg.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.renderscript.Double2;
import android.renderscript.Script;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.util.Pair;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.picudg.catapp.picudg.Modelo.CentroEstudio;
import com.picudg.catapp.picudg.Modelo.Contacto;
import com.picudg.catapp.picudg.Modelo.Contactos_Ubicacion;
import com.picudg.catapp.picudg.Modelo.Coordenadas;
import com.picudg.catapp.picudg.Modelo.Market;
import com.picudg.catapp.picudg.Modelo.Reporte;
import com.picudg.catapp.picudg.Modelo.ReporteCardView;
import com.picudg.catapp.picudg.Modelo.Ubicacion;
import com.picudg.catapp.picudg.Modelo.Usuario;
import com.picudg.catapp.picudg.SQLite.BaseDatosPicudg.Tablas;
import com.picudg.catapp.picudg.Tools.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javi-cuc on 14/11/2016.
 */

public final class OperacionesBaseDatos {

    /** PATRON DE DISEÑO SINGLETON PARA MANENER UNA SOLA CONEXION **/
    private static  BaseDatosPicudg baseDatos;
    private static OperacionesBaseDatos instancia = new OperacionesBaseDatos();
    private OperacionesBaseDatos(){}
    public static OperacionesBaseDatos obtenerInstancia(Context contexto){
        if(baseDatos == null){
            baseDatos = new BaseDatosPicudg(contexto);
        }
        return instancia;
    }

    /** LISTA DE PROYECCIONES PARA ALGUNAS TABLAS **/
    //PROYECCION DE LOS CONTACTOS (NOMBRE,ROL,CORREO) Y SUS RESPECTIVAS AREAS (NOMBRE)
    private final String[] proyContactosUbicaciones = new String[]{
            Tablas.CONTACTO  + "." + InibdPicudg.Contacto.NOMBRE,
            InibdPicudg.Contacto.ROL,
            InibdPicudg.Contacto.CORREO};
            //Tablas.UBICACION + "." + InibdPicudg.Ubicacion.NOMBRE};

    //PROYECCION DE LOS REPORTES Y MARKETS DE UN USUARIO
    private final String[] proyUsuarioReporteMarket = new String[]{
            Tablas.REPORTE     + "." + InibdPicudg.Reporte.ASUNTO,
            Tablas.COORDENADAS + "." + InibdPicudg.Coordenadas.LATITUD,
            Tablas.COORDENADAS + "." + InibdPicudg.Coordenadas.LONGITUD,
            Tablas.USUARIO     + "." + InibdPicudg.Usuario.NOMBRE
    };
    //PROYECCION PARA LLENAR LOS CARDVIEWS DEL RECYCLER
    private final String[] proyReportesCardView = new String[]{
            Tablas.MARKET  + "." + InibdPicudg.Market.TITULO,
            Tablas.USUARIO + "." + InibdPicudg.Usuario.NOMBRE,
            Tablas.USUARIO + "." + InibdPicudg.Usuario.FK_CENTRO,
            Tablas.REPORTE + "." + InibdPicudg.Reporte.DESCRIPCION,
            Tablas.REPORTE + "." + InibdPicudg.Reporte.IMAGENURI,
            Tablas.REPORTE + "." + InibdPicudg.Reporte.FECHA,
            Tablas.REPORTE + "." + InibdPicudg.Reporte.UBICACION
    };
    //PROYECCION PARA LLENAR LOS MARKETS EN EL MAPA
    private final String[] proyMarketLatitudLongitud = new String[]{
            Tablas.MARKET      + "." + InibdPicudg.Market.TITULO,
            Tablas.COORDENADAS + "." + InibdPicudg.Coordenadas.LATITUD,
            Tablas.COORDENADAS + "." + InibdPicudg.Coordenadas.LONGITUD
    };
    //PROYECCION PARA OBTENER LATITUD, LONGITUD, NOMBRE DE LOS EDIFICIOS DE UN CENTRO
    private final String[] proyCoordenadasUbicacionCentro = new String[]{
            Tablas.COORDENADAS + "." + InibdPicudg.Coordenadas.LATITUD,
            Tablas.COORDENADAS + "." + InibdPicudg.Coordenadas.LONGITUD,
            Tablas.UBICACION   + "." + InibdPicudg.Ubicacion.NOMBRE
    };
    //INNER JOIN PARA CONSULTAR LOS CONTACTOS DE TODAS LAS AREAS
    private static final String CONTACTO_JOIN_UBICACION_ENCARGADOS = "Contacto " +
            "INNER JOIN Contactos_Ubicacion " +
                    "ON ID_Contacto = Contactos_Ubicacion.FK_Contacto " +
                    "INNER JOIN Ubicacion " +
                    "ON ID_Ubicacion = Contactos_Ubicacion.FK_Ubicacion ";
    //INNER JOIN PARA CONSULTAR LOS CONTACTOS ENCARGADOS DE UNA AREA
    private static final String CONTACTO_JOIN_UBICACION_ENCARGADOS_CONDICION = "Contacto " +
            "INNER JOIN Contactos_Ubicacion " +
            "ON ID_Contacto = Contactos_Ubicacion.FK_Contacto " +
            "INNER JOIN Ubicacion " +
            "ON ID_Ubicacion = Contactos_Ubicacion.FK_Ubicacion " +
            "WHERE Ubicacion.Nombre=?";
    //INNER JOIN PARA OBTENER LOS REPORTES, MARKETS, COORDENADAS DE UN USUARIO
    private static final String MARKET_JOIN_REPORTE_JOIN_USUARIO = "Market " +
            "INNER JOIN Reporte " +
            "ON Market.ID_Market = Reporte.FK_Market " +
            "INNER JOIN Coordenadas " +
            "ON Market.ID_Market = Coordenadas.FK_Market " +
            "INNER JOIN Usuario " +
            "ON Reporte.FK_Usuario = usuario.ID_Usuario ";
    //INNER JOIN PARA OBTENER LOS REPORTES, MARKETS, COORDENADAS REGISTRADOS EN LA BD
    private static final String MARKET_JOIN_COORDENADAS_ALL = "Market " +
            "INNER JOIN Reporte " +
            "ON Market.ID_Market = Reporte.FK_Market " +
            "INNER JOIN Coordenadas " +
            "ON Market.ID_Market = Coordenadas.FK_Market ";

    /**  OPERACIONES CON CONTACTOS **/
    public String insertarContacto(Contacto nvContacto){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        //Generar PK
        String idContacto = InibdPicudg.Contacto.generarIdContacto();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Contacto.ID_CONTACTO, idContacto);
        valores.put(InibdPicudg.Contacto.NOMBRE, nvContacto.nombreContacto);
        valores.put(InibdPicudg.Contacto.CORREO,nvContacto.correoContacto);
        valores.put(InibdPicudg.Contacto.TELEFONO,nvContacto.telefonoContacto);
        valores.put(InibdPicudg.Contacto.ROL,nvContacto.rolContacto);
        valores.put(InibdPicudg.Contacto.FK_CENTRO,nvContacto.fk_Centro);

        db.insertOrThrow(Tablas.CONTACTO,null,valores);

        return idContacto;
    }
    public boolean actualizarContacto(Contacto nvContacto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Contacto.NOMBRE, nvContacto.nombreContacto);
        valores.put(InibdPicudg.Contacto.CORREO,nvContacto.correoContacto);
        valores.put(InibdPicudg.Contacto.TELEFONO,nvContacto.telefonoContacto);
        valores.put(InibdPicudg.Contacto.ROL,nvContacto.rolContacto);
        valores.put(InibdPicudg.Contacto.FK_CENTRO,nvContacto.fk_Centro);

        String whereClause = String.format("%s=?", InibdPicudg.Contacto.ID_CONTACTO);
        String[] whereArgs = {nvContacto.idContacto};

        int resultado = db.update(Tablas.CONTACTO, valores, whereClause, whereArgs);

        return resultado > 0;
    }
    public boolean eliminarContacto(String idContacto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = InibdPicudg.Contacto.ID_CONTACTO + "=?";
        String[] whereArgs = {idContacto};

        int resultado = db.delete(Tablas.CONTACTO, whereClause, whereArgs);

        return resultado > 0;
    }
    public Cursor obtenerContactos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.CONTACTO);

        return db.rawQuery(sql, null);
    }

    /** OPERACIONES CON UBICACIONES **/
    public String insertarUbicacion(Ubicacion nvUbicacion){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        //Generar PK
        String idUbicacion = InibdPicudg.Ubicacion.generarIdUbicacion();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Ubicacion.ID_UBICACION, idUbicacion);
        valores.put(InibdPicudg.Ubicacion.NOMBRE,nvUbicacion.nombreUbicacion);
        valores.put(InibdPicudg.Ubicacion.FK_CENTRO,nvUbicacion.fk_Centro);

        db.insertOrThrow(Tablas.UBICACION,null,valores);

        return idUbicacion;
    }
    public boolean actualizarUbicacion(Ubicacion nvUbicacion) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Ubicacion.NOMBRE,nvUbicacion.nombreUbicacion);
        valores.put(InibdPicudg.Ubicacion.FK_CENTRO,nvUbicacion.fk_Centro);

        String whereClause = String.format("%s=?", InibdPicudg.Ubicacion.ID_UBICACION);
        String[] whereArgs = {nvUbicacion.idUbicacion};

        int resultado = db.update(Tablas.UBICACION, valores, whereClause, whereArgs);

        return resultado > 0;
    }
    public boolean eliminarUbicacion(String idUbicacion) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = InibdPicudg.Ubicacion.ID_UBICACION + "=?";
        String[] whereArgs = {idUbicacion};

        int resultado = db.delete(Tablas.UBICACION, whereClause, whereArgs);

        return resultado > 0;
    }
    public Cursor obtenerUbicaciones() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.UBICACION);

        return db.rawQuery(sql, null);
    }

    /** OPERACIONES CON CENTROS DE ESTUDIOS */
    public String insertarCentroEstudio(CentroEstudio nvCentro){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        //Generar PK
        String idCentroEstudio = InibdPicudg.CentroEstudio.gemerarIdCentroEstudio();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.CentroEstudio.ID_CENTROESTUDIO, idCentroEstudio);
        valores.put(InibdPicudg.CentroEstudio.NOMBRE,nvCentro.nombreCentro);
        valores.put(InibdPicudg.CentroEstudio.ACRONIMO,nvCentro.acronimoCentro);
        valores.put(InibdPicudg.CentroEstudio.DIRECCION,nvCentro.direccionCentro);

        db.insertOrThrow(Tablas.CENTROESTUDIO,null,valores);

        return idCentroEstudio;
    }
    public boolean actualizarCentroEstudio(CentroEstudio nvCentro) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.CentroEstudio.NOMBRE,nvCentro.nombreCentro);
        valores.put(InibdPicudg.CentroEstudio.ACRONIMO,nvCentro.acronimoCentro);
        valores.put(InibdPicudg.CentroEstudio.DIRECCION,nvCentro.direccionCentro);

        String whereClause = String.format("%s=?", InibdPicudg.CentroEstudio.ID_CENTROESTUDIO);
        String[] whereArgs = {nvCentro.idCentroEstudio};

        int resultado = db.update(Tablas.CENTROESTUDIO, valores, whereClause, whereArgs);

        return resultado > 0;
    }
    public boolean eliminarCentroEstudio(String idCentroEstudio) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = InibdPicudg.CentroEstudio.ID_CENTROESTUDIO + "=?";
        String[] whereArgs = {idCentroEstudio};

        int resultado = db.delete(Tablas.CENTROESTUDIO, whereClause, whereArgs);

        return resultado > 0;
    }
    public Cursor obtenerCentroEstudio() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.CENTROESTUDIO);

        return db.rawQuery(sql, null);
    }
    public Cursor obtenerIdCentroEstudio(String acronimo){
        String[] params = new String[]{acronimo};
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = "SELECT * FROM " + Tablas.CENTROESTUDIO + " WHERE Acronimo_Centro = ?";
        return  db.rawQuery(sql,params);
    }
    public String obtenerNombreCentro(String id){
        String[] params = new String[]{id};
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String nombreCentro = "Desconocido";
        String sql = "SELECT * FROM " + Tablas.CENTROESTUDIO + " WHERE ID_Centro = ?";
        Cursor c = db.rawQuery(sql,params);
        c.moveToFirst();
        if(c != null)
            nombreCentro = c.getString(c.getColumnIndex("Acronimo_Centro"));
        return  nombreCentro;
    }

    /** OPERACIONES CON LA TABLA INTERMEDIA CONTACTOS-UBICACIONES **/
    public String insertarContactosUbicaciones(Contactos_Ubicacion nvCU){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        //Generar PK
        String idContactos_ubicacion = InibdPicudg.Contactos_Ubicacion.generarIdContactos_Ubicacion();

        ContentValues valores = new ContentValues();

        valores.put(InibdPicudg.Contactos_Ubicacion.ID_CONTACTOSUBICACION,idContactos_ubicacion);
        valores.put(InibdPicudg.Contactos_Ubicacion.FK_UBICACION,nvCU.fk_Ubicacion);
        valores.put(InibdPicudg.Contactos_Ubicacion.FK_CONTACTO,nvCU.fk_Contacto);

        db.insertOrThrow(Tablas.CONTACTOS_UBICACION,null,valores);

       return idContactos_ubicacion;
    }
    public boolean actualizarContactosUbicaciones(Contactos_Ubicacion updateCC){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Contactos_Ubicacion.FK_UBICACION,updateCC.fk_Ubicacion);
        valores.put(InibdPicudg.Contactos_Ubicacion.FK_CONTACTO,updateCC.fk_Contacto);

        String whereClause = String.format("%s=?", InibdPicudg.Contactos_Ubicacion.ID_CONTACTOSUBICACION);
        String[] whereArgs = {updateCC.idContactosUbicacion};

        int resultado = db.update(Tablas.CONTACTOS_UBICACION, valores, whereClause, whereArgs);

        return resultado > 0;
    }
    public boolean eliminarContactosUsuario(String idCC){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = InibdPicudg.Contactos_Ubicacion.ID_CONTACTOSUBICACION + "=?";
        String[] whereArgs = {idCC};

        int resultado = db.delete(Tablas.CONTACTOS_UBICACION, whereClause, whereArgs);

        return resultado > 0;
    }
    public Cursor obtenerContactosUbicacionesALL() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.CONTACTOS_UBICACION);

        return db.rawQuery(sql, null);
    }
    // OBTENEMOS TODOS LOS CONTACTOS CON SUS RESPECTIVAS UBICACIONES
    public Cursor obtenerContactosUbicaciones() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(CONTACTO_JOIN_UBICACION_ENCARGADOS);

        return builder.query(db, proyContactosUbicaciones, null, null, null, null, null);
    }
    // OBTENEMOS LOS CONTACTOS DE UNA RESPECTIVA AREA
    public Cursor obtenerContactosUbicacionEdificio(String edificio){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String[] selectionArgs = {edificio};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CONTACTO_JOIN_UBICACION_ENCARGADOS_CONDICION);

        return builder.query(db,proyContactosUbicaciones,null,selectionArgs,null,null,null);
    }
    public Cursor obtenerIdUbicacion(String nombreUbicacion){
        String[] params = new String[]{nombreUbicacion};
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = "SELECT * FROM " + Tablas.UBICACION + " WHERE Nombre = ?";

        return  db.rawQuery(sql,params);
    }

    /** OPERACIONES CON LA TABLA COORDENADAS **/
    public String insertarCoordenadas(Coordenadas coordenadas){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        //Generar PK
        String idCoordenada = InibdPicudg.Coordenadas.generarIdCoordenadas();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Coordenadas.ID_COORDENADA, idCoordenada);
        valores.put(InibdPicudg.Coordenadas.LONGITUD, coordenadas.Longitud);
        valores.put(InibdPicudg.Coordenadas.LATITUD, coordenadas.Latitud);
        valores.put(InibdPicudg.Coordenadas.FK_UBICACION, coordenadas.fk_Ubicacion);
        valores.put(InibdPicudg.Coordenadas.FK_CENTRO, coordenadas.fk_Centro);
        valores.put(InibdPicudg.Coordenadas.FK_MARKET, coordenadas.fk_Market);
        valores.put(InibdPicudg.Coordenadas.INSERCION, coordenadas.insercion);

        db.insertOrThrow(Tablas.COORDENADAS,null,valores);

        return idCoordenada;
    }
    public boolean actualizarCoordenadas(Coordenadas updateCoordenadas){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Coordenadas.LONGITUD, updateCoordenadas.Longitud);
        valores.put(InibdPicudg.Coordenadas.LATITUD, updateCoordenadas.Latitud);
        valores.put(InibdPicudg.Coordenadas.FK_UBICACION, updateCoordenadas.fk_Ubicacion);
        valores.put(InibdPicudg.Coordenadas.FK_CENTRO, updateCoordenadas.fk_Centro);
        valores.put(InibdPicudg.Coordenadas.FK_MARKET, updateCoordenadas.fk_Market);
        valores.put(InibdPicudg.Coordenadas.INSERCION, updateCoordenadas.insercion);

        String whereClause = String.format("%s=?", InibdPicudg.Coordenadas.ID_COORDENADA);
        String[] whereArgs = {updateCoordenadas.idCoordenada};

        int resultado = db.update(Tablas.COORDENADAS, valores, whereClause, whereArgs);
        return resultado > 0;
    }
    public boolean deleteCoordenadas(String idCoordenada){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = InibdPicudg.Coordenadas.ID_COORDENADA + "=?";
        String[] whereArgs = {idCoordenada};

        int resultado = db.delete(Tablas.COORDENADAS, whereClause, whereArgs);
        return resultado > 0;
    }
    public Cursor obtenerCoordenadas() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.COORDENADAS);

        return db.rawQuery(sql, null);
    }
    public Cursor obtenerLatidLongitudCentro(String acronimo){
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        Cursor id =  obtenerIdCentroEstudio(acronimo);
        String ID_Centro;
        String[] params = new String[0];
        id.moveToFirst();
        if(id != null) {
            ID_Centro = id.getString(id.getColumnIndex("ID_Centro"));
            params = new String[]{ID_Centro};
        }
        String sql = "SELECT Latitud, Longitud FROM " + Tablas.COORDENADAS + " WHERE FK_Centro = ?" + " ORDER BY "+ InibdPicudg.Coordenadas.INSERCION +" ASC";
        id.close();

        return db.rawQuery(sql, params);

    }
    //OBTENEMOS LOS POLIGONOS DE TODOS LOS CENTROS DE ESTUDIO, Y SU ACRONIMO
    public List<Pair> obtenerPoligonosCentros(){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        List<LatLng> listCoor = new ArrayList<>();
        List<Pair> listPolys = new ArrayList<>();
        PolygonOptions mPoly;
        String tmpAcronimo;

        String sql = "SELECT Insercion, Latitud, Longitud, CentroEstudio.Acronimo_Centro FROM " + Tablas.COORDENADAS + " INNER JOIN " + Tablas.CENTROESTUDIO +
                " ON Coordenadas.FK_Centro = ID_Centro ORDER BY " + InibdPicudg.Coordenadas.INSERCION + " AND "+ InibdPicudg.CentroEstudio.ACRONIMO +" ASC";
        Cursor c = db.rawQuery(sql,null);
        c.moveToFirst();
        if(c != null){
            while (!c.isAfterLast()){
                tmpAcronimo = c.getString(c.getColumnIndex("Acronimo_Centro"));
                LatLng mLatLng= new LatLng(c.getDouble(c.getColumnIndex("Latitud")),c.getDouble(c.getColumnIndex("Longitud")));
                listCoor.add(mLatLng);
                Log.i("Flag2->",c.getString(c.getColumnIndex("Acronimo_Centro")));
                if((c.moveToNext() && !tmpAcronimo.equals(c.getString(c.getColumnIndex("Acronimo_Centro")))) || c.isLast()){
                    mPoly = new PolygonOptions()
                            .strokeColor(0xE6CCFFFF)
                            .fillColor(0x7FCCFFFF);
                    mPoly.addAll(listCoor);
                    Log.i("Flag1->",c.getString(c.getColumnIndex("Acronimo_Centro")));
                    listPolys.add(new Pair(mPoly,tmpAcronimo));
                    listCoor.clear();
                }
            }
        }
        return listPolys;
    }
    public Cursor obtnerLatittudLongitudUbicacionID(String condicion){
        String[] params = new String[]{condicion};
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = "SELECT Latitud, Longitud FROM " + Tablas.COORDENADAS + " WHERE FK_Ubicacion = ?" + " ORDER BY "+ InibdPicudg.Coordenadas.INSERCION +" ASC";
        return db.rawQuery(sql, params);
    }
    public List<Pair> obtenerEdificiosCentro(String centroNombre){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        Cursor idCentro = obtenerIdCentroEstudio(centroNombre);
        idCentro.moveToFirst();

        String[] selectionArgs = {idCentro.getString(idCentro.getColumnIndex("ID_Centro"))};

        String sql = "SELECT Insercion, Latitud, Longitud, Ubicacion.Nombre FROM " + Tablas.COORDENADAS + " INNER JOIN Ubicacion " +
                "ON Ubicacion.ID_Ubicacion = Coordenadas.FK_Ubicacion " +
                "WHERE Ubicacion.FK_Centro =? ORDER BY " + InibdPicudg.Coordenadas.INSERCION + " AND " + InibdPicudg.Ubicacion.NOMBRE +" ASC";

        String tmpUbicacion;
        List<LatLng> listCoor = new ArrayList<>();
        List<Pair> listPolys = new ArrayList<>();
        PolygonOptions mPoly;

        Cursor c = db.rawQuery(sql,selectionArgs);
        c.moveToFirst();
        while (!c.isAfterLast()){
            tmpUbicacion = c.getString(c.getColumnIndex("Nombre"));
            LatLng mLatLng= new LatLng(c.getDouble(c.getColumnIndex("Latitud")),c.getDouble(c.getColumnIndex("Longitud")));
            listCoor.add(mLatLng);
            Log.i("NombreTMP->",tmpUbicacion);
            if((c.moveToNext() && !tmpUbicacion.equals(c.getString(c.getColumnIndex("Nombre")))) || c.isLast()){
                mPoly = new PolygonOptions()
                        .strokeColor(0xE6CC45FF)
                        .fillColor(0xE6CC45FF);
                mPoly.addAll(listCoor);
                Log.i("Flag->",c.getString(c.getColumnIndex("Nombre")));
                listPolys.add(new Pair(mPoly,tmpUbicacion));
                listCoor.clear();
            }
        }
        return listPolys;
    }
    public Cursor obtenerEdificiosSpinner(String acronimo){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String[] selectionArgs = {acronimo};

        String sql = "SELECT * FROM " + Tablas.UBICACION + " INNER JOIN " + Tablas.CENTROESTUDIO +
                " ON Ubicacion.FK_Centro = CentroEstudio.ID_Centro " +
                "WHERE CentroEstudio.Acronimo_Centro =? " + "ORDER BY " + InibdPicudg.Ubicacion.NOMBRE + " ASC";

        return db.rawQuery(sql,selectionArgs);

    }

    /** OPERACIONES CON LA TABLA REPORTE**/
    public String insertarReportes(Reporte reporte){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        //Generar PK
        String idReporte = InibdPicudg.Reporte.generarIdReporte();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Reporte.ID_REPORTE,  idReporte);
        valores.put(InibdPicudg.Reporte.ASUNTO,      reporte.asuntoReporte);
        valores.put(InibdPicudg.Reporte.DESCRIPCION, reporte.descripcionReporte);
        valores.put(InibdPicudg.Reporte.REPORTEURI,  reporte.reporteuriReporte);
        valores.put(InibdPicudg.Reporte.FK_USUARIO,  reporte.fk_Usuario);
        valores.put(InibdPicudg.Reporte.FK_MARKET,   reporte.fk_Market);
        valores.put(InibdPicudg.Reporte.IMAGENURI,   reporte.imagenUri);
        valores.put(InibdPicudg.Reporte.FECHA,       reporte.fecha);
        valores.put(InibdPicudg.Reporte.UBICACION,   reporte.ubicacion);

        db.insertOrThrow(Tablas.REPORTE,null,valores);

        return idReporte;
    }
    public boolean actualizarReporte(Reporte updateReporte){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Reporte.ASUNTO,      updateReporte.asuntoReporte);
        valores.put(InibdPicudg.Reporte.DESCRIPCION, updateReporte.descripcionReporte);
        valores.put(InibdPicudg.Reporte.REPORTEURI,  updateReporte.reporteuriReporte);
        valores.put(InibdPicudg.Reporte.FK_USUARIO,  updateReporte.fk_Usuario);
        valores.put(InibdPicudg.Reporte.FK_MARKET,   updateReporte.fk_Market);
        valores.put(InibdPicudg.Reporte.IMAGENURI,   updateReporte.imagenUri);

        String whereClause = String.format("%s=?", InibdPicudg.Reporte.ID_REPORTE);
        String[] whereArgs = {updateReporte.idReporte};

        int resultado = db.update(Tablas.REPORTE, valores, whereClause, whereArgs);
        return resultado > 0;
    }
    public boolean eliminarReporte(String idReporte){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = InibdPicudg.Reporte.ID_REPORTE + "=?";
        String[] whereArgs = {idReporte};

        int resultado = db.delete(Tablas.REPORTE, whereClause, whereArgs);
        return resultado > 0;
    }
    public Cursor obtenerReportes() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.REPORTE);

        return db.rawQuery(sql, null);
    }
    public List<ReporteCardView> obtenerListaReportes(){
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        List<ReporteCardView>  list = new ArrayList<>();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(MARKET_JOIN_REPORTE_JOIN_USUARIO);

        Cursor c  =  builder.query(db,proyReportesCardView,null,null,null,null,null);
        c.moveToFirst();

        String nombreCentro = obtenerNombreCentro(c.getString(c.getColumnIndex("FK_Centro")));

        while(!c.isAfterLast()){
            DatabaseUtils.dumpCursor(c);
            ReporteCardView reporte = new ReporteCardView(c.getString(c.getColumnIndex("Titulo")),nombreCentro,
                    c.getString(c.getColumnIndex("Ubicacion")),c.getString(c.getColumnIndex("Descripcion")),
                    c.getString(c.getColumnIndex("Imagen")),c.getString(c.getColumnIndex("Nombre")),c.getString(c.getColumnIndex("Fecha")));
            list.add(reporte);
            c.moveToNext();
        }
        return list;
    }

    /** OPERACIONES CON LA TABLA USUARIO **/
    public String insertarUsuario(Usuario usuario){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        //Generar PK
        String idUsuario = InibdPicudg.Usuario.geneararIdUsuario();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Usuario.ID_USUARIO, idUsuario);
        valores.put(InibdPicudg.Usuario.NOMBRE, usuario.nombreUsuario);
        valores.put(InibdPicudg.Usuario.CORREO, usuario.correoUsuario);
        valores.put(InibdPicudg.Usuario.CODIGO, usuario.codigoUsuario);
        valores.put(InibdPicudg.Usuario.FK_CENTRO, usuario.fk_Centro);

        db.insertOrThrow(Tablas.USUARIO,null,valores);

        return idUsuario;
    }
    public boolean actualizarUsuario(Usuario updateUsuario){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Usuario.NOMBRE, updateUsuario.nombreUsuario);
        valores.put(InibdPicudg.Usuario.CORREO, updateUsuario.correoUsuario);
        valores.put(InibdPicudg.Usuario.CODIGO, updateUsuario.codigoUsuario);
        valores.put(InibdPicudg.Usuario.FK_CENTRO, updateUsuario.fk_Centro);

        String whereClause = String.format("%s=?", InibdPicudg.Usuario.ID_USUARIO);
        String[] whereArgs = {updateUsuario.idUsuario};

        int resultado = db.update(Tablas.USUARIO, valores, whereClause, whereArgs);
        return resultado > 0;
    }
    public boolean eliminarUsuario(String idUsuario){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = InibdPicudg.Usuario.ID_USUARIO + "=?";
        String[] whereArgs = {idUsuario};

        int resultado = db.delete(Tablas.USUARIO, whereClause, whereArgs);
        return resultado > 0;
    }
    public Cursor obtenerUsuarios() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.USUARIO);

        return db.rawQuery(sql, null);
    }
    public Cursor obtenerUsuarioCorreo(String correo){
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String[] params = new String[]{correo};

        String sql = "SELECT * FROM " + Tablas.USUARIO + " WHERE Correo = ?";

        return db.rawQuery(sql, params);
    }

    /** OPERACIONES CON LA TABLA MARKET **/
    public String insertarMarket(Market market){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        //Generar PK
        String idMarket = InibdPicudg.Market.generarIdMarket();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Market.ID_MARKET, idMarket);
        valores.put(InibdPicudg.Market.ICON, market.iconMarket);
        valores.put(InibdPicudg.Market.TIPO, market.tipoMarket);
        valores.put(InibdPicudg.Market.TITULO, market.tituloMarket);

        db.insertOrThrow(Tablas.MARKET,null,valores);

        return idMarket;
    }
    public boolean actualizarMarket(Market updateMarket){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(InibdPicudg.Market.ICON, updateMarket.iconMarket);
        valores.put(InibdPicudg.Market.TIPO, updateMarket.tipoMarket);
        valores.put(InibdPicudg.Market.TITULO, updateMarket.tituloMarket);

        String whereClause = String.format("%s=?", InibdPicudg.Market.ID_MARKET);
        String[] whereArgs = {updateMarket.idMarket};

        int resultado = db.update(Tablas.MARKET, valores, whereClause, whereArgs);
        return resultado > 0;
    }
    public boolean eliminarMarket(String idMarket){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = InibdPicudg.Market.ID_MARKET + "=?";
        String[] whereArgs = {idMarket};

        int resultado = db.delete(Tablas.MARKET, whereClause, whereArgs);
        return resultado > 0;
    }
    public boolean eliminarMarketNombre(String nombre){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = InibdPicudg.Market.TITULO + "=?";
        String[] whereArgs = {nombre};

        int resultado = db.delete(Tablas.MARKET, whereClause,whereArgs);
        return resultado > 0;
    }
    public Cursor obtenerMarket() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.MARKET);

        return db.rawQuery(sql, null);
    }
    public int obtenerCountMarkets() {
        String countQuery = "SELECT  * FROM " + Tablas.COORDENADAS + " WHERE FK_Market is NOT NULL";
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        int cnt = 0;

        Cursor cursor = db.rawQuery(countQuery,null);
        if(cursor.getCount() > 0){
            cnt = cursor.getCount();
        }
        cursor.close();
        return cnt;
    }


    /** QUERYS ESPECIFICAS
     * PARA EL FUNCIONAMIENTO DE LA APP **/

    //OBTIENE TODOS LOS MARKER RELACIONADOS CON LOS REPORTES DE UN USUARIO
    public Cursor obtenerReportesMarketCoordenadas(String usuario){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String[] selectionArgs = {usuario};
        String whereClause = InibdPicudg.Usuario.NOMBRE + "=?";

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(MARKET_JOIN_REPORTE_JOIN_USUARIO);

        return builder.query(db,proyUsuarioReporteMarket,whereClause,selectionArgs,null,null,null);
    }
    //OBTIENE TODOS LOS MARKERS REGISTRADOS EN LA BASE DE DATOS
    public Cursor obtenerMarketLatitudLongitudAll(){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(MARKET_JOIN_COORDENADAS_ALL);

        return builder.query(db,proyMarketLatitudLongitud,null,null,null,null,null);

    }
    //RETORNA PERMISOS DE ESCRITURA PARA LA BD
    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }

}
