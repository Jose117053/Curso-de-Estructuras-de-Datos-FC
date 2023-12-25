package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            // Aquí va su código.
            cola = new Cola<>();
            if (raiz != null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override
        public boolean hasNext() {
            // Aquí va su código.
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override
        public T next() {
            // Aquí va su código.
            if (cola.esVacia())
                throw new NoSuchElementException("No hay cola");
            Vertice vertice = cola.saca();
            if (vertice.hayIzquierdo()) //ifVertice.izquierdo !=null
                cola.mete(vertice.izquierdo);
            if (vertice.hayDerecho())
                cola.mete(vertice.derecho);
            return vertice.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() {
        super();
    }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     *
     * @param coleccion la colección a partir de la cual creamos el árbol
     *                  binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     *
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *                                  <code>null</code>.
     */
    @Override
    public void agrega(T elemento) {
        if(elemento==null)
            throw new IllegalArgumentException("elemento no valido");
        Vertice nuevo = nuevoVertice(elemento);
        elementos++;

        if (raiz == null) {
            raiz = nuevo;
            return;
        }
        Vertice v = vertice(agregaux());
        nuevo.padre = v;

        if (v.izquierdo == null)
            v.izquierdo = nuevo;
        else
            v.derecho = nuevo;
    }

    private Vertice agregaux(){
        Cola<Vertice> c=new Cola<>();
        c.mete(raiz);
        Vertice v;
        while (!c.esVacia()) {
            v = c.saca();
            if (v.izquierdo == null || v.derecho == null)
                return v;
            c.mete(v.izquierdo);
            c.mete(v.derecho);
        }
        return null;

    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice v=vertice(busca(elemento));
        if(v==null)
            return;
        elementos--;
        if (elementos == 0) {
            raiz = null;
            return;

        }
        Vertice ultimo = ultimoVertice();
        T temporal= v.elemento;
        v.elemento=ultimo.elemento;
        ultimo.elemento=temporal;
        Vertice padreUltimo = ultimo.padre;
        if (padreUltimo.hayIzquierdo() && padreUltimo.izquierdo == ultimo) {
            padreUltimo.izquierdo = null;
        } else {
            padreUltimo.derecho = null;
        }
    }

    private Vertice ultimoVertice() {
        Cola<Vertice> c = new Cola<>();
        c.mete(raiz);
        Vertice ultimo=raiz;
        while (!c.esVacia()) {
            ultimo = c.saca();

            if (ultimo.hayIzquierdo())
                c.mete(ultimo.izquierdo);

            if (ultimo.hayDerecho())
                c.mete(ultimo.derecho);
        }
        return ultimo;
    }


    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        // Aquí va su código.
        if(elementos==0)
            return -1;
        return log2(elementos);
    }
    private int log2(int elementos){
        int log=0;
        while(elementos>1) {
            elementos /= 2;
            log++;
        }
        return log;
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.

        if(raiz==null)
            return;
        Cola<Vertice> c=new Cola<>();
        c.mete(raiz);
        while (!c.esVacia()){
            Vertice v=c.saca();
            accion.actua(v);
            if(v.hayIzquierdo())
                c.mete(v.izquierdo);
            if(v.hayDerecho())
                c.mete(v.derecho);
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
