package com.picudg.catapp.picudg;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.picudg.catapp.picudg.Modelo.Coordenadas;
import com.picudg.catapp.picudg.Modelo.Market;
import com.picudg.catapp.picudg.Modelo.Reporte;
import com.picudg.catapp.picudg.Modelo.Usuario;
import com.picudg.catapp.picudg.SQLite.OperacionesBaseDatos;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PicMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    @BindView(R.id.content_pic_main)
    RelativeLayout RL_Main;
    @BindView(R.id.bt_Informe)
    ImageButton bt_Info;
    @BindView(R.id.bt_CrearReporte)
    ImageButton bt_Reportes;
    @BindView(R.id.bt_ListaReportes)
    ImageButton bt_listReportes;

    private final int MY_PERMISISONS = 100;
    private final int LOCATION_CODE = 400;

    private GoogleMap mMap;
    private LatLng mLatLongActual;
    private CameraUpdate cuceiPos;
    private Polygon poligonoCucei;
    private GoogleApiClient mGoogleApiClient;
    private OperacionesBaseDatos datos;
    private FirebaseUser user;
    private String userName;
    private String userEmail;
    private String userCod;
    private String userSchool;
    private TextView nav_userName;
    private TextView nav_userEmail;
    private ImageView nav_userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PICUDG");
        ButterKnife.bind(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        nav_userName = (TextView) hView.findViewById(R.id.TV_UserName_NavHeader);
        nav_userEmail = (TextView) hView.findViewById(R.id.TV_UserEmail_NavHeader);
        nav_userPhoto = (ImageView) hView.findViewById(R.id.IV_UserPhoto_NavHeader);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapMain);
        mapFragment.getMapAsync(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        navigationInfo();

        if(myRequestPermission()){
            bt_Reportes.setEnabled(true);
        }

        /** Animacion solo disponible para dispositivos 5.0 en adelante **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.setDuration(2000);
            getWindow().setEnterTransition(explode);
            getWindow().setReturnTransition(explode);
        }

        bt_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        bt_Reportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comprobarUsuario()) {
                    Intent actReporte = new Intent(PicMain.this, FormEmail.class);
                    actReporte.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    actReporte.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(actReporte);
                }else{
                    llenarUsuario();
                }
            }
        });

        bt_listReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void navigationInfo(){
        if(user != null){
            if(comprobarUsuario()) {
                if (user.getPhotoUrl() != null)
                    Glide.with(this)
                            .load(user.getPhotoUrl())
                            .centerCrop()
                            .override(100, 100)
                            .into(nav_userPhoto);
                if (userName != null)
                    nav_userName.setText(userName);
                else
                    nav_userName.setText("PICUDG");
                nav_userEmail.setText(user.getEmail());
            }else{
                llenarUsuario();
            }
        }else{
            startLogin();
        }
    }
    private boolean comprobarUsuario(){
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());

        userEmail = user.getEmail();
        Log.i("Correo->",userEmail);
        Cursor datosUsuario = datos.obtenerUsuarioCorreo(userEmail);
        DatabaseUtils.dumpCursor(datosUsuario);
        if(datosUsuario != null && datosUsuario.moveToFirst())
            return true;
        datos.getDb().close();
        return false;
    }
    private AlertDialog llenarUsuario() {
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());

        final AlertDialog.Builder builder = new AlertDialog.Builder(PicMain.this);

        LayoutInflater inflater = PicMain.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.llenarusuario_dialog, null);
        builder.setView(v);

        Button okReporte = (Button) v.findViewById(R.id.BT_reporteUsuario);
        final TextInputLayout tilNombre = (TextInputLayout) v.findViewById(R.id.TIL_NombreUsuario);
        final TextInputLayout tilCodigo = (TextInputLayout) v.findViewById(R.id.TIL_CodigoUsuario);
        final AlertDialog dialog = builder.create();
        tilNombre.getEditText().setText(user.getDisplayName());
        okReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tilNombre.getEditText().getText() != null && tilCodigo.getEditText().getText().length() == 9) {

                    /** Esta funcion deber ser un Spinner dentro del dialog, cuando seleccione un acronimo, bucara
                     * el id del centro, si se encuentra dentro de las coordenadas del centro seleccionado retornara TRUE, de
                     * momento asumiremos que todos los nuevos usuarios pertenecen a CUCEI**/
                    Cursor idCentro = datos.obtenerIdCentroEstudio("CUCEI");
                    idCentro.moveToFirst();
                    if (idCentro != null) {
                        try {
                            datos.getDb().beginTransaction();
                            userSchool = idCentro.getString(idCentro.getColumnIndex("ID_Centro"));
                            userName = tilNombre.getEditText().getText().toString().trim();
                            userCod = tilCodigo.getEditText().getText().toString();
                            datos.insertarUsuario(new Usuario(null, userName,
                                    userEmail, userCod, userSchool));
                            datos.getDb().setTransactionSuccessful();
                        }catch (SQLiteConstraintException e){
                            Toast.makeText(PicMain.this, "Datos inconclusos o no validos", Toast.LENGTH_SHORT).show();
                            Log.d("Insertar Usuario->", "Fallo al insertar: ,", e);
                            return;
                        }finally{
                            datos.getDb().endTransaction();
                        }
                    }
                    idCentro.close();
                    dialog.dismiss();
                } else {
                    Toast.makeText(PicMain.this, "Datos inconclusos o no validos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
        datos.getDb().close();
        return builder.create();
    }
    private void startLogin() {
        Intent intent = new Intent(this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PicMain.this.startActivity(intent);
        finish();
    }
    public void logout(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);

        startLogin();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pic_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.Delete_PicMain){
            Toast.makeText(this, "Seleccionaste Delete", Toast.LENGTH_SHORT).show();
            return true;
        } else if(id == R.id.ayuda_PicMain){
            Toast.makeText(this, "Seleccionaste Ayuda", Toast.LENGTH_SHORT).show();
            return true;
        } else if(id == R.id.configurar_PicMain){
            Toast.makeText(this, "Seleccionaste Ayuda", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_makeReporte) {

        } else if (id == R.id.nav_listReportes) {

        } else if (id == R.id.share_fb) {

        } else if (id == R.id.share_google) {

        } else if (id == R.id.Sop_send) {

        } else if (id == R.id.Sop_ayuda) {

        }else if(id == R.id.Sop_term) {

        }else if (id == R.id.Sop_conf) {

        } else if (id == R.id.out) {
            logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng mLatLngCucei = new LatLng(20.653910, -103.325807);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            mMap.setMyLocationEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
                mMap.setMyLocationEnabled(true);
            }
        }
        // Agregando un market al mapa
        mMap.addMarker(new MarkerOptions().position(mLatLngCucei).title("Centro Universitario de Ciencias Exactas e Ingenierias")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_school_black_36dp)));
        cuceiPos = CameraUpdateFactory.newLatLngZoom(mLatLngCucei, 16);
        mMap.animateCamera(cuceiPos);

        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());


        ArrayList<LatLng> LatLong = new ArrayList<LatLng>();
        Cursor cursor = datos.obtenerLatidLongitudCentro("CUCEI");
        //DatabaseUtils.dumpCursor(cursor);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            LatLong.add(new LatLng(cursor.getDouble(cursor.getColumnIndex("Latitud")),cursor.getDouble(cursor.getColumnIndex("Longitud"))));
            cursor.moveToNext();
        }
        cursor.close();

        final PolygonOptions PolyCucei = new PolygonOptions()
                .strokeColor(0xE6CCFFFF)
                .fillColor(0x7FCCFFFF);
        PolyCucei.addAll(LatLong);
        poligonoCucei = mMap.addPolygon(PolyCucei);
        myUbication();

        Cursor AllMarkers = datos.obtenerMarketLatitudLongitudAll();
        AllMarkers.moveToFirst();
        DatabaseUtils.dumpCursor(AllMarkers);
        while(!AllMarkers.isAfterLast()) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(AllMarkers.getDouble(AllMarkers.getColumnIndex("Latitud")),
                    AllMarkers.getDouble(AllMarkers.getColumnIndex("Longitud")))).title(AllMarkers.getString(AllMarkers.getColumnIndex("Titulo")))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_report_problem_black_18dp)));
            AllMarkers.moveToNext();
        }
        AllMarkers.close();

        if (pointInPolygon(mLatLongActual, poligonoCucei)) {
            Snackbar snackbar = Snackbar
                    .make(RL_Main, "¡Bienvenido Buitre!", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar
                    .make(RL_Main, "¡No estas dentro de un centro de estudio!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    public boolean pointInPolygon(LatLng point, Polygon polygon) {
        if (point == null) return false;
        // ray casting alogrithm http://rosettacode.org/wiki/Ray-casting_algorithm
        int crossings = 0;
        List<LatLng> path = polygon.getPoints();
        path.remove(path.size() - 1); //remove the last point that is added automatically by getPoints()
        // for each edge
        for (int i = 0; i < path.size(); i++) {
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
    public boolean rayCrossesSegment(LatLng point, LatLng a, LatLng b) {
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
        if (px < 0 || ax < 0 || bx < 0) {
            px += 360;
            ax += 360;
            bx += 360;
        }
        // if the point has the same latitude as a or b, increase slightly py
        if (py == ay || py == by) py += 0.00000001;
        // if the point is above, below or to the right of the segment, it returns false
        if ((py > by || py < ay) || (px > Math.max(ax, bx))) {
            return false;
        }
        // if the point is not above, below or to the right and is to the left, return true
        else if (px < Math.min(ax, bx)) {
            return true;
        }
        // if the two above conditions are not met, you have to compare the slope of segment [a,b] (the red one here) and segment [a,p] (the blue one here) to see if your point is to the left of segment [a,b] or not
        else {
            double red = (ax != bx) ? ((by - ay) / (bx - ax)) : Double.POSITIVE_INFINITY;
            double blue = (ax != px) ? ((py - ay) / (px - ax)) : Double.POSITIVE_INFINITY;
            return (blue >= red);
        }
    }
    private void updatePosition(Location location) {
        if (location != null) {
            mLatLongActual = new LatLng(location.getLatitude(), location.getLongitude());
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
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        updatePosition(loc);
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 10, locListener);
    }
    private boolean myRequestGPSPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            mMap.setMyLocationEnabled(true);
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
                mMap.setMyLocationEnabled(true);
                return true;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION))){
                Snackbar.make(RL_Main, "Necesitamos saber tu locaclizacion para generar reportes :(", Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View v) {
                                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, LOCATION_CODE);
                            }
                        }).show();
            }else{
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, LOCATION_CODE);
            }
        }
        return false;
    }
    private boolean myRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) ||
                (shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION))) {
            Snackbar.make(RL_Main, "Permisos necesarios para la aplicación", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA, ACCESS_FINE_LOCATION}, MY_PERMISISONS);
                        }
                    }).show();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA, ACCESS_FINE_LOCATION}, MY_PERMISISONS);
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISISONS) {
            if (grantResults.length == 3 && grantResults[0] == getPackageManager().PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos Aceptados", Toast.LENGTH_SHORT).show();
                bt_Reportes.setEnabled(true);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        } else {
            showExplanation();
        }
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
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

}
