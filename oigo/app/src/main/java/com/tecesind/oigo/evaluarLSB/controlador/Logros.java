package com.tecesind.oigo.evaluarLSB.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tecesind.oigo.R;

import java.util.List;

import dato.DaoMaster;
import dato.DaoSession;
import dato.Usuario;

/**
 * Created by Rosember on 11/24/2015.
 */
public class Logros extends AppCompatActivity implements View.OnClickListener{

    private TextView tvNota;
    private TextView tvMejor;
    private Button btnEvaluar;
    private Button btnAprender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);
        Bundle bundle = getIntent().getExtras();

        int nota = bundle.getInt("nota");

        tvNota = (TextView) findViewById(R.id.tvNota);
        tvMejor = (TextView) findViewById(R.id.tvMejor);
        btnAprender = (Button) findViewById(R.id.btnVAprender);
        btnEvaluar = (Button) findViewById(R.id.btnVEvaluar);

        btnAprender.setOnClickListener(this);
        btnEvaluar.setOnClickListener(this);
        mostrarLogros(nota);

    }

    private void mostrarLogros(int nota) {

        tvNota.setText(nota+"");
        DaoSession daoSession= DaoMaster.getSession();

        List<Usuario> users = daoSession.getUsuarioDao().loadAll();
        if(users.isEmpty()){
            tvMejor.setText("Inicia sesion para ver tu puntuacion anterior");
        }else{

            if(users.get(0).getNotaMax()<nota) {
                users.get(0).setNotaMax(nota);
                tvMejor.setText("Es tu mejor puntuacion hasta ahora");
            }else
                tvMejor.setText("Mejor puntuacion: "+users.get(0).getNotaMax());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnVAprender:
                finish();
                break;
            case R.id.btnVEvaluar:
                Intent intent = new Intent(this, EvaluarLSB.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
