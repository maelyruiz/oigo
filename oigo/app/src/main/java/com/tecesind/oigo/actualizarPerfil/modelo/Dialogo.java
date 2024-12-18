package com.tecesind.oigo.actualizarPerfil.modelo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.tecesind.oigo.actualizarPerfil.controlador.DatosUsuario;
import com.tecesind.oigo.consumir.modelo.Consumir;

import org.ksoap2.serialization.SoapPrimitive;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Rosember on 10/12/2015.
 */
public class Dialogo extends DialogFragment {
    Consumir consumir;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Important");
        alertDialog.setMessage("Enter the number we sent you by SMS");

        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setBackgroundColor(Color.CYAN);
        input.setTextColor(Color.BLACK);
        alertDialog.setView(input);


        alertDialog.setPositiveButton("Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String password = input.getText().toString();
                        Toast.makeText(getActivity(), password, Toast.LENGTH_SHORT).show();

                        if (!password.equals("")){

                            ((DatosUsuario)getActivity()).postivo(password);
                        }


                    }
                });

        alertDialog.setNegativeButton("Modify",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.setNeutralButton("Forward PIN",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //AndroidSendSMS androidSendSMS= new AndroidSendSMS();
                //androidSendSMS.sendSMS("78482228","Antibabel te ha enviado un mensaje");
            }
        });


       return alertDialog.show();
    }





}
