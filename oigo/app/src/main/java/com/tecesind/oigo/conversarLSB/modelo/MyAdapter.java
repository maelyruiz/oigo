package com.tecesind.oigo.conversarLSB.modelo;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.tecesind.oigo.R;
import com.tecesind.oigo.conversarLSB.controlador.ChatActivity;

import dato.Usuario;

public class MyAdapter extends BaseAdapter implements OnClickListener {

	private static final String TAG="My Adapter";
	
	private Context context;
	private LayoutInflater inflater;

	private List<Usuario> listaUsuario;
	private BitMapString bitMapString;

	public MyAdapter(Context context, List<Usuario> listUsuario) {

		this.context= context;
		inflater = LayoutInflater.from(context);
		this.listaUsuario = listUsuario;
		bitMapString= new BitMapString();
	
	}

	@Override
	public int getCount() {
		return listaUsuario.size();
	}

	@Override
	public Object getItem(int i) {
		return listaUsuario.get(i);
	}

	

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {

		View v = view;
		ImageView picture;
		TextView name;

		if (v == null) {
			v = inflater.inflate(R.layout.grid_item, viewGroup, false);
			v.setTag(R.id.picture, v.findViewById(R.id.picture));
			v.setTag(R.id.text, v.findViewById(R.id.text));
		}

		picture = (ImageView) v.getTag(R.id.picture);
		name = (TextView) v.getTag(R.id.text);

	
		Usuario usuario = (Usuario) getItem(i);


		if (usuario.getFoto().equals("")) {
			//si la imagen es vacia coloco una imagen por default solo para mostrar
			picture.setImageResource(R.mipmap.img_usuario);
		}else{
			picture.setImageBitmap(bitMapString.StringToBitMap(usuario.getFoto()));
		}
		
		name.setText(usuario.getNombre());
		
		picture.setId(Integer.parseInt(usuario.getId().toString()));
		picture.setOnClickListener(this);
		

		return v;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub


		verConversacion(v.getId());

		
	}

	private void verConversacion(Integer x) {

		Intent i = new Intent(context,ChatActivity.class);

		i.putExtra("id", x.longValue());

		Log.e(TAG, "Se supone el id del contacto:  "+x.longValue());
		context.startActivity(i);
	}


}