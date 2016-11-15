package com.picudg.catapp.picudg;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.DatabaseUtils;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.picudg.catapp.picudg.Modelo.CentroEstudio;
import com.picudg.catapp.picudg.Modelo.Contacto;
import com.picudg.catapp.picudg.Modelo.Contactos_Ubicacion;
import com.picudg.catapp.picudg.Modelo.Coordenadas;
import com.picudg.catapp.picudg.Modelo.Market;
import com.picudg.catapp.picudg.Modelo.Reporte;
import com.picudg.catapp.picudg.Modelo.Ubicacion;
import com.picudg.catapp.picudg.Modelo.Usuario;
import com.picudg.catapp.picudg.SQLite.OperacionesBaseDatos;

import org.w3c.dom.Text;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity{


    @BindView(R.id.signin)
    TextView signin;
    @BindView(R.id.LoginFacebook)
    LoginButton LogBook;
    @BindView(R.id.TIL_passwordLogin)
    TextInputLayout tilPassword;
    @BindView(R.id.TIL_correoLogin)
    TextInputLayout tilCorreo;
    @BindView(R.id.fabreg)
    FloatingActionButton fabReg;
    @BindView(R.id.bt_Facebook)
    Button bt_fb;
    @BindView(R.id.LoginGoogle)
    SignInButton LogGoogle;
    @BindView(R.id.google)
    Button bt_google;
    @BindView(R.id.progress_bar)
    ProgressBar prog_bar;
    @BindView(R.id.forgotpass)
    TextView tvResetPass;

    private CallbackManager callbackManager;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;


    OperacionesBaseDatos datos;

    public class TareaPruebaDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            // [INSERCIONES]
            try {

                datos.getDb().beginTransaction();

                /** INSERTAR CENTROS DE ESTUDIO **/
                String centro1 = datos.insertarCentroEstudio(new CentroEstudio(null, "Centro Universitario de Ciencias Exactas e Ingenierias", "CUCEI",
                        "Blvd. Marcelino García Barragán 1421, Ciudad Universitaria, 44430 Guadalajara, JAL"));
                String centro2 = datos.insertarCentroEstudio(new CentroEstudio(null, "Centro Universitario de Ciencias Economico Administrativas", "CUCEA",
                        "Av. José Parres Arias 74, Núcleo Universitario Los Belenes, 45100 Zapopan, Jal."));

                /** INSERTAR USUARIOS **/
                String usuario1 = datos.insertarUsuario(new Usuario(null, "Javier Alejandro López Rangel", "javier.jalr7@gmail.com", "215256109", centro1));
                String usuario2 = datos.insertarUsuario(new Usuario(null, "Oscar Brandon Ramos Sanches", "javier.jalr7@gmail.com", "215254110", centro1));

                /** INSERTAR UBICACIONES **/
                String ubicacion1 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_ALFA", centro1));
                String ubicacion2 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_beta", centro1));
                String ubicacion3 = datos.insertarUbicacion(new Ubicacion(null, "Biblioteca", centro1));
                String ubicacion4 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_X", centro1));
                String ubicacion5 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_U", centro1));
                String ubicacion6 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_V1", centro1));
                String ubicacion7 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_V2", centro1));
                String ubicacion8 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_O", centro1));
                String ubicacion9 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_T", centro1));
                String ubicacion10 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_R", centro1));
                String ubicacion11 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_GAMA", centro1));
                String ubicacion12 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_S", centro1));
                String ubicacion13 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_W", centro1));
                String ubicacion14 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_E", centro1));
                String ubicacion15 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_A", centro1));
                String ubicacion16 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_B", centro1));
                String ubicacion17 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_C", centro1));
                String ubicacion18 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_D", centro1));
                String ubicacion19 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_J", centro1));
                String ubicacion20 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_N", centro1));
                String ubicacion21 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_M", centro1));
                String ubicacion22 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_L", centro1));
                String ubicacion23 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_Z1", centro1));
                String ubicacion24 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_Z2", centro1));
                String ubicacion25 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_G", centro1));
                String ubicacion26 = datos.insertarUbicacion(new Ubicacion(null, "EDIF_Q", centro1));
                String ubicacion27 = datos.insertarUbicacion(new Ubicacion(null, "Auditorio Matute Remus", centro1));
                String ubicacion28 = datos.insertarUbicacion(new Ubicacion(null, "Auditorio Enrique Díaz de León", centro1));
                String ubicacion29 = datos.insertarUbicacion(new Ubicacion(null, "Auditorio Antonio Alatorre", centro1));
                String ubicacion30 = datos.insertarUbicacion(new Ubicacion(null, "Auditorio Antonio Rodríguez", centro1));

                /** INSERTAR CONTACTOS **/
                String contacto1 = datos.insertarContacto(new Contacto(null, "Jorge Alberto Calvillo Vargas", "javier.jalr7@gmail.com", "3310086200",
                        "Coodinador de Servicios Generales", centro1));
                String contacto2 = datos.insertarContacto(new Contacto(null, "Edwin Francisco Ruiz Martínez", "javier_jalr@hotmail.com", "3310086201",
                        "Jefe de Unidad de Mantenimiento", centro1));
                String contacto3 = datos.insertarContacto(new Contacto(null, "Ricardo Fernando Sánchez Hernández", "javier.lopez@alumnos.udg.mx", "3310086202",
                        "Jefe de Unidad de Adquisición y Suministros", centro1));
                String contacto4 = datos.insertarContacto(new Contacto(null, "Javier Alejandro Lopez Rangel", "javimex51@gmail.com", "3310086244",
                        "Jefe de Unidad de Computación", centro2));

                /** INSERTAR MARKETS **/
                String market1 = datos.insertarMarket(new Market(null,null,"Reporte","Socket Mal Puesto"));
                String market2 = datos.insertarMarket(new Market(null,null,"Reporte","Fuga De Agua"));
                /** INSERTAR REPORTES **/
                String reporte1 = datos.insertarReportes(new Reporte(null,"Socket Mal Puesto","El socket esta mal puesto",null,usuario1,market1));
                String reporte2 = datos.insertarReportes(new Reporte(null,"Fuga De Agua","Hay una fuga de agua en el X",null,usuario1,market1));


                /** RELACIONAR UBICACIONES CON SUS RESPECTIVOS CONTACTOS **/
                String ContactosUbicaciones1 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion1, contacto1));
                String ContactosUbicaciones2 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion2, contacto2));
                String ContactosUbicaciones3 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion3, contacto3));
                String ContactosUbicaciones4 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion4, contacto1));
                String ContactosUbicaciones5 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion5, contacto2));
                String ContactosUbicaciones6 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion6, contacto3));
                String ContactosUbicaciones7 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion7, contacto1));
                String ContactosUbicaciones8 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion8, contacto2));
                String ContactosUbicaciones9 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion9, contacto3));
                String ContactosUbicaciones10 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion10, contacto1));
                String ContactosUbicaciones11 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion11, contacto2));
                String ContactosUbicaciones12 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion12, contacto3));
                String ContactosUbicaciones13 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion13, contacto1));
                String ContactosUbicaciones14 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion14, contacto2));
                String ContactosUbicaciones15 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion15, contacto3));
                String ContactosUbicaciones16 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion16, contacto1));
                String ContactosUbicaciones17 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion17, contacto2));
                String ContactosUbicaciones18 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion18, contacto3));
                String ContactosUbicaciones19 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion19, contacto1));
                String ContactosUbicaciones20 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion20, contacto2));
                String ContactosUbicaciones21 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion21, contacto3));
                String ContactosUbicaciones22 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion22, contacto1));
                String ContactosUbicaciones23 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion23, contacto2));
                String ContactosUbicaciones24 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion24, contacto3));
                String ContactosUbicaciones25 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion25, contacto1));
                String ContactosUbicaciones26 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion26, contacto2));
                String ContactosUbicaciones27 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion27, contacto3));
                String ContactosUbicaciones28 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion28, contacto1));
                String ContactosUbicaciones29 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion29, contacto2));
                String ContactosUbicaciones30 = datos.insertarContactosUbicaciones(new Contactos_Ubicacion(null, ubicacion30, contacto3));

                /** INSERTAR COORDENADAS                                        ID,  Longitud,   Latitud, fkUbi,fkCentro,fkMarket **/
                String coordenada1 = datos.insertarCoordenadas(new Coordenadas(null, -103.326595, 20.656397, null, centro1, null));
                String coordenada2 = datos.insertarCoordenadas(new Coordenadas(null, -103.327721, 20.655883, null, centro1, null));
                String coordenada3 = datos.insertarCoordenadas(new Coordenadas(null, -103.327721, 20.655883, null, centro1, null));
                String coordenada4 = datos.insertarCoordenadas(new Coordenadas(null, -103.325795, 20.653345, null, centro1, null));
                String coordenada5 = datos.insertarCoordenadas(new Coordenadas(null, -103.324238, 20.654013, null, centro1, null));
                String coordenada6 = datos.insertarCoordenadas(new Coordenadas(null, -103.324711, 20.655633, null, centro1, null));
                String coordenada7 = datos.insertarCoordenadas(new Coordenadas(null, -103.323984, 20.656062, null, centro1, null));
                String coordenada8 = datos.insertarCoordenadas(new Coordenadas(null, -103.32418, 20.659559, null, centro1, null));
                String coordenada9 = datos.insertarCoordenadas(new Coordenadas(null, -103.324616, 20.659792, null, centro1, null));
                String coordenada10 = datos.insertarCoordenadas(new Coordenadas(null, -103.324515, 20.658278, null, centro1, null));
                String coordenada11 = datos.insertarCoordenadas(new Coordenadas(null, -103.325909, 20.658169, null, centro1, null));
                String coordenada12 = datos.insertarCoordenadas(new Coordenadas(null, -103.325929, 20.658384, null, centro1, null));
                String coordenada13 = datos.insertarCoordenadas(new Coordenadas(null, -103.326397, 20.658399, null, centro1, null));
                String coordenada14 = datos.insertarCoordenadas(new Coordenadas(null, -103.326466, 20.659354, null, centro1, null));
                String coordenada15 = datos.insertarCoordenadas(new Coordenadas(null, -103.327018, 20.659757, null, centro1, null));
                String coordenada16 = datos.insertarCoordenadas(new Coordenadas(null, -103.327352, 20.659329, null, centro1, null));
                String coordenada17 = datos.insertarCoordenadas(new Coordenadas(null, -103.327261, 20.657806, null, centro1, null));
                String coordenada18 = datos.insertarCoordenadas(new Coordenadas(null, -103.327666, 20.657616, null, centro1, null));
                String coordenada19 = datos.insertarCoordenadas(new Coordenadas(null, -103.327535, 20.657027, null, centro1, null));
                String coordenada20 = datos.insertarCoordenadas(new Coordenadas(null, -103.327385, 20.657037, null, centro1, null));
                String coordenada21 = datos.insertarCoordenadas(new Coordenadas(null, -103.327376, 20.656939, null, centro1, null));
                String coordenada22 = datos.insertarCoordenadas(new Coordenadas(null, -103.326632, 20.656987, null, centro1, null));
                String coordenada23 = datos.insertarCoordenadas(new Coordenadas(null,-103.326632, 20.656987,null,null,market1));

                // Eliminación Contacto
                datos.eliminarContacto(contacto4);

                // Actualización Ubicacion
               datos.actualizarUbicacion(new Ubicacion(ubicacion2,"EDIF_BETA",centro1));

                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }

            // [QUERIES]
            Log.d("ContactosUbicaciones","UbicacionesUbicaciones");
            DatabaseUtils.dumpCursor(datos.obtenerCentroEstudio());
            DatabaseUtils.dumpCursor(datos.obtenerUsuarios());
            DatabaseUtils.dumpCursor(datos.obtenerUbicaciones());
            DatabaseUtils.dumpCursor(datos.obtenerContactos());
            DatabaseUtils.dumpCursor(datos.obtenerContactosUbicaciones());
            //DatabaseUtils.dumpCursor(datos.obtenerContactosUbicacionNombre("EDIF_ALFA"));
            DatabaseUtils.dumpCursor(datos.obtenerCoordenadas());
            DatabaseUtils.dumpCursor(datos.obtenerReportes());
            DatabaseUtils.dumpCursor(datos.obtenerMarket());

            DatabaseUtils.dumpCursor(datos.obtenerMarketCoordenadas("Javier Alejandro López Rangel"));


            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getApplicationContext().deleteDatabase("PICUDG.db");
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());

        new TareaPruebaDatos().execute();

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        AutoCompleteTextView completeTextView = (AutoCompleteTextView) findViewById(R.id.ACT_correoLogin);
        completeTextView.setAdapter(getEmailAddressAdapter(this));

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Login.this, "¡Error al iniciar sesión!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        bt_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        final Intent actReg = new Intent(this, Register.class);

        LogBook.setReadPermissions(Arrays.asList("email"));
        LogBook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException error) {

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    startMain();
                }
            }
        };
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilCorreo.clearFocus();
                tilPassword.clearFocus();
                String email = tilCorreo.getEditText().getText().toString().trim();
                String password = tilPassword.getEditText().getText().toString().trim();
                if(esCorreoValido(email)) {
                    prog_bar.setVisibility(View.VISIBLE);
                    //authenticate user
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    prog_bar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(Login.this, "¡Error de autentificación!", Toast.LENGTH_LONG).show();
                                    } else {
                                        startMain();
                                    }
                                }
                            });
                }
            }
        });
        fabReg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                actReg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                actReg.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                actReg.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(actReg);
                finish();
            }
        });
        bt_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogBook.performClick();
            }
        });

        tvResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent actForgot = new Intent(Login.this, ForgotPassword.class);
                actForgot.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                actForgot.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                actForgot.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(actForgot);
                finish();
            }
        });
    }
    private void handleFacebookAccessToken(AccessToken accessToken) {
        prog_bar.setVisibility(View.VISIBLE);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"¡Error al ingresar!",Toast.LENGTH_LONG).show();
                }
                prog_bar.setVisibility(View.GONE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            prog_bar.setVisibility(View.VISIBLE);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
            prog_bar.setVisibility(View.GONE);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Google", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Google2", "signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Google3", "signInWithCredential", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    @Override
    protected void onStop(){
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
    private void startMain(){
        Intent intent = new Intent(this, PicMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Login.this.startActivity(intent);
        finish();
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @SuppressWarnings("MissingPermission")
    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        Account[] accounts = AccountManager.get(context).getAccounts();
        String[] addresses = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            addresses[i] = accounts[i].name;
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, addresses);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    private boolean esCorreoValido(String correo) {
        Pattern patron = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        if (!patron.matcher(correo).matches()) {
            tilCorreo.setError("Correo electrónico inválido");
            return false;
        } else {
            tilCorreo.setError(null);
        }
        return true;
    }
}