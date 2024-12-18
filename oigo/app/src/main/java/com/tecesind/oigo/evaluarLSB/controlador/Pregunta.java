package com.tecesind.oigo.evaluarLSB.controlador;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.tecesind.oigo.R;
import com.tecesind.oigo.actualizarVocabulario.modelo.NegocioPalabra;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rosember on 11/17/2015.
 */
public class Pregunta extends AppCompatActivity implements OnClickListener, MediaPlayer.OnCompletionListener {

    private int cantPreguntas;
    private int preguntasFormuladas;
    private int nota;
    private VideoView vvPreguntas;
    private TextView tvPreguntas;
    private TextView tvPuntos;
    private LinearLayout llRespuestas;
    private List<Palabra> palabras;
    private Palabra palabra;
    private Button btnAceptar;
    private Button btnOtraVez;
    private Button btnSalir;
    private final RadioButton[] rb = new RadioButton[2];
    private RadioGroup rg;
    private final RadioButton[] rb2 = new RadioButton[3];
    private RadioGroup rg2;
    private int correcta;
    EditText etRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        Bundle bundle = getIntent().getExtras();
        boolean[] modulos = bundle.getBooleanArray("modulos");
        rg = new RadioGroup(this);
        rg2 = new RadioGroup(this);
        preguntasFormuladas =0;
        cantPreguntas = 6;
        nota=0;
        correcta = 0;

        vvPreguntas = (VideoView) findViewById(R.id.vVPreguntas);
        tvPreguntas = (TextView) findViewById(R.id.tvPregunta);
        tvPuntos = (TextView) findViewById(R.id.tvPuntos);
        llRespuestas = (LinearLayout) findViewById(R.id.llOpcionesRespuesta);
        btnAceptar = (Button) findViewById(R.id.btnResponder);
        btnOtraVez = (Button) findViewById(R.id.btnOtraVez);
        btnSalir = (Button) findViewById(R.id.btnSalir);
        etRespuesta = new EditText(this);
        btnOtraVez.setOnClickListener(this);
        btnAceptar.setOnClickListener(this);
        btnSalir.setOnClickListener(this);

