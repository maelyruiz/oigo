package com.tecesind.oigo.evaluarLSB.controlador;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tecesind.oigo.R;

/**
 * Created by Rosember on 11/17/2015.
 */
public class EvaluarLSB extends AppCompatActivity implements View.OnClickListener{

    private ImageView btnModules;
    private ImageView btnPreguntar;
    private boolean[] modules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion);

        modules = new boolean[19];
        for (int i=0; i<19; i++){
            modules[I]=true;

        }

        btnModules = (ImageView) findViewById(R.id.btnModules);
        btnPreguntar = (ImageView) findViewById(R.id.btnPreguntar);

        btnModulos.setOnClickListener(this);
        btnPreguntar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnModules:

                seleccionarModules().show();
                break;

            case R.id.btnPreguntar:
                iniciarEvaluacion();
                finish();
                break;
            default:
                break;
        }
    }

    private void iniciarEvaluacion() {
        Intent i = new Intent(this, Pregunta.class);
        i.putExtra("modulos", modulos);
        startActivity(i);
    }

    public AlertDialog seleccionarModulos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyAlertTheme);

        final boolean[] checkedItems = new boolean[19];

        for (int i=0; i<19; i++){
            checkedItems[i]=false;

        }
        final CharSequence[] items = new CharSequence[19];

        items[0] = "Alfabeto";
        items[1] = "Numeros";
        items[2] = "Normas de cortesia";
        items[3] = "Miembros de la familia";
        items[4] = "Lugares de la ciudad";
        items[5] = "Deportes";
        items[6] = "Comidas";
        items[7] = "Nombres de los alimentos";
        items[8] = "Trabajos";
        items[9] = "Colores";
        items[10] = "Dias de la semana";
        items[11] = "Meses";
        items[12] = "Tiempos";
        items[13] = "Pronombres";
        items[14] = "Palabras interrogativas";
        items[15] = "Antonimos";
        items[16] = "Sustantivos";
        items[17] = "Departamentos de Bolivia";
        items[18] = "Verbos";

        builder.setTitle("Modulos").setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                // TODO Auto-generated method stub

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                armarLista(checkedItems);
            }


        });


        return builder.create();
    }

    private void armarLista(boolean[] checkedItems) {
        // TODO Auto-generated method stub
        modulos = checkedItems;
    }
}
