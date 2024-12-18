package com.tecesind.oigo.aprenderLSB.modelo;

import java.util.List;

import android.util.Log;

import com.tecesind.oigo.actualizarVocabulario.modelo.NegocioModulo;
import com.tecesind.oigo.actualizarVocabulario.modelo.NegocioPalabra;

public class NegocioTutor {

	public List<Palabra> getLista(int modulo){
			
		NegocioModulo negocioModulo = new NegocioModulo();
		NegocioPalabra negocioPalabra = new NegocioPalabra();
		Modulo m = negocioModulo.getModulo(modulo);
		if (m!=null)
		return negocioPalabra.getAllModulo(Integer.parseInt(m.getId().toString()));
		else{
			Log.e("NegocioTutor.class", "No hay ningun modulo en la BD");
			return null;
			
		}
			
	}
}
