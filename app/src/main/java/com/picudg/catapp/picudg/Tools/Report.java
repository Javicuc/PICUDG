package com.picudg.catapp.picudg.Tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
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
import com.itextpdf.text.pdf.PdfWriter;
import com.picudg.catapp.picudg.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by javilubz on 5/11/16.
 */

public class Report extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String  mPathPdf;
    private Image   userImage;
    private byte[]  imageInByte;

    private String  alumno;
    private String  edificio;
    private String  centroEstudio;
    private String  fAsunto;
    private String  fDescripcion;

    //Progressdialog para mostrar el estado del envio
    private ProgressDialog progressDialog;

    public Report(String path, String asunto, String descripcion,byte[] img, Context context) {

        this.context = context;
        this.mPathPdf = path;
        this.fAsunto = asunto;
        this.fDescripcion = descripcion;
        this.imageInByte = img;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context, "Generando reporte", "Espere un momento...", false, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context, "Reporte creado con exito", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        final Document New_Document = new Document();
        Date date = new Date();
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy/MM/dd, hh:mm:ss");

        File New_File = new File(mPathPdf);
        if (New_File.exists()) {
            New_File.delete();
        }
        try {
            PdfWriter writter = PdfWriter.getInstance(New_Document, new FileOutputStream(New_File));
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
            Bitmap bitmapLogo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.udg);
            bitmapLogo.compress(Bitmap.CompressFormat.PNG, 100, streamLogo);
            Image LogoUni = null;
            try {
                LogoUni = Image.getInstance(streamLogo.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            LogoUni.setAlignment(Image.LEFT);
            New_Document.add(LogoUni);

            /** Creamos parrafo y seteamos la font **/
            Paragraph p1 = new Paragraph();
            p1.add(new Phrase("SISTEMA DE REPORTE DE INFRAESTRUCTURA UDG-CUCEI.", fontTitulos));
            p1.add(new Phrase(Chunk.NEWLINE));
            p1.add(new Phrase(Chunk.NEWLINE));
            p1.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            New_Document.add(p1);

            /** Ubicacion del problema y detalles **/
            Paragraph pdet = new Paragraph();
            pdet.add(new Phrase("Centro Uniersitario de Ciencias Exactas e Ingenierias (CUCEI).", fontContenido));
            pdet.add(new Phrase(Chunk.NEWLINE));
            pdet.add(new Phrase("Blvd. Marcelino García Barragán 1421, Ciudad Universitaria, 44430 Guadalajara, JAL.", fontContenido));
            pdet.add(new Phrase(Chunk.NEWLINE));
            pdet.add(new Phrase("Departamento de Ciencias Basicas, Jorge Zamudio Hernandez.", fontContenido));
            pdet.add(new Phrase(Chunk.NEWLINE));
            pdet.add(new Phrase(ft.format(date).toString() + ", DEDX-A015", fontContenido));
            pdet.add(new Phrase(Chunk.NEWLINE));
            pdet.add(new Phrase(Chunk.NEWLINE));
            New_Document.add(pdet);

            /** Agregamos la descripcion del usuario **/
            Paragraph p2 = new Paragraph();
            p2.add(new Phrase(fAsunto + ":", fontTitulos));
            p2.add(new Phrase(Chunk.NEWLINE));
            p2.add(new Phrase(Chunk.NEWLINE));
            p2.add(new Phrase(fDescripcion, fontContenido));
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
            userImage = null;
            try {
                userImage = Image.getInstance(imageInByte);
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /** Cambiamos el tamaño de la imagen, para que se adapte al documento **/
            //float documentWidth = New_Document.getPageSize().getWidth() - New_Document.leftMargin() - New_Document.rightMargin();
            //float documentHeight = New_Document.getPageSize().getHeight() - New_Document.topMargin() - New_Document.bottomMargin();
            //userImage.scaleToFit(documentWidth, documentHeight);
            userImage.setAlignment(Image.MIDDLE);

            /**Agregamos la imagen que capturo el usuario(intent) al documento **/
            New_Document.add(userImage);

            /** Cerramos el documento (Importante) **/
            New_Document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
