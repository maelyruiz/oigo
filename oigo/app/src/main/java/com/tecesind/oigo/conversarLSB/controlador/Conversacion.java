package com.tecesind.oigo.conversarLSB.controlador;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecesind.oigo.R;
import com.tecesind.oigo.StaticOigo;
import com.tecesind.oigo.actualizarPerfil.controlador.DatosUsuario;
import com.tecesind.oigo.consumir.modelo.Consumir;
import com.tecesind.oigo.conversarLSB.modelo.Consulta;
import com.tecesind.oigo.conversarLSB.modelo.MyAdapter;

import org.ksoap2.serialization.SoapPrimitive;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import dato.Contacto;
import dato.ContactoDao;
import dato.DaoMaster;
import dato.DaoSession;
import dato.Usuario;
import dato.UsuarioDao;

/**
 * Created by Rosember on 11/1/2015.
 */
public class Conversacion extends Fragment {


    //para mostrar al usuario que se esta actualizando los contactos
    private ProgressBar spinner;

    //para mostrar a los contactos en una grilla
    GridView gridView;

    //lista de usuarios de sus contactos
    List<Usuario> listaContactos;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 6;

    public static Conversacion newInstance() {
        Bundle args = new Bundle();
        Conversacion fragment = new Conversacion();
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
        return inflater.inflate(R.layout.tab_conversacion, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinner = (ProgressBar) this.getActivity().findViewById(R.id.progressUpdateContacts);
        gridView = (GridView) this.getActivity().findViewById(R.id.gridview);

        //Carga los contactos de la base de datos
        //si no hay usuario registrado lanza al registro de la misma
        //con el objetivo de que pueda consultar sus contactos
        CargarContactos cargarContactos = new CargarContactos(this.getActivity());
        cargarContactos.execute();

        //se inicia la lista de contactos
        listaContactos = new LinkedList<>();
    }

    /**
     * Verificamos si es primera ves que esta utilizando la apliacion
     *
     * @return true si no hay el usuario en la base de datos
     * false en caso contrario
     */
    private boolean primerInicio() {

        Usuario emisor = new Usuario();
        DaoSession daoSession = DaoMaster.getSession();
        UsuarioDao usuarioDao = daoSession.getUsuarioDao();


        List<Usuario> listUsuario = usuarioDao.loadAll();
        if (listUsuario.isEmpty())
            return true;
        else
            return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            DaoSession daoSession = DaoMaster.getSession();
            ContactoDao contactoDao = daoSession.getContactoDao();
            List<Contacto> contactoList = contactoDao.loadAll();

            if (!contactoList.isEmpty()) {
                //Cambio el objeto contactoList a UsuarioList porque MyAdapter recibe UsuarioList
                List<Usuario> usuarioList = new LinkedList<>();
                for (int i = 0; i < contactoList.size(); i++) {
                    Usuario usuario = new Usuario(contactoList.get(i).getId(),
                            contactoList.get(i).getNombre(),
                            contactoList.get(i).getFoto(),
                            contactoList.get(i).getRegId(),
                            contactoList.get(i).getTelefono(), 0);
                    usuarioList.add(usuario);
                }

                gridView.setAdapter(new MyAdapter(this.getActivity(), usuarioList));

            } else {
                //Actualizamos Saliendo al servidor
                UpdateContacts updateContacts = new UpdateContacts(getActivity());
                updateContacts.execute();
            }

            spinner.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_conversacion, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.contacts) {
            if (primerInicio()) {
                Intent intent = new Intent(this.getActivity(), DatosUsuario.class);
//                startActivity(intent);
                startActivityForResult(intent, 1);
            } else {
                preguntarPermisosContactosAlUsuario();
            }
            return true;
        }

        if (id == R.id.updatephoto) {
            Intent intent = new Intent(this.getActivity(), DatosUsuario.class);
            if (primerInicio()) {
                intent.putExtra(StaticOigo.HABILITAR_TEXTVIEW_TELEFONO, true);
            } else {
                intent.putExtra(StaticOigo.HABILITAR_TEXTVIEW_TELEFONO, false);
            }
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void preguntarPermisosContactosAlUsuario() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the perm ission.

                consultaAAgenda();
                UpdateContacts update = new UpdateContacts(this.getActivity());
                update.execute();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.


            }
        } else {

            consultaAAgenda();
            UpdateContacts update = new UpdateContacts(this.getActivity());
            update.execute();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    consultaAAgenda();
                    UpdateContacts update = new UpdateContacts(this.getActivity());
                    update.execute();

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

    private void consultaAAgenda() {
        // Query: contacts with phone shorted by name
        Cursor cursor = this.getActivity().getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE},
                ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                        + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL", null,
                ContactsContract.Data.DISPLAY_NAME + " ASC");


