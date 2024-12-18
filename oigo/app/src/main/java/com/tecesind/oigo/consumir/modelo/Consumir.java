package com.tecesind.oigo.consumir.modelo;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Rosember on 10/9/2015.
 */
public class Consumir {

    private static final String TAG = Consumir.class.getSimpleName();

    public static final String NAMESPACE = "http://WsOigo/";
    private static final String URL = "http://192.168.43.164:8080/WebServiceOigo/WsOigo?xsd=1";



    public Object consumir( String metodo, Map<String, Object> parametros) throws IOException,
            XmlPullParserException {

        String soapAction=NAMESPACE + metodo;
        // Request
        SoapObject soapObject = new SoapObject(NAMESPACE, metodo);


        if (parametros != null) {
            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                soapObject.addProperty(param.getKey(), param.getValue());
            }
        }

        // Sobre
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        soapEnvelope.setOutputSoapObject(soapObject);

        // Marshal
        MarshalFloat marshalFloat = new MarshalFloat();
        marshalFloat.register(soapEnvelope);

        // Transporte
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(soapAction, soapEnvelope);

            return soapEnvelope.getResponse();

        } catch (IOException e) {
            Log.e(TAG + "io ", e.getMessage());

            return null;

        } catch (XmlPullParserException e) {
            Log.e(TAG + " xmlpull erro", e.getMessage());
            return null;
        }


    }
}
