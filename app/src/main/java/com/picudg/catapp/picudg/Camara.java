package com.picudg.catapp.picudg;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Camara extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.content_camara) RelativeLayout RL_cam;
    @BindView(R.id.IV_Cam_img) ImageView IV_photo;
    @BindView(R.id.fab) FloatingActionButton fabdelete;
    @BindView(R.id.BT_cam) ImageButton BT_photo;
    @BindView(R.id.BT_1) ImageButton BT_boton1;
    @BindView(R.id.BT_2) ImageButton BT_boton2;
    @BindView(R.id.BT_3) ImageButton BT_boton3;
    @BindView(R.id.TV_LatLong) TextView TV_LatLong;

    private static String APP_DIRECTORY = "picudg/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Images";

    private final int MY_PERMISISONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private String mPath;
    private Uri pathuri;
    boolean takepic;
    private GoogleMap mMap;
    private LatLng mLatLongActual;
    private Marker marcador;
    private CameraUpdate cuceiPos;
    private Polygon poligonoCucei;
    private boolean photolistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapcam);
        mapFragment.getMapAsync(this);

        photolistener = false;

        /** Animacion solo disponible para dispositivos 5.0 en adelante **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.setDuration(2000);
            getWindow().setEnterTransition(explode);
            getWindow().setReturnTransition(explode);
        }
        /** Comprobamos permisos de almacenamiento y GPS
         si los permisos estan correctos, habilitamos los botones. **/
        if (myRequestStoragePermission()) {
            BT_photo.setEnabled(true);
        } else {
            BT_photo.setEnabled(false);
        }
        /** Opciones de captura de imagen: Galeria ó Camara **/
        BT_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });
        /** Boton para obtener Altitud y Longitud **/
        BT_boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /** Boton para pasar a la siguiente actividad una vez capturada la imagen **/
        BT_boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = IV_photo.getDrawable();
                boolean hasImage = (drawable != null);
                /** Si tenemos una captura de imagen en IV_Photo entramos en la condicion **/
                if (hasImage && (drawable instanceof BitmapDrawable)) {
                    Intent actForm = new Intent(Camara.this, FormEmail.class);
                    if (takepic) {
                        /** Si la imagen proviene de la camara, agregamos mPath al intent **/
                        actForm.putExtra("imagePath", mPath.toString());
                        Toast.makeText(Camara.this, "Tome una foto", Toast.LENGTH_SHORT).show();
                    } else if (!takepic) {
                        /** Si la imagen proviene de la galeria, agregamos pathuri al intent **/
                        actForm.putExtra("imagePath", pathuri.toString());
                        Toast.makeText(Camara.this, "Tome una imagen", Toast.LENGTH_SHORT).show();
                    }
                    /** Banderas que nos ayudaran a gestionar la navegacion entre actiidades **/
                    actForm.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    actForm.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    /** Animacion solo disponibles para dispositivos >= Lollipop **/
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slid = new Slide();
                        slid.setDuration(2000);
                        //getString(R.string.transicionIMG)
                        startActivity(actForm,
                                ActivityOptionsCompat.makeSceneTransitionAnimation(Camara.this, v, "").toBundle());
                    } else {
                        startActivity(actForm);
                    }
                    startActivity(actForm);
                    Camara.this.finish(); // Finalizamos para no permitir la navegacion con el onBack.
                }
                Snackbar snackbar = Snackbar
                        .make(RL_cam, "Captura una imagen", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        fabdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IV_photo.getDrawable() != null) {
                    IV_photo.setImageResource(0);
                    IV_photo.setVisibility(View.GONE);
                    photolistener=false;
                    if (marcador != null) {
                        marcador.remove();
                        TV_LatLong.setText("");
                    }
                    mMap.animateCamera(cuceiPos);
                }
            }
        });
        BT_boton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Maps = new Intent(Camara.this, MapsActivity.class);
                Maps.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(Maps);
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Agregando un market al mapa
        LatLng mLatLngCucei = new LatLng(20.653910, -103.325807);
        mMap.addMarker(new MarkerOptions().position(mLatLngCucei).title("Centro Universitario de Ciencias Exactas e Ingenierias"));
        cuceiPos = CameraUpdateFactory.newLatLngZoom(mLatLngCucei, 16);
        mMap.animateCamera(cuceiPos);
        final PolygonOptions PolyCucei = new PolygonOptions()
                .add(new LatLng(20.655883, -103.327721))
                .add(new LatLng(20.656397, -103.326595))
                .add(new LatLng(20.656987, -103.326632))
                .add(new LatLng(20.656939, -103.327376))
                .add(new LatLng(20.657037, -103.327385))
                .add(new LatLng(20.657027, -103.327535))
                .add(new LatLng(20.657616, -103.327666))
                .add(new LatLng(20.657806, -103.327261))
                .add(new LatLng(20.659329, -103.327352))
                .add(new LatLng(20.659757, -103.327018))
                .add(new LatLng(20.659354, -103.326466))
                .add(new LatLng(20.658399, -103.326397))
                .add(new LatLng(20.658384, -103.325929))
                .add(new LatLng(20.658169, -103.325909))
                .add(new LatLng(20.658278, -103.324515))
                .add(new LatLng(20.659792, -103.324616))
                .add(new LatLng(20.659559, -103.324180))
                .add(new LatLng(20.656062, -103.323984))
                .add(new LatLng(20.655633, -103.324711))
                .add(new LatLng(20.654013, -103.324238))
                .add(new LatLng(20.653345, -103.325795))
                .add(new LatLng(20.655883, -103.327721))
                .strokeColor(Color.CYAN)
                .fillColor(Color.TRANSPARENT);
        poligonoCucei = mMap.addPolygon(PolyCucei);
        myUbication();
        if(pointInPolygon(mLatLongActual,poligonoCucei)){
            Snackbar snackbar = Snackbar
                    .make(RL_cam, "!Bienvenido Buitre!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else{
            TV_LatLong.setText("Desconocido");
            Snackbar snackbar = Snackbar
                    .make(RL_cam, "!No estas dentro de un centro de estudio!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public boolean pointInPolygon(LatLng point, Polygon polygon) {
        if(point==null) return false;
        // ray casting alogrithm http://rosettacode.org/wiki/Ray-casting_algorithm
        int crossings = 0;
        List<LatLng> path = polygon.getPoints();
        path.remove(path.size()-1); //remove the last point that is added automatically by getPoints()
        // for each edge
        for (int i=0; i < path.size(); i++) {
            LatLng a = path.get(i);
            int j = i + 1;
            //to close the last edge, you have to take the first point of your polygon
            if (j >= path.size()) {
                j = 0;
            }
            LatLng b = path.get(j);
            if (rayCrossesSegment(point, a, b)) {
                crossings++;
            }
        }
        // odd number of crossings?
        return (crossings % 2 == 1);
    }
    public boolean rayCrossesSegment(LatLng point, LatLng a,LatLng b) {
        // Ray Casting algorithm checks, for each segment, if the point is 1) to the left of the segment and 2) not above nor below the segment. If these two conditions are met, it returns true
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
        // alter longitude to cater for 180 degree crossings
        if (px < 0 || ax <0 || bx <0) { px += 360; ax+=360; bx+=360; }
        // if the point has the same latitude as a or b, increase slightly py
        if (py == ay || py == by) py += 0.00000001;
        // if the point is above, below or to the right of the segment, it returns false
        if ((py > by || py < ay) || (px > Math.max(ax, bx))){
            return false;
        }
        // if the point is not above, below or to the right and is to the left, return true
        else if (px < Math.min(ax, bx)){
            return true;
        }
        // if the two above conditions are not met, you have to compare the slope of segment [a,b] (the red one here) and segment [a,p] (the blue one here) to see if your point is to the left of segment [a,b] or not
        else {
            double red = (ax != bx) ? ((by - ay) / (bx - ax)) : Double.POSITIVE_INFINITY;
            double blue = (ax != px) ? ((py - ay) / (px - ax)) : Double.POSITIVE_INFINITY;
            return (blue >= red);
        }
    }
    private void addMarketReport(LatLng coordenadas) {
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        //if(marcador != null) marcador.remove();
        if(pointInPolygon(coordenadas,poligonoCucei)) {
            marcador = mMap.addMarker(new MarkerOptions()
                    .position(coordenadas)
                    .title("Mi Reporte PICUDG"));
            mMap.animateCamera(miUbicacion);
        }else{
            Snackbar snackbar = Snackbar
                    .make(RL_cam, "!Market incorrecto!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        TV_LatLong.setText(coordenadas.toString());
    }
    private void updatePosition(Location location) {
        if (location != null) {
            mLatLongActual = new LatLng(location.getLatitude(), location.getLongitude());
            //addMarketReport(mLatLongActual);
        }
    }
    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updatePosition(location);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        @Override
        public void onProviderEnabled(String provider) {

        }
        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private void myUbication() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        updatePosition(loc);
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,15000,10,locListener);
    }
    private void showOptions(){
        final CharSequence[] option = {"Elegir Foto","Tomar Foto","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una opción:");
        builder.setIcon(R.drawable.ic_add_to_photos_black_24dp);
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which] == "Elegir Foto") {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/.*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona una imagen"), SELECT_PICTURE);
                } else if (option[which] == "Tomar Foto") {
                    opcCamera();
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void opcCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreate = file.exists();
        if(!isDirectoryCreate) {
            isDirectoryCreate = file.mkdirs();
        }
        if(isDirectoryCreate){
            Long TimeStamp = System.currentTimeMillis() / 1000;
            String Img_name = TimeStamp.toString() + ".jpg";
            mPath = Environment.getExternalStorageDirectory() + File.separator
                    + MEDIA_DIRECTORY + File.separator + Img_name;
            File New_File = new File(mPath);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(New_File));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }
    private boolean myRequestStoragePermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(RL_cam, "Permisos necesarios para la aplicación", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISISONS);
                        }
                    }).show();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISISONS);
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this, new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener(){
                                @Override
                                public void onScanCompleted(String path, Uri uri){
                                    Log.i("ExternalStorage", "Scanner" + path + ":");
                                    Log.i("ExternalStorage", "->Uri" + uri);
                                }
                            });
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    IV_photo.setVisibility(View.VISIBLE);
                    IV_photo.setImageBitmap(bitmap);
                    takepic = true;
                    addMarketReport(mLatLongActual);
                    break;
                case SELECT_PICTURE:
                    IV_photo.setVisibility(View.VISIBLE);
                    pathuri = data.getData();
                    IV_photo.setImageURI(pathuri);
                    takepic = false;
                    addMarketReport(mLatLongActual);
                    break;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_PERMISISONS){
            if(grantResults.length == 2 && grantResults[0] == getPackageManager().PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permisos Aceptados", Toast.LENGTH_SHORT).show();
                BT_photo.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("file_path",mPath);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPath = savedInstanceState.getString("file_path");
    }
    private void showExplanation() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permisos Denegados");
        builder.setMessage("Para usar esta aplicación, necesitas aceptar los permisos solicitados");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }
    private void alertExitMsg(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_info_outline_white_24dp);
        builder.setTitle("¿Estas Seguro?");
        builder.setMessage("Presiona SI para salir de la aplicación")
            .setCancelable(false)
            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void onBackPressed() {
        alertExitMsg();
        //super.onBackPressed();  // Invoca al método
    }

}