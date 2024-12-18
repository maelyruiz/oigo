package com.tecesind.oigo.visualizarSenha.modelo;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import dato.DaoMaster;
import dato.DaoSession;

/**
 * La clase Analizador es responsable del caso de uso Visualizar Senhas
 *
 * @author rosember
 */
public class Analizador {

    /**
     * Se encarga de traducir la expresion recibida, revisando las palabras, sus
     * variaciones o en su defecto el deletreo de las mismas
     *
     * @param expresion
     * @return Lista de urls de sus videos correspondientes
     */
    public List<String> analizar(String expresion) {

        List<String> urls = new LinkedList<String>();

        expresion = quitarTildes(expresion);
        expresion = expresion.toUpperCase();

        Log.e("analizador",expresion);
        String delimitadores = "[ ]+";

        String[] palabras = expresion.split(delimitadores);
        Palabra palabra = new Palabra();
        Variacion variacion = new Variacion();
        List<Palabra> listaPalabras;
        List<Palabra> letras;
        List<Variacion> listaVariaciones;

        palabras = verificarCompleja(palabras);

        DaoSession daoSession = DaoMaster.getSession();
        PalabraDao palabraDao = daoSession.getPalabraDao();
        listaPalabras = palabraDao.loadAll();
        VariacionDao variacionDao = daoSession.getVariacionDao();
        listaVariaciones = variacionDao.loadAll();


        for (int i = 0; i < palabras.length; i++) {

            if (!esArticulo(palabras[i])) {

                palabra = getPalabra(palabras[i], listaPalabras);

                if (palabra == null) {
                    variacion = getVariacion(palabras[i], listaVariaciones);

                    if (variacion == null) {

                        letras = deletrear(palabras[i], listaPalabras);

                        for (int j = 0; j < letras.size(); j++) {
                            String urlVideoNormal = letras.get(j).getUrlVideoNormal();
                            if (urlVideoNormal != null) {
                                urls.add(letras.get(j).getUrlVideoNormal());
                            }
                        }

                    } else {
                        for (int j = 0; j < listaPalabras.size(); j++) {
                            Log.e("Analizador", "" + variacion.getIdPalabra());
                            if (listaPalabras.get(i).getId() == Integer.parseInt(variacion
                                    .getIdPalabra().toString())) {
                                urls.add(listaPalabras.get(j)
                                        .getUrlVideoNormal());
                            }
                        }

                    }
                } else {
                    Log.e("Analizador ", palabra.getUrlVideoNormal());
                    urls.add(palabra.getUrlVideoNormal());
                }
            }

        }
        return urls;

    }


    /**
     * Recibe una palabra y la deletrea
     *
     * @param palabra
     * @param listaPalabras
     * @return lista de las letras en forma de objeto palabra
     */
    private List<Palabra> deletrear(String palabra, List<Palabra> listaPalabras) {
        // TODO Auto-generated method stub
        palabra = palabra.trim();
        List<Palabra> letras = new LinkedList<Palabra>();

        for (int i = 0; i < palabra.length(); i++) {

            letras.add(getPalabra(palabra.charAt(i) + "", listaPalabras));
        }
        return letras;

    }

    /**
     * Recibe una cadena y revisa en la lista de variaciones
     *
     * @param palabra
     * @param listaVariaciones
     * @return la palabra encontrada en forma de objeto variacion null en caso
     * de que no la encuentre
     */

    private Variacion getVariacion(String palabra,
                                   List<Variacion> listaVariaciones) {
        // TODO Auto-generated method stub
        for (int i = 0; i < listaVariaciones.size(); i++) {
            if (listaVariaciones.get(i).getNombre().equals(palabra))
                return listaVariaciones.get(i);

        }
        return null;
    }

    /**
     * Recibe una cadena y la busca en la lista de palabras
     *
     * @param palabra
     * @param listaPalabras
     * @return la palabra en forma de objeto Palabra null en caso de que no este
     * en la lista
     */
    private Palabra getPalabra(String palabra, List<Palabra> listaPalabras) {
        // TODO Auto-generated method stub
        Palabra p = null;
        for (int i = 0; i < listaPalabras.size(); i++) {
            if (listaPalabras.get(i).getNombre().equals(palabra)) {
                Log.e("getPalabra", "Se encontro la palabra en el id " + i);
                p = listaPalabras.get(i);
                break;
            }
        }
        return p;
    }

