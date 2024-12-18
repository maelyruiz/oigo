package com.tecesind.oigo.armarOracionLSB.modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import dato.DaoMaster;
import dato.DaoSession;
import dato.Grupo;
import dato.GrupoDao;


public class NegocioGrupo {

	private Gson gson;
	public NegocioGrupo(){
		gson = new Gson();
	}
	public List<Grupo> getGrupo(long id) {
		DaoSession daoSession = DaoMaster.getSession();
		GrupoDao grupoDao = daoSession.getGrupoDao();
		GrupoFraseDao grupoFraseDao = daoSession.getGrupoFraseDao();
		List<GrupoFrase> listaGrupoFrase = grupoFraseDao.loadAll();
		List<Grupo> listaGrupo = grupoDao.loadAll();
		
		for (int i= 0; i<listaGrupo.size(); i++){
			
			if (! pertenece (listaGrupo.get(i), id, listaGrupoFrase)){
				listaGrupo.remove(i);
				i--;
			}
		}
		
		
		return ordenar(listaGrupo, listaGrupoFrase); 
		// TODO Auto-generated method stub

	}

	

	private List<Grupo> ordenar(List<Grupo> listaGrupo,
			List<GrupoFrase> listaGrupoFrase) {
		// TODO Auto-generated method stub
		
		if (listaGrupo!= null){
			List<Grupo> nuevaList = new LinkedList<Grupo>();
			int contador =1;
			while(nuevaList.size()!= listaGrupo.size()){
				for (int i= 0; i< listaGrupoFrase.size(); i++){
					if (listaGrupoFrase.get(i).getOrden()==contador){
						for (int j=0; j<listaGrupo.size(); j++){
							if (listaGrupo.get(j).getId() == Integer.parseInt(listaGrupoFrase.get(i).getIdGrupo().toString())){
								nuevaList.add(listaGrupo.get(j));
								contador++;
								i= listaGrupoFrase.size();
								j=listaGrupo.size();
							}
						}
						
					}
					
				}
				
			}
			
			return nuevaList;
		}
		return null;
	}



	private boolean pertenece(Grupo grupo, long id,
			List<GrupoFrase> listaGrupoFrase) {
		
		for (int i=0; i< listaGrupoFrase.size(); i++){
			if ((listaGrupoFrase.get(i).getIdGrupo() == Integer.parseInt(grupo.getId().toString()))&&(listaGrupoFrase.get(i).getIdFrase()==id))
				return true;
			
		}
		// TODO Auto-generated method stub
		return false;
	}


	public void agregar(String lista){

		Type typeGrupos = new TypeToken<List<Grupo>>() {
		}.getType();

		List<Grupo> grupos = new LinkedList<Grupo>();
		grupos = gson.fromJson(lista, typeGrupos);

		DaoSession daoSession = DaoMaster.getSession();
		GrupoDao grupoDao = daoSession.getGrupoDao();

		for (int i=0; i<grupos.size(); i++){

			Grupo m = grupoDao.load(grupos.get(i).getId());

			if (m== null){

				grupoDao.insertOrReplace(grupos.get(i));
			}
		}
	}



	public List<Grupo> getExistentes() {
		// TODO Auto-generated method stub
		DaoSession daoSession = DaoMaster.getSession();
		GrupoDao grupoDao = daoSession.getGrupoDao();
		List<Grupo> listaGrupos = grupoDao.loadAll();
		return listaGrupos;
	}



}
