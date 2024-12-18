package com.tecesind.oigo.actualizarPerfil.modelo;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Rosember on 11/23/2015.
 */
public class Imagen {

    public Bitmap redimencionar(Bitmap bitmap,float newWidth,float newHeigth){

        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        float scaleWidth=((float)newWidth)/width;
        float scaleHeigth=((float)newHeigth)/height;

        Matrix matrix= new Matrix();

        matrix.postScale(scaleWidth,scaleHeigth);
        return Bitmap.createBitmap(bitmap,0,0,width,height,matrix,false);
    }
}