    /**
     * Revisa si es una palabra que no puede ser traducida a LSB
     *
     * @param palabra
     * @return true si debe ser eliminada false en caso contrario
     */
    public boolean esArticulo(String palabra) {
        // TODO Auto-generated method stub
        String[] articulo = {"EL", "LA", "LOS", "LAS", "UN", "UNA", "UNOS",
                "UNAS", "LO", "AL", "DEL"};

        for (int i = 0; i < articulo.length; i++) {
            if (articulo[i].equals(palabra)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Verifica dentro de todo el vector si hay palabras que se encuentran
     * unidas en el LSB y las une si las hay
     *
     * @param pal
     * @return El mismo vector de cadenas recibido pero con las palabras
     * compuestas ya unidas
     */
    private String[] verificarCompleja(String[] pal) {
        // TODO Auto-generated method stub
        Log.e("verificarCompleja", "entrada");
        String[] palabras = pal;
        DaoSession daoSession = DaoMaster.getSession();
        List<Compuesta> compuestas;
        CompuestaDao compuestaDao = daoSession.getCompuestaDao();
        compuestas = compuestaDao.loadAll();
        int[] indices;
        for (int i = 0; i < palabras.length; i++) {
            Log.e("verificarCompleja", "palabras " + palabras.length);


            indices = lugar(palabras[i], compuestas);
            int cont = 0;
            boolean sw = false;

            Log.e("verificarCompleja", "indice " + indices[0]);
            while ((indices[cont] > -1) && (compuestas.get(indices[cont]).getOrden() == 1) && cont < indices.length && !sw) {
                int l = 1;
                String compuesta = palabras[i];
                int k = mayorCompuesta(compuestas.get(indices[cont]).getIdPalabra(),
                        compuestas);
                sw = true;
                while (l < k) {
                    if (!(palabras[i + l].equals(siguiente(compuestas,
                            compuestas.get(indices[cont]).getIdPalabra(), l)))) {
                        sw = false;

                    } else compuesta = compuesta + palabras[i + l];

                    l++;
                }

                if (sw) {
                    palabras[i] = compuesta;
                    palabras = nuevaOracion(palabras, i, k);

                }
                cont++;
            }

        }

        return palabras;
    }

    /**
     * Recibe una palabra que es parte de una compuesta y revisa en la lista la
     * que sigue en la composicion
     *
     * @param compuestas
     * @param idPalabra
     * @param orden
     * @return la palabra que sigue en la compuesta
     */
    private String siguiente(List<Compuesta> compuestas, int idPalabra,
                             int orden) {
        Log.e("Analizador", "Siguiente");
        for (int i = 0; i < compuestas.size(); i++) {
            if ((compuestas.get(i).getIdPalabra() == idPalabra)
                    && (compuestas.get(i).getOrden() == orden + 1)) {
                return compuestas.get(i).getNombre();
            }
        }
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Reduce del vector las palabras que fueron excluidas por ser composicion
     * de otras
     *
     * @param palabras
     * @param indice
     * @param catidad_de_palabra
     * @return un nuevo vector con las palabras ya compuestas
     */
    private String[] nuevaOracion(String[] palabras, int indice,
                                  int catidad_de_palabra) {

        Log.e("Analizador", "NuevaOracion " + indice + " " + catidad_de_palabra + " " + palabras.length);
        String[] nuevaCadena = new String[palabras.length
                - (catidad_de_palabra - 1)];

        int numeroqueQuedo = 0;
        for (int i = 0; i <= indice; i++) {
            Log.e("Analizador", "NuevaOracion For 1");
            nuevaCadena[i] = palabras[i];
            numeroqueQuedo = i;
        }

        for (int i = indice + catidad_de_palabra; i < palabras.length; i++) {
            Log.e("Analizador", "NuevaOracion For 2");
            numeroqueQuedo++;
            nuevaCadena[numeroqueQuedo] = palabras[i];

        }
        return nuevaCadena;
    }

    /**
     * Verifica cuantas palabras componen la palabra de un id especifico
     *
     * @param idPalabra
     * @param lista
     * @return el orden mayor que compone ese idPalabra
     */
    private int mayorCompuesta(int idPalabra, List<Compuesta> lista) {
        // TODO Auto-generated method stub

        int k = 1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdPalabra() == idPalabra
                    && lista.get(i).getOrden() > k)
                k = lista.get(i).getOrden();

        }
        return k;
    }

    /**
     * Revisa si una cadena se encuentra en la lista de compuestas
     *
     * @param palabra
     * @param compuestas
     * @return el orden de la composicion si esta en la lista -1 en el caso por
     * defecto
     */
    private int[] lugar(String palabra, List<Compuesta> compuestas) {
        // TODO Auto-generated method stub

        int[] vector = new int[10];
        Log.e("verificarCompleja", "compuestas " + compuestas.size());
        int cont = 0;
        for (int i = 0; i < compuestas.size(); i++) {

            Log.e("verificarCompleja", "compuestas " + compuestas.get(i).getNombre());
            if (compuestas.get(i).getNombre().equals(palabra)) {
                vector[cont] = i;
                cont++;
            }

        }

        while (cont < 10) {
            vector[cont] = -1;
            cont++;
        }

        return vector;
    }

    /**
     * Quita los acentos ortograficos que no son utilizados en la traduccion
     *
     * @param cadena
     * @return la misma cadena pero sin tildes
     */
    private String quitarTildes(String cadena) {

        cadena = cadena.replaceAll("á", "a");
        cadena = cadena.replaceAll("é", "e");
        cadena = cadena.replaceAll("í", "i");
        cadena = cadena.replaceAll("ó", "o");
        cadena = cadena.replaceAll("ú", "u");

        return cadena;
    }
}
