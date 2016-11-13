package com.picudg.catapp.picudg;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPassword extends AppCompatActivity {

    @BindView(R.id.TV_reset)
    TextView tvReset;
    @BindView(R.id.TV_backLoginReset)
    TextView tvBack;
    @BindView(R.id.ProgBar_Forgot)
    ProgressBar prog_bar;
    @BindView(R.id.TIL_correoForgot)
    TextInputLayout tilCorreo;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        AutoCompleteTextView completeTextView = (AutoCompleteTextView) findViewById(R.id.ACT_correoForgot);
        completeTextView.setAdapter(getEmailAddressAdapter(this));

        auth = FirebaseAuth.getInstance();
        final Intent bLogin = new Intent(this, Login.class);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                bLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                bLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(bLogin);
                finish();
            }
        });

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tilCorreo.getEditText().getText().toString().trim();

                if (esCorreoValido(email)==false) {
                    tilCorreo.setError("Correo electrónico inválido");
                    return;
                }
                prog_bar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "Verifica las intrucciones en tu correo!", Toast.LENGTH_SHORT).show();
                                    Intent actLogin = new Intent(ForgotPassword.this,Login.class);
                                    startActivity(actLogin);
                                    finish();
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Error al enviar correo!", Toast.LENGTH_SHORT).show();
                                }
                                prog_bar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        Account[] accounts = AccountManager.get(context).getAccounts();
        String[] addresses = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            addresses[i] = accounts[i].name;
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, addresses);
    }
    private boolean esCorreoValido(String correo) {
        Pattern patron = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        if (!patron.matcher(correo).matches()) {
            return false;
        } else {
            tilCorreo.setError(null);
        }
        return true;
    }
}
