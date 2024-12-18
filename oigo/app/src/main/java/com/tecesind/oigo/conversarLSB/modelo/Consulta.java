package com.tecesind.oigo.conversarLSB.modelo;

import java.util.LinkedList;
import java.util.List;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class Consulta {

	private Context context;
	
	public Consulta(Context context) {
		// TODO Auto-generated constructor stub
		this.context= context;
	}
	
	/**
	 * Octiene los contactos del celular 
	 * @return una lista de Enteros con el telefono de cada contacto
	 * y una lista vacia en caso no tratarse del mismo.
	 */
	public List<String> getContactos(){

		//verificar si tiene permiso


		// Query: contacts with phone shorted by name
		Cursor cursor = context.getContentResolver().query(
				Data.CONTENT_URI,
				new String[]{Data._ID, Data.DISPLAY_NAME, Phone.NUMBER,
						Phone.TYPE},
				Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "' AND "
						+ Phone.NUMBER + " IS NOT NULL", null,
				Data.DISPLAY_NAME + " ASC");
		
		
		List<String> listUsuario= new LinkedList<String>();	
		
		
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			
			int phoneNumber = cursor.getColumnIndex(Phone.NUMBER);
			String name = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
			
			Log.e("Consulta", ""+ cursor.getString(phoneNumber));
			
			listUsuario.add(cursor.getString(phoneNumber));
		}
		cursor.close();
		Log.e("Consulta", ""+listUsuario.size());
		
		return listUsuario;
	}
}
