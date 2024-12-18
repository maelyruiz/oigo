package com.tecesind.oigo.actualizarVocabulario.modelo;

import android.util.Log;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dato.DaoMaster;
import dato.DaoSession;

public class NegocioModulo {

    private Gson gson;

    public NegocioModulo() {

        gson = new Gson();
    }

    public void agregar(String lista) {
        Log.e("NegocioModulo", "agregar");
        Type typeModulos = new TypeToken<List<Modulo>>() {
        }.getType();

        List<Modulo> modulos = new LinkedList<Modulo>();
        modulos = gson.fromJson(lista, typeModulos);

        DaoSession daoSession = DaoMaster.getSession();
        ModuloDao moduloDao = daoSession.getModuloDao();

        for (int i = 0; i < modulos.size(); i++) {

            Modulo m = moduloDao.load(modulos.get(i).getId());

            if (m == null) {

                moduloDao.insertOrReplace(modulos.get(i));
            }
        }
    }

    public Modulo getModulo(int modulo) {
        // TODO Auto-generated method stub
        DaoSession daoSession = DaoMaster.getSession();
        ModuloDao moduloDao = daoSession.getModuloDao();
        List<Modulo> listaModulos = moduloDao.loadAll();
        for (int i = 0; i < listaModulos.size(); i++) {
            if (listaModulos.get(i).getId() == modulo)
                return listaModulos.get(i);
        }
        return null;
    }

    public List<Modulo> getExistentes() {
        // TODO Auto-generated method stub
        DaoSession daoSession = DaoMaster.getSession();
        ModuloDao moduloDao = daoSession.getModuloDao();
        List<Modulo> listaModulos = moduloDao.loadAll();
        return listaModulos;
    }
}
