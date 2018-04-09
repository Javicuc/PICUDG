package com.picudg.catapp.picudg;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.picudg.catapp.picudg.Modelo.ReporteCardView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReporteFullView extends AppCompatActivity {

    @BindView(R.id.TV_descripcionFullView)
    TextView tvDescFull;
    @BindView(R.id.IV_ReporteFull)
    ImageView ivImgReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_full_view);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(getIntent().hasExtra("Reporte")){
            ReporteCardView reporte =  getIntent().getExtras().getParcelable("Reporte");
            toolbar.setTitle(reporte.getAsuntoCard());
            tvDescFull.setText(reporte.getDescripcionCard());
            Glide.with(this)
                    .load(reporte.getImagenCard())
                    .centerCrop()
                    .into(ivImgReporte);

        }
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_EditFullView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
