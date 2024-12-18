package com.tecesind.oigo.actualizarVocabulario.modelo;

import android.util.Log;

import com.google.gson.Gson;
import com.tecesind.oigo.consumir.modelo.Consumir;

import org.ksoap2.serialization.SoapPrimitive;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rosember on 12/3/2015.
 */
public class AdministradorWS {

    private static final String METHOD_NAME_GET_FALTANTES= "getFaltantes";
    private Consumir consumidor;
    private Gson gson;

    public AdministradorWS() {
        gson = new Gson();
        consumidor = new Consumir();
    }


    public String getAllFaltantes(List<String> listaAll) {

        HashMap<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("listaAll", gson.toJson(listaAll));
        SoapPrimitive result = null;

        try {
            result = (SoapPrimitive) consumidor.consumir(METHOD_NAME_GET_FALTANTES, parametros);

            Log.i("administradorWS",
                    "se procedio a realizar el consumir datos de la Web service");




            if (result!=null) {
                Log.e("admgetCompuestas", "DATOS:  " + result.toString());
                return result.toString();
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("admgetCompuestas", "No se pudo realizar bien el consumir  "+e.getMessage());
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;

    }

}
