package com.picudg.catapp.picudg;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Path;
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
import com.picudg.catapp.picudg.SQLite.LoadBD;
import com.picudg.catapp.picudg.SQLite.OperacionesBaseDatos;

import org.w3c.dom.Text;

import java.io.File;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //getApplicationContext().deleteDatabase("PICUDG.db");
        if(!doesDatabaseExist(this,"PICUDG.db"))
            new LoadBD(getApplicationContext()).execute();

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
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        Log.d("Pathdb -> ",dbFile.toString());
        return dbFile.exists();
    }
}