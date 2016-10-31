package com.picudg.catapp.picudg;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.Normalizer;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Camara extends AppCompatActivity {


    @BindView(R.id.content_camara) RelativeLayout RL_cam;
    @BindView(R.id.IV_Cam_img) ImageView IV_photo;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.BT_cam) ImageButton BT_photo;
    @BindView(R.id.BT_1) ImageButton BT_boton1;
    @BindView(R.id.BT_2) ImageButton BT_boton2;
    @BindView(R.id.BT_3) ImageButton BT_boton3;

    private static String APP_DIRECTORY = "picudg/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Images";

    private final int MY_PERMISISONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private String mPath;
    private Uri pathuri;
    boolean takepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = new Intent(Camara.this, FormEmail.class);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Explode explode = new Explode();
            explode.setDuration(2000);
            getWindow().setEnterTransition(explode);

            getWindow().setReturnTransition(explode);
        }

        if(myRequestStoragePermission()) {
            BT_photo.setEnabled(true);
        }
        else {
            BT_photo.setEnabled(false);
        }

        BT_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showOptions();
            }
        });

        BT_boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = IV_photo.getDrawable();
                boolean hasImage = (drawable != null);
                if(hasImage && (drawable instanceof BitmapDrawable)) {
                    if (takepic)
                        intent.putExtra("imagePath", mPath.toString());
                    else if (!takepic)
                        intent.putExtra("imagePath", pathuri.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        Slide slid = new Slide();
                        slid.setDuration(2000);
                        //getString(R.string.transicionIMG)
                        startActivity(intent,
                                ActivityOptionsCompat.makeSceneTransitionAnimation(Camara.this,v,"").toBundle());
                    }else {
                        startActivity(intent);
                    }*/
                    startActivity(intent);
                    Camara.this.finish();
                }
                Snackbar snackbar = Snackbar
                        .make(RL_cam, "Captura una imagen", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IV_photo.setImageResource(0);
                IV_photo.setVisibility(View.GONE);
            }
        });
    }

    private void showOptions(){
        final CharSequence[] option = {"Elegir Foto","Tomar Foto","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una opción:");
        builder.setIcon(R.drawable.ic_add_to_photos_black_24dp);
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which] == "Elegir Foto") {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/.*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona una imagen"), SELECT_PICTURE);
                } else if (option[which] == "Tomar Foto") {
                    opcCamera();
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void opcCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreate = file.exists();

        if(!isDirectoryCreate) {
            isDirectoryCreate = file.mkdirs();
        }

        if(isDirectoryCreate){
            Long TimeStamp = System.currentTimeMillis() / 1000;
            String Img_name = TimeStamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + Img_name;

            File New_File = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(New_File));
            startActivityForResult(intent, PHOTO_CODE);
        }

    }

    private boolean myRequestStoragePermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(RL_cam, "Permisos necesarios para la aplicación", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISISONS);
                        }
                    }).show();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISISONS);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this, new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener(){
                                @Override
                                public void onScanCompleted(String path, Uri uri){
                                    Log.i("ExternalStorage", "Scanner" + path + ":");
                                    Log.i("ExternalStorage", "->Uri" + uri);
                                }
                            });
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    IV_photo.setVisibility(View.VISIBLE);
                    IV_photo.setImageBitmap(bitmap);
                    takepic = true;
                    break;
                case SELECT_PICTURE:
                    IV_photo.setVisibility(View.VISIBLE);
                    pathuri = data.getData();
                    IV_photo.setImageURI(pathuri);
                    takepic = false;
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISISONS){
            if(grantResults.length == 2 && grantResults[0] == getPackageManager().PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permisos Aceptados", Toast.LENGTH_SHORT).show();
                BT_photo.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("file_path",mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPath = savedInstanceState.getString("file_path");
    }

    private void showExplanation() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permisos Denegados");
        builder.setMessage("Para usar esta aplicación, necesitas aceptar los permisos solicitados");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }
}