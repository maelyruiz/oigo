package com.tecesind.oigo.armarOracionLSB.controlador;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.tecesind.oigo.R;

/**
 * PERMINTE AL SORDO REALIZAR UN TEXTO MEDIANTE UNA SITUACION EN LA QUE SE
 * ENCUENTRE EJ. SER/ESTAR , CASA , COLEGIO , TRABAJO
 * 
 * @author rosember
 *
 */
public class ControlPaquete extends AppCompatActivity implements OnClickListener {

	private ImageButton ibEstoy;
	private ImageButton ibCasa;
	private ImageButton ibColegio;
	private ImageButton ibTrabajo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paquete);
		ibEstoy = (ImageButton) findViewById(R.id.IBEstoy);
		ibCasa = (ImageButton) findViewById(R.id.IBCasa);
		ibColegio = (ImageButton) findViewById(R.id.IBColegio);
		ibTrabajo = (ImageButton) findViewById(R.id.IBTrabajo);
		ibEstoy.setOnClickListener(this);
		ibCasa.setOnClickListener(this);
		ibColegio.setOnClickListener(this);
		ibTrabajo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent i = new Intent(this, ControlFrase.class);
		switch (v.getId()) {
		case R.id.IBEstoy:
			i.putExtra("paquete", 1);
			startActivityForResult(i, 0);
			break;

		case R.id.IBCasa:
			i.putExtra("paquete", 2);
			startActivityForResult(i,0);
			
			break;

		case R.id.IBColegio:
			i.putExtra("paquete", 3);
			startActivityForResult(i, 0);
			
			break;

		case R.id.IBTrabajo:
			i.putExtra("paquete", 4);
			startActivityForResult(i, 0);
			
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Intent data1 = new Intent();
			data1.setData(Uri.parse(data.getDataString()));
			setResult(resultCode,data1);
			finish();
		}

	}
}
