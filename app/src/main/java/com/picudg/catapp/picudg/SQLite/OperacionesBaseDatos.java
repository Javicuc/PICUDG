package com.picudg.catapp.picudg.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.renderscript.Double2;
import android.renderscript.Script;
import android.util.Log;
import android.util.Pair;


import com.google.android.gms.maps.model.LatLng;
import com.picudg.catapp.picudg.Modelo.CentroEstudio;
import com.picudg.catapp.picudg.Modelo.Contacto;
import com.picudg.catapp.picudg.Modelo.Contactos_Ubicacion;
import com.picudg.catapp.picudg.Modelo.Coordenadas;
import com.picudg.catapp.picudg.Modelo.Market;
import com.picudg.catapp.picudg.Modelo.Reporte;
import com.picudg.catapp.picudg.Modelo.Ubicacion;
import com.picudg.catapp.picudg.Modelo.Usuario;
import com.picudg.catapp.picudg.SQLite.BaseDatosPicudg.Tablas;

import java.util.ArrayList;

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
            Tablas.CONTACTO + "." + InibdPicudg.Contacto.NOMBRE,
            InibdPicudg.Contacto.ROL,InibdPicudg.Contacto.CORREO,
            Tablas.UBICACION + "." + InibdPicudg.Ubicacion.NOMBRE};
    //PROYECCION DE LOS REPORTES Y MARKETS DE UN USUARIO
    private final String[] proyUsuarioReporteMarket = new String[]{
            Tablas.REPORTE + "." + InibdPicudg.Reporte.ASUNTO,
            Tablas.COORDENADAS + "." + InibdPicudg.Coordenadas.LATITUD,
            Tablas.COORDENADAS + "." + InibdPicudg.Coordenadas.LONGITUD,
            Tablas.USUARIO + "." + InibdPicudg.Usuario.NOMBRE
    };
    //PROYECCION PARA LLENAR EL RECYCLER
    private final String[] proyMarketLatitudLongitud = new String[]{
            Tablas.COORDENADAS + "." + InibdPicudg.Coordenadas.LATITUD,
            Tablas.COORDENADAS + "." + InibdPicudg.Coordenadas.LONGITUD,
            Tablas.MARKET + "." + InibdPicudg.Market.TITULO
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
    //INNER JOIN PARA OBTENER LOS REPORTES, MARKETS DE UN USUARIO
    private static final String MARKET_JOIN_REPORTE_JOIN_USUARIO_NOMBRE = "Market " +
            "INNER JOIN Reporte " +
            "ON Market.ID_Market = Reporte.FK_Market " +
            "INNER JOIN Coordenadas " +
            "ON Market.ID_Market = Coordenadas.FK_Market " +
            "INNER JOIN Usuario " +
            "ON Reporte.FK_Usuario = usuario.ID_Usuario " +
            "WHERE Usuario.Nombre=?";
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
    public Cursor obtenerContactosUbicacionNombre(String Nombre){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String[] selectionArgs = {Nombre};

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
    public Cursor obtenerLatidLongitudCentro(String condicion){
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        Cursor id =  obtenerIdCentroEstudio(condicion);
        id.moveToFirst();
        String ID_Centro = id.getString(id.getColumnIndex("ID_Centro"));

        String[] params = new String[]{ID_Centro};

        String sql = "SELECT Latitud, Longitud FROM " + Tablas.COORDENADAS + " WHERE FK_Centro = ?" + " ORDER BY "+ InibdPicudg.Coordenadas.INSERCION +" ASC";
        id.close();
        return db.rawQuery(sql, params);

    }
    public Cursor obtnerLatittudLongitudUbicacion(String condicion){
        String[] params = new String[]{condicion};
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = "SELECT Latitud, Longitud FROM " + Tablas.COORDENADAS + " WHERE FK_Ubicacion = ?" + " ORDER BY "+ InibdPicudg.Coordenadas.INSERCION +" ASC";
        return db.rawQuery(sql, params);
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
    //OBTIENE TODOS LOS MARKER RELACIONADOS CON LOS REPORTES DE UN USUARIO, LO USAREMOS EN UN RECYCLERVIEW
    public Cursor obtenerMarketCoordenadas(String usuario){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String[] selectionArgs = {usuario};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(MARKET_JOIN_REPORTE_JOIN_USUARIO_NOMBRE);

        return builder.query(db,proyUsuarioReporteMarket,null,selectionArgs,null,null,null);
    }
    public Cursor obtenerMarketLatitudLongitudAll(){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(MARKET_JOIN_COORDENADAS_ALL);

        return builder.query(db,proyMarketLatitudLongitud,null,null,null,null,null);

    }

    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }

}
