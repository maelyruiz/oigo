package com.tecesind.oigo.armarOracionLSB.modelo;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import dato.DaoMaster;
import dato.DaoSession;
import dato.Notificacion;
import dato.NotificacionDao;


public class NegocioFrase {

	Gson gson;

	public NegocioFrase(){
		gson = new Gson();

	}

	public List<Notificacion> getFrase(int paquete) {
		// TODO Auto-generated method stub
		
		DaoSession daoSession = DaoMaster.getSession();
		NotificacionDao notificacionDao = daoSession.getNotificacionDao();
		List<Notificacion> listaNotificacion = notificacionDao.loadAll();
		Log.e("NegocioFrase", "Frases del paquete: "+paquete+ "  "+ listaNotificacion.size());
		for (int i = 0; i< listaNotificacion.size(); i++){
			Log.e("NegocioFrase", listaNotificacion.get(i).getIdPaquete()+" Notificacion Paquete");
			if (listaNotificacion.get(i).getIdPaquete()!= paquete){
				listaNotificacion.remove(i);
				i--;
			}
		}
		
		return listaNotificacion;
	}

	public void agregar(String lista){

		Type typeFrases = new TypeToken<List<Notificacion>>() {
		}.getType();

		List<Notificacion> notificacions = new LinkedList<Notificacion>();
		notificacions = gson.fromJson(lista, typeFrases);

		DaoSession daoSession = DaoMaster.getSession();
		NotificacionDao notificacionDao = daoSession.getNotificacionDao();

		for (int i = 0; i< notificacions.size(); i++){

			Notificacion m = notificacionDao.load(notificacions.get(i).getId());

			if (m== null){

				notificacionDao.insertOrReplace(notificacions.get(i));
			}
		}
	}



	public List<Notificacion> getExistentes() {
		// TODO Auto-generated method stub
		DaoSession daoSession = DaoMaster.getSession();
		NotificacionDao notificacionDao = daoSession.getNotificacionDao();
		List<Notificacion> listaNotificacions = notificacionDao.loadAll();
		return listaNotificacions;
	}

}
