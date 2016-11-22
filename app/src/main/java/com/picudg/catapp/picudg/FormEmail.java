package com.picudg.catapp.picudg;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaouan.revealator.Revealator;
import com.jaouan.revealator.animations.AnimationListenerAdapter;
import com.picudg.catapp.picudg.Modelo.Coordenadas;
import com.picudg.catapp.picudg.Modelo.Market;
import com.picudg.catapp.picudg.Modelo.Reporte;
import com.picudg.catapp.picudg.SQLite.OperacionesBaseDatos;
import com.picudg.catapp.picudg.Tools.IfConect;
import com.picudg.catapp.picudg.Tools.PointInPoly;
import com.picudg.catapp.picudg.Tools.Report;
import com.picudg.catapp.picudg.Tools.SendMail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FormEmail extends AppCompatActivity implements OnMapReadyCallback {

    /*** Inyecciones, utilizando ButterKnife ***/
    @BindView(R.id.fab) View mFab;
    @BindView(R.id.plane) View mPlaneImageView;
    @BindView(R.id.plane_layout) View mPlaneLayout;
    @BindView(R.id.inputs_layout) View mInputsLayout;
    @BindView(R.id.sky_layout) View mSkyLayout;
    @BindView(R.id.sent_layout) View mSentLayout;
    @BindView(R.id.check) View mCheckImageView;
    @BindView(R.id.IV_Form_img) ImageView IV_photo;
    @BindView(R.id.coorlayout) CoordinatorLayout RL_FormEmail;
    @BindView(R.id.BT_cam) ImageButton BT_photo;
    @BindView(R.id.BT_Limpiar) Button bt_Limpiar;
    @BindView(R.id.TB_MakePDF) ToggleButton tb_MakePDF;


    private static String APP_DIRECTORY = "picudg/";
    private static String MEDIA_DIRECTORY_FILES = APP_DIRECTORY + "files";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Images";

    private String pdfname;
    private String asunto;
    private String descripcion;

    private final int MY_PERMISISONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private final int LOCATION_CODE = 400;

    private String mPathPdf;
    private String mPath;
    private Uri pathuri;
    boolean takepic;
    boolean reporteListo;
    private GoogleMap mMap;
    private LatLng mLatLongActual;
    private LatLng mLatLongReporteActual;
    private Marker marcador;
    private CameraUpdate cuceiPos;
    private Polygon poligonoCucei,PoligonoEdificio;
    private List<Polygon> listPoligonos;
    private FirebaseUser user;
    private List<android.util.Pair> listPolys;
    PointInPoly inPoly;
    OperacionesBaseDatos datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alertMsg();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reporteListo = false;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapForm);
        mapFragment.getMapAsync(this);

        if (myRequestPermission()) {
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

        tb_MakePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IV_photo.getDrawable() != null && reporteListo == false) {
                    llenarReporte();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(RL_FormEmail, "¡Toma una pic!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    tb_MakePDF.setChecked(false);
                }
            }
        });

        bt_Limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datos = OperacionesBaseDatos
                        .obtenerInstancia(getApplicationContext());
                LimpiarPantalla();
                if (marcador != null) {
                    datos.eliminarMarketNombre(marcador.getTitle());
                    marcador.remove();
                }
                mMap.animateCamera(cuceiPos);
                datos.getDb().close();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reporteListo) {
                    IfConect conex = new IfConect(getApplicationContext());
                    if (conex.isNetDisponible() && conex.isOnlineNet()) {
                        enviarmail();
                        send();
                        startMain();
                    } else {
                        datos = OperacionesBaseDatos
                                .obtenerInstancia(getApplicationContext());
                        if (marcador != null) {
                            datos.eliminarMarketNombre(marcador.getTitle());
                            marcador.remove();
                        }
                        datos.getDb().close();
                        alertConect();
                        startMain();
                    }
                }else{
                    Snackbar snackbar = Snackbar
                            .make(RL_FormEmail, "¡Completa el formulario!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
    public void alertConect(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setIcon(R.drawable.);
        builder.setTitle("ATENCIÓN");
        builder.setMessage("¡Verifica tu conexion a internet!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private AlertDialog llenarReporte() {
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());
        final AlertDialog.Builder builder = new AlertDialog.Builder(FormEmail.this);

        LayoutInflater inflater = FormEmail.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.fullscreen_dialog, null);
        builder.setView(v);

        Button okReporte = (Button) v.findViewById(R.id.BT_reporteDialog);
        final TextInputLayout tilAsunto = (TextInputLayout) v.findViewById(R.id.TIL_asunto);
        final TextInputLayout tilDescripcion = (TextInputLayout) v.findViewById(R.id.TIL_descripcion);
        final AlertDialog dialog = builder.create();

        okReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asunto = tilAsunto.getEditText().getText().toString().trim();
                descripcion = tilDescripcion.getEditText().getText().toString().trim();

                if (validaCampos(asunto) && validaCampos(descripcion)) {
                    WritePDF();
                    addMarketReport(mLatLongReporteActual);
                    String idMarkerTMP = datos.insertarMarket(new Market(null,null,"Reporte",asunto));
                    Cursor idUsuarioTMP = datos.obtenerUsuarioCorreo(user.getEmail());
                    idUsuarioTMP.moveToFirst();
                    DatabaseUtils.dumpCursor(idUsuarioTMP);
                    datos.insertarReportes(new Reporte(null,asunto,descripcion,mPathPdf,
                            idUsuarioTMP.getString(idUsuarioTMP.getColumnIndex("ID_Usuario")),idMarkerTMP,String.valueOf(takepic?mPath:pathuri)));
                    datos.insertarCoordenadas(new Coordenadas(null,mLatLongReporteActual.longitude,mLatLongReporteActual.latitude,null,null,idMarkerTMP,2));
                    tb_MakePDF.setClickable(false);
                    tb_MakePDF.setChecked(true);
                    tb_MakePDF.setBackgroundColor(getResources().getColor(R.color.accent));
                    reporteListo = true;
                    dialog.dismiss();
                } else {
                    Toast.makeText(FormEmail.this, "Datos inconclusos o no validos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
        datos.getDb().close();
        tb_MakePDF.setClickable(true);
        tb_MakePDF.setChecked(false);
        return builder.create();
    }
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(myRequestGPSPermission());

        // Agregando un market al mapa
        LatLng mLatLngCucei = new LatLng(20.653910, -103.325807);
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
        listPoligonos = new ArrayList<>();
        listPolys = datos.obtenerEdificiosCentro("CUCEI");
        for(int i = 0; i < listPolys.size(); i++) {
            PoligonoEdificio = mMap.addPolygon((PolygonOptions) listPolys.get(i).first);
            Log.i("Edificio: ", (String) listPolys.get(i).second);
            listPoligonos.add(PoligonoEdificio);
        }
        datos.getDb().close();

        inPoly = new PointInPoly();
        myUbication();
        if (inPoly.pointInPolygon(mLatLongActual, poligonoCucei)) {
            Snackbar snackbar = Snackbar
                    .make(RL_FormEmail, "!Bienvenido Buitre!", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar
                    .make(RL_FormEmail, "!No estas dentro de un centro de estudio!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    public void startMain(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent actMain = new Intent(FormEmail.this, PicMain.class);
                actMain.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                actMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                actMain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                actMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(actMain);
                FormEmail.this.finish();
            }
        }, 4500);
    }
    /*** Adjuntamos el metodo enviar email.*/
    @OnClick(R.id.fab)
    void send() {
        // - Prepare views visibility.
        mCheckImageView.setVisibility(View.INVISIBLE);
        mSentLayout.setVisibility(View.INVISIBLE);

        // - Rotate fab.
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setDuration(280);
        mFab.startAnimation(rotateAnimation);

        // - Hide inputs layout.
        final Animator circularReveal = ViewAnimationUtils.createCircularReveal(mInputsLayout, (int) (mFab.getX() + mFab.getWidth() / 2), (int) (mFab.getY() + mFab.getHeight() / 2), mInputsLayout.getHeight(), 0);
        circularReveal.setDuration(250);
        circularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // - Update views visibility.
                mInputsLayout.setVisibility(View.INVISIBLE);
                // - Fly away.
                flyAway();
            }
        });
        circularReveal.start();
    }
    private void enviarmail() {
        /** Obtenemos el contenido del correo **/
        //Creamos el objecto SendMail
        SendMail sm = new SendMail(this, "javier.jalr7@gmail.com", asunto, pdfname);
        //llamamos a ejecutar el proceso que envia el correo
        sm.execute();
    }
    /*** Comienza la animacion de volar.*/
    private void flyAway() {
        // - Combine rotation and translation animations.
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 180);
        rotateAnimation.setDuration(1000);
        mPlaneImageView.startAnimation(rotateAnimation);
        Revealator.reveal(mSentLayout)
                .from(mPlaneLayout)
                .withTranslateDuration(1000)
                .withCurvedTranslation(new PointF(-1200, 0))
                .withRevealDuration(200)
                .withHideFromViewAtTranslateInterpolatedTime(.5f)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // - Display checked icon.
                        final ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
                        scaleAnimation.setInterpolator(new BounceInterpolator());
                        scaleAnimation.setDuration(500);
                        scaleAnimation.setAnimationListener(new AnimationListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mInputsLayout.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // - Restore inputs layout.
                                        retoreInputsLayout();
                                    }
                                }, 1000);
                            }
                        });
                        mCheckImageView.startAnimation(scaleAnimation);
                        mCheckImageView.setVisibility(View.VISIBLE);
                    }
                }).start();
    }
    private void LimpiarPantalla() {
        mInputsLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                final Animator circularReveal = ViewAnimationUtils.createCircularReveal(mInputsLayout,
                        (int) (mFab.getX() + mFab.getWidth() / 2),
                        (int) (mFab.getY() + mFab.getHeight() / 2), 0, mInputsLayout.getHeight());
                circularReveal.setDuration(400);
                circularReveal.start();
                mCheckImageView.setVisibility(View.INVISIBLE);
                mSentLayout.setVisibility(View.INVISIBLE);
                mInputsLayout.setVisibility(View.VISIBLE);
                IV_photo.setImageResource(0);
                IV_photo.setVisibility(View.GONE);
                tb_MakePDF.setClickable(true);
                tb_MakePDF.setChecked(false);
                tb_MakePDF.setBackgroundColor(getResources().getColor(R.color.primary_light));
                if(takepic) {
                    File file = new File(mPath);
                    file.delete();
                    takepic = false;
                    getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(mPath))));
                }else if(reporteListo){
                    File file = new File(mPathPdf);
                    file.delete();
                    reporteListo = false;
                }
                mLatLongReporteActual=null;
            }
        }, 1000);
    }
    /**Inicializamos nuestros componentes.*/
    private void retoreInputsLayout() {
        mInputsLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Animator circularReveal = ViewAnimationUtils.createCircularReveal(mInputsLayout,
                        (int) (mFab.getX() + mFab.getWidth() / 2),
                        (int) (mFab.getY() + mFab.getHeight() / 2), 0, mInputsLayout.getHeight());
                circularReveal.setDuration(250);
                circularReveal.start();
                mInputsLayout.setVisibility(View.VISIBLE);
                IV_photo.setImageResource(0);
                IV_photo.setVisibility(View.GONE);
                tb_MakePDF.setClickable(true);
                tb_MakePDF.setChecked(false);
                tb_MakePDF.setBackgroundColor(getResources().getColor(R.color.primary_light));
                reporteListo = false;
            }
        }, 1000);
    }
    private boolean validaCampos(String texto){
        Pattern patron = Pattern.compile("[a-zA-ZñÑáéíóúÁÉÍÓÚ0-9\\s,_.-;:+!¡¿?'()]*$");
        if(!patron.matcher(texto).matches() || texto.length() == 0 || texto.trim().equals("")){
            return false;
        }
        return true;
    }
    private void WritePDF(){
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY_FILES);
        boolean isDirectoryCreate = file.exists();

        if(!isDirectoryCreate) {
            isDirectoryCreate = file.mkdirs();
        }
        if(isDirectoryCreate) {

            Long TimeStamp = System.currentTimeMillis() / 1000;
            pdfname = TimeStamp.toString() + ".pdf";
            mPathPdf = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY_FILES + File.separator + pdfname;
            Log.i("Path->", "WritePDF: " + mPathPdf.toString().trim());

            BitmapDrawable drawable = (BitmapDrawable) IV_photo.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] imageInByte = stream.toByteArray();

            //Creamos el objecto Report
            Report makePDF = new Report(mPathPdf, asunto, descripcion, imageInByte, this);
            makePDF.execute();
        }
    }
    private void ReadPDF(String archivo, Context context){
        File wFile = new File(archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(wFile),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Snackbar snackbar2 = Snackbar
                    .make(RL_FormEmail, "Necesitas un lector de PDF", Snackbar.LENGTH_LONG);
            snackbar2.show();
        }
    }
    private void alertMsg(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_picture_as_pdf_black_24dp);
        builder.setTitle("¿Estas Seguro?");
        builder.setMessage("El reporte no sera enviado");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                datos = OperacionesBaseDatos
                        .obtenerInstancia(getApplicationContext());
                if (marcador != null) {
                    datos.eliminarMarketNombre(marcador.getTitle());
                    marcador.remove();
                }
                datos.getDb().close();
                Intent actMain = new Intent(FormEmail.this, PicMain.class);
                actMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                actMain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                actMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(actMain);
                FormEmail.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.makePDF_MenuMain:
                if(reporteListo)
                    ReadPDF(mPathPdf,getApplicationContext());
                else
                    Toast.makeText(this, "Crea el reporte", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ayuda_MenuMain:
                Toast.makeText(this, "Ayuda", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete_MenuMain:
                Toast.makeText(this, "Borrar", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        alertMsg();
        //super.onBackPressed();  // Invoca al método
    }
    private void addMarketReport(LatLng coordenadas) {
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        //if(marcador != null) marcador.remove();
        PointInPoly inPoly = new PointInPoly();
        if (inPoly.pointInPolygon(coordenadas, poligonoCucei)||!inPoly.pointInPolygon(coordenadas, poligonoCucei)) {
            marcador = mMap.addMarker(new MarkerOptions()
                    .position(coordenadas)
                    .title(asunto)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_report_problem_black_18dp)));
            mMap.animateCamera(miUbicacion);
        } else {
            Snackbar snackbar = Snackbar
                    .make(RL_FormEmail, "¡Market incorrecto!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
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
                Snackbar.make(RL_FormEmail, "Necesitamos saber tu locaclizacion para generar reportes :(", Snackbar.LENGTH_INDEFINITE)
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
    private void showOptions() {
        final CharSequence[] option = {"Elegir Foto", "Tomar Foto", "Cancelar"};
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
        if (!isDirectoryCreate) {
            isDirectoryCreate = file.mkdirs();
        }
        if (isDirectoryCreate) {
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
            Snackbar.make(RL_FormEmail, "Permisos necesarios para la aplicación", Snackbar.LENGTH_INDEFINITE)
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this, new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanner" + path + ":");
                                    Log.i("ExternalStorage", "->Uri" + uri);
                                }
                            });
                    IV_photo.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load(mPath)
                            .asBitmap()
                            .override(400, 400)
                            .centerCrop()
                            .into(IV_photo);
                    takepic = true;
                    mLatLongReporteActual = mLatLongActual;
                    break;
                case SELECT_PICTURE:
                    IV_photo.setVisibility(View.VISIBLE);
                    pathuri = data.getData();
                    Glide.with(this)
                            .load(pathuri)
                            .asBitmap()
                            .override(400, 400)
                            .centerCrop()
                            .into(IV_photo);
                    takepic = false;
                    mLatLongReporteActual = mLatLongActual;
                    break;
            }
            llenarReporte();
            inPoly = new PointInPoly();
            for(int i = 0; i < listPoligonos.size(); i++) {
                if (inPoly.pointInPolygon(mLatLongReporteActual, listPoligonos.get(i))) {
                    Toast.makeText(this, "Estas en el Edificio: " + listPolys.get(i).second, Toast.LENGTH_LONG).show();
                    Log.i("Edificio->", (String) listPolys.get(i).second);
                    break;
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISISONS) {
            if (grantResults.length == 3 && grantResults[0] == getPackageManager().PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos Aceptados", Toast.LENGTH_SHORT).show();
                BT_photo.setEnabled(true);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
        } else {
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

}
