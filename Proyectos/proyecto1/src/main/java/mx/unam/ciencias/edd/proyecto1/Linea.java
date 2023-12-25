package mx.unam.ciencias.edd.proyecto1;

import java.text.Normalizer;
/**
 * <p>Clase para normalizar Lineas</p>
 */

public class Linea implements Comparable<Linea>{


    /*El string original */
    private String linea;

    /*Construye una Linea a partir del string original*/
    public Linea(String linea){
        this.linea=linea;
    }

    /**
     * Normaliza el string original, es decir, devuelve un string en
     * minusculas, sin acentos, sin espacios en blanco, sin caracteres
     * especiales.
     * @return El string original en su version normalizada
     */
    public String getLinea(){
        return Normalizer.normalize(linea, Normalizer.Form.NFD).toLowerCase().replaceAll("[^a-z]","");
        
    }

    @Override public String toString(){
        return linea;
    }

    /**
     * Compara los strings en su version normalizada, para
     * su posterior uso con mergesort.
     */
    @Override
    public int compareTo(Linea linea) {
       return this.getLinea().compareTo(linea.getLinea());
    }


}
