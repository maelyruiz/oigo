package com.tecesind.oigo.conversarLSB.modelo;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecesind.oigo.conversarLSB.controlador.ChatActivity;


public class PintarMensaje extends AsyncTask<String, Void, String> {

	
	private Context context;


	public PintarMensaje(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return params[0];
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		Gson gson = new Gson();
		Type tipoMensajeServer = new TypeToken<Msg>() {
		}.getType();
		
		Msg mensaje = gson.fromJson(result.toString(), tipoMensajeServer);

		long date = System.currentTimeMillis();
		SimpleDateFormat formatohora = new SimpleDateFormat("HH:mm");
		String horaString = formatohora.format(date);

		SimpleDateFormat formatofecha = new SimpleDateFormat("d/MM/yyyy");
		String fechaString = formatofecha.format(date);
		
		ChatActivity.pintar(mensaje);
		

	}
}
