package com.tecesind.oigo.conversarLSB.controlador;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.tecesind.oigo.R;
import com.tecesind.oigo.armarOracionLSB.controlador.ControlPaquete;
import com.tecesind.oigo.conversarLSB.modelo.CryptA51;
import com.tecesind.oigo.conversarLSB.modelo.MessagesListAdapter;
import com.tecesind.oigo.conversarLSB.modelo.Msg;
import com.tecesind.oigo.conversarLSB.modelo.PintarMensaje;
import com.tecesind.oigo.conversarLSB.modelo.SendMessageServer;
import com.tecesind.oigo.visualizarSenha.modelo.Analizador;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dato.Contacto;
import dato.ContactoDao;
import dato.DaoMaster;
import dato.DaoSession;
import dato.Mensaje;
import dato.MensajeDao;
import dato.Usuario;
import dato.UsuarioDao;
import de.greenrobot.dao.query.Query;

/**
 * Created by Rosember on 11/15/2015.
 */
public class ChatActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,MediaPlayer.OnCompletionListener  {

    private static ListView listMsg;
    private static ArrayList<Msg> listMessages;
    private static MessagesListAdapter adapter;

    private static EditText nuevoMensaje;
    private ImageButton nuevoMensajeEnviar;
    private Long id;

    private Toolbar toolbar;
    private ToggleButton btnEscuchar;
    public static Context CONTEXTO_CHAT_ACTIVITY;
    private CryptA51 a51;

    public static final String TAG="ChatActivity";

    //LA RUTA DE LOS VIDEOS QUE VA A REPRODUCIR
    private List<String> urls;
    private Uri uri;
    private int contador;
    private VideoView video;

