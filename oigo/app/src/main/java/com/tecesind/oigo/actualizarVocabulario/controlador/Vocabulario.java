package com.tecesind.oigo.actualizarVocabulario.controlador;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tecesind.oigo.ManejadorTabs;
import com.tecesind.oigo.R;
import com.tecesind.oigo.StaticOigo;
import com.tecesind.oigo.actualizarVocabulario.modelo.Actualizar;
import com.tecesind.oigo.actualizarVocabulario.modelo.DetectorConexion;


/**
 * 
 * @author rosember
 *
 */

public class Vocabulario extends AppCompatActivity {

	private DetectorConexion detectorConexion;
	private ProgressBar spinner;
	private Actualizar actualizar;
	private Conector conector;
	private Boolean first;
	private Context contexto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actualizar);
		Bundle bundle = getIntent().getExtras();
		first = bundle.getBoolean(StaticOigo.ACTUALIZAR_VOCABULARIO);
		//first = true;
		actualizarVocabulario();
		contexto=this;

	}

	public void finalizar(){
		Intent i = new Intent().setClass(getApplicationContext(), ManejadorTabs.class);
		startActivity(i);
		spinner.setVisibility(View.INVISIBLE);
		finish();
	}

	private void actualizarVocabulario() {
		detectorConexion = new DetectorConexion(getApplicationContext());

		spinner = (ProgressBar) findViewById(R.id.progressBar1);

		if (detectorConexion.hayConexionInternet()) {

			actualizar = new Actualizar(getApplicationContext());

			conector= new Conector();
			conector.execute();

		} else {
			Toast.makeText(this, "No hay Conexion a Internet",
					Toast.LENGTH_LONG).show();
			finalizar();
		}
	}

	public class Conector extends AsyncTask<Void, Void, Void>{

		private boolean descarga=false;
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (actualizar.iniciar(first)) {
				descarga=true;
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			spinner.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

				Toast.makeText(contexto,"No se pudo actualizar el vocabulario", Toast.LENGTH_LONG).show();
				finalizar();
			
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);


			spinner.setVisibility(View.VISIBLE);
		}
		
		
		
	}

}
