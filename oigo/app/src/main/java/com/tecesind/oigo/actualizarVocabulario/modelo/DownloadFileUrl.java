package com.tecesind.oigo.actualizarVocabulario.modelo;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecesind.oigo.ManejadorTabs;
import com.tecesind.oigo.StaticOigo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

public class DownloadFileUrl extends AsyncTask<String, String, String> {

    private boolean cancelarSiHayMas100Archivos;
    private ProgressBar miBarraDeProgreso;
    private final String TAG_LOG = "DownloadFileUrl";
    private Gson gson;
    private Context mContext;
    private DetectorConexion detectorConexion;

    public DownloadFileUrl(boolean CancelarSiHayMas100Archivos, Context context) {
        // TODO Auto-generated constructor stub
        this.cancelarSiHayMas100Archivos = cancelarSiHayMas100Archivos;
        gson = new Gson();
        Log.i("DownloadConstructor", "Entro al constructor...");
        mContext = context;
        detectorConexion = new DetectorConexion(mContext);
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
//		super.onPreExecute();
//		showDialog(progress_bar_type);
//		miBarraDeProgreso = (ProgressBar) findViewById(R.id.progressBar_indicador);
    }

    @Override
    protected String doInBackground(String... f_url) {
        // TODO Auto-generated method stub

        String vacio = "";
        Type typePalabras = new TypeToken<List<Palabra>>() {
        }.getType();


        List<Palabra> palabras = new LinkedList<Palabra>();
        palabras = gson.fromJson(f_url[0], typePalabras);

        //Log.i("DownloadFile", ""+palabras.get(1).getUrlVideoNormal());

        int cantidadImagenesDescargadas = -1;
        float progreso = 0.0f;

        while (cantidadImagenesDescargadas < palabras.size() - 1 && detectorConexion.hayConexionInternet()) {

            cantidadImagenesDescargadas++;

            Log.i(TAG_LOG, "Video descarga " + palabras.get(cantidadImagenesDescargadas).getNombre() + " VideoEns " + palabras.get(cantidadImagenesDescargadas).getUrlVideoEnsenhanza() + " Img " + palabras.get(cantidadImagenesDescargadas).getImgRepresentacion());

            int count;

            if (!(palabras.get(cantidadImagenesDescargadas) == null)) {
                if (!(palabras.get(cantidadImagenesDescargadas).getUrlVideoNormal() == null) || !vacio.equals(palabras.get(cantidadImagenesDescargadas).getUrlVideoNormal())) {


                    try {
                        URL url = new URL(palabras.get(cantidadImagenesDescargadas).getUrlVideoNormal());


                        URLConnection conection = url.openConnection();
                        conection.connect();
                        // this will be useful so that you can show a tipical 0-100% progress bar
                        int lenghtOfFile = conection.getContentLength();

                        // download the file
                        InputStream input = new BufferedInputStream(url.openStream(), 8192);


                        // Output stream
                        OutputStream output = new FileOutputStream(StaticOigo.PATH + "/videosNormales/vn" + palabras.get(cantidadImagenesDescargadas).getId() + ".mp4");

                        byte data[] = new byte[1024];

                        long total = 0;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            publishProgress("" + (int) ((cantidadImagenesDescargadas * 100) / palabras.size()));

                            // writing data to file
                            output.write(data, 0, count);
                        }

                        // flushing output
                        output.flush();

                        // closing streams
                        output.close();
                        input.close();

                    } catch (Exception e) {
                        Log.e("Error: ", "oye  " + e.getMessage());
                    }
                }


                if (!(palabras.get(cantidadImagenesDescargadas).getUrlVideoEnsenhanza() == null) || !(vacio.equals(palabras.get(cantidadImagenesDescargadas).getUrlVideoEnsenhanza()))) {

                    try {

                        URL url = new URL(palabras.get(cantidadImagenesDescargadas).getUrlVideoEnsenhanza());
                        URLConnection conection = url.openConnection();
                        conection.connect();
                        // this will be useful so that you can show a tipical 0-100% progress bar
                        int lenghtOfFile = conection.getContentLength();

                        // download the file
                        InputStream input = new BufferedInputStream(url.openStream(), 8192);

                        // Output stream
                        OutputStream output = new FileOutputStream(StaticOigo.PATH + "/videosEnsenhanza/ve" + palabras.get(cantidadImagenesDescargadas).getId() + ".mp4");

                        byte data[] = new byte[1024];

                        long total = 0;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                            // writing data to file
                            output.write(data, 0, count);
                        }

                        // flushing output
                        output.flush();

                        // closing streams
                        output.close();
                        input.close();

                    } catch (Exception e) {
                        Log.e("Error: ", "oye " + e.getMessage());
                    }
                }


                if (!(palabras.get(cantidadImagenesDescargadas).getImgRepresentacion() == null) || !(vacio.equals(palabras.get(cantidadImagenesDescargadas).getImgRepresentacion()))) {

                    try {
                        URL url = new URL(palabras.get(cantidadImagenesDescargadas).getImgRepresentacion());
                        URLConnection conection = url.openConnection();
                        conection.connect();
                        // this will be useful so that you can show a tipical 0-100% progress bar
                        int lenghtOfFile = conection.getContentLength();

                        // download the file
                        InputStream input = new BufferedInputStream(url.openStream(), 8192);

                        // Output stream
                        OutputStream output = new FileOutputStream(StaticOigo.PATH + "/imagenes/im" + palabras.get(cantidadImagenesDescargadas).getId() + ".png");

                        byte data[] = new byte[1024];

                        long total = 0;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                            // writing data to file
                            output.write(data, 0, count);
                        }

                        // flushing output
                        output.flush();

                        // closing streams
                        output.close();
                        input.close();

                    } catch (Exception e) {
                        Log.e("Error: ", "oye " + e.getMessage());
                    }
                }
            }
        }

        return null;
    }


    /**
     * After completing background task
     * Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String file_url) {

        // dismiss the dialog after the file was downloaded
//        dismissDialog(progress_bar_type);
//
//        // Displaying downloaded image into image view
//        // Reading image path from sdcard
//        String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
//        // setting downloaded into image view

        Intent i;
        i = new Intent().setClass(
                mContext, ManejadorTabs.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
//        my_image.setImageDrawable(Drawable.createFromPath(imagePath));

    }


    /**
     * Updating progress bar
     */
    @Override
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
//        pDialog.setProgress(Integer.parseInt(progress[0]));

    }


}
