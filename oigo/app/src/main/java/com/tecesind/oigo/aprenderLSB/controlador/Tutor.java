package com.tecesind.oigo.aprenderLSB.controlador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.tecesind.oigo.R;
import com.tecesind.oigo.aprenderLSB.modelo.NegocioTutor;
import com.tecesind.oigo.evaluarLSB.controlador.EvaluarLSB;
import com.tecesind.oigo.hablar.modelo.Hablador;

import java.util.List;

/**
 * Created by Rosember on 11/1/2015.
 */
public class Tutor  extends Fragment implements View.OnClickListener {

    private static Tutor tutorFragment;
    private Hablador habla;
    private VideoView videoView;
    private VideoView videoLentoview;
    private NegocioTutor tutor;
    private Button btnEvaluar;
    private String nombre;
    private Button btnLento;
    private TextView tvTitulo;
    private List<Palabra> lista;
    private Uri uri;
    private boolean isLentoVisible;
    public int getModulo() {
        return modulo;
    }
    public int actual;
    public void setModulo(int modulo) {
        this.modulo = modulo;
    }

    private int modulo;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Tutor", "OnCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Tutor", "OnCreateView");
        return inflater.inflate(R.layout.tab_tutor, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Tutor", "OnActivityCreate");
        modulo = 1;
        nombre = "Alfabeto";
        actual=0;
        habla = new Hablador(this.getActivity());
        tvTitulo = (TextView) this.getActivity().findViewById(R.id.tvTituloModulo);
        videoView = (VideoView) this.getActivity().findViewById(R.id.vAlfabeto);
        videoLentoview = (VideoView) this.getActivity().findViewById(R.id.vLento);
        tutor = new NegocioTutor();
        btnEvaluar = (Button) this.getActivity().findViewById(R.id.btnEvaluar);
        btnEvaluar.setOnClickListener(this);
        btnLento = (Button) this.getActivity().findViewById(R.id.btnLento);
        btnLento.setOnClickListener(this);

        isLentoVisible = false;
        tutorFragment = this;
        cargarModulo();
        videoLentoview.setBackgroundResource(R.drawable.snap);
        videoView.setBackgroundResource(R.drawable.snap);
        videoView.bringToFront();
        videoView.requestFocus();
    }

    public static Tutor getInstance(){
        return tutorFragment;
    }

    public static Tutor newInstance(int modulo, String nombre) {
       // Bundle args = new Bundle();

        Tutor fragment = new Tutor();
        fragment.setNombre(nombre);
        fragment.setModulo(modulo);

        return fragment;
    }



    @SuppressLint("NewApi")
    private void revealVideoLento(int id){

        Log.e("Tutor", "Revelar Lento" + isLentoVisible);
        if(!isLentoVisible) {

            isLentoVisible = true;
            videoView.animate().translationXBy(-1000).setDuration(1000).start();
            reproducirLento(id);
        }else{

            reproducirLento(id);
        }
    }

    @SuppressLint("NewApi")
    private void reproducirLento(int id) {

        Log.e("TutorLento", lista.get(id).getUrlVideoEnsenhanza() + " y " + lista.get(id).getImgRepresentacion());
        if (lista.get(id).getUrlVideoEnsenhanza()!=null) {

            uri = Uri.parse(lista.get(id).getUrlVideoEnsenhanza());
            videoLentoview.setVideoURI(uri); // Associates path with videoView
            if (!videoLentoview.isPlaying()) {
                videoLentoview.setBackground(null);
                videoLentoview.requestFocus();
                videoLentoview.start();
            }

        }else {
            if (lista.get(id).getImgRepresentacion()!=null&&!"".equals(lista.get(id).getImgRepresentacion())){

                if (!videoLentoview.isPlaying()) {
                    videoLentoview.setBackground(Drawable.createFromPath(lista.get(id).getImgRepresentacion()));
                    videoLentoview.requestFocus();

                }
            }else{
                uri = Uri.parse(lista.get(id).getUrlVideoNormal()); // Gets
                videoLentoview.setVideoURI(uri); // Associates path with videoView
                if (!videoLentoview.isPlaying()) {
                    videoLentoview.setBackground(null);
                    videoLentoview.requestFocus();
                    videoLentoview.start();
                }


            }
        }// Gets

    }

    @SuppressLint("NewApi")
    private void hideVideoLento(int id){

        if(isLentoVisible) {

            isLentoVisible = false;
            videoView.animate().translationXBy(1000).setDuration(1000).start();

        }else
            revealVideoLento(id);
    }


    public void cargarModulo() {
        // TODO Auto-generated method stub
        LinearLayout ll = (LinearLayout) this.getActivity().findViewById(
                R.id.imAlfabeto);
        tvTitulo.setText(nombre);
        ll.removeAllViews();
        lista = tutor.getLista(modulo);
        videoView.setBackgroundResource(R.drawable.snap);
        if (lista==null)
            Log.e("Aprendizaje.class", "La lista esta nula");
        else{
            Log.e("Aprendizaje.class", "lista tiene" + lista.size());
            for (int i = 0; i < lista.size(); i++) {
                TextView tv = new TextView(this.getActivity());
                tv.setWidth(dpToPx(180));
                tv.setHeight(dpToPx(80));
                tv.setId(i);
                tv.setBackgroundResource(R.color.primary_light);
                tv.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.primary_text));
                LinearLayout.LayoutParams llp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                llp.setMargins(0,0,5,3);
                tv.setLayoutParams(llp);
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setText(lista.get(i).getNombre());

                ll.addView(tv);

                tv.setOnClickListener(this);

            }
        }


    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getView().getResources()
                .getDisplayMetrics();
        int px = Math.round(dp
                * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }



    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v.getId()==R.id.btnLento) {

            if(!isLentoVisible)
            revealVideoLento(actual);
            else
                hideVideoLento(actual);
        }else {
            if (v.getId() == R.id.btnEvaluar) {

                iniciarEvaluacion();

            } else {
                if (v.getId() < lista.size()) {
                    actual=v.getId();
                    representacion(v.getId());
                }
            }
        }
    }

    private void representacion(int id) {
        pronunciarPalabra(lista.get(id).getNombre());
        mostrarSenha(id);
    }

    @SuppressLint("NewApi")
    private void mostrarSenha(int id) {


        if(!isLentoVisible) {
            uri = Uri.parse(lista.get(id).getUrlVideoNormal()); // Gets
            videoView.setVideoURI(uri); // Associates path with videoView
            if (!videoView.isPlaying()) {
                videoView.setBackground(null);
                videoView.requestFocus();
                videoView.start();
            }
        }else hideVideoLento(id);
    }

    private void pronunciarPalabra(String nombre) {
        habla.dime_algo(nombre);
    }


    public void iniciarEvaluacion(){

        Intent i= new Intent(this.getActivity(), EvaluarLSB.class);
        startActivity(i);
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        habla.destructor();
        super.onDestroy();
    }
}
