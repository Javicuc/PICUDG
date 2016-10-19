package com.picudg.catapp.picudg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.PointF;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.jaouan.revealator.Revealator;
import com.jaouan.revealator.animations.AnimationListenerAdapter;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormEmail extends AppCompatActivity {

    /* --> Con la libreria ButterKnife ya no necesitamos estas variables
        private TextInputLayout tilNombre;
        private TextInputLayout tilTelefono;
        private TextInputLayout tilCorreo;
    */

    @BindView(R.id.fab)
    View mFab;

    @BindView(R.id.plane)
    View mPlaneImageView;

    @BindView(R.id.plane_layout)
    View mPlaneLayout;

    @BindView(R.id.inputs_layout)
    View mInputsLayout;

    @BindView(R.id.sky_layout)
    View mSkyLayout;

    @BindView(R.id.sent_layout)
    View mSentLayout;

    @BindView(R.id.check)
    View mCheckImageView;

    @BindView(R.id.til_codigo)
    TextInputLayout tilCodigo;
    /* De momento no utilizaremos telfono
       @BindView(R.id.til_telefono)
       TextInputLayout tilTelefono;
    */
    @BindView(R.id.til_correo)
    TextInputLayout tilCorreo;

    @BindView(R.id.til_maildpto)
    EditText tilMaildpto;

    @BindView(R.id.spin_dpto)
    Spinner spinDpto;

    @BindView(R.id.til_asunto)
    TextInputLayout tilAsunto;

    @BindView(R.id.til_descripcion)
    TextInputLayout tilDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_email);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ButterKnife.bind(this);

        /* --> Referencias TILs (Sustituimos las referencias con la libreria ButterKnife)
            tilNombre = (TextInputLayout) findViewById(R.id.til_nombre);
            tilTelefono = (TextInputLayout) findViewById(R.id.til_telefono);
            tilCorreo = (TextInputLayout) findViewById(R.id.til_correo);
        */

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatos();
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
    /**
     * Send something.
     */
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

    /**
     * Starts fly animation.
     */
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

    /**
     * Restores inputs layout.
     */
    private void retoreInputsLayout() {
        mInputsLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                final Animator circularReveal = ViewAnimationUtils.createCircularReveal(mInputsLayout, (int) (mFab.getX() + mFab.getWidth() / 2), (int) (mFab.getY() + mFab.getHeight() / 2), 0, mInputsLayout.getHeight());
                circularReveal.setDuration(250);
                circularReveal.start();

                mInputsLayout.setVisibility(View.VISIBLE);
                //tilTelefono.getEditText().setText("");
                tilCodigo.getEditText().setText("");
                tilCorreo.getEditText().setText("");
                tilDesc.getEditText().setText("");
                tilAsunto.getEditText().setText("");
                spinDpto.setSelection(0);

            }
        }, 1000);
    }


    /*
   private boolean esTelefonoValido(String telefono) {
       if (!Patterns.PHONE.matcher(telefono).matches()) {
           tilTelefono.setError("Teléfono inválido");
           return false;
       } else {
           tilTelefono.setError(null);
       }

       return true;
   }
   */
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
        Pattern patron = Pattern.compile("[A-Za-z0-9\\s,_.-:+!¡¿?]*$");
        if(!patron.matcher(asunto).matches() || asunto.length() == 0 || asunto.trim().equals("")){
            tilAsunto.setError("Asunto inválido");
            return false;
        }else {
            tilAsunto.setError(null);
        }
        return true;
    }
    private boolean esDescValido(String desc){
        Pattern patron = Pattern.compile("[A-Za-z0-9\\s,_.-:+!¡¿?]*$");
        if(!patron.matcher(desc).matches() || desc.length() == 0 || desc.trim().equals("")){
            tilDesc.setError("Descripción no valida");
            return false;
        }else {
            tilDesc.setError(null);
        }
        return  true;
    }

    private void validarDatos() {
        //String telefono = tilTelefono.getEditText().getText().toString();
        String codigo   = tilCodigo.getEditText().getText().toString();
        String correo   = tilCorreo.getEditText().getText().toString();
        String depend   = spinDpto.getSelectedItem().toString();
        String asunto   = tilAsunto.getEditText().getText().toString();
        String desc     = tilDesc.getEditText().getText().toString();
        int pos         = spinDpto.getSelectedItemPosition();

        //boolean f = esTelefonoValido(telefono);
        boolean a = esCodigoValido(codigo);
        boolean b = esCorreoValido(correo);
        boolean c = esDptoValido(depend,pos);
        boolean d = esAsuntoValido(asunto);
        boolean e = esDescValido(desc);

        if (a && b && c && d && e) {
            // Si se cumple la condicion, enviamos correo
            Toast.makeText(this, "Enviando Correo", Toast.LENGTH_LONG).show();
            send();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
