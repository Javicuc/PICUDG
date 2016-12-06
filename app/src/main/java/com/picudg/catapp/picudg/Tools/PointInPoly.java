package com.picudg.catapp.picudg.Tools;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.List;

/**
 * Created by javilubz on 19/11/16.
 */

public class PointInPoly{

    //crear un asyntask para el algoritmo
    public boolean pointInPolygonOptions(LatLng point, PolygonOptions polygon) {
        if (point == null) return false;
        // ray casting alogrithm http://rosettacode.org/wiki/Ray-casting_algorithm
        int crossings = 0;
        List<LatLng> path = polygon.getPoints();
        try {
            path.remove(path.size() - 1); //removemos el utlimo point que es agregado automaticamento por getPoints()
        }catch (Exception e){
            Log.i("Exception",e.toString());
            return false;
        }
            // recorremos las coordendas
        for (int i = 0; i < path.size(); i++) {
            LatLng a = path.get(i);
            int j = i + 1;
            //para cerrar la ultima coordenada debemos tomar la primer coordenada del poligono
            if (j >= path.size()) {
                j = 0;
            }
            LatLng b = path.get(j);
            if (rayCrossesSegment(point, a, b)) {
                crossings++;
            }
        }
        // numero impar de crossings ???
        return (crossings % 2 == 1);
    }

    public boolean pointInPolygon(LatLng point, Polygon polygon) {
        if (point == null) return false;
        int crossings = 0;
        List<LatLng> path = polygon.getPoints();
        path.remove(path.size() - 1); //remove the last point that is added automatically by getPoints()
        for (int i = 0; i < path.size(); i++) {
            LatLng a = path.get(i);
            int j = i + 1;
            if (j >= path.size()) {
                j = 0;
            }
            LatLng b = path.get(j);
            if (rayCrossesSegment(point, a, b)) {
                crossings++;
            }
        }
        return (crossings % 2 == 1);
    }
    public boolean rayCrossesSegment(LatLng point, LatLng a, LatLng b) {
        // El algoritmo de Ray Casting comprueba, para cada segmento, si el punto es 1) a la izquierda del segmento y 2)
        // no superior ni inferior al segmento. Si se cumplen estas dos condiciones, devuelve verdadero
        double px = point.longitude,
                py = point.latitude,
                ax = a.longitude,
                ay = a.latitude,
                bx = b.longitude,
                by = b.latitude;
        if (ay > by) {
            ax = b.longitude;
            ay = b.latitude;
            bx = a.longitude;
            by = a.latitude;
        }
        // alteramos la longitud para los cruces de 180 grados
        if (px < 0 || ax < 0 || bx < 0) {
            px += 360;
            ax += 360;
            bx += 360;
        }
        // si el punto tiene la misma latitud que a o b, aumentamos ligeramente py
        if (py == ay || py == by) py += 0.00000001;
        // si el punto está por encima, por debajo o a la derecha del segmento, devuelve false
        if ((py > by || py < ay) || (px > Math.max(ax, bx))) {
            return false;
        }
        // si el punto no esta arriba, debajo o hacia la derecha y esta a la izquierda, devuelva true
        else if (px < Math.min(ax, bx)) {
            return true;
        }

        // si no se cumplen las dos condiciones anteriores, hay que comparar la pendiente del segmento [a, b]
        // (el rojo aquí) y el segmento [a, p] (el azul aquí) para ver si su punto es La izquierda del segmento [a, b] o no
        else {
            double red = (ax != bx) ? ((by - ay) / (bx - ax)) : Double.POSITIVE_INFINITY;
            double blue = (ax != px) ? ((py - ay) / (px - ax)) : Double.POSITIVE_INFINITY;
            return (blue >= red);
        }
    }

}
