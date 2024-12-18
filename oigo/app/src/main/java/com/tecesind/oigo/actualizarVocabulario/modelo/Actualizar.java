package com.tecesind.oigo.actualizarVocabulario.modelo;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecesind.oigo.armarOracionLSB.modelo.NegocioFrase;
import com.tecesind.oigo.armarOracionLSB.modelo.NegocioGrupo;
import com.tecesind.oigo.armarOracionLSB.modelo.NegocioGrupoFrase;
import com.tecesind.oigo.armarOracionLSB.modelo.NegocioPalabraGrupo;

/**
 * Empieza el caso de uso Actualizar vocabulario
 *
 * @author rosember
 */
public class Actualizar {

    private NegocioPalabra nPalabra;
    private NegocioModulo nModulo;
    private NegocioCompuesta nCompuesta;
    private NegocioVariacion nVariacion;
    private NegocioFrase nFrase;
    private NegocioGrupo nGrupo;
    private NegocioGrupoFrase nGrupoFrase;
    private NegocioPalabraGrupo nPalabraGrupo;

    private AdministradorWS admWs;

    private Context mContext;
    private Gson gson;

    public Actualizar(Context context) {
        super();
        gson = new Gson();
        nPalabra = new NegocioPalabra();
        nModulo = new NegocioModulo();
        nCompuesta = new NegocioCompuesta();
        nVariacion = new NegocioVariacion();
        nFrase = new NegocioFrase();
        nGrupo = new NegocioGrupo();
        nGrupoFrase = new NegocioGrupoFrase();
        nPalabraGrupo = new NegocioPalabraGrupo();


        admWs = new AdministradorWS();
        mContext = context;
    }

    /**
     * Empieza el caso de uso Actualizar vocabulary
     *
     * @param first true para el caso de que sea el primer inicio, false en caso
     *              contrario
     * @return true si se tiene todo el vocabulario en android, false caso
     * cantrario y empieza la descarga.
     */
    public boolean iniciar(boolean first) {

        String listDownload = null;
        List<String> listaDevolucion;
        String lista;
        if (first || !nPalabra.existeVideo()) {

            listaDevolucion = new LinkedList<>();
            listaDevolucion.add(null);
            listaDevolucion.add(null);
            listaDevolucion.add(null);
            listaDevolucion.add(null);
            listaDevolucion.add(null);
            listaDevolucion.add(null);
            listaDevolucion.add(null);
            listaDevolucion.add(null);

            lista = admWs.getAllFaltantes(listaDevolucion);

        } else {


            // preguntar que videos tienen descagados en el sdcard
            listaDevolucion = new LinkedList<>();


            listaDevolucion.add(gson.toJson(nPalabra.getExistentes()));
            listaDevolucion.add(gson.toJson(nModulo.getExistentes()));
            listaDevolucion.add(gson.toJson(nCompuesta.getExistentes()));
            listaDevolucion.add(gson.toJson(nVariacion.getExistentes()));
            listaDevolucion.add(gson.toJson(nFrase.getExistentes()));
            listaDevolucion.add(gson.toJson(nGrupo.getExistentes()));
            listaDevolucion.add(gson.toJson(nGrupoFrase.getExistentes()));
            listaDevolucion.add(gson.toJson(nPalabraGrupo.getExistentes()));

            lista = admWs.getAllFaltantes(listaDevolucion);

            List<Palabra> list = new LinkedList<Palabra>();

            list = nPalabra.getExistentes();
            Log.e("Actualmente","Existe " + list.size());
            // pasar al metodo descargar

        }
        Type tipoLista = new TypeToken<List<String>>(){}.getType();
        if(lista!=null) {
            listaDevolucion = gson.fromJson(lista, tipoLista);
            if (listaDevolucion.get(1) != null)
                nModulo.agregar(listaDevolucion.get(1));
            if (listaDevolucion.get(2)!= null)
                nCompuesta.agregar(listaDevolucion.get(2));
            if (listaDevolucion.get(3)!= null)
                nVariacion.agregar(listaDevolucion.get(3));
            if (listaDevolucion.get(4)!= null)
                nFrase.agregar(listaDevolucion.get(4));
            if (listaDevolucion.get(5)!= null)
                nGrupo.agregar(listaDevolucion.get(5));
            if (listaDevolucion.get(6)!= null)
                nGrupoFrase.agregar(listaDevolucion.get(6));
            if (listaDevolucion.get(7)!= null)
                nPalabraGrupo.agregar(listaDevolucion.get(7));
            if (listaDevolucion.get(0)!= null) {

                nPalabra.agregar(listaDevolucion.get(0));

                new DownloadFileUrl(false, mContext).execute(listaDevolucion.get(0));

            } else {
                return true;

            }

        }


        return false;
    }
}
