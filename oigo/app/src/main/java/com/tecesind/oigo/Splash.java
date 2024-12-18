package com.tecesind.oigo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tecesind.oigo.actualizarVocabulario.controlador.Vocabulario;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dato.DaoMaster;
import dato.DaoSession;

public class Splash extends AppCompatActivity {
    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
       // requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        StaticOigo.PATH = getFilesDir().getAbsolutePath();
        Log.e("Splah","StaticOigo.PATH : "+StaticOigo.PATH);
        File dir = new File(StaticOigo.PATH+"/videosNormales/");
        dir.mkdirs();
        dir = new File(StaticOigo.PATH+"/imagenes/");
        dir.mkdirs();
        dir = new File(StaticOigo.PATH+"/videosEnsenhanza/");
        dir.mkdirs();


        //Instanciar la Base de Datos
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "dboigo", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster= new DaoMaster(db);
        daoMaster.newSession();


        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                DaoSession daoSession = DaoMaster.getSession();
                List<Palabra> listaPalabra;
                PalabraDao palabraDao = daoSession.getPalabraDao();
                listaPalabra = palabraDao.loadAll();
                Intent mainIntent;

                if (listaPalabra.size()>0)
                    // Start the next activity

                    mainIntent = new Intent().setClass(
                            Splash.this, ManejadorTabs.class);

                else


                    mainIntent = new Intent().setClass(
                            Splash.this, Vocabulario.class);
                    mainIntent.putExtra(StaticOigo.ACTUALIZAR_VOCABULARIO,true);
                startActivity(mainIntent);


            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();


        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        finish();
    }



}
