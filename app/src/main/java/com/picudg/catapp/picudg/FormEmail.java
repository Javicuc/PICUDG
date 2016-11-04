package com.picudg.catapp.picudg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.MailTo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPHeaderCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.jaouan.revealator.Revealator;
import com.jaouan.revealator.animations.AnimationListenerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormEmail extends AppCompatActivity {

    /*** Paths que uyilizamos en el proceso de la activity ***/
    private static String APP_DIRECTORY = "picudg/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "files";
    private String mPath;
    private String image_path;
    private String pdfname;

    /*** Inyecciones, utilizando ButterKnife ***/
    @BindView(R.id.fab) View mFab;
    @BindView(R.id.plane) View mPlaneImageView;
    @BindView(R.id.plane_layout) View mPlaneLayout;
    @BindView(R.id.inputs_layout) View mInputsLayout;
    @BindView(R.id.sky_layout) View mSkyLayout;
    @BindView(R.id.sent_layout) View mSentLayout;
    @BindView(R.id.check) View mCheckImageView;
    @BindView(R.id.til_codigo) TextInputLayout tilCodigo;
    @BindView(R.id.til_correo) TextInputLayout tilCorreo;
    @BindView(R.id.til_maildpto) EditText tilMaildpto;
    @BindView(R.id.spin_dpto) Spinner spinDpto;
    @BindView(R.id.til_asunto) TextInputLayout tilAsunto;
    @BindView(R.id.til_descripcion) TextInputLayout tilDesc;
    @BindView(R.id.IV_Mail) ImageView IV_photo;
    @BindView(R.id.coorlayout) CoordinatorLayout RL_FormEmail;
    byte[] imageInByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_email);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ButterKnife.bind(this);

        final Intent intent = getIntent();
        image_path = intent.getStringExtra("imagePath");
        Uri fileUri = Uri.parse(image_path);
        IV_photo.setImageURI(fileUri);

        BitmapDrawable drawable = (BitmapDrawable) IV_photo.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageInByte = stream.toByteArray();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarDatos()){
                    if (isNetDisponible() && isOnlineNet()) {
                        WritePDF();
                        send();
                        startCamara();
                    }else{
                        Snackbar snackbar = Snackbar.make(RL_FormEmail, "¡Verifica tu conexion a internet!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        startCamara();
                    }
                }else{
                    Snackbar snackbar = Snackbar.make(RL_FormEmail, "Datos inconclusos", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        spinDpto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String [] Mails = getResources().getStringArray(R.array.Correo_dependencia);
                tilMaildpto.setText(Mails[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }
    public void startCamara(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent actCam = new Intent(FormEmail.this, Camara.class);
                actCam.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                actCam.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                actCam.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                actCam.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(actCam);
            }
        }, 3000);
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
        enviarmail();
        circularReveal.start();
    }

    private void enviarmail() {

        /** Obtenemos el contenido del correo **/
        String asunto = tilAsunto.getEditText().getText().toString().trim();
        String cc = tilCorreo.getEditText().getText().toString().trim();
        String dest = tilMaildpto.getText().toString();

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

    /**Inicializamos nuestros componentes.*/
    private void retoreInputsLayout() {
        mInputsLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                final Animator circularReveal = ViewAnimationUtils.createCircularReveal(mInputsLayout, (int) (mFab.getX() + mFab.getWidth() / 2), (int) (mFab.getY() + mFab.getHeight() / 2), 0, mInputsLayout.getHeight());
                circularReveal.setDuration(250);
                circularReveal.start();

                mInputsLayout.setVisibility(View.VISIBLE);
                tilCodigo.getEditText().setText("");
                tilCorreo.getEditText().setText("");
                tilDesc.getEditText().setText("");
                tilAsunto.getEditText().setText("");
                IV_photo.setImageResource(0);
                spinDpto.setSelection(0);
            }
        }, 1000);
    }
    private boolean esCodigoValido(String codigo) {
        Pattern patron = Pattern.compile("^[0-9]*");
        if (!patron.matcher(codigo).matches() || (codigo.length() != 9)) {
            tilCodigo.setError("Codigo inválido");
            return false;
        } else {
            tilCodigo.setError(null);
        }
        return true;
    }
    private boolean esCorreoValido(String correo) {
        Pattern patron = Pattern.compile("^[A-Za-z0-9+.]+@alumnos.udg.mx$");
        if (!patron.matcher(correo).matches()) {
            tilCorreo.setError("Correo electrónico inválido");
            return false;
        } else {
            tilCorreo.setError(null);
        }

        return true;
    }
    private boolean esDptoValido(String dpto,int pos) {
        if (dpto.equals("Dpto...")) {
            Toast.makeText(this, "Departamento inválido", Toast.LENGTH_LONG).show();
            return false;
        }else {
            String [] Mails = getResources().getStringArray(R.array.Correo_dependencia);
            tilMaildpto.setText(Mails[pos]);
        }
        return true;
    }
    private boolean esAsuntoValido(String asunto){
        Pattern patron = Pattern.compile("[a-zA-ZñÑáéíóúÁÉÍÓÚ0-9\\s,_.-:+!¡¿?'()]*$");
        if(!patron.matcher(asunto).matches() || asunto.length() == 0 || asunto.trim().equals("")){
            tilAsunto.setError("Asunto inválido");
            return false;
        }else {
            tilAsunto.setError(null);
        }
        return true;
    }
    private boolean esDescValido(String desc){
        Pattern patron = Pattern.compile("[a-zA-ZñÑáéíóúÁÉÍÓÚ0-9\\s,_.-:+!¡¿?()']*$");
        if(!patron.matcher(desc).matches() || desc.length() == 0 || desc.trim().equals("")){
            tilDesc.setError("Descripción no valida");
            return false;
        }else {
            tilDesc.setError(null);
        }
        return  true;
    }
    private boolean validarDatos() {
        String codigo   = tilCodigo.getEditText().getText().toString();
        String correo   = tilCorreo.getEditText().getText().toString();
        String depend   = spinDpto.getSelectedItem().toString();
        String asunto   = tilAsunto.getEditText().getText().toString();
        String desc     = tilDesc.getEditText().getText().toString();
        int pos         = spinDpto.getSelectedItemPosition();

        boolean a = esCodigoValido(codigo);
        //boolean b = esCorreoValido(correo);
        boolean c = esDptoValido(depend,pos);
        boolean d = esAsuntoValido(asunto);
        boolean e = esDescValido(desc);

        if (a && c && d && e) {
            return true;
        }
        return false;
    }
    private void WritePDF(){
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreate = file.exists();

        if(!isDirectoryCreate) {
            isDirectoryCreate = file.mkdirs();
        }
        if(isDirectoryCreate) {

            Document New_Document = new Document();
            Date date = new Date();
            Long TimeStamp = System.currentTimeMillis() / 1000;
            pdfname = TimeStamp.toString() + ".pdf";
            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + pdfname;
            File New_File = new File(mPath);
            if(New_File.exists()){
                New_File.delete();
            }
            try {
                PdfWriter writter = PdfWriter.getInstance(New_Document,new FileOutputStream(New_File));
                writter.setLinearPageMode();
                writter.setFullCompression();

                /** Propiedades del docuemtno y abrimos **/
                New_Document.setPageSize(PageSize.A4);
                New_Document.open();

                /** Crear las fuentes para el contenido y los titulos **/
                Font fontContenido = FontFactory.getFont(
                        FontFactory.TIMES_ROMAN.toString(), 11, Font.NORMAL,
                        BaseColor.DARK_GRAY);
                Font fontTitulos = FontFactory.getFont(
                        FontFactory.TIMES_ROMAN, 16, Font.BOLDITALIC,
                        BaseColor.DARK_GRAY);

                /** Insertando Logo de la universidad **/
                ByteArrayOutputStream streamLogo = new ByteArrayOutputStream();
                Bitmap bitmapLogo = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.udg);
                bitmapLogo.compress(Bitmap.CompressFormat.PNG, 100 , streamLogo);
                Image LogoUni = null;
                try {
                    LogoUni = Image.getInstance(streamLogo.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LogoUni.setAlignment(Image.LEFT);
                New_Document.add(LogoUni);

                /** Creamos parrafo y seteamos la font **/
                Paragraph p1  = new Paragraph();
                p1.add(new Phrase("SISTEMA DE REPORTE DE INFRAESTRUCTURA UDG-CUCEI.",fontTitulos));
                p1.add(new Phrase(Chunk.NEWLINE));
                p1.add(new Phrase(Chunk.NEWLINE));
                p1.setAlignment(Paragraph.ALIGN_JUSTIFIED);
                New_Document.add(p1);

                /** Ubicacion del problema y detalles **/
                Paragraph pdet = new Paragraph();
                pdet.add(new Phrase("Centro Uniersitario de Ciencias Exactas e Ingenierias (CUCEI).",fontContenido));
                pdet.add(new Phrase(Chunk.NEWLINE));
                pdet.add(new Phrase("Blvd. Marcelino García Barragán 1421, Ciudad Universitaria, 44430 Guadalajara, JAL.",fontContenido));
                pdet.add(new Phrase(Chunk.NEWLINE));
                pdet.add(new Phrase("Departamento de Ciencias Basicas, Jorge Zamudio Hernandez.",fontContenido));
                pdet.add(new Phrase(Chunk.NEWLINE));
                pdet.add(new Phrase(date.toString()+", DEDX-A015",fontContenido));
                pdet.add(new Phrase(Chunk.NEWLINE));
                pdet.add(new Phrase(Chunk.NEWLINE));
                New_Document.add(pdet);

                /** Agregamos la descripcion del usuario **/
                Paragraph p2 = new Paragraph();
                p2.add(new Phrase(tilAsunto.getEditText().getText().toString().trim() + ":",fontTitulos));
                p2.add(new Phrase(Chunk.NEWLINE));
                p2.add(new Phrase(Chunk.NEWLINE));
                p2.add(new Phrase(tilDesc.getEditText().getText().toString().trim(),fontContenido));
                p2.add(new Phrase(Chunk.NEWLINE));
                p2.add(new Phrase(Chunk.NEWLINE));
                //Parrafo de prueba
                p2.add(new Phrase(
                        "El sensor de la X-E1 presenta el mismo excelente rendimiento que el X-Trans CMOS "
                                + "de 16 megapíxeles del modelo superior de la serie X, la X-Pro1. Gracias la matriz "
                                + "de filtro de color con disposición aleatoria de los píxeles, desarrollada originalmente"
                                + " por Fujifilm, el sensor X-Trans CMOS elimina la necesidad del filtro óptico de paso bajo"
                                + " que se utiliza en los sistemas convencionales para inhibir el muaré a expensas de la"
                                + " resolución. Esta matriz innovadora permite al sensor X-Trans CMOS captar la luz sin filtrar"
                                + " del objetivo y obtener una resolución sin precedentes. La exclusiva disposición aleatoria de"
                                + " la matriz de filtro de color resulta asimismo muy eficaz para mejorar la separación de ruido"
                                + " en la fotografía de alta sensibilidad. Otra ventaja del gran sensor APS-C es la capacidad"
                                + " para crear un hermoso efecto “bokeh”, el estético efecto desenfocado que se crea al disparar"
                                + " con poca profundidad de campo.",
                        fontContenido));
                p2.setAlignment(Paragraph.ALIGN_JUSTIFIED);
                p2.add(new Phrase(Chunk.NEWLINE));
                p2.add(new Phrase(Chunk.NEWLINE));
                New_Document.add(p2);

                /** Obtenemos la imagen que capturamos en el intent **/

                Image userImage = null;
                try {
                    userImage = Image.getInstance(imageInByte);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /** Cambiamos el tamaño de la imagen, para que se adapte al documento **/
                float documentWidth = New_Document.getPageSize().getWidth() - New_Document.leftMargin() - New_Document.rightMargin();
                float documentHeight = New_Document.getPageSize().getHeight() - New_Document.topMargin() - New_Document.bottomMargin();
                userImage.scaleToFit(documentWidth, documentHeight);
                userImage.setAlignment(Image.MIDDLE);
                /**Agregamos la imagen que capturo el usuario(intent) al documento **/
                New_Document.add(userImage);
                New_Document.close();

            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private void ReadPDF(String archivo, Context context){
        Snackbar snackbar = Snackbar
                .make(RL_FormEmail, "Leyendo reporte", Snackbar.LENGTH_LONG);
        snackbar.show();

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
                Intent actCam = new Intent(FormEmail.this, Camara.class);
                actCam.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                actCam.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                actCam.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(actCam);
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
    public void onBackPressed() {
        alertMsg();
        //super.onBackPressed();  // Invoca al método
    }
    private void alertConect(){
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
    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }
    public Boolean isOnlineNet() {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.mx");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
