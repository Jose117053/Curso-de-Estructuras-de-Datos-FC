package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
            // Aquí va su código.
            this.llave=llave;
            this.valor=valor;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
            // Aquí va su código.
            this.indice=-1;
            buscaIterador();
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            // Aquí va su código.
            return iterador != null;
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() {
            // Aquí va su código.
            if(iterador==null)
                throw new NoSuchElementException("No hay siguiente");

            Entrada siguiente=iterador.next();

            if(!iterador.hasNext())
                buscaIterador();

            return siguiente;
        }

        /**
         * Busca en el arreglo la primera entrada distinta de null
         * e inicia el indice en esta entrada y su iterador con
         * el iterador de esa lista, el iterador es null
         */
        private void buscaIterador(){

            if (iterador != null && iterador.hasNext())
                return;

            iterador = null;
            for (int i = indice + 1; i < entradas.length; i++) {
                if (entradas[i] != null) {
                    indice = i;
                    iterador = entradas[i].iterator();
                    return;
                }
            }
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            // Aquí va su código.
            return siguiente().llave;
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            // Aquí va su código.
            return siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        // Aquí va su código.

        this.dispersor = dispersor;
        capacidad = Math.max(capacidad, MINIMA_CAPACIDAD);
        capacidad = siguientePotenciaDeDos(capacidad * 2);
        entradas=nuevoArreglo(capacidad);

    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        // Aquí va su código.
        if(llave == null || valor == null)
            throw new IllegalArgumentException("No se permiten elementos nulos");

        int dispersion=hash(llave);
        Entrada colision= buscaEntrada(dispersion, llave);

        if(entradas[dispersion] == null)
            entradas[dispersion] = new Lista<>();

        //se verifica que no haya colision antes de agregar una nueva entrada
        if(colision !=null)
            colision.valor=valor;
        else {
            entradas[dispersion].agrega(new Entrada(llave, valor));
            elementos++;
            }


        if(carga() >= MAXIMA_CARGA) {
            Lista<Entrada>[] nuevoArreglo= nuevoArreglo(entradas.length * 2);
            Iterador iterador=new Iterador();

            while (iterador.hasNext()){
                Entrada entrada = iterador.siguiente();
                int dispersionLlave= dispersor.dispersa(entrada.llave) & (nuevoArreglo.length-1);

                if(nuevoArreglo[dispersionLlave] == null)
                    nuevoArreglo[dispersionLlave] = new Lista<Entrada>();

                nuevoArreglo[dispersionLlave].agrega(entrada);

            }
            entradas=nuevoArreglo;
        }
    }



    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        // Aquí va su código.
        if(llave == null)
            throw new IllegalArgumentException("La llave es nula");

        int dispersion=hash(llave);

        Entrada entrada=buscaEntrada(dispersion, llave);
        if(entrada !=null)
            return entrada.valor;

        throw new NoSuchElementException("La llave no está en el diccionario");

    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <code>true</code> si la llave está en el diccionario,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(K llave) {
        // Aquí va su código.
        if(llave == null)
            return false;

        int dispersion = hash(llave);
        Entrada entrada=buscaEntrada(dispersion, llave);

        return entrada !=null;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        // Aquí va su código.

        if(llave == null)
            throw new IllegalArgumentException("La llave es nula");

        int dispersion = hash(llave);

        Lista<Entrada> lista = entradas[dispersion];

        if(lista == null )
            throw new NoSuchElementException("La llave no está en el diccionario");

        for(Entrada entrada: lista)
            if(entrada.llave.equals(llave)) {
                lista.elimina(entrada);

                if (entradas[dispersion].getLongitud() == 0)
                    entradas[dispersion] = null;

                elementos--;
                return;
            }

        throw new NoSuchElementException("La llave no está en el diccionario");

    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        // Aquí va su código.
        int colisiones=0;

        for(Lista<Entrada> lista: entradas)
            if(lista != null)
                 colisiones+=lista.getLongitud();

        return colisiones == 0 ? colisiones : colisiones-1;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        // Aquí va su código.
        int colisionMaxima=0;
        for(Lista<Entrada> lista: entradas)
            if(lista != null && lista.getLongitud()>colisionMaxima)
                colisionMaxima=lista.getLongitud();

        return colisionMaxima == 0 ? colisionMaxima : colisionMaxima-1;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        // Aquí va su código.
        return (double) elementos / entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        // Aquí va su código.
        return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        // Aquí va su código.
        return elementos == 0;
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        // Aquí va su código.
        Lista<Entrada>[] nuevo=nuevoArreglo(entradas.length);

        entradas=nuevo;
        elementos=0;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
        // Aquí va su código.


        if (elementos == 0)
            return "{}";


        Iterador iterador= new Iterador();
        String inicio = "{ ";

        while (iterador.hasNext()){
            Entrada entrada=iterador.siguiente();
            inicio+="'"+entrada.llave+"': '"+entrada.valor+"', ";
        }

        return inicio+"}";
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
        // Aquí va su código.

        if(this.elementos != d.elementos)
            return false;

        for (Lista<Entrada> entradaLista : entradas)
            if (entradaLista != null)
                for (Entrada entrada : entradaLista)
                    if (!d.contiene(entrada.llave))
                        return false;

        return true;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }

    /**
     * Busca la lista que contenga la entrada en el arreglo
     * de entradas dado el indice y la llave de la entrada
     * @param indice el indice en el que se buscara la entrada
     * @param llave la llave de la entrada
     * @return null si no existe la lista en el indice del arreglo
     * o si la llave no está en la lista; la entrada en otro caso.
     */
    private Entrada buscaEntrada(int indice, K llave) {
        if (entradas[indice] == null) //cuando ni siquiera existe la lista
            return null;

        for (Entrada entrada : entradas[indice])
            if (entrada.llave.equals(llave))
                return entrada;

        return null;
    }

    /**
     * Busca la siguiente potencia de 2 que sea mayor o igual a n
     * @param n el valor del que buscaremos la potencia de dos que sea mayor
     * o igual a este
     */
    private int siguientePotenciaDeDos(int n) {
        int potencia = 1;
        while (potencia < n) {
            potencia <<= 1; //desplazamiento de 1 posicion a la aizquierda
        }
        return potencia;
    }

    /**
     * Dispersa la llave y le aplica la mascara
     * @param llave la llave a dispersar
     * @return el entero resultante de dispersar la llave
     */

    private int hash(K llave){
        return dispersor.dispersa(llave) & (entradas.length-1);

    }
}
