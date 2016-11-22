package com.picudg.catapp.picudg.Tools;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

/**
 * Created by javilubz on 19/11/16.
 */

public class IfConect {
    private Context contexto;

    public IfConect(Context context){
        this.contexto = context;
    }
    public boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                contexto.getSystemService(contexto.CONNECTIVITY_SERVICE);
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
