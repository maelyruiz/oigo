package com.tecesind.oigo.actualizarVocabulario.modelo;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dato.DaoMaster;
import dato.DaoSession;

public class NegocioVariacion {

private Gson gson;
	
	public NegocioVariacion(){
		
		gson = new Gson();
	}

	public void agregar(String lista){
		
		Type typeVariaciones = new TypeToken<List<Variacion>>() {
		}.getType();

		List<Variacion> variaciones = new LinkedList<Variacion>();
		variaciones = gson.fromJson(lista, typeVariaciones);

		DaoSession daoSession = DaoMaster.getSession();
		VariacionDao variacionDao = daoSession.getVariacionDao();

		//variacionDao.deleteAll();
		for (int i=0; i<variaciones.size(); i++){
			
			Variacion v = variacionDao.load(variaciones.get(i).getId());
			
			if (v== null){
				
				variacionDao.insertOrReplace(variaciones.get(i));
			}
		}
	}
	
	public List<Variacion> getExistentes() {
		// TODO Auto-generated method stub
		DaoSession daoSession = DaoMaster.getSession();
		VariacionDao variacionDao = daoSession.getVariacionDao();
		
		List<Variacion> variaciones = variacionDao.loadAll();
		return variaciones;
	}
}
