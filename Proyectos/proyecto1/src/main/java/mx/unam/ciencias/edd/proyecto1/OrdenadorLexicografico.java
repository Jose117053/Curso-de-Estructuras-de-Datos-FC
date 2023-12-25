package mx.unam.ciencias.edd.proyecto1;
import mx.unam.ciencias.edd.Lista;

public class OrdenadorLexicografico {


    /**
     * Regresa una copia de la lista de líneas recibida, pero ordenada de manera
     * lexicográfica.Si la bandera -o es true, no imprirá nada, de lo contrario
     * imprimirá la lista ordenada.
     * @param lista la lista a ordenar.
     * @return una copia ordenada de la lista de líneas.
     */

    static Lista<Linea> ordena(Lista<Linea> lista){
        lista = Lista.mergeSort(lista);
        if(!ReaderAndWritter.guardar) //No se imprimira en caso que se quiera guardar el archivo(Como en sort)
            for (Linea lineas : lista)
                System.out.println(lineas);
        return lista;
    }

    /**
     * Regresa una copia de la lista de líneas recibida, pero ordenada de manera
     * lexicográfica en reversa.Si la bandera -o es true, no imprirá nada, de lo
     * contrario imprimirá la lista ordenada en reversa.
     * @param lista la lista a ordenar.
     * @return una copia ordenada de la lista de líneas.
     */
    static Lista<Linea> ordenaAlReves(Lista<Linea> lista){
        lista =Lista.mergeSort(lista);
        lista=lista.reversa();
        if(!ReaderAndWritter.guardar)
            for (Linea lineas : lista)
                System.out.println(lineas);
        return lista;
    }
}
