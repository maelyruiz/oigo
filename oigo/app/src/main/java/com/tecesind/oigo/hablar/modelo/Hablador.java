package com.tecesind.oigo.hablar.modelo;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class Hablador implements OnInitListener{

	private TextToSpeech tts;//motor de voz
	private Context context;
	
	public Hablador(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		tts= new TextToSpeech(context, this);
	}
	
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.getDefault());

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				
				Log.e("TTS", "This Language is not supported");
			} else {
				
			}
		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}
	
	
	public void dime_algo(String texto) {
		tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public void destructor(){
		tts.shutdown();
	}
	
}
