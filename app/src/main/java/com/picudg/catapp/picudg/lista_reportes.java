package com.picudg.catapp.picudg;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.picudg.catapp.picudg.Modelo.Reporte;
import com.picudg.catapp.picudg.Modelo.ReporteCardView;
import com.picudg.catapp.picudg.SQLite.BaseDatosPicudg;
import com.picudg.catapp.picudg.SQLite.InibdPicudg;
import com.picudg.catapp.picudg.SQLite.OperacionesBaseDatos;
import com.picudg.catapp.picudg.Tools.AdaptadorReportes;
import com.picudg.catapp.picudg.Tools.PicudgApp;

import java.util.List;

public class lista_reportes extends AppCompatActivity {

    private OperacionesBaseDatos datos;
    private RecyclerView recycler;
    private AdaptadorReportes adapter;
    private RecyclerView.LayoutManager lmanager;
    private List<ReporteCardView> listaItemsReportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reportes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Reportes");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(lista_reportes.this,PicMain.class);
                startActivity(main);
                lista_reportes.this.finish();
            }
        });
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_Reportes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        inicializarRecicler();
    }

    public void inicializarRecicler(){

        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());

        listaItemsReportes = datos.obtenerListaReportes();
        Log.i("items->",listaItemsReportes.get(0).AsuntoCard);

        recycler = (RecyclerView) findViewById(R.id.lista);
        recycler.setHasFixedSize(true);

        lmanager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lmanager);

        adapter = new AdaptadorReportes(listaItemsReportes,this);

        recycler.setAdapter(adapter);

        recycler.setItemAnimator(new DefaultItemAnimator());

        datos.getDb().close();
    }

}
