/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecesind.oigo.conversarLSB.modelo;

/**
 *
 * @author MaelyR
 */
public class LFSR {

    int[] Polinomio;
    BitWise seed;
    int controlBit;
    int max;

    public LFSR(int max, int[] Poly, long seed, int control) {

        this.max = max;
        Polinomio = new int[max];
        Polinomio = Poly;
        this.seed = new BitWise();
        this.seed.setX(seed);
        controlBit = control;
    }

    public int getControlBit() {

        return seed.getBit(max - controlBit - 1);
    }

    public int mover() {

        int x = Polinomio[0];
        int i = 0;
        int bit = 0;
        while (x != -1 && i < max) {

            if (i == 0) {
                bit = seed.getBit(max - x);
            } else {
                bit = bit ^ seed.getBit(max - x);
            }

            i++;
            x = Polinomio[i];
        }
        return seed.rotar(bit, max);
    }


}
