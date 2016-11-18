package com.picudg.catapp.picudg.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.design.widget.CoordinatorLayout;

import com.picudg.catapp.picudg.SQLite.InibdPicudg.CentroEstudio;
import com.picudg.catapp.picudg.SQLite.InibdPicudg.Contacto;
import com.picudg.catapp.picudg.SQLite.InibdPicudg.Contactos_Ubicacion;
import com.picudg.catapp.picudg.SQLite.InibdPicudg.Coordenadas;
import com.picudg.catapp.picudg.SQLite.InibdPicudg.Market;
import com.picudg.catapp.picudg.SQLite.InibdPicudg.Reporte;
import com.picudg.catapp.picudg.SQLite.InibdPicudg.Ubicacion;
import com.picudg.catapp.picudg.SQLite.InibdPicudg.Usuario;
import com.picudg.catapp.picudg.Tools.Report;


/**
 * Created by Javi-cuc on 13/11/2016.
 */

public class BaseDatosPicudg extends SQLiteOpenHelper{

    private static final String NOMBRE_BASE_DATOS = "PICUDG.db";

    private static final  int VERSION_ACTUAL = 1;

    private final Context contexto;

    public BaseDatosPicudg(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    interface Tablas {
        String CENTROESTUDIO       = "CentroEstudio";
        String CONTACTO            = "Contacto";
        String CONTACTOS_UBICACION = "Contactos_Ubicacion";
        String COORDENADAS         = "Coordenadas";
        String MARKET              = "Market";
        String REPORTE             = "Reporte";
        String UBICACION           = "Ubicacion";
        String USUARIO             = "Usuario";
    }

    interface Referencias {

        String ID_CENTROESTUDIO = String.format("REFERENCES %s(%s)",
            Tablas.CENTROESTUDIO, CentroEstudio.ID_CENTROESTUDIO);

        String ID_CONTACTO = String.format("REFERENCES %s(%s)",
                Tablas.CONTACTO, Contacto.ID_CONTACTO);

        String ID_COORDENADAS = String.format("REFERENCES %S(%S)",
                Tablas.COORDENADAS, Coordenadas.ID_COORDENADA);

        String ID_MARKET = String.format("REFERENCES %s(%s)",
                Tablas.MARKET, Market.ID_MARKET);

        String ID_REPORTE = String.format("REFERENCES %s($s)",
                Tablas.REPORTE, Reporte.ID_REPORTE);

        String ID_UBICACION = String.format("REFERENCES %s(%s)",
                Tablas.UBICACION, Ubicacion.ID_UBICACION);

        String ID_USUARIO = String.format("REFERENCES %s(%s)",
                Tablas.USUARIO, Usuario.ID_USUARIO);
    }

    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        if(!db.isReadOnly()){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                db.setForeignKeyConstraintsEnabled(true);
            }else{
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL,%s TEXT NOT NULL)",
                Tablas.CENTROESTUDIO, BaseColumns._ID,
                CentroEstudio.ID_CENTROESTUDIO, CentroEstudio.NOMBRE,
                CentroEstudio.ACRONIMO, CentroEstudio.DIRECCION));

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT UNIQUE NOT NULL, %s TEXT NOT NULL,%s TEXT NOT NULL," +
                    "%s TEXT,%s TEXT NOT NULL,%s TEXT NOT NULL %s)",
                Tablas.CONTACTO, BaseColumns._ID,
                Contacto.ID_CONTACTO, Contacto.NOMBRE, Contacto.CORREO,
                Contacto.TELEFONO, Contacto.ROL, Contacto.FK_CENTRO, Referencias.ID_CENTROESTUDIO));

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT UNIQUE NOT NULL, %s REAL NOT NULL, %s REAL NOT NULL," +
                    "%s TEXT %s, %s TEXT %s, %s TEXT %s ON DELETE CASCADE, %s INTEGER)",
                Tablas.COORDENADAS, BaseColumns._ID,
                Coordenadas.ID_COORDENADA, Coordenadas.LONGITUD, Coordenadas.LATITUD,
                Coordenadas.FK_UBICACION, Referencias.ID_UBICACION,
                Coordenadas.FK_CENTRO, Referencias.ID_CENTROESTUDIO,
                Coordenadas.FK_MARKET, Referencias.ID_MARKET,
                Coordenadas.INSERCION));

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT UNIQUE NOT NULL,%s TEXT," +
                    "%s TEXT,%s TEXT NOT NULL)",
                Tablas.MARKET, BaseColumns._ID,
                Market.ID_MARKET, Market.ICON,
                Market.TIPO, Market.TITULO));

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT," +
                "%s TEXT NOT NULL %s,%s TEXT NOT NULL %s ON DELETE CASCADE, %s TEXT)",
                Tablas.REPORTE, BaseColumns._ID,
                Reporte.ID_REPORTE, Reporte.ASUNTO, Reporte.DESCRIPCION, Reporte.REPORTEURI,
                Reporte.FK_USUARIO, Referencias.ID_USUARIO,
                Reporte.FK_MARKET, Referencias.ID_MARKET, Reporte.IMAGENURI));

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL,%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL %s)",
                Tablas.UBICACION, BaseColumns._ID,
                Ubicacion.ID_UBICACION, Ubicacion.NOMBRE,
                Ubicacion.FK_CENTRO, Referencias.ID_CENTROESTUDIO));

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL,%s TEXT DEFAULT PICUDG,%s TEXT UNIQUE NOT NULL,"+
                 "%s INTEGER UNIQUE NOT NULL,%s TEXT NOT NULL %s)",
                Tablas.USUARIO,BaseColumns._ID,
                Usuario.ID_USUARIO, Usuario.NOMBRE, Usuario.CORREO, Usuario.CODIGO,
                Usuario.FK_CENTRO, Referencias.ID_CENTROESTUDIO));
        /*
        db.execSQL("CREATE TABLE " + Tablas.CONTACTOS_UBICACION + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY , " +
                Contactos_Ubicacion.ID_CONTACTOSUBICACION + " TEXT UNIQUE NOT NULL, " +
                "FOREIGN KEY (" + Contactos_Ubicacion.FK_CONTACTO + ") REFERENCES " + Tablas.CONTACTO + " (" + Contacto.ID_CONTACTO + ") " +
                "FOREIGN KEY (" + Contactos_Ubicacion.FK_UBICACION + ") REFERENCES " + Tablas.UBICACION + " (" + Ubicacion.ID_UBICACION + "));");
        */

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL,%s TEXT %s,%s TEXT %s)",
                Tablas.CONTACTOS_UBICACION,BaseColumns._ID,
                Contactos_Ubicacion.ID_CONTACTOSUBICACION,
                Contactos_Ubicacion.FK_UBICACION, Referencias.ID_UBICACION,
                Contactos_Ubicacion.FK_CONTACTO, Referencias.ID_CONTACTO));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CENTROESTUDIO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CONTACTO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CONTACTOS_UBICACION);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.COORDENADAS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.MARKET);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.UBICACION);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.REPORTE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.USUARIO);

        onCreate(db);
    }
}