    public ChatActivity() {
        // TODO Auto-generated constructor stub
        CONTEXTO_CHAT_ACTIVITY=this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Cero videos faltan reproducir
        contador=0;
        a51 = new CryptA51();
        listMsg = (ListView) findViewById(R.id.listViewChat);
        nuevoMensaje = (EditText) findViewById(R.id.newmsg);
        nuevoMensajeEnviar = (ImageButton) findViewById(R.id.newmsgsend);
        listMessages = new ArrayList<Msg>();
        btnEscuchar = (ToggleButton) findViewById(R.id.btnEscucharChat);
        CONTEXTO_CHAT_ACTIVITY=this;

        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");
        toolbar = (Toolbar) findViewById(R.id.appbarChat);
        setSupportActionBar(toolbar);
        leerMsgDB(bundle.getLong("id"));

        listMsg.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    public void senhas(View v){
        Intent i = new Intent(this, ControlPaquete.class);
        startActivityForResult(i, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void enviarMensaje(View view) {

        if (!nuevoMensaje.getText().toString().equals("")) {

            // pintar el menaje en la actividad de android
            String mensaje = nuevoMensaje.getText().toString();

            Usuario emisor = new Usuario();
            DaoSession daoSession = DaoMaster.getSession();
            UsuarioDao usuarioDao = daoSession.getUsuarioDao();
            emisor = usuarioDao.loadAll().get(0);
            emisor.setFoto("");
            ContactoDao contactoDao = daoSession.getContactoDao();
            Contacto contacto = contactoDao.loadByRowId(id);

            Usuario destino = new Usuario();
            destino.setId(contacto.getId());
            destino.setNombre(contacto.getNombre());
            destino.setRegId(contacto.getRegId());
            destino.setTelefono(contacto.getTelefono());


            long date = System.currentTimeMillis();
            SimpleDateFormat formatohora = new SimpleDateFormat("HH:mm");
            String horaString = formatohora.format(date);

            SimpleDateFormat formatofecha = new SimpleDateFormat("d/MM/yyyy");
            String fechaString = formatofecha.format(date);

            Mensaje mensajeBD = new Mensaje();

            mensajeBD.setTexto(mensaje);

            mensajeBD.setFecha(fechaString);
            mensajeBD.setHora(horaString);
            mensajeBD.setTipo(true);

            mensajeBD.setIdContacto(contacto.getId());

            MensajeDao mensajeDao = daoSession.getMensajeDao();
            mensajeDao.insertOrReplace(mensajeBD);

            Msg nuevoMsg = new Msg(emisor, destino, mensaje, mensaje, true,
                    fechaString, horaString);

            Gson gson = new Gson();
            String nuevo = gson.toJson(nuevoMsg);


            PintarMensaje pintarMensaje = new PintarMensaje(this);
            pintarMensaje.execute(nuevo);

            nuevoMsg.setMensaje(a51.cifrado(mensaje));
            nuevoMsg.setMensajeOriginal(a51.cifrado(mensaje));
            String nuevoServer= gson.toJson(nuevoMsg);
            // enviar le mensaje al servidor par que sea dirigido al que
            // corresponde
            // y guarda el mensaje en android
            SendMessageServer messageServer = new SendMessageServer();
            messageServer.execute(nuevoServer);

        } else {

            Toast.makeText(this, "write message", Toast.LENGTH_SHORT).show();

        }

    }

    public void escuchar(View view){
        if (btnEscuchar.isChecked()) {
            inciarReconocimiento(0);
        }

    }


    /**
     * Metodo llamado solamente por la clase hilo PintarMensaje
     * @param mensaje el mensaje que se quiere pintar
     */
    public static void pintar(Msg mensaje){

        if (mensaje.isTipo()) {
            listMessages.add(mensaje);
        } else {
            listMessages.add(mensaje);
        }

        adapter = new MessagesListAdapter(CONTEXTO_CHAT_ACTIVITY,listMessages);

        listMsg.setAdapter(adapter);

        listMsg.setSelection(listMsg.getAdapter().getCount()-1);

        nuevoMensaje.setText("");

    }

    private void leerMsgDB(Long idContacto) {

        DaoSession daoSession = DaoMaster.getSession();
        if (daoSession==null){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "dboigo", null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster= new DaoMaster(db);
            daoMaster.newSession();
            daoSession = DaoMaster.getSession();
        }

        ContactoDao contactoDao = daoSession.getContactoDao();
        Contacto contacto = contactoDao.loadByRowId(idContacto);

        ActionBar actionBar = getSupportActionBar();

        /**INDICAR TITULO Y SUBTITULO**/
        actionBar.setTitle(contacto.getNombre());
        actionBar.setSubtitle("Conversacion");
        MensajeDao mensajeDao = daoSession.getMensajeDao();


        List<Mensaje> listaMsg = new LinkedList<Mensaje>();

        Query query= mensajeDao.queryBuilder().where(MensajeDao.Properties.IdContacto.eq(idContacto)).build();

        List list = query.list();

        for (int i = 0; i < list.size(); i++) {
            listaMsg.add((Mensaje) list.get(i));
        }

        Gson gson = new Gson();

        for (int i = 0; i < listaMsg.size(); i++) {

            Msg msg = new Msg();

            msg.setFecha(listaMsg.get(i).getFecha());
            msg.setHora(listaMsg.get(i).getHora());
            msg.setMensaje(listaMsg.get(i).getTexto());

            msg.setTipo(listaMsg.get(i).getTipo());
            msg.setHora(listaMsg.get(i).getHora());

            PintarMensaje pintarMensaje = new PintarMensaje(this);
            pintarMensaje.execute(gson.toJson(msg));

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


        if(requestCode==0) {

            btnEscuchar.setChecked(false);
            // Si el reconocimiento a sido bueno

            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> matches = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                // Separo el texto en palabras.

                nuevoMensaje.setText(matches.get(0).toString().trim());
            }

        }else{

            if(resultCode==Activity.RESULT_OK){

                nuevoMensaje.setText(data.getDataString());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        String expresion=txtMsg.getText().toString();

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbarChat);
        setSupportActionBar(toolbar);

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialogo_conversacion, null);

        video= (VideoView)dialoglayout.findViewById(R.id.VideoViewConversacion);


        Analizador analizador = new Analizador();
        urls = analizador.analizar(expresion);
        realizarSenhas();


        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setView(dialoglayout);
        AlertDialog alert;
        alert=builder.create();
        alert.show();


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void realizarSenhas() {
        if (urls != null&&urls.size()>0) {
            File file = new File(urls.get(0));
            if (file.exists()) {

                uri = Uri.parse(urls.get(0));
                video.setVideoURI(uri);


                Log.e(TAG,"Realizar Senhas: Por preguntar si esta reproduciendo...");
                if (!video.isPlaying()) {
                    video.setBackground(null);


                    Log.e(TAG, "Realizar Senhas: listo para inicia.....");
                    video.start();
                }
            }
            else Log.e("Traductor","No existe el video");
        }

    }


    @SuppressLint("NewApi")
    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
        contador++;

        if (contador < urls.size()) {
            uri = Uri.parse(urls.get(contador));
            //videoView.setVideoURI(Uri.parse("android.resource://" + this.getActivity().getPackageName() + "/"
            //        + R.raw.a));
            video.setVideoURI(uri);

            if (!video.isPlaying()) {
                video.setBackground(null);
                video.start();
            }
        } else
            contador = 0;
    }
}