        List<Usuario> listUsuario = new LinkedList<Usuario>();


        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Usuario u = new Usuario();
            int phoneNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            u.setTelefono(cursor.getString(phoneNumber));
            u.setNombre(name);
            Log.e("Consulta", "" + cursor.getString(phoneNumber));

            listUsuario.add(u);
        }
        cursor.close();
        Log.e("Consulta", "" + listUsuario.size());

        listaContactos = listUsuario;


    }

    private class UpdateContacts extends AsyncTask<Void, String, String> {

        private Consumir consumidor;
        private Gson gson;

        private static final String METHOD_NAME_GET_CONTACTS = "updateContacts";

        //private Consulta consulta;
        private Context context;

        public UpdateContacts(Context context) {
            super();
            this.context = context;
            consumidor = new Consumir();
            gson = new Gson();
            consulta = new Consulta(context);

        }

        @Override
        protected String doInBackground(Void... params) {

            List<Usuario> listUsusario = listaContactos;

            if (listUsusario.size() > 0) {

                Usuario emisor = new Usuario();
                DaoSession daoSession = DaoMaster.getSession();
                UsuarioDao usuarioDao = daoSession.getUsuarioDao();
                emisor = usuarioDao.loadAll().get(0);
                emisor.setFoto("");

                HashMap<String, Object> parametro = new HashMap<String, Object>();
                parametro.put("contactos", gson.toJson(listUsusario));
                parametro.put("usuario", gson.toJson(emisor));

                SoapPrimitive result = null;
                try {
                    result = (SoapPrimitive) consumidor.consumir(
                            METHOD_NAME_GET_CONTACTS, parametro);

                    if (result != null) {

                        return result.toString();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                Log.e("resul.................", "" + result);

            } else {
                Log.e("UpdateContact",
                        "No tiene una lista de contactos en el smart");
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (!listaContactos.isEmpty()) {
                Type listUsuario = new TypeToken<List<Usuario>>() {
                }.getType();

                if (result!=null){

                List<Usuario> listaUsuario = gson.fromJson(result.toString(),
                        listUsuario);

                if (!listaUsuario.isEmpty()) {
                    Log.e("UpdateContacts", "" + result.toString());

                    DaoSession daoSession = DaoMaster.getSession();
                    ContactoDao contactoDao = daoSession.getContactoDao();

                    for (int i = 0; i < listaUsuario.size(); i++) {

                        Contacto contacto = new Contacto();

                        contacto.setId(listaUsuario.get(i).getId());
                        contacto.setNombre(listaUsuario.get(i).getNombre());
                        contacto.setTelefono(listaUsuario.get(i).getTelefono());
                        contacto.setFoto(listaUsuario.get(i).getFoto());
                        contacto.setRegId(listaUsuario.get(i).getRegId());

                        contactoDao.insertOrReplace(contacto);

                    }

                    gridView.setAdapter(new MyAdapter(context, listaUsuario));

                }
                } else {
                    Log.e("U", "La lista esta vacia");
                }
                spinner.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(getActivity(), "No hay contactos", Toast.LENGTH_SHORT).show();
            }


        }

    }


    /**
     * Carga los contactos desde la base de datos
     * si es que ya el usuario se registro, si no dirige al registro.
     */
    public class CargarContactos extends AsyncTask<Void, Void, Void> {

        private Context context;

        public CargarContactos(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (primerInicio()) {
                //falta que se registre el usuario
                Intent intent = new Intent(getActivity(), DatosUsuario.class);
                startActivityForResult(intent, 1);
            } else {
                DaoSession daoSession = DaoMaster.getSession();
                ContactoDao contactoDao = daoSession.getContactoDao();
                List<Contacto> contactoList = contactoDao.loadAll();

                if (!contactoList.isEmpty()) {
                    //Cambio el objeto contactoList a UsuarioList porque MyAdapter recibe UsuarioList
                    List<Usuario> usuarioList = new LinkedList<>();
                    for (int i = 0; i < contactoList.size(); i++) {
                        Usuario usuario = new Usuario(contactoList.get(i).getId(),
                                contactoList.get(i).getNombre(),
                                contactoList.get(i).getFoto(),
                                contactoList.get(i).getRegId(),
                                contactoList.get(i).getTelefono(), 0);
                        usuarioList.add(usuario);
                    }

                    gridView.setAdapter(new MyAdapter(context, usuarioList));

                } else {
                    //Actualizamos Saliendo al servidor
                    UpdateContacts updateContacts = new UpdateContacts(getActivity());
                    updateContacts.execute();
                }

                spinner.setVisibility(View.INVISIBLE);
            }
        }
    }
}
