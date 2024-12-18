package com.tecesind.oigo.actualizarPerfil.controlador;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.tecesind.oigo.R;
import com.tecesind.oigo.StaticOigo;
import com.tecesind.oigo.actualizarPerfil.modelo.BitMapString;
import com.tecesind.oigo.actualizarPerfil.modelo.Dialogo;
import com.tecesind.oigo.actualizarPerfil.modelo.Imagen;
import com.tecesind.oigo.actualizarPerfil.modelo.NUsuario;
import com.tecesind.oigo.actualizarPerfil.modelo.PhotoUtils;
import com.tecesind.oigo.consumir.modelo.Consumir;

import org.ksoap2.serialization.SoapPrimitive;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dato.DaoMaster;
import dato.DaoSession;
import dato.Usuario;
import dato.UsuarioDao;

/**
 * Created by Rosember on 11/13/2015.
 */
public class DatosUsuario extends AppCompatActivity{

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 150;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 151;
    private static int CODIDGO_VERIFICACION = 0;
    //para cargar la foto del usuario
    private ImageView foto;
    private TextView telefono;

    private Uri mImageUri;
    private PhotoUtils photoUtils;
    private BitMapString bitMapString;
    private AlertDialog alert;

    //preparar al usuario que se va a guardar en la base de datos.
    private Usuario usuario;

    public static String NUMERO_TELEFONO_DE_USUARIO ="";
    public static String FOTO_DE_USUARIO="";

    private Toolbar toolbar;

