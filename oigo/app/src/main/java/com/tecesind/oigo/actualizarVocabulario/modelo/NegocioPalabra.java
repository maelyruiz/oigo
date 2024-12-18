package com.tecesind.oigo.actualizarVocabulario.modelo;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecesind.oigo.StaticOigo;

import dato.DaoMaster;
import dato.DaoSession;
import dato.Grupo;


/**
 * @author rosember
 * @version 1.0
 * @created 30-jul-2015 14:45:39
 */
public class NegocioPalabra {

    private Gson gson;

    public NegocioPalabra() {

        gson = new Gson();

    }

    /**
     * @throws Throwable Throwable
     */
    public void finalize() throws Throwable {

    }

    /**
     * Verifica en la Base de Datos Local si existe por lo menos una palabra
     *
     * @return true para el caso optimo, false en caso contrario
     */
    public boolean existeVideo() {

        DaoSession daoSession = DaoMaster.getSession();
        List<Palabra> listaPalabra;
        PalabraDao palabraDao = daoSession.getPalabraDao();
        listaPalabra = palabraDao.loadAll();
        if (listaPalabra.size() > 0)
            return true;

        return false;
    }

    /**
     * Debe devolver la lista de videos que tiene descargados la app movil
     */
    public List<Palabra> getExistentes() {
        File file;
        // consultar a la base de datos de android y obtener una lista
        DaoSession daoSession = DaoMaster.getSession();
        List<Palabra> listaPalabra;
        PalabraDao palabraDao = daoSession.getPalabraDao();
        listaPalabra = palabraDao.loadAll();

        for (int i = 0; i < listaPalabra.size(); i++) {
            Palabra palabra = listaPalabra.get(i);

            if (palabra.getUrlVideoNormal() != null) {
                file = new File(palabra.getUrlVideoNormal());
                if (!file.isFile())
                    palabra.setUrlVideoNormal(null);

            }

            if (palabra.getUrlVideoEnsenhanza() != null) {
                file = new File(palabra.getUrlVideoEnsenhanza());
                if (!file.isFile())
                    palabra.setUrlVideoEnsenhanza(null);

            }

            if (palabra.getImgRepresentacion() != null) {
                file = new File(palabra.getImgRepresentacion());
                if (!file.isFile())
                    palabra.setImgRepresentacion(null);

            }

            listaPalabra.set(i, palabra);
        }
        // Revisar si los archivos se encuentran en la ruta correspondiente

        Log.e("NPalabragetExistentes ", gson.toJson(listaPalabra)
                .toString());
        return listaPalabra;
    }

    public void agregar(String listDownload) {
        // TODO Auto-generated method stub
        Type typePalabras = new TypeToken<List<Palabra>>() {
        }.getType();

        List<Palabra> palabras = new LinkedList<Palabra>();
        palabras = gson.fromJson(listDownload, typePalabras);

        Log.e("ActualizarTraidaWS", "Trajo "+ palabras.size());
        DaoSession daoSession = DaoMaster.getSession();
        PalabraDao palabraDao = daoSession.getPalabraDao();

        for (int i = 0; i < palabras.size(); i++) {
            if (palabras.get(i).getUrlVideoNormal() != null
                    || palabras.get(i).getUrlVideoEnsenhanza() != null
                    || palabras.get(i).getImgRepresentacion() != null) {

                Palabra p = palabraDao.load(palabras.get(i).getId());
                if (p == null) {
                    Log.e("NegocioPalabra","VALOR DEL STATICoIGO.PATH: "+StaticOigo.PATH);
                    if (palabras.get(i).getUrlVideoNormal() != null)
                        palabras.get(i).setUrlVideoNormal(
                                StaticOigo.PATH + "/videosNormales/vn"
                                        + palabras.get(i).getId() + ".mp4");
                    else
                        palabras.get(i).setUrlVideoNormal(null);

                    if (palabras.get(i).getUrlVideoEnsenhanza() != null)
                        palabras.get(i).setUrlVideoEnsenhanza(
                                StaticOigo.PATH + "/videosEnsenhanza/ve"
                                        + palabras.get(i).getId() + ".mp4");
                    else
                        palabras.get(i).setUrlVideoEnsenhanza(null);

                    if (palabras.get(i).getImgRepresentacion() != null || !"".equals(palabras.get(i).getImgRepresentacion()))
                        palabras.get(i).setImgRepresentacion(
                                StaticOigo.PATH + "/imagenes/im"
                                        + palabras.get(i).getId() + ".png");
                    else
                        palabras.get(i).setImgRepresentacion(null);

                    palabraDao.insertOrReplace(palabras.get(i));

                } else {

                    if (palabras.get(i).getUrlVideoNormal() != null)
                        p.setUrlVideoNormal(
                                StaticOigo.PATH + "/videosNormales/vn"
                                        + palabras.get(i).getId() + ".mp4");

                    if (palabras.get(i).getUrlVideoEnsenhanza() != null)
                        p.setUrlVideoEnsenhanza(
                                StaticOigo.PATH + "/videosEnsenhanza/ve"
                                        + palabras.get(i).getId() + ".mp4");

                    if (palabras.get(i).getImgRepresentacion() != null || !"".equals(palabras.get(i).getImgRepresentacion()))
                        p.setImgRepresentacion(
                                StaticOigo.PATH + "/imagenes/im"
                                        + palabras.get(i).getId() + ".png");

                    palabraDao.insertOrReplace(p);


                }

            }
        }

    }

