package com.picudg.catapp.picudg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity{

    /*
    CheckBox remember;
    TextInputLayout tilPassword;
    TextInputLayout tilCodigo;
    */
    @BindView(R.id.signin)
    TextView signin;

    @BindView(R.id.password)
    TextInputLayout tilPassword;

    @BindView(R.id.codigo)
    TextInputLayout tilCodigo;

    @BindView(R.id.fabreg)
    FloatingActionButton fabReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        //remember = (CheckBox)findViewById(R.id.remember);


        final Intent intent = new Intent(this, Camara.class);
        final Intent actReg = new Intent(this, Register.class);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilCodigo.clearFocus();
                tilPassword.clearFocus();
                //if(validarDatos()) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Login.this.startActivity(intent);
                    finish();
                //}
            }
        });
        fabReg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(actReg);
            }
        });
    }

    private boolean esCodigoValido(String codigo) {
        Pattern patron = Pattern.compile("^[0-9]*");
        if (!patron.matcher(codigo).matches() || (codigo.length() != 9)) {
            tilCodigo.setError("Codigo inválido");
            return false;
        } else {
            tilCodigo.setError(null);
        }
        return  true;
    }
    private boolean esPasswordValido(String pass) {
        if (pass.equals("qwer")) {
            return true;
        }else {
            return false;
        }

    }

    private boolean validarDatos() {

        String cod = tilCodigo.getEditText().getText().toString();
        String pass   = tilPassword.getEditText().getText().toString();
        boolean passvalido = esPasswordValido(pass);
        boolean codvalido  = esCodigoValido(cod);
        if(codvalido && passvalido){
            Toast.makeText(this, "Acceso Valido", Toast.LENGTH_LONG).show();
            return true;
        }else {
            Toast.makeText(this, "Acceso Denegado", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}