        palabras = listaPalabras(modulos);
        vvPreguntas.setBackgroundResource(R.drawable.snap);
        preguntar();
    }

    private List<Palabra> listaPalabras(boolean[] modulos) {
        // TODO Auto-generated method stub

        NegocioPalabra nPalabra = new NegocioPalabra();
        List<Palabra> lista = new LinkedList<Palabra>();
        for (int i=0; i<modulos.length; i++){
            if (modulos[i])
                lista.addAll(nPalabra.getAllModulo(i+1));
        }

        return lista;
    }
    private void preguntar() {
        // TODO Auto-generated method stub
        if (preguntasFormuladas<cantPreguntas){
            if (preguntasFormuladas<2)
                preguntar(1);
            else{
                if (preguntasFormuladas<4)
                    preguntar(2);
                else
                    preguntar(3);
            }
        }else{
            salir();
        }
    }

    private void preguntar(int i) {
        // TODO Auto-generated method stub
        llRespuestas.removeAllViews();
        palabra = palabras.get((int)((Math.random()*palabras.size()) -1));
        Log.e("Pregunta",palabra.getNombre());
        if (i==1){ //Falso o verdadero


            int verdad = (int)(Math.random()*2);
            if (verdad ==1){
                tvPreguntas.setText("Esta palabra es: "+ palabra.getNombre());
                correcta =1;
            }else{
                correcta = 2;
                Palabra p2= new Palabra();
                p2.setId(palabra.getId());
                while(p2.getId()==palabra.getId()){
                    p2 = palabras.get((int)((Math.random()*palabras.size())-1));
                }
                tvPreguntas.setText("Esta palabra es: "+ p2.getNombre());
            }


            //create the RadioGroup
            rg.removeAllViews();
            rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL

            rb[0]  = new RadioButton(this);
            rg.addView(rb[0]); //the RadioButtons are added to the radioGroup instead of the layout
            rb[0].setText("Verdadero");

            rb[1]  = new RadioButton(this);
            rg.addView(rb[1]); //the RadioButtons are added to the radioGroup instead of the layout
            rb[1].setText("Falso");

            llRespuestas.addView(rg);//you add the whole RadioGroup to the layout


            tvPuntos.setText("POR 10 PUNTOS");
            realizarSenhas();
        }
        else {
            if (i==2){ // Opciones

                int posicion = (int)(Math.random()*3);

                tvPreguntas.setText("Que palabra es esta?");

                correcta = posicion;
                rg2.removeAllViews();
                rg2.setOrientation(RadioGroup.VERTICAL);
                for (int j=0; j<3; j++){

                    rb2[j] = new RadioButton(this);
                    rg2.addView(rb2[j]);
                    if (j== posicion-1){
                        rb2[j].setText(palabra.getNombre());
                    }else{
                        Palabra p2= new Palabra();
                        p2.setId(palabra.getId());
                        while(p2.getId()==palabra.getId()){
                            p2 = palabras.get((int)((Math.random()*palabras.size())-1));
                        }
                        rb2[j].setText(p2.getNombre());
                    }
                }

                llRespuestas.addView(rg2);

                tvPuntos.setText("POR 15 PUNTOS");

                realizarSenhas();
            }else{
                if (i==3){ //Completar
                    if(preguntasFormuladas==5)
                        llRespuestas.removeView(etRespuesta);

                    tvPreguntas.setText("Que palabra es esta?");

                    realizarSenhas();

                    etRespuesta.setText("");
                    etRespuesta.setHint("Escribe aqui la palabra");

                    llRespuestas.addView(etRespuesta);

                    tvPuntos.setText("POR 25 PUNTOS");

                    realizarSenhas();
                }
            }
        }
        preguntasFormuladas++;
    }

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.btnOtraVez:
                realizarSenhas();
                break;
            case R.id.btnResponder:
                responder();

                break;

            case R.id.btnSalir:
                salir();
                break;

            default:
                break;
        }

        // TODO Auto-generated method stub

    }

    private void responder() {

        if(preguntasFormuladas>0){
        int respuesta;
            if (preguntasFormuladas<3){

                respuesta = rg.getCheckedRadioButtonId();
                if (respuesta == rb[correcta-1].getId())
                    nota +=10;
            }else{

                if(preguntasFormuladas<5){

                    respuesta = rg2.getCheckedRadioButtonId();
                    if (respuesta == rb2[correcta-1].getId())
                        nota +=15;

                }else{

                    if(etRespuesta.getText().toString().toUpperCase().equals(palabra.getNombre()))
                        nota+=25;


                }
            }

        }
        preguntar();
    }

    private void salir() {

        Intent intent = new Intent(this,Logros.class);
        intent.putExtra("nota",nota);
        startActivity(intent);
        finish();
    }


    @SuppressLint("NewApi")
    private void realizarSenhas() {
        Uri uri;
        String vacio ="";
        if (palabra != null) {
            if (palabra.getUrlVideoEnsenhanza()!=null&& !vacio.equals(palabra.getUrlVideoEnsenhanza())) {
                uri = Uri.parse(palabra.getUrlVideoEnsenhanza());
                vvPreguntas.setVideoURI(uri);
                if(!vvPreguntas.isPlaying()){
                    vvPreguntas.setBackground(null);
                    vvPreguntas.requestFocus();
                    vvPreguntas.start();
                }
            }else {
                if (palabra.getImgRepresentacion()!=null && !vacio.equals(palabra.getImgRepresentacion())){
                    if (!vvPreguntas.isPlaying()) {
                        vvPreguntas.setBackground(Drawable.createFromPath(palabra.getImgRepresentacion()));
                        vvPreguntas.requestFocus();
                        //videoLentoview.start();
                    }
                }else{
                    uri = Uri.parse(palabra.getUrlVideoNormal()); // Gets
                    vvPreguntas.setVideoURI(uri); // Associates path with videoView
                    if (!vvPreguntas.isPlaying()) {
                        vvPreguntas.setBackground(null);
                        vvPreguntas.requestFocus();
                        vvPreguntas.start();
                    }


                }
            }



        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("Si sale ahora finaliza la evaluacion");
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                salir();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
     vvPreguntas.stopPlayback();
    }
}
