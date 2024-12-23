package com.tecesind.oigo.actualizarVocabulario.modelo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DetectorConexion {

	private Context mContext;
	
	public DetectorConexion(Context context){
		
		mContext = context;
	}
	
	public boolean hayConexionInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null){
			
			NetworkInfo[] info= connectivity.getAllNetworkInfo();
			if (info!=null){
				
				for (int i=0; i<info.length; i++){
					
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
						return true;
				}
			}
		}
		
		return false;
	}
}
