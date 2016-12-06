package com.picudg.catapp.picudg.Tools;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.picudg.catapp.picudg.R;
import com.picudg.catapp.picudg.SQLite.OperacionesBaseDatos;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by javilubz on 24/11/16.
 */

public class OperacionesMaps {

    private OperacionesBaseDatos datos;
    private GoogleMap      mMap;
    private Context        contexto;
    private List<Pair>     listPolys;
    private Polygon        poligonoCentro;
    private String         Acronimo;
    private List<Polygon>  listPoligonosCentros;
    private List<Pair>     listPolysCentros;
    private int            i;

    public OperacionesMaps(GoogleMap map, Context contexto){
        this.mMap = map;
        this.contexto = contexto;
    }
    public GoogleMap configurarMapa() {

        LatLng mLatLngCucei = new LatLng(20.653910, -103.325807);
        LatLng mLatLngCucea = new LatLng(20.741980, -103.380219);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        // Agregando un marker al mapa
        mMap.addMarker(new MarkerOptions().position(mLatLngCucei).title("Centro Universitario de Ciencias Exactas e Ingenierias")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mortarboard)));
        mMap.addMarker(new MarkerOptions().position(mLatLngCucea).title("Centro Universitario de Ciencias Economico Administrativas")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mortarboard)));

        datos = OperacionesBaseDatos
                .obtenerInstancia(contexto);

        listPoligonosCentros = new ArrayList<>();
        listPolysCentros = datos.obtenerPoligonosCentros();

        int k;
        for (i = 0; i < listPolysCentros.size(); i++) {
            poligonoCentro = mMap.addPolygon((PolygonOptions) listPolysCentros.get(i).first);
            Log.i("FlagNombreCentro", (String) listPolysCentros.get(i).second);
            listPoligonosCentros.add(poligonoCentro);
            Acronimo = (String) listPolysCentros.get(i).second;
            k = listPolysCentros.size()+1;
            if( k > listPolysCentros.size() || !Acronimo.equals(listPolysCentros.get(i+1).second)){
                listPolys = datos.obtenerEdificiosCentro(Acronimo);
                if(!listPolys.isEmpty()) {
                    for (int j = 0; j < listPolys.size(); j++) {
                        mMap.addPolygon((PolygonOptions) listPolys.get(j).first);
                    }
                }
            }
        }

        Cursor AllMarkers = datos.obtenerMarketLatitudLongitudAll();
        AllMarkers.moveToFirst();
        DatabaseUtils.dumpCursor(AllMarkers);
        while (!AllMarkers.isAfterLast()) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(AllMarkers.getDouble(AllMarkers.getColumnIndex("Latitud")),
                    AllMarkers.getDouble(AllMarkers.getColumnIndex("Longitud")))).title(AllMarkers.getString(AllMarkers.getColumnIndex("Titulo")))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_danger)));
            AllMarkers.moveToNext();
        }
        AllMarkers.close();
        datos.getDb().close();
        return mMap;
    }
    public List<Pair> getListNombrePolyEdificiosCentro(String acronimoCentro) {
        listPolys = datos.obtenerEdificiosCentro(acronimoCentro);
        return listPolys;
    }
    public List<Polygon> getListPoligonosEdificiosCentro(String acronimoCentro) {
        List<Polygon>  listPoligonos = new ArrayList<>() ;
        listPolys = getListNombrePolyEdificiosCentro(acronimoCentro);
        if(!listPolys.isEmpty()) {
            for (int i = 0; i < listPolys.size(); i++) {
                Polygon tmp = (Polygon) listPolys.get(i).first;
                listPoligonos.add(tmp);
                Log.i("EdificioPoligono", (String) listPolys.get(i).second);
            }
        }
        return listPoligonos;
    }
    public List<Pair> getListPolysCentros() {
        return listPolysCentros;
    }
    public List<Polygon> getListPoligonosCentros() {
        return listPoligonosCentros;
    }
}
