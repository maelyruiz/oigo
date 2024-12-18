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
public class NegocioGrupoFrase {

    private Gson gson;

    public NegocioGrupoFrase(){

        gson = new Gson();
    }

    public void agregar(String lista){

        Type typeGrupoFrases = new TypeToken<List<GrupoFrase>>() {
        }.getType();

        List<GrupoFrase> grupoFrases = new LinkedList<GrupoFrase>();
        grupoFrases = gson.fromJson(lista, typeGrupoFrases);

        DaoSession daoSession = DaoMaster.getSession();
        GrupoFraseDao grupoFraseDao = daoSession.getGrupoFraseDao();

        for (int i=0; i<grupoFrases.size(); i++){

            GrupoFrase m = grupoFraseDao.load(grupoFrases.get(i).getId());

            if (m== null){

                grupoFraseDao.insertOrReplace(grupoFrases.get(i));
            }
        }
    }



    public List<GrupoFrase> getExistentes() {
        // TODO Auto-generated method stub
        DaoSession daoSession = DaoMaster.getSession();
        GrupoFraseDao grupoFraseDao = daoSession.getGrupoFraseDao();
        List<GrupoFrase> listaGrupoFrases = grupoFraseDao.loadAll();
        return listaGrupoFrases;
    }
}
