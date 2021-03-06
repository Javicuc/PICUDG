package com.picudg.catapp.picudg;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.android.gms.nearby.messages.IBeaconId;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaouan.revealator.Revealator;
import com.jaouan.revealator.animations.AnimationListenerAdapter;
import com.picudg.catapp.picudg.Modelo.Coordenadas;
import com.picudg.catapp.picudg.Modelo.Market;
import com.picudg.catapp.picudg.Modelo.Reporte;
import com.picudg.catapp.picudg.SQLite.BaseDatosPicudg;
import com.picudg.catapp.picudg.SQLite.InibdPicudg;
import com.picudg.catapp.picudg.SQLite.OperacionesBaseDatos;
import com.picudg.catapp.picudg.Tools.IfConect;
import com.picudg.catapp.picudg.Tools.OperacionesMaps;
import com.picudg.catapp.picudg.Tools.PointInPoly;
import com.picudg.catapp.picudg.Tools.Report;
import com.picudg.catapp.picudg.Tools.SendMail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FormEmail extends AppCompatActivity implements OnMapReadyCallback {

    /*** Inyecciones, utilizando ButterKnife ***/
    @BindView(R.id.fab)           View         mFab;
    @BindView(R.id.plane)         View         mPlaneImageView;
    @BindView(R.id.sky_layout)    View         mSkyLayout;
    @BindView(R.id.check)         View         mCheckImageView;
    @BindView(R.id.sent_layout)   View         mSentLayout;
    @BindView(R.id.plane_layout)  View         mPlaneLayout;
    @BindView(R.id.inputs_layout) View         mInputsLayout;
    @BindView(R.id.TB_MakePDF)    ToggleButton tb_MakePDF;
    @BindView(R.id.BT_cam)        Button       BT_photo;
    @BindView(R.id.BT_Limpiar)    Button       bt_Limpiar;
    @BindView(R.id.IV_Form_img)   ImageView    IV_photo;
    @BindView(R.id.coorlayout)    CoordinatorLayout RL_FormEmail;

    private static String APP_DIRECTORY = "picudg/";
    private static String MEDIA_DIRECTORY_FILES = APP_DIRECTORY + "files";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Images";
    private final int     PHOTO_CODE = 200;
    private final int     SELECT_PICTURE = 300;

    private String pdfname;
    private String asunto;
    private String descripcion;
    private String correoContacto;
    private String encargadoContacto;
    private String rolContacto;
    private String nombreCentro;
    private String direccionCentro;
    private String acronimoCentro;

    private String mPathPdf;
    private String mPath;
    private Uri    pathuri;

    private boolean       takepic;
    private boolean       reporteListo;
    private String        EdificioActual;
    private GoogleMap     mMap;
    private LatLng        mLatLongActual;
    private LatLng        mLatLongReporteActual;
    private Marker        marcador;
    private PolygonOptions poligonoCentro;
    private CameraUpdate  camaraPos;
    private FirebaseUser  user;
    private PointInPoly   inPoly;
    private OperacionesBaseDatos datos;
    private List<android.util.Pair> listPolys;
    private List<Polygon> listCentros;
    List<Pair> listpolyscentros;
    OperacionesMaps config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Crear Reporte");

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
                    obtenerEdificioActual();
                    obtenerInfoCentro();
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
                LimpiarPantalla();
                mMap.animateCamera(camaraPos);
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
        builder.setIcon(R.mipmap.ic_wireless_error);
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
        if(getEdificioActual() == null) {

            final TextView tv_depend = (TextView) v.findViewById(R.id.TV_dependencia);
            final Spinner spinner = (Spinner) v.findViewById(R.id.SPN_ubicacionesForm);
            spinner.setVisibility(View.VISIBLE);
            tv_depend.setVisibility(View.VISIBLE);

            //Creando Adaptador para Spinner
            DatabaseUtils.dumpCursor(datos.obtenerEdificiosSpinner(acronimoCentro));
            SimpleCursorAdapter spinnerEdificios = new SimpleCursorAdapter(this,
                    android.R.layout.simple_spinner_item,
                    datos.obtenerEdificiosSpinner(acronimoCentro),
                    new String[]{InibdPicudg.Ubicacion.NOMBRE},
                    new int[]{android.R.id.text1},
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

            spinner.setAdapter(spinnerEdificios);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Cursor c1 = (Cursor) parent.getItemAtPosition(position);
                    EdificioActual = c1.getString(c1.getColumnIndex(InibdPicudg.Ubicacion.NOMBRE));
                    obtenerContactoEdificio(EdificioActual);
                    //Toast.makeText(FormEmail.this, EdificioActual, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        Button okReporte = (Button) v.findViewById(R.id.BT_reporteDialog);
        final TextInputLayout tilAsunto = (TextInputLayout) v.findViewById(R.id.TIL_asunto);
        final TextInputLayout tilDescripcion = (TextInputLayout) v.findViewById(R.id.TIL_descripcion);

        final AlertDialog dialog = builder.create();

        okReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asunto      = tilAsunto.getEditText().getText().toString().trim();
                descripcion = tilDescripcion.getEditText().getText().toString().trim();

                if (validaCampos(getDescripcion()) && validaCampos(getAsunto())) {

                    WritePDF();

                    Date date = new Date();
                    SimpleDateFormat ft =
                            new SimpleDateFormat ("yyyy/MM/dd, hh:mm:ss");
                    addMarketReport(mLatLongReporteActual);
                    String idMarkerTMP = datos.insertarMarket(new Market(null,null,"Reporte",getAsunto()));

                    Cursor idUsuarioTMP = datos.obtenerUsuarioCorreo(user.getEmail());
                    idUsuarioTMP.moveToFirst();
                    DatabaseUtils.dumpCursor(idUsuarioTMP);
                    if(idUsuarioTMP != null){
                        try {
                            datos.getDb().beginTransaction();

                            datos.insertarReportes(new Reporte(null, getAsunto(), getDescripcion(), mPathPdf,
                                    idUsuarioTMP.getString(idUsuarioTMP.getColumnIndex("ID_Usuario")), idMarkerTMP,
                                    String.valueOf(takepic ? mPath : pathuri), ft.format(date).toString(), EdificioActual));

                            datos.insertarCoordenadas(new Coordenadas(null, mLatLongReporteActual.longitude, mLatLongReporteActual.latitude,
                                    null, null, idMarkerTMP, datos.obtenerCountMarkets()));

                            datos.getDb().setTransactionSuccessful();
                        } catch (SQLiteConstraintException e){
                            Toast.makeText(FormEmail.this, "Datos inconclusos o no validos", Toast.LENGTH_SHORT).show();
                            Log.d("Insertar Usuario->", "Fallo al insertar: ,", e);
                        }finally {
                            tb_MakePDF.setClickable(false);
                            tb_MakePDF.setChecked(true);
                            tb_MakePDF.setBackgroundColor(getResources().getColor(R.color.accent));
                            reporteListo = true;
                            datos.getDb().endTransaction();
                        }
                    } else {
                        Toast.makeText(FormEmail.this, "¡Fallo al comprobar autentificación!", Toast.LENGTH_LONG).show();
                    }

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng mLatLngCucei = new LatLng(20.653910, -103.325807);
        LatLng mLatLngCucea = new LatLng(20.741980, -103.380219);

        config = new OperacionesMaps(mMap,getApplicationContext());

        mMap          = config.configurarMapa();

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            mMap.setMyLocationEnabled(true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
                mMap.setMyLocationEnabled(true);
            }
        }
        inPoly = new PointInPoly();
        listpolyscentros = config.getListPolysCentros();
        listCentros = config.getListPoligonosCentros();
        if(comprobarUbicacion()) {
            if (acronimoCentro != null) {
                if (acronimoCentro.equals("CUCEI")) {
                    camaraPos = CameraUpdateFactory.newLatLngZoom(mLatLngCucei, 16);
                    mMap.animateCamera(camaraPos);
                } else if (acronimoCentro.equals("CUCEA")) {
                    camaraPos = CameraUpdateFactory.newLatLngZoom(mLatLngCucea, 16);
                    mMap.animateCamera(camaraPos);
                }
                Snackbar snackbar = Snackbar
                        .make(RL_FormEmail, acronimoCentro + ": ¡Bienvenido!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }else {
            camaraPos = CameraUpdateFactory.newLatLngZoom(mLatLongActual, 16);
            mMap.animateCamera(camaraPos);
            Snackbar snackbar = Snackbar
                    .make(RL_FormEmail, "¡No estas dentro de un centro de estudio!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    public boolean comprobarUbicacion(){
        myUbication();
        for(int i = 0; i < listCentros.size(); i++) {
            //OBTENEMOS TRUE, SI ES ESTA EN UN CENTRO DE ESTUDIO
            if (inPoly.pointInPolygon(mLatLongActual, listCentros.get(i))) {
                acronimoCentro = (String) listpolyscentros.get(i).second; //OBTENEMOS EL ACRONIMO DEL CENTRO DE ESTUDIO
                Log.i("Acronimo?",acronimoCentro);
                poligonoCentro = (PolygonOptions) listpolyscentros.get(i).first; //OBTENEMOS EL POLIGONO DEL CENTRO DE ESTUDIO
                Log.i("Centro?",poligonoCentro.toString());
                listPolys     = config.getListNombrePolyEdificiosCentro(acronimoCentro); //OBTENEMOS LA LISTA DE NOMBRES Y POLIGONOS DEL CENTRO DE ESTUDIO
                BT_photo.setClickable(true);
                return true;
            }
        }
        //SI EL USUARIO NO ESTA EN EL CENTRO DE ESTUDIO, BLOQUEAMOS EL BOTON DE CAPTURA DE IMAGEN
        BT_photo.setClickable(false);
        return false;
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
                mFab.setVisibility(View.INVISIBLE);
                // - Fly away.
                flyAway();
            }
        });
        circularReveal.start();
    }
    private void enviarmail() {
        //Creamos el objecto SendMail
        Log.i("PDFName-> ", getPdfname());
        Log.i("CorreoContacto-> ", getCorreoContacto());
        SendMail sm = new SendMail(this, getCorreoContacto(), asunto, getPdfname());
        //llamamos a ejecutar el proceso que envia el correo
        sm.execute();
    }
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
                eliminarReporte();
            }
        }, 1000);
    }
    private void eliminarReporte(){
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());
        if (marcador != null) {
            datos.eliminarMarketNombre(marcador.getTitle());
            marcador.remove();
        }
        else if(takepic) {
            File file = new File(mPath);
            file.delete();
            takepic = false;
            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(mPath))));
        }else if(reporteListo){
            File file = new File(mPathPdf);
            file.delete();
            reporteListo = false;
        }
        datos.getDb().close();
        mLatLongReporteActual = null;
    }
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
                mFab.setVisibility(View.VISIBLE);
                IV_photo.setImageResource(0);
                IV_photo.setVisibility(View.GONE);
                tb_MakePDF.setClickable(true);
                tb_MakePDF.setChecked(false);
                tb_MakePDF.setBackgroundColor(getResources().getColor(R.color.primary_light));
                reporteListo = false;
                EdificioActual = null;
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
            mPathPdf = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY_FILES + File.separator + getPdfname();
            Log.i("Path->", "WritePDF: " + mPathPdf.toString().trim());

            BitmapDrawable drawable = (BitmapDrawable) IV_photo.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] imageInByte = stream.toByteArray();

            //Creamos el objecto Report
            Report makePDF = new Report(mPathPdf, getAsunto(), getDescripcion(), imageInByte,getEdificioActual(),getNombreCentro(),getDireccionCentro(),
                    getEncargadoContacto(),getRolContacto(),this);
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
        builder.setIcon(R.mipmap.ic_cancel);
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
                if(reporteListo){

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
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewPDF_FormEmail:
                if(reporteListo)
                    ReadPDF(mPathPdf,getApplicationContext());
                else
                    Toast.makeText(this, "Crea el reporte", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ayuda_FormEmail:
                Toast.makeText(this, "Ayuda", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.syncUserMap:
                myUbication();
                if(comprobarUbicacion()) {
                    Snackbar snackbar = Snackbar
                            .make(RL_FormEmail, acronimoCentro + ": ¡Bienvenido!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else
                    Toast.makeText(this, "¡Accede a tu centro de estudios!", Toast.LENGTH_SHORT).show();
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
        inPoly = new PointInPoly();
        if (mLatLongReporteActual != null) {
            marcador = mMap.addMarker(new MarkerOptions()
                    .position(coordenadas)
                    .title(asunto)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_danger)));
            mMap.animateCamera(miUbicacion);
        } else {
            Snackbar snackbar = Snackbar
                    .make(RL_FormEmail, "¡Fallo al obtener localizacón!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
    private void updatePosition(Location location) {
        if (location != null) {
            mLatLongActual = new LatLng(location.getLatitude(), location.getLongitude());
            //addMarketReport(mLatLongActual); //CADA QUE SE REFRESCA LA UBICACION COLOCAMOS UN MARKER
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
    private void showOptions() {
        final CharSequence[] option = {"Elegir Foto", "Tomar Foto"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una opción:");
        builder.setIcon(R.mipmap.ic_addpicture);
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
    public String getEncargadoContacto() {
        return encargadoContacto;
    }
    public String getRolContacto() {
        return rolContacto;
    }
    public String getEdificioActual() {
        return EdificioActual;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getAsunto() {
        return asunto;
    }
    public String getPdfname() {
        return pdfname;
    }
    public String getCorreoContacto() {
        return correoContacto;
    }
    public String getNombreCentro() {
        return nombreCentro;
    }
    public String getDireccionCentro() {
        return direccionCentro;
    }
    private void obtenerEdificioActual() {
        if(listPolys != null) {
            inPoly = new PointInPoly();
            Log.i("SizePolys", String.valueOf(listPolys.size()));
            Log.i("NamePolys", (String) listPolys.get(0).second);
            Log.i("LatLongReporte", String.valueOf(mLatLongReporteActual));
            for (int i = 0; i < listPolys.size(); i++) {
                Log.i("FlagFor", String.valueOf(i));
                if (inPoly.pointInPolygonOptions(mLatLongReporteActual, (PolygonOptions) listPolys.get(i).first)) {
                    EdificioActual = listPolys.get(i).second.toString();
                    Log.i("Edificio->", (String) listPolys.get(i).second);
                    obtenerContactoEdificio(EdificioActual);
                    break;
                } else {
                    EdificioActual = null;
                }
            }
        }
    }
    private void obtenerInfoCentro(){
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());
        Cursor c = datos.obtenerIdCentroEstudio(acronimoCentro);
        c.moveToFirst();
        if(c != null){
            nombreCentro    = c.getString(c.getColumnIndex("Nombre_Centro"));
            direccionCentro = c.getString(c.getColumnIndex("Direccion_Centro"));
        }else{
            nombreCentro    = "Desconocido";
            direccionCentro = "Desconocido";
        }
        c.close();
        datos.getDb().close();
    }
    private void obtenerContactoEdificio(String edificioActual){
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());
        Cursor c = datos.obtenerContactosUbicacionEdificio(edificioActual);
        c.moveToFirst();
        if(c != null){
            encargadoContacto = c.getString(c.getColumnIndex("Nombre"));
            correoContacto    = c.getString(c.getColumnIndex("Correo"));
            rolContacto       = c.getString(c.getColumnIndex("Rol"));
        }else{
            correoContacto = "javier.jalr7+ServiciosGenerales@gmail.com";
        }
        c.close();
        datos.getDb().close();

    }

}
