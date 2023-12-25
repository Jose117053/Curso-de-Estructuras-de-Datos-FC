package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
        extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
            // Aquí va su código.
            pila = new Pila<>();
            if (raiz == null)
                return;
            pila.mete(raiz);
            Vertice v = raiz;

            while (v.hayIzquierdo()) {
                pila.mete(v.izquierdo);
                v = v.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override
        public boolean hasNext() {
            // Aquí va su código.
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override
        public T next() {
            // Aquí va su código.

            Vertice v = pila.saca();
            if (v.derecho !=null) {

                Vertice derecho=v.derecho;
                pila.mete(derecho);
                derecho=derecho.izquierdo;
                while (derecho !=null) {
                    pila.mete(derecho);
                    derecho = derecho.izquierdo;
                }
            }
            return v.elemento;

        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.

        if(elemento==null)
            throw new IllegalArgumentException("No se permiten elementos nulos");
        Vertice v=nuevoVertice(elemento);
        elementos++;
        if(raiz==null)
            raiz = v;
        else
            agregaux(raiz, v);
        ultimoAgregado=v; //tambien faltaba esto:(
    }

    private void agregaux(Vertice actual, Vertice nuevoElemento){
        if(nuevoElemento.elemento.compareTo(actual.elemento)<=0) {
            if (actual.izquierdo == null) {
                actual.izquierdo = nuevoElemento;
                nuevoElemento.padre=actual; //me faltaba renombrar :(
            } else {
                agregaux(actual.izquierdo, nuevoElemento);
            }
        }else{
            if(actual.derecho==null){
                actual.derecho=nuevoElemento;
                nuevoElemento.padre=actual;//me faltaba renombrar :( x2
            }else{
                agregaux(actual.derecho, nuevoElemento);
            }
        }

    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Vertice v= vertice(busca(elemento));
        if(v==null)
            return;
        eliminaux(v);
        elementos--;
    }
    private void eliminaux(Vertice v){
        if (esHoja(v)){
            if(v==raiz)
                raiz=null;
            else if(v.padre.derecho==v){
                v.padre.derecho=null;
            }else
                v.padre.izquierdo=null;
        }
        else if((v.hayIzquierdo() && !v.hayDerecho()) || (v.hayDerecho() && !v.hayIzquierdo()))
            eliminaVertice(v);
        else{
            Vertice u=maxSubArbol(v.izquierdo);
            v.elemento = u.elemento;
            eliminaux(u);
        }
    }
    private boolean esHoja(Vertice v){
        return !v.hayIzquierdo() && !v.hayDerecho();
    }
    private Vertice maxSubArbol(Vertice v){
        if(v.derecho==null)
            return v;
        return maxSubArbol(v.derecho);
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        // Aquí va su código.
        Vertice maximo=maxSubArbol(vertice.izquierdo);
        T maximoElemento=maximo.elemento;//para no perder el elemento

        maximo.elemento=vertice.elemento;
        vertice.elemento=maximoElemento;


        return maximo;
    }


    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        // Aquí va su código.

        Vertice hijo;
        if(vertice.izquierdo !=null)
            hijo=vertice.izquierdo;
        else
            hijo=vertice.derecho;

        if(vertice.padre != null) {
            if (vertice.padre.izquierdo == vertice) {
                vertice.padre.izquierdo = hijo;
            } else
                vertice.padre.derecho = hijo;
        }else
            raiz=hijo;
        if(hijo !=null)
            hijo.padre=vertice.padre;
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
        return buscaux(raiz, elemento);
    }
    private VerticeArbolBinario<T> buscaux(Vertice v, T elemento){
        if(v==null)
            return null;
        int compara=elemento.compareTo(v.elemento);
        if(compara==0) //equals equivale cuando compara=0, sin necesidad de comparar
            return v;
        if(compara<0)
            return buscaux(v.izquierdo, elemento);
        else
            return buscaux(v.derecho, elemento);

    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        if(esVacia() || vertice==null)
            return;
        Vertice q=vertice(vertice);
        if(!vertice.hayIzquierdo())
            return;
        Vertice p=q.izquierdo;
        Vertice r=p.izquierdo;
        Vertice s=p.derecho;
        Vertice t= q.derecho;
        Vertice a=null;
        boolean b=false;
        if(q !=raiz)
            a=q.padre;
        if(a !=null && a.derecho==q)
            b=true;
        p.derecho=q;
        q.padre=p;
        q.izquierdo=s;
        q.derecho=t;
        if(s !=null)
            s.padre=q;
        if(t!=null)
            t.padre=q;
        if(a !=null){
            p.padre=a;
            if(b)
                a.derecho=p;
            else
                a.izquierdo=p;
        }else{
            p.padre=null;
            raiz=p;
        }
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        if(esVacia() || vertice==null)
            return;
        Vertice p=vertice(vertice);
        if(!vertice.hayDerecho())
            return;
        Vertice q=p.derecho;
        Vertice r=p.izquierdo;
        Vertice s=q.izquierdo;
        Vertice t= q.derecho;
        Vertice a=null; //padre de p
        boolean b=false;

        if(p !=raiz)
            a=p.padre;
        if(a !=null && a.derecho==p) //a.izq?
            b=true;

        p.derecho=s;
        q.izquierdo=p;
        p.padre=q;
        q.derecho=t;
        if(s !=null)
            s.padre=p;
        if(t!=null)
            t.padre=q;
        if(a!=null) {
            q.padre = a;
            if(b)
                a.derecho=q;
            else
                a.izquierdo=q;
        }else{
            q.padre=null;
            raiz=q;
        }
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPreOrderAux(raiz, accion);
    }
    private void dfsPreOrderAux(Vertice v, AccionVerticeArbolBinario<T> accion){
        if(v==null)
            return;
        accion.actua(v);
        dfsPreOrderAux(v.izquierdo, accion);
        dfsPreOrderAux(v.derecho, accion);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsInOrderAux(raiz, accion);
    }
    private void dfsInOrderAux(Vertice v, AccionVerticeArbolBinario<T> accion){
        if(v==null)
            return;
        dfsInOrderAux(v.izquierdo, accion);
        accion.actua(v);
        dfsInOrderAux(v.derecho, accion);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPostOrderAux(raiz, accion);
    }
    private void dfsPostOrderAux(Vertice v, AccionVerticeArbolBinario <T> accion){
        if(v==null)
            return;
        dfsPostOrderAux(v.izquierdo, accion);
        dfsPostOrderAux(v.derecho, accion);
        accion.actua(v);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
