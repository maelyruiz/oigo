package com.tecesind.oigo.conversarLSB.controlador;

import java.lang.reflect.Type;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecesind.oigo.R;
import com.tecesind.oigo.conversarLSB.modelo.Consts;
import com.tecesind.oigo.conversarLSB.modelo.CryptA51;
import com.tecesind.oigo.conversarLSB.modelo.Msg;
import com.tecesind.oigo.conversarLSB.modelo.PintarMensaje;

import dato.DaoMaster;
import dato.DaoSession;
import dato.Mensaje;
import dato.MensajeDao;


public class GCMIntentService extends  GcmListenerService {

    public static final int NOTIFICATION_ID = 1;
    


//    private static final String TAG = GCMIntentService.class.getSimpleName();
    public static final String TAG="GCMIntentService";

    private NotificationManager notificationManager;
    private Gson mGson;
    private CryptA51 a51 = new CryptA51();

    
    @Override
	public void onMessageReceived(String from, Bundle data) {
		// TODO Auto-generated method stub
		
		
		String message = data.getString("message");
		
		Log.e(from, message);
		processNotification(from, message);
	}


	


//
//    // Put the message into a notification and post it.
//    // This is just one simple example of what you might choose to do with
//    // a GCM message.
@SuppressLint("NewApi")
    private void processNotification(String type, String mensajeReci) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        final String messageValue = mensajeReci;
        
        Type tipoMensajeServer = new TypeToken<Msg>(){}.getType();
        mGson = new Gson();
        Msg mensaje = mGson.fromJson(messageValue, tipoMensajeServer);
        mensaje.setMensajeOriginal(a51.cifrado(mensaje.getMensajeOriginal()));
        mensaje.setMensaje(a51.cifrado(mensaje.getMensaje()));
        mensaje.setTipo(false);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("id", mensaje.getEmisor().getId());
        Mensaje mensajeNuevo = new Mensaje(mensaje.getMensaje(), mensaje.getFecha(),mensaje.getHora(),false,mensaje.getEmisor().getId());
        
        
        DaoSession daoSession = DaoMaster.getSession();
        Boolean abierto = true;
        if (daoSession==null){
        	DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "dboigo", null);
      		SQLiteDatabase db = helper.getWritableDatabase();
      		DaoMaster daoMaster= new DaoMaster(db);
      		daoMaster.newSession();
        	daoSession = DaoMaster.getSession();
        	abierto = false;
        }
        	
        MensajeDao mensajeDao = daoSession.getMensajeDao();
        
        mensajeDao.insertOrReplace(mensajeNuevo);
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        if (abierto) {
			//solo pintar el nuevo mensaje
        	///armo el objeto para pintar
        	
        	PintarMensaje pintarMensaje= new PintarMensaje(ChatActivity.CONTEXTO_CHAT_ACTIVITY);
        	
        	
        	pintarMensaje.execute(mGson.toJson(mensaje));
        	Log.e("ProcessNotification", "Deberia pintar mensaje porque la app esta abierta");
        	
		}else{
			
			 Notification notification = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_icono
	                ).setContentTitle("Oigo").setStyle(
	                new Notification.BigTextStyle().bigText(mensaje.getMensaje())).setAutoCancel(true).setContentText(mensaje.getMensaje()).setContentIntent(contentIntent).build();

	        notification.defaults |= Notification.DEFAULT_SOUND;
	        notification.defaults |= Notification.DEFAULT_VIBRATE;
	        notificationManager.notify(NOTIFICATION_ID, notification);

	        // notify about new push
	        //
	        Intent intentNewPush = new Intent(Consts.NEW_PUSH_EVENT);
	        intentNewPush.putExtra(Consts.EXTRA_MESSAGE, messageValue);
	        LocalBroadcastManager.getInstance(this).sendBroadcast(intentNewPush);
		}

        

        Log.e(TAG, "Broadcasting event " + Consts.NEW_PUSH_EVENT + " with data: " + messageValue);
    }
}