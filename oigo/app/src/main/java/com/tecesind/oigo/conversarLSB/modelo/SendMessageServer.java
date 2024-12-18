package com.tecesind.oigo.conversarLSB.modelo;

import java.io.IOException;
import java.util.HashMap;

import org.ksoap2.serialization.SoapPrimitive;
import org.xmlpull.v1.XmlPullParserException;


import android.os.AsyncTask;
import android.util.Log;

import com.tecesind.oigo.consumir.modelo.Consumir;

public class SendMessageServer extends AsyncTask<String, Void, Void>{

	private Consumir consumir;
	 
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		
        HashMap<String,Object>parametro= new HashMap<String, Object>();
        parametro.put("mensaje",params[0].toString());

        consumir= new Consumir();
        SoapPrimitive result=null;
        try {
            result= (SoapPrimitive) consumir.consumir("sendMessage",parametro);

            if (result!= null){

            	Log.e("SendMenssageServer", "Recibio el mensaje el server" + result.toString());
              
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
		
		
	}

}
