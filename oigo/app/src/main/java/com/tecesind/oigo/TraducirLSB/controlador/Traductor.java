package com.tecesind.oigo.TraducirLSB.controlador;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.tecesind.oigo.R;
import com.tecesind.oigo.StaticOigo;
import com.tecesind.oigo.TraducirLSB.modelo.SpeechRecognitionHelper;
import com.tecesind.oigo.actualizarVocabulario.controlador.Vocabulario;
import com.tecesind.oigo.armarOracionLSB.controlador.ControlPaquete;
import com.tecesind.oigo.hablar.modelo.Hablador;
import com.tecesind.oigo.visualizarSenha.modelo.Analizador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rosember on 11/1/2015.
 */
public class Traductor extends Fragment implements View.OnClickListener,
        MediaPlayer.OnCompletionListener {

    private Button btnHablar;
    private ImageView btnSenhas;
    private ToggleButton btnEscuchar;
    private EditText etTexto;
    private VideoView videoView;
    private String expresion;
    private SpeechRecognitionHelper speechHelper;
    private Uri uri;
    private int contador;
    private List<String> urls;
    private Hablador habla;
    private final int REQUEST_CODE = 1;
    private static final String TAG = "Class Traductor";
    private MediaPlayer mediaPlayer;

    public static Traductor newInstance() {
        Bundle args = new Bundle();
        Traductor fragment = new Traductor();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.tab_traductor, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        speechHelper = new SpeechRecognitionHelper();
        btnHablar = (Button) this.getActivity().findViewById(R.id.btnHablar);
        btnSenhas = (ImageView) this.getActivity().findViewById(R.id.btnSenhas);
        btnEscuchar = (ToggleButton) this.getActivity().findViewById(
                R.id.btnEscuchar);
        etTexto = (EditText) this.getActivity().findViewById(R.id.etHablar);
        videoView = (VideoView) this.getActivity().findViewById(
                R.id.vVConversacion);
        contador = 0;
        btnEscuchar.setOnClickListener(this);
        btnHablar.setOnClickListener(this);
        btnSenhas.setOnClickListener(this);
        habla = new Hablador(this.getActivity());
        videoView.setOnCompletionListener(this);
        videoView.setBackgroundResource(R.drawable.snap);


    }

    public void pronunciarTexto(String texto) {
        habla.dime_algo(texto);
    }

    public void escuchar() {
        if (btnEscuchar.isChecked()) {
            inciarReconocimiento(0);
        }

    }

    public void senhas() {
        Intent i = new Intent(getContext(), ControlPaquete.class);
        startActivityForResult(i, 1);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnHablar:
                pronunciarTexto(etTexto.getText().toString());
                break;

            case R.id.btnEscuchar: {
                escuchar();

                break;
            }
            case R.id.btnSenhas: {
                senhas();

                break;
            }
            default:
                break;
        }
    }

    private void inciarReconocimiento(int accion) {
        // TODO Auto-generated method stub
        // Definición del intent para realizar en análisis del mensaje


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Indicamos el modelo de lenguaje para el intent
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Definimos el mensaje que aparecerá
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable porfavor..");

        // Lanzamos la actividad esperando resultados
        startActivityForResult(intent, accion);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Traductor", "Finalizando: " + requestCode);
        switch (requestCode) {
            case 0:
                btnEscuchar.setChecked(false);
                // Si el reconocimiento a sido bueno

                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> matches = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // Separo el texto en palabras.
                    expresion = matches.get(0).toString().trim();

                    Log.e("TraductorActivityResult", expresion.trim());
                    traducir();
                }
                break;

            case 1:
                if (resultCode == getActivity().RESULT_OK) {
                    Log.e("Traductor", "Entro por result ok con: " + data.getDataString());
                    etTexto.setText(data.getDataString());
                }
                break;

            default:
                break;
        }

    }

    private void traducir() {
        // TODO Auto-generated method stub
        Analizador analizador = new Analizador();
        urls = analizador.analizar(expresion);
        realizarSenhas();

    }


    @SuppressLint("NewApi")
    private void realizarSenhas() {
        if (urls != null && urls.size() > 0) {
            File file = new File(urls.get(0));
            if (file.exists()) {

                uri = Uri.parse(urls.get(0));
                videoView.setVideoURI(uri);


                Log.e(TAG, "Realizar Senhas: Por preguntar si esta reproduciendo...");
                if (!videoView.isPlaying()) {
                    videoView.setBackground(null);


                    Log.e(TAG, "Realizar Senhas: listo para inicia.....");
                    videoView.start();
                }
            } else Log.e("Traductor", "No existe el video");
        }

    }


    @SuppressLint("NewApi")
    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
        contador++;

        if (contador < urls.size()) {

            File file = new File(urls.get(contador));
            if (file.exists()) {

                uri = Uri.parse(urls.get(contador));
                videoView.setVideoURI(uri);


                Log.e(TAG, "Realizar Senhas: Por preguntar si esta reproduciendo...");
                if (!videoView.isPlaying()) {
                    videoView.setBackground(null);


                    Log.e(TAG, "Realizar Senhas: listo para inicia.....");
                    videoView.start();
                }
            } else Log.e("Traductor", "No existe el video");

        } else
            contador = 0;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_traductor, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.update) {
            Intent intent = new Intent(this.getActivity(), Vocabulario.class);
            intent.putExtra(StaticOigo.ACTUALIZAR_VOCABULARIO ,false);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        habla.destructor();
        super.onDestroy();
    }
}

