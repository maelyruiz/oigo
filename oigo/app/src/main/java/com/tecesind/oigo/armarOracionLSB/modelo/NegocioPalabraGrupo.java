package com.tecesind.oigo.armarOracionLSB.modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import dato.DaoMaster;
import dato.DaoSession;

/**
 * Created by Rosember on 11/6/2015.
 */
public class NegocioPalabraGrupo {
    private Gson gson;

    public NegocioPalabraGrupo(){

        gson = new Gson();
    }

    public void agregar(String lista){

        Type typePalabraGrupos = new TypeToken<List<PalabraGrupo>>() {
        }.getType();

        List<PalabraGrupo> palabraGrupos = new LinkedList<PalabraGrupo>();
        palabraGrupos = gson.fromJson(lista, typePalabraGrupos);

        DaoSession daoSession = DaoMaster.getSession();
        PalabraGrupoDao palabraGrupoDao = daoSession.getPalabraGrupoDao();

        for (int i=0; i<palabraGrupos.size(); i++){

            PalabraGrupo m = palabraGrupoDao.load(palabraGrupos.get(i).getId());

            if (m== null){

                palabraGrupoDao.insertOrReplace(palabraGrupos.get(i));
            }
        }
    }

    public List<PalabraGrupo> getExistentes() {
        // TODO Auto-generated method stub
        DaoSession daoSession = DaoMaster.getSession();
        PalabraGrupoDao palabraGrupoDao = daoSession.getPalabraGrupoDao();
        List<PalabraGrupo> listaPalabraGrupos = palabraGrupoDao.loadAll();
        return listaPalabraGrupos;
    }
}
