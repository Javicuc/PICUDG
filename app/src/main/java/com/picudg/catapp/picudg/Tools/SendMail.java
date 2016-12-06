package com.picudg.catapp.picudg.Tools;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.picudg.catapp.picudg.Tools.Config;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;

/**
 * Created by javilubz on 30/10/16.
 */

public class SendMail extends AsyncTask<Void, Void, Void> {
    /** Utilizamos AsyncTask porque esta clase trabajara con networking operations y la ocupamos en segundo plano **/

    private Context context;
    private Session session;
    //Informacion del correo
    private String email;
    private String subject;
    private String message;
    private String pdfname;


    //Progressdialog para mostrar el estado del envio
    //private ProgressDialog progressDialog;

    //Class Constructor
    public SendMail(Context context, String email, String subject, String pdf) {
        this.context = context;
        this.email   = email;
        this.subject = subject;
        this.pdfname =  pdf;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        //progressDialog = ProgressDialog.show(context, "Enviando reporte", "Espere un momento...", false, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        //progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context, "¡Reporte Enviado con exito!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creando propiedades
        Properties props = new Properties();

        //Configurando propiedades para gmail
        //Si queremos usar otro servicio de correos debemos cambiar estas propiedades
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creamos una nueva session, en este caso simularemos nuestro servicio de correos
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Autentificamos el usuario y contraseña
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });
        try {
            // Creamos un default MimeMessage objecto.
            Message message = new MimeMessage(session);
            // setFrom: Establecemos el correo remitente.
            message.setFrom(new InternetAddress(Config.EMAIL));

            // Set To: Establecemos el destinatario.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            // Set Subject: Asunto del correo.
            message.setSubject(subject);
            // Creamos el cuerpo del correo
            BodyPart messageBodyPart = new MimeBodyPart();
            // Agregamos el mensaje que sera enviado para cada reporte nuevo.
            messageBodyPart.setText("SISTEMA DE REPORTE DE INFRAESTRUCTURA CUCEI-UDG");
            // Creamos un multipart mensaje, con el fin de agregar un archivo adjunto.
            Multipart multipart = new MimeMultipart();
            // Establecemos el cuerpo del correo en el multipart.
            multipart.addBodyPart(messageBodyPart);
            // Agregamos el archivo adjunto
            messageBodyPart = new MimeBodyPart();
            //Indicamos el origen de nuestros archivos para no tener problemas.
            String filename =  "/storage/emulated/0/picudg/files/";
            //Concatenamos el origen y el nombre del archivo.
            filename = filename.concat(pdfname);
            //Creamos un source y lo establecemos con el archivo origen.
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("PICUDG-" + pdfname);
            //Agregamos el archivo al multipart.
            multipart.addBodyPart(messageBodyPart);

            // Juntamos ambas partes del correo en el menssage.
            message.setContent(multipart);

            // Enviamos el correo.
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