    public List<Palabra> getAllModulo(int id) {
        // TODO Auto-generated method stub
        DaoSession daoSession = DaoMaster.getSession();
        List<Palabra> listaPalabra;
        PalabraDao palabraDao = daoSession.getPalabraDao();
        listaPalabra = palabraDao.loadAll();
        for (int i = 0; i < listaPalabra.size(); i++) {
            if (listaPalabra.get(i).getIdModulo() != id) {
                listaPalabra.remove(i);
                i--;
            }

        }
        return listaPalabra;
    }

    public List<Palabra> getPalabras(Grupo grupo) {
        // TODO Auto-generated method stub

        DaoSession daoSession = DaoMaster.getSession();
        PalabraDao palabraDao = daoSession.getPalabraDao();
        PalabraGrupoDao palabraGrupoDao = daoSession.getPalabraGrupoDao();
        List<PalabraGrupo> listaPalabraGrupo = palabraGrupoDao.loadAll();
        List<Palabra> listaPalabra = palabraDao.loadAll();

        for (int i = 0; i < listaPalabra.size(); i++) {

            if (!pertenece(listaPalabra.get(i), grupo.getId(), listaPalabraGrupo)) {
                listaPalabra.remove(i);
                i--;
            }
        }


        return ordenar(listaPalabra, listaPalabraGrupo);

    }

    private List<Palabra> ordenar(List<Palabra> listaPalabra,
                                  List<PalabraGrupo> listaPalabraGrupo) {
        // TODO Auto-generated method stub

        if (listaPalabra != null) {
            List<Palabra> nuevaList = new LinkedList<Palabra>();
            int contador = 1;
            while (nuevaList.size() != listaPalabra.size()) {
                for (int i = 0; i < listaPalabraGrupo.size(); i++) {
                    if (listaPalabraGrupo.get(i).getOrden() == contador) {
                        for (int j = 0; j < listaPalabra.size(); j++) {
                            if (listaPalabra.get(j).getId() == Integer.parseInt(listaPalabraGrupo.get(i).getIdPalabra().toString())) {
                                nuevaList.add(listaPalabra.get(j));
                                contador++;
                                i = listaPalabraGrupo.size();
                                j = listaPalabra.size();
                            }
                        }

                    }

                }

            }

            return nuevaList;
        }
        return null;
    }

    private boolean pertenece(Palabra palabra, long id,
                              List<PalabraGrupo> listaPalabraGrupo) {

        for (int i = 0; i < listaPalabraGrupo.size(); i++) {
            if ((listaPalabraGrupo.get(i).getIdPalabra() == Integer.parseInt(palabra.getId().toString())) && (listaPalabraGrupo.get(i).getIdGrupo() == id))
                return true;

        }
        // TODO Auto-generated method stub
        return false;
    }

}