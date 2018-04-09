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
import android.util.Pair;
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
import android.widget.TabHost;
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
import com.picudg.catapp.picudg.SQLite.InibdPicudg;
import com.picudg.catapp.picudg.SQLite.LoadBD;
import com.picudg.catapp.picudg.SQLite.OperacionesBaseDatos;
import com.picudg.catapp.picudg.Tools.OperacionesMaps;
import com.picudg.catapp.picudg.Tools.PointInPoly;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
    Button bt_Info;
    @BindView(R.id.bt_CrearReporte)
    Button bt_Reportes;
    @BindView(R.id.bt_ListaReportes)
    Button bt_listReportes;

    private final int MY_PERMISISONS = 100;
    private GoogleMap mMap;
    private LatLng mLatLongActual;
    private GoogleApiClient mGoogleApiClient;
    private OperacionesBaseDatos datos;
    private FirebaseUser user;
    private TextView nav_userName;
    private TextView nav_userEmail;
    private ImageView nav_userPhoto;
    private String acronimoCentro;

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

        querysBD();

        myRequestPermission();

        bt_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorialApp();
            }
        });

        bt_Reportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActReporte();
            }
        });

        bt_listReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActListReportes();
            }
        });
    }

    private void startActListReportes() {
        Intent actListaReportes = new Intent(PicMain.this, lista_reportes.class);
        actListaReportes.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        actListaReportes.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(actListaReportes);
    }
    private void startActReporte() {
        if (myRequestPermission()) {
            if (comprobarUsuario()) {
                Intent actReporte = new Intent(PicMain.this, FormEmail.class);
                actReporte.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                actReporte.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(actReporte);
            } else {
                llenarUsuario();
            }
        }
    }
    public void querysBD(){

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());
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

        //DatabaseUtils.dumpCursor(datos.obtenerEdificiosCentro("CUCEI"));

        datos.getDb().close();
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

                datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());
                Cursor c = datos.obtenerUsuarioCorreo(user.getEmail());
                c.moveToFirst();

                nav_userName.setText(c.getString(c.getColumnIndex("Nombre")));
                nav_userEmail.setText(user.getEmail());

                datos.getDb().close();
                c.close();
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

        Cursor datosUsuario = datos.obtenerUsuarioCorreo(user.getEmail());
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
                    Cursor idCentro = datos.obtenerIdCentroEstudio(acronimoCentro);
                    Toast.makeText(PicMain.this, acronimoCentro, Toast.LENGTH_SHORT).show();
                    idCentro.moveToFirst();
                    if (idCentro != null) {
                        try {
                            datos.getDb().beginTransaction();

                            String userName,userCod,userSchool;

                            userSchool = idCentro.getString(idCentro.getColumnIndex("ID_Centro"));
                            userName = tilNombre.getEditText().getText().toString().trim();
                            userCod = tilCodigo.getEditText().getText().toString();

                            datos.insertarUsuario(new Usuario(null, userName,
                                    user.getEmail(), userCod, userSchool));

                            datos.getDb().setTransactionSuccessful();
                        }catch (SQLiteConstraintException e){
                            Toast.makeText(PicMain.this, "Datos inconclusos o no validos", Toast.LENGTH_SHORT).show();
                            Log.d("Insertar Usuario->", "Fallo al insertar: ,", e);
                            return;
                        }finally{
                            datos.getDb().endTransaction();
                            navigationInfo();
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
    private AlertDialog tutorialApp(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(PicMain.this);
        LayoutInflater inflater = PicMain.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_tutorial, null);
        Button bt = (Button) v.findViewById(R.id.BT_tuto);
        builder.setView(v);

        final AlertDialog dialog = builder.create();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
            alertExitMsg();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_makeReporte) {
            startActReporte();
        } else if (id == R.id.nav_listReportes) {
            startActListReportes();
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
    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng mLatLngCucei = new LatLng(20.653910, -103.325807);
        LatLng mLatLngCucea = new LatLng(20.741980, -103.380219);

        OperacionesMaps config = new OperacionesMaps(mMap,getApplicationContext());
        mMap = config.configurarMapa();

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            mMap.setMyLocationEnabled(true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
                mMap.setMyLocationEnabled(true);
            }
        }
        myUbication();
        PointInPoly inPoly = new PointInPoly();
        List<Pair> listpolyscentros = config.getListPolysCentros();
        List<Polygon> listCentros = config.getListPoligonosCentros();
        for(int i = 0; i < listCentros.size(); i++) {
            if (inPoly.pointInPolygon(mLatLongActual, listCentros.get(i))) {
                acronimoCentro = (String) listpolyscentros.get(i).second;
                break;
            }
        }
        if(acronimoCentro != null) {
            if(acronimoCentro.equals("CUCEI")){
                CameraUpdate camaraPos = CameraUpdateFactory.newLatLngZoom(mLatLngCucei, 16);
                mMap.animateCamera(camaraPos);
            }else if (acronimoCentro.equals("CUCEA")){
                CameraUpdate camaraPos = CameraUpdateFactory.newLatLngZoom(mLatLngCucea, 16);
                mMap.animateCamera(camaraPos);
            }
            Snackbar snackbar = Snackbar
                    .make(RL_Main, acronimoCentro + ": ¡Bienvenido!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else {
            CameraUpdate camaraPos = CameraUpdateFactory.newLatLngZoom(mLatLongActual, 16);
            mMap.animateCamera(camaraPos);
            Snackbar snackbar = Snackbar
                    .make(RL_Main, "¡No estas dentro de un centro de estudio!", Snackbar.LENGTH_LONG);
            snackbar.show();
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
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        } else {
            Toast.makeText(this, "Permisos Denegados", Toast.LENGTH_SHORT).show();
        }
    }
    private void alertExitMsg(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_cancel);
        builder.setTitle("¿Estas Seguro?");
        builder.setMessage("Presiona SI para salir de la aplicación")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PicMain.this.finish();
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
