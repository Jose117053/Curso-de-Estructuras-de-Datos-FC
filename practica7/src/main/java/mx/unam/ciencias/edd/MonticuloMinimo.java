package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return indice<elementos;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
            if(indice>=elementos)
                throw new NoSuchElementException("No hay siguente"); //habia escrito IllegalArgumenException, no pasaba test

            return arbol[indice++];

        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            // Aquí va su código.
            this.elemento=elemento;
            this.indice=-1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            // Aquí va su código.
            return  indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            // Aquí va su código.
            this.indice=indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            // Aquí va su código.
            return elemento.compareTo(adaptador.elemento);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        // Aquí va su código.
        arbol=nuevoArreglo(100);
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        // Aquí va su código.
        arbol=nuevoArreglo(n);

        for (T elemento: iterable){
            arbol[elementos] =elemento;
            elemento.setIndice(elementos);
            elementos++;
        }

        for(int j=n/2-1; j>=0; j--)
            acomodaHaciaAbajo(j);

    }
    private void acomodaHaciaAbajo(int i){
        int hijoIzquierdo=2*i+1;
        int hijoDerecho=2*i+2;
        int minimo=i;

        if(hijoIzquierdo<elementos && arbol[hijoIzquierdo].compareTo(arbol[minimo])<0)
            minimo=hijoIzquierdo;

        if(hijoDerecho<elementos && arbol[hijoDerecho].compareTo(arbol[minimo])<0)
            minimo=hijoDerecho;

        if(minimo !=i) {
            intercambia(i, minimo);
            acomodaHaciaAbajo(minimo);
        }
    }
    private void intercambia(int i, int j) {
        T temp = arbol[i];
        arbol[i] = arbol[j];
        arbol[j] = temp;
        arbol[i].setIndice(i);
        arbol[j].setIndice(j);
    }
    private void acomodaHaciaArriba(int i){
        T v =arbol[i];
        while (i>0 && v.compareTo(arbol[(i-1)/2])<0){
            intercambia(i, (i-1)/2);
            i=(i-1)/2;
        }
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.

        if(elementos==arbol.length) {
            T[] nuevo= nuevoArreglo(arbol.length * 2);
            for(int i=0; i<elementos; i++)
                nuevo[i]=arbol[i];
            arbol=nuevo;
        }
        arbol[elementos]=elemento;
        arbol[elementos].setIndice(elementos);
        elementos++;
        acomodaHaciaArriba(elementos-1);

    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        // Aquí va su código.
        if (elementos == 0)
            throw new IllegalStateException("El montículo es vacío");

        T eliminado=arbol[0];
        intercambia(0,elementos-1);
        arbol[elementos-1].setIndice(-1);
        elementos--;
        acomodaHaciaAbajo(0);

        return eliminado;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        int indice=elemento.getIndice();

        if(indice<0 || indice >=elementos)
            return;

        intercambia(indice, elementos-1);
        arbol[elementos - 1].setIndice(-1);
        elementos--;

        if (indice < elementos)
             reordena(arbol[indice]);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        int indice= elemento.getIndice();

        if(indice<0 || indice>=elementos)
            return false;

        return arbol[indice].compareTo(elemento)==0;

    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return elementos==0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        // Aquí va su código.
        elementos=0;

    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        // Aquí va su código.
        int indice=elemento.getIndice();
        acomodaHaciaAbajo(indice);
        acomodaHaciaArriba(indice);

    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        // Aquí va su código.
        if(i<0)
            throw new NoSuchElementException("Debes ingresar indices mayores a 0");

        if(i>=elementos)
            throw new NoSuchElementException("Debes ingresar indices menores que la cantidad de elementos");

        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        // Aquí va su código.
        String cadena="";
        for (T t : arbol)
            cadena += t.toString() + ", ";
        return cadena;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        // Aquí va su código.
        if (monticulo.elementos != elementos)
            return false;

        for (int i = 0; i < elementos; i++) {

            if (arbol[i].equals(monticulo.arbol[i]))
                continue;//!equals return false

            return false;
        }

        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        // Aquí va su código.
        Lista<Adaptador<T>> adaptadores = new Lista<>();

        for (T elemento : coleccion)
            adaptadores.agrega(new Adaptador<>(elemento));

        Lista<T> elementos = new Lista<>();
        MonticuloMinimo<Adaptador<T>> monticulo = new MonticuloMinimo<>(adaptadores);

        while (!monticulo.esVacia()) {
            Adaptador<T> eliminado = monticulo.elimina();
            elementos.agrega(eliminado.elemento);
        }

        return elementos;
    }
}
