package com.picudg.catapp.picudg.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.picudg.catapp.picudg.SQLite.InibdPicudg;

/**
 * Created by Javi-cuc on 18/11/2016.
 */

public class ReporteCardView implements Parcelable {

    public String AsuntoCard;
    public String CentroEstudioCard;
    public String EdificioCard;
    public String DescripcionCard;
    public String ImagenCard;
    public String AutorCard;
    public String FechaCard;

    public ReporteCardView (String Asunto, String Centro, String Edificio, String Descripcion,
                            String Uri, String Autor, String Fecha){

        this.AsuntoCard        = Asunto;
        this.CentroEstudioCard = Centro;
        this.EdificioCard      = Edificio;
        this.DescripcionCard   = Descripcion;
        this.ImagenCard        = Uri;
        this.AutorCard         = Autor;
        this.FechaCard         = Fecha;

    }

    protected ReporteCardView(Parcel in) {
        AsuntoCard = in.readString();
        CentroEstudioCard = in.readString();
        EdificioCard = in.readString();
        DescripcionCard = in.readString();
        ImagenCard = in.readString();
        AutorCard = in.readString();
        FechaCard = in.readString();
    }

    public static final Creator<ReporteCardView> CREATOR = new Creator<ReporteCardView>() {
        @Override
        public ReporteCardView createFromParcel(Parcel in) {
            return new ReporteCardView(in);
        }

        @Override
        public ReporteCardView[] newArray(int size) {
            return new ReporteCardView[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AsuntoCard);
        dest.writeString(CentroEstudioCard);
        dest.writeString(EdificioCard);
        dest.writeString(DescripcionCard);
        dest.writeString(ImagenCard);
        dest.writeString(AutorCard);
        dest.writeString(FechaCard);
    }

    public String getAsuntoCard() {
        return AsuntoCard;
    }

    public String getCentroEstudioCard() {
        return CentroEstudioCard;
    }

    public String getEdificioCard() {
        return EdificioCard;
    }

    public String getDescripcionCard() {
        return DescripcionCard;
    }

    public String getImagenCard() {
        return ImagenCard;
    }

    public String getAutorCard() {
        return AutorCard;
    }

    public String getFechaCard() {
        return FechaCard;
    }
}
