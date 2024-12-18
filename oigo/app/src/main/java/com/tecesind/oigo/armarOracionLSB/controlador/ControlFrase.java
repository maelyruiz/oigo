package com.tecesind.oigo.armarOracionLSB.controlador;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecesind.oigo.R;
import com.tecesind.oigo.actualizarVocabulario.modelo.NegocioPalabra;
import com.tecesind.oigo.armarOracionLSB.modelo.NegocioFrase;
import com.tecesind.oigo.armarOracionLSB.modelo.NegocioGrupo;

import dato.Notificacion;
import dato.Grupo;

public class ControlFrase extends AppCompatActivity implements OnClickListener {

    private LinearLayout llPrincipal;
    private List<List<Palabra>> listaPalabraGlobal;
    private int contador;
    private List<Notificacion> listaNotificacion;
    private List<TextView> textos;
    private List<String[]> fraseSeleccionadas;
    private Gson gson;
    private List<ImageView> imagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frase);
        Bundle bundle = getIntent().getExtras();
        int paquete = bundle.getInt("paquete");
        gson = new Gson();
        llPrincipal = (LinearLayout) findViewById(R.id.llFrases);
        contador = 0;
        imagenes = new LinkedList<>();

        fraseSeleccionadas = new LinkedList<>();
        PintarLayout pintarLayout = new PintarLayout();
        pintarLayout.execute(paquete);

    }

    private class PintarLayout extends AsyncTask<Integer,Void,Integer>{


        @Override
        protected Integer doInBackground(Integer... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(Integer paquete) {
            super.onPostExecute(paquete);

            cargar(paquete);
        }
    }

    private void cargar(int paquete) {
        listaPalabraGlobal = new LinkedList<List<Palabra>>();
        NegocioFrase nFrase = new NegocioFrase();
        NegocioGrupo nGrupo = new NegocioGrupo();
        NegocioPalabra nPalabra = new NegocioPalabra();
        listaNotificacion = nFrase.getFrase(paquete);
        textos = new LinkedList<>();
        Log.e("ControlFrase", listaNotificacion.size() + " frases");
        for (int i = 0; i < listaNotificacion.size(); i++) {
            HorizontalScrollView HScrollView = new HorizontalScrollView(this);
            String[] texto=null;
            LinearLayout llayout = new LinearLayout(this);
//            ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT/3 );
//            llayout.setLayoutParams(params);
            llayout.setOrientation(LinearLayout.HORIZONTAL);
            llayout.setMinimumHeight(dpToPx(300));
            llayout.setMinimumWidth(dpToPx(400));
            List<Grupo> listaGrupo = nGrupo.getGrupo(listaNotificacion.get(i).getId());
            for (int j = 0; j < listaGrupo.size(); j++) {
                if (texto==null){
                    texto = new String[listaGrupo.size()];
                }

                ImageView imagen = new ImageView(this);
                List<Palabra> listaPalabras = nPalabra.getPalabras(listaGrupo
                        .get(j));
                listaPalabraGlobal.add(listaPalabras);
                imagen.setId(contador);
                imagenes.add(imagen);
                cambiarImagen(contador, listaPalabras.get(0));


                texto[j] = listaPalabras.get(0).getNombre();

                imagen.setOnClickListener(this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        dpToPx(250), dpToPx(250));

                layoutParams.setMargins(5, 5, 0, 5);
                llayout.addView(imagen,layoutParams);
                contador++;

            }

            fraseSeleccionadas.add(texto);

            LinearLayout llayout2 = new LinearLayout(this);
            llayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv = new TextView(this);
            tv.setText(textoFrom(texto));
            llayout2.addView(tv);
            textos.add(tv);

            ImageButton btn = new ImageButton(this);
            btn.setId(50+i);
            btn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_ok));
            btn.setOnClickListener(this);
            llayout2.addView(btn);

            HScrollView.addView(llayout);
            llPrincipal.addView(HScrollView);
            llPrincipal.addView(llayout2);

        }
        // TODO Auto-generated method stub

    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp
                * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId()>=50) {
            //ES Boton
            Intent data = new Intent();
            data.setData(Uri.parse(textoFrom(fraseSeleccionadas.get(v.getId() - 50))));
            Log.e("ControlFrase", "devolviendo: "+textoFrom(fraseSeleccionadas.get(v.getId() - 50))+ " de " + (v.getId()-50));
            setResult(RESULT_OK, data);
            finish();
        }else{
            //ES Imagen
            Intent i = new Intent(this, ControlPalabras.class);
            i.putExtra("palabras", gson.toJson(listaPalabraGlobal.get(v.getId())));
            startActivityForResult(i,v.getId());

        }
    }

    private String textoFrom(String[] strings) {
        String texto = "";
        for (int i=0; i< strings.length; i++){

            texto = texto + strings[i]+" ";
        }
        return texto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){


            Type tipoPalabra = new TypeToken<Palabra>(){}.getType();
            Palabra p = gson.fromJson(data.getDataString(),tipoPalabra);
            cambiarTexto(requestCode, p.getNombre());
            cambiarImagen(requestCode, p);
        }
    }

    private void cambiarImagen(int idImagen, Palabra p) {


        if (p.getImgRepresentacion() == null)
            imagenes.get(idImagen).setImageDrawable(getResources().getDrawable(
                    R.mipmap.snap));
        else {
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory
                        .decodeStream((InputStream) new URL(
                                p.getImgRepresentacion())
                                .getContent());
                imagenes.get(idImagen).setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                imagenes.get(idImagen).setImageDrawable(getResources().getDrawable(
                        R.mipmap.snap));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                imagenes.get(idImagen).setImageDrawable(getResources().getDrawable(
                        R.mipmap.snap));
            }

        }
    }

    private void cambiarTexto(int requestCode, String dataString) {
        int contar =0;

        for (int i=0; i<fraseSeleccionadas.size(); i++){

            contar += fraseSeleccionadas.get(i).length;
            if (contar > requestCode){
                contar -= fraseSeleccionadas.get(i).length;
                fraseSeleccionadas.get(i)[requestCode-contar]=dataString;
                textos.get(i).setText(textoFrom(fraseSeleccionadas.get(i)));
                return;
            }

        }
    }
}