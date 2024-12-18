/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecesind.oigo.conversarLSB.modelo;

/**
 *
 * @author Maely
 */
public class BitWise {

    private long x;

    public BitWise() {
        x = 0;
    }

    public void setBit(int pos, int estado) {
        /*habilita o desabilita un determinado bit en un bit*/
        /*estado es para habilitar el bit o lo contrario*/
        long y = 1;
        y <<= pos;  //y=y<<pos-1;
        if (estado == 0) {//desahabilitar
            x &= ~y;
        } else {  ///habilitar
            x |= y;
        }
    }
   //pos= el numero de posiciones que recorre
    //x= vector que almacena los bits
    public int getBit(int pos) {
        int y = 1;
        y <<= pos;
        y &= x;
        y >>= pos;
        return y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public int rotar(int poner, int cant) {
        int bit = getBit(0);
        x = x >> 1;
        setBit(cant - 1, poner);
        return bit;
    }
}
