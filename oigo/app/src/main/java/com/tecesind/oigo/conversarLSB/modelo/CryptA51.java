/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecesind.oigo.conversarLSB.modelo;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author MaelyR
 */
public class CryptA51 {

    LFSR lfsr1;
    LFSR lfsr2;
    LFSR lfsr3;
    LFSR lfsr4;
    int cant = 4;
    int max = 5;
    int[][] Polinomios = new int[cant][max];
    int[] control = new int[cant];
    int[] grado = new int[cant];
    long[] seed = new long[cant];
    BitWise[] LFSR = new BitWise[cant];

    public CryptA51() {
        llenarCeros();
        cargarPolinomios();
        cargarSeeds("Oigo2015");
    }

    public String cifrado(String mensaje){

        lfsr1 = new LFSR(grado[0], Polinomios[0], seed[0], control[0]);
        lfsr2 = new LFSR(grado[1], Polinomios[1], seed[1], control[1]);
        lfsr3 = new LFSR(grado[2], Polinomios[2], seed[2], control[2]);
        lfsr4 = new LFSR(grado[3], Polinomios[3], seed[3], control[3]);
        char[] msg= mensaje.toCharArray();
        char[] vacio = new char[msg.length];
        for(int i=0; i<msg.length; i++){
            byte b = generar();
            vacio[i] = (char)((msg[i]^b));

        }


        return String.valueOf(vacio);

    }



    private void llenarCeros() {
        for (int i = 0; i < cant; i++) {

            for (int j = 0; j < max; j++) {

                Polinomios[i][j] = -1;
            }
        }
    }

    private void cargarPolinomios() {
        control[0] = 9;
        Polinomios[0][0] = 14;
        Polinomios[0][1] = 17;
        Polinomios[0][2] = 18;
        Polinomios[0][3] = 19;
        grado[0] = 19;

        control[1] = 11;
        Polinomios[1][0] = 21;
        Polinomios[1][1] = 22;
        grado[1] = 22;

        control[2] = 11;
        Polinomios[2][0] = 8;
        Polinomios[2][1] = 21;
        Polinomios[2][2] = 22;
        Polinomios[2][3] = 23;
        grado[2] = 23;

        control[3] = 9;
        Polinomios[3][0] = 63;
        Polinomios[3][1] = 64;
        grado[3] = 64;

    }

    private byte generar() {

        byte b = 0;
        for (int i = 7; i >= 0; i--) {
            int nuevo = 0;
            int F = (lfsr1.getControlBit() * lfsr2.getControlBit()) ^ (lfsr1.getControlBit() * lfsr3.getControlBit()) ^ (lfsr2.getControlBit() * lfsr3.getControlBit());
            int mueve = 0;

            if (lfsr1.getControlBit() == F) {
                nuevo = lfsr1.mover();
                mueve++;
            }

            if (lfsr2.getControlBit() == F) {

                if (mueve == 0) {

                    nuevo = lfsr2.mover();
                    mueve++;
                } else {

                    nuevo = nuevo ^ lfsr2.mover();
                }

            }

            if (lfsr3.getControlBit() == F) {

                if (mueve == 0) {

                    nuevo = lfsr3.mover();
                    mueve++;
                } else {

                    nuevo = nuevo ^ lfsr3.mover();
                }
            }

            nuevo = nuevo ^ lfsr4.mover();
            byte y = 1;
            y <<= i - 1;  //y=y<<pos-1;
            if (nuevo == 0) {//desahabilitar
                b &= ~y;
            } else {  ///habilitar
                b |= y;
            }

        }
        return b;
    }

    private void cargarSeeds(String x) {

        byte[] p = x.getBytes();
        long y = 0;
        BitWise b = new BitWise();
        int z;
        for (int k = 1; k <= 8; k++) {

            for (int s = 0; s < 8; s++) {
                z = 1;
                z <<= s;
                z &= p[k-1];
                z >>= s;

                b.setBit(s * k, z);

            }
        }
        y=b.getX();
        b = new BitWise();
        BitWise c = new BitWise();
        c.setX(y);
        for (int i = 0; i < 19; i++) {

            b.setBit(i, c.getBit(i));

        }

        seed[0] = b.getX();
        b = new BitWise();
        int j = 0;
        for (int i = 19; i < 19 + 22; i++) {

            b.setBit(j, c.getBit(i));

            j++;
        }
        seed[1] = b.getX();

        b = new BitWise();
        j = 0;
        for (int i = 19 + 22; i < 19 + 22 + 23; i++) {

            b.setBit(j, c.getBit(i));
            j++;
        }
        seed[2] = b.getX();

        seed[3] = y;
    }

}

/**
 * @param args the command line arguments
 */