    private static final int  ACTIVITY_SELECT_IMAGE = 1020,
            ACTIVITY_SELECT_FROM_CAMERA = 1040, ACTIVITY_SHARE = 1030;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_datos_usuario);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        bitMapString = new BitMapString();
        photoUtils = new PhotoUtils(this);
        usuario= new Usuario();
        foto= (ImageView)findViewById(R.id.iv_foto_usuario);
        telefono=(TextView)findViewById(R.id.et_numeroTelefono);

        //habilita o desabilita el edit numero telefonico
        Bundle bundle = new Bundle();
        Boolean habilitar=bundle.getBoolean(StaticOigo.HABILITAR_TEXTVIEW_TELEFONO);

        /*
        telefono.setClickable(habilitar);
        telefono.setCursorVisible(habilitar);
        telefono.setFocusable(habilitar);
        telefono.setFocusableInTouchMode(habilitar);*/

        getUser();
    }

    public void getUser() {

        NUsuario nUsuario= new NUsuario();
        Usuario usuario = nUsuario.getUsuario(1);

        if (usuario != null) {

            telefono.setText(usuario.getTelefono());

            BitMapString convertidor = new BitMapString();
            Bitmap laFoto = convertidor.StringToBitMap(usuario.getFoto());

            //imagenActual = laFoto;

            foto.setImageBitmap(laFoto);

        } else {
            Toast.makeText(this, "No hay usuario", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_datos_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.guardar_usuario) {

            if (!usuario.getFoto().equals("")&& telefono.getText().toString()!=""){

                NUMERO_TELEFONO_DE_USUARIO =telefono.getText().toString();
                FOTO_DE_USUARIO=usuario.getFoto();
                //Empesamos con la verificacion del numero de telefonico
                VerificarTelefono verificar= new VerificarTelefono(telefono.getText().toString());
                verificar.execute();

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Cuado el dialogo presiono aceptar o positivo
     * @param password el codigo que ingreso
     */
    public void postivo(String password) {
        VerifcarCodigo verifcar= new VerifcarCodigo(password,this);
        verifcar.execute();
    }

    public class VerifcarCodigo extends AsyncTask<Void, Void, Boolean> {

        private String codigo;
        private Context context;

        public VerifcarCodigo(String codigo, Context context) {
            this.codigo=codigo; this.context= context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            HashMap<String,Object>parametros= new HashMap<String, Object>();
            parametros.put("telf", DatosUsuario.NUMERO_TELEFONO_DE_USUARIO);
            parametros.put("codigo",Integer.parseInt(codigo));

            Consumir consumir = new Consumir();

            SoapPrimitive result= null;
            try {
                result=(SoapPrimitive)consumir.consumir("verificarCodigo",parametros);
                if (Boolean.parseBoolean(result.toString())){
                    Log.e("Hilo","retornar true");
                    return true;

                }
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }



            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                SaveUser save = new SaveUser(DatosUsuario.FOTO_DE_USUARIO, "+591" +DatosUsuario.NUMERO_TELEFONO_DE_USUARIO, getApplicationContext());
                save.execute();
            }
        }
    }




    /**
     * Pedimos un CODIGO generado por la web service
     * para que el usuario se envie ese codigo via SMS al propietario
     * del numero que ingreso. Luego se espera la verificacion con el Receive del
     */
    private class VerificarTelefono extends AsyncTask<Void,Void,Void>{

        String telefono;

        public VerificarTelefono(String telefono) {
            this.telefono = telefono;
        }

        @Override
        protected Void doInBackground(Void... params) {

            HashMap<String,Object> parametro = new HashMap<>();
            parametro.put("telf",telefono);

            Consumir consumir= new Consumir();
            SoapPrimitive result=null;
            try {
                result= (SoapPrimitive) consumir.consumir("verificarTelf",parametro);

                if (result!= null){

                    CODIDGO_VERIFICACION=Integer.parseInt(result.toString());
                    NUMERO_TELEFONO_DE_USUARIO =telefono;

                    preguntarPermisosSMS_AlUsuario();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private void enviarElMensaje(){
        
        
        //mandamos un mensaje SMS con el codigo pin
        SmsManager sms= SmsManager.getDefault();
        Log.e("DATOS_USUARIO","Enviando mensaje a :"+telefono);
        sms.sendTextMessage(NUMERO_TELEFONO_DE_USUARIO, null, "Tu codigo PIN: "+CODIDGO_VERIFICACION, null, null);


        //lazamos el dialogo para que inserte el codigo que se le envio
                    Dialogo dialog = new Dialogo();
                    dialog.show(getSupportFragmentManager(), "Dialog");
    }

    /**
     * Actualiza la imagen del usuario mandando un dialogo para que seleccione su foto
     * @param v
     */
    public void actualizarImagen(View v){
        LayoutInflater inflater = getLayoutInflater();

        View dialoglayout = inflater.inflate(R.layout.dialogo_camara_galeria, null);

        ImageView camara= (ImageView)dialoglayout.findViewById(R.id.iv_camara);
        ImageView galeria= (ImageView)dialoglayout.findViewById(R.id.iv_galeria);

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preguntarPermisosCamaraAlUsuario();

            }
        });
        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(DatosUsuario.this);
        builder.setView(dialoglayout);
        alert=builder.create();
        alert.show();

    }

    public void lanzarLaCamara(){

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = null;
        try {
            // place where to store camera taken picture
            photo = PhotoUtils
                    .createTemporaryFile("picture", ".jpg", DatosUsuario.this);
            photo.delete();
        } catch (Exception e) {
            Log.v(getClass().getSimpleName(),
                    "Can't create file to take picture!");
        }
        mImageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, ACTIVITY_SELECT_FROM_CAMERA);
    }

    private void preguntarPermisosSMS_AlUsuario() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the perm ission.

                enviarElMensaje();
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.


            }
        } else {

            //SI TIENE LOS PERMISOS OTORGADOS
            enviarElMensaje();
        }


    }

    private void preguntarPermisosCamaraAlUsuario() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the perm ission.

                lanzarLaCamara();
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.


            }
        } else {

           //SI TIENE LOS PERMISOS OTORGADOS
            lanzarLaCamara();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    lanzarLaCamara();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    enviarElMensaje();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //se cierra el diaglo
        alert.cancel();

        //verifico si la seleccionada fue la galeria
        if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            getImage(mImageUri);

        } else if (requestCode == ACTIVITY_SELECT_FROM_CAMERA
                && resultCode == RESULT_OK) {
            getImage(mImageUri);
        }

    }

    public void getImage(Uri uri) {
        Bitmap bounds = photoUtils.getImage(uri);
        if (bounds != null) {

            Imagen imagen= new Imagen();
            bounds=imagen.redimencionar(bounds,426,496);

            foto.setImageBitmap(bounds);

            bitMapString = new BitMapString();
            //seteo la nueva imagen que se asigno
            usuario.setFoto(bitMapString.BitMapToString(bounds));

        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private static int getAppVersion(Context context) {

        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageInfo.versionCode;

    }


    /**
     * Registrar la aplicacion en google cloud message
     * Registrar en el servidor de oigo
     * Registrar en la base de datos de android
     * Created by Rosember on 11/15/2015.
     */
    public class SaveUser extends AsyncTask<Void,Void,Void> {

        private static final String TAG="SaveUser";
        private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        private static final String SENDER_ID = "779575362758";
        private static final String PROPERTY_REG_ID = "registration_id";
        private static final String PROPERTY_USER = "user";
        private static final String PROPERTY_APP_VERSION = "appVersion";
        private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";

        private String foto;
        private String telefono;
        private Context context;

        private Consumir consumir;
        //El usuario que se va a guardar en el servidor y en la BD de android
        private Usuario usuario;
        private String regId;
        private GoogleCloudMessaging gcm;

        public SaveUser(String foto,String telefono, Context context) {
            this.foto=foto;
            this.telefono=telefono;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (!foto.equals("")&&!telefono.equals("")){

                usuario = new Usuario();
                usuario.setFoto(foto);
                usuario.setTelefono(telefono);

                Gson gson = new Gson();
                DaoSession daoSession = DaoMaster.getSession();
                UsuarioDao usuarioDao = daoSession.getUsuarioDao();
                List<Usuario> usuarios = usuarioDao.loadAll();


                if (usuarios.size() > 0) {
                    usuario.setId(usuarios.get(0).getId());
                    usuario.setTelefono(usuarios.get(0).getTelefono());
                    usuario.setRegId(usuarios.get(0).getRegId());
                } else {
                    String regId = saveUserGoogle();

                    usuario.setId((long) -1);
                    usuario.setRegId(regId);
                }

                if (!(usuario.getRegId().equals("")))
                //Guardar en el servidor
                saveInServerOigo();

                else
                    Toast.makeText(context,"No hay conexion a internet. Intente nuevamente",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(context,"Inserte foto y telefono",Toast.LENGTH_LONG).show();
            }

            return null;
        }

        private String saveUserGoogle() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);

            // Obtenemos el Registration ID guardado
            regId = getRegistrationId(context);

            // Si no disponemos de Registration ID comenzamos el
            // registro
            if (regId.equals("")) {

                return registrarIdGoogle(usuario.getNombre());
            }
        } else {
            Log.e(TAG, "No se ha encontrado Google Play Services.");
        }
            return "";
        }

        private String registrarIdGoogle(String nombre) {
            String msg = "";

            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }

                // Nos registramos en los servidores de GCM
                regId = gcm.register(SENDER_ID);
                msg=regId;
                Log.e(TAG, "Registrado en GCM: registration_id=" + regId);

            } catch (IOException ex) {
                Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
            }

            return msg;
        }

        private String getRegistrationId(Context context) {
            SharedPreferences prefs = getSharedPreferences(
                    context.getClass().getSimpleName(), Context.MODE_PRIVATE);

            Log.e(TAG, "getRegistrationId");
            String registrationId = prefs.getString(PROPERTY_REG_ID, "");

            if (registrationId.length() == 0) {
                Log.e(TAG, "Registro GCM no encontrado.");
                return "";
            }

            String registeredUser = prefs.getString(PROPERTY_USER, "user");

            int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                    Integer.MIN_VALUE);

            long expirationTime = prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                    Locale.getDefault());
            String expirationDate = sdf.format(new Date(expirationTime));

            Log.e(TAG, "Registro GCM encontrado (usuario=" + registeredUser
                    + ", version=" + registeredVersion + ", expira="
                    + expirationDate + ")");

            int currentVersion = getAppVersion(context);

            if (registeredVersion != currentVersion) {
                Log.e(TAG, "Nueva versión de la aplicación.");
                return "";
            } else if (System.currentTimeMillis() > expirationTime) {
                Log.e(TAG, "Registro GCM expirado.");
                return "";
            } else if (usuario.getNombre().equals(registeredUser)) {
                Log.e(TAG, "Nuevo nombre de usuario.");
                return "";
            }

            return registrationId;
        }




        private boolean checkPlayServices() {
            int resultCode = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(context);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                    Log.i(TAG, "Dispositivo soportado.");
                } else {
                    Log.i(TAG, "Dispositivo no soportado.");
                    finish();
                }
                return false;
            }
            return true;
        }


        //Guarda los datos del usuario en la servidor Oigo y luego en la base de datos de android

        private void saveInServerOigo(){

            Log.e(TAG,"Guardando en el server oigo");

            Gson gson = new Gson();

            HashMap<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("usuario", gson.toJson(usuario));

            consumir = new Consumir();

            SoapPrimitive result = null;

            try {
                result = (SoapPrimitive) consumir.consumir("regUsuario", parametros);

                if (result != null) {
                    Log.e("Result soap", result.toString());

                    //Guardamos en la BD de Android
                    if (Integer.parseInt(result.toString()) != -1) {
                        usuario.setId(Long.parseLong(result.toString()));
                        usuario.setNotaMax(0);
                        DaoSession daoSession = DaoMaster.getSession();
                        UsuarioDao usuarioDao = daoSession.getUsuarioDao();
                        usuarioDao.insertOrReplace(usuario);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent data1 = new Intent();
//        data1.setData(Uri.parse(data.getDataString()));

            setResult(RESULT_OK,data1);
            finish();
        }
    }



}
