package com.tecesind.oigo.armarOracionLSB.modelo;

import com.tecesind.oigo.R;

/**
 * Created by Rosember on 12/2/2015.
 */
public class Paquete {

    public String nombre;
    public int iddrawable;

    public Paquete(String nombre, int iddrawable) {
        this.nombre = nombre;
        this.iddrawable = iddrawable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIddrawable() {
        return iddrawable;
    }

    public void setIddrawable(int iddrawable) {
        this.iddrawable = iddrawable;
    }

    public int getId() {
        return nombre.hashCode();
    }

    public static Paquete[] ITEMS = {
            new Paquete("Estoy / yo", R.mipmap.ic_estoy),
            new Paquete("Tiempo", R.mipmap.ic_tiempo),
            new Paquete("Acciones",R.mipmap.ic_acciones),
            new Paquete("Preguntas",R.mipmap.ic_pregunta)
    };

    /**
     * Obtiene item basado en su identificador
     *
     * @param id identificador
     * @return Coche
     */
    public static Paquete getItem(int id) {
        for (Paquete item : ITEMS) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
