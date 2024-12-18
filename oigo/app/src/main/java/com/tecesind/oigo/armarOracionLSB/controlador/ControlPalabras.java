package com.tecesind.oigo.armarOracionLSB.controlador;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecesind.oigo.R;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Rosember on 11/6/2015.
 */
public class ControlPalabras extends Activity implements View.OnClickListener, MediaPlayer.OnCompletionListener{

    private List<Palabra> listaPalabras;
    private Gson gson;
    private ImageButton btnAnterior;
    private ImageButton btnSiguiente;
    private ImageView btnOk;
    private VideoView vvPalabra;
    private int visualizando;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palabra);
        Bundle bundle = getIntent().getExtras();
        String palabras = bundle.getString("palabras");
        Log.e("ControlPalabras",palabras);
        gson = new Gson();
        Type typePalabras = new TypeToken<List<Palabra>>(){}.getType();
        listaPalabras = gson.fromJson(palabras, typePalabras);

        btnAnterior = (ImageButton) findViewById(R.id.btnAnterior);
        btnSiguiente = (ImageButton) findViewById(R.id.btnSiguiente);
        btnOk = (ImageView) findViewById(R.id.btnOKpalabra);
        vvPalabra = (VideoView) findViewById(R.id.vvPalabra);

        btnAnterior.setOnClickListener(this);
        btnSiguiente.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        vvPalabra.setOnCompletionListener(this);
        visualizando = 0;
        vvPalabra.setBackgroundResource(R.drawable.snap);
        //mostrarPalabra();
    }

    @SuppressLint("NewApi")
    private void mostrarPalabra() {

        File file = new File(listaPalabras.get(visualizando).getUrlVideoNormal());
        if (file.isFile()) {
            Log.e("ControlPalabras", "El video existe");


            vvPalabra.setVideoPath(file.getAbsolutePath());
            if (!vvPalabra.isPlaying()) {
                vvPalabra.setBackground(null);
                vvPalabra.start();
            }
        }
        else
            Toast.makeText(this, "Actualice su vocabulario", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case(R.id.btnAnterior):

                if (visualizando!=0) {
                    visualizando--;
                    mostrarPalabra();

                }
                else
                    Toast.makeText(this,"No hay palabras anteriores", Toast.LENGTH_LONG).show();
                break;

            case(R.id.btnSiguiente):
                if (visualizando!=listaPalabras.size()-1) {
                    visualizando++;
                    mostrarPalabra();

                }
                else
                    Toast.makeText(this,"No hay mas palabras", Toast.LENGTH_LONG).show();

                break;
            case(R.id.btnOKpalabra):

                Intent data = new Intent();
                data.setData(Uri.parse(gson.toJson(listaPalabras.get(visualizando))));
                Log.e("ControlPalabra", "devolviendo: "+listaPalabras.get(visualizando).getNombre());
                setResult(RESULT_OK, data);
                finish();
                break;

        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        mostrarPalabra();
    }
}
