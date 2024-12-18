package com.tecesind.oigo.actualizarVocabulario.modelo;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dato.DaoMaster;
import dato.DaoSession;

public class NegocioCompuesta {

private Gson gson;
	
	public NegocioCompuesta(){
		
		gson = new Gson();
	}

	public void agregar(String lista){
		
		Type typeCompuestas = new TypeToken<List<Compuesta>>() {
		}.getType();

		List<Compuesta> compuestas = new LinkedList<Compuesta>();
		compuestas = gson.fromJson(lista, typeCompuestas);

		DaoSession daoSession = DaoMaster.getSession();
		CompuestaDao compuestaDao = daoSession.getCompuestaDao();

		for (int i=0; i<compuestas.size(); i++){
			
			Compuesta c = compuestaDao.load(compuestas.get(i).getId());
			
			if (c== null){
				
				compuestaDao.insertOrReplace(compuestas.get(i));
			}
		}
	}

	public List<Compuesta> getExistentes() {
		// TODO Auto-generated method stub
		DaoSession daoSession = DaoMaster.getSession();
		CompuestaDao compuestaDao = daoSession.getCompuestaDao();

		//compuestaDao.deleteAll();

		List<Compuesta> compuestas = compuestaDao.loadAll();
		return compuestas;
	}
}
