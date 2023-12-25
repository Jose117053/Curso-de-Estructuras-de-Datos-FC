package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura;//super?
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            return toStringAux(this);
        }
        private String toStringAux(VerticeAVL vertice){
            return vertice.elemento.toString()+" "+vertice.altura()+"/"+balance(vertice);
        }


        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            return altura==vertice.altura && super.equals(objeto); //vertice?
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {

        super.agrega(elemento);
        rebalancea(verticeAVL(ultimoAgregado.padre));

    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {

        VerticeAVL vertice = (VerticeAVL) busca(elemento);

        if(vertice==null)
            return;
        elementos--;

        if(vertice.hayDerecho() && vertice.hayIzquierdo())
            vertice=verticeAVL(super.intercambiaEliminable(vertice));

        super.eliminaVertice(vertice);

        rebalancea(verticeAVL(vertice.padre));
    }

    private void rebalancea(VerticeAVL vertice){

        if(vertice==null)
            return;

        actualizaAltura(vertice);
        VerticeAVL padreFinal= verticeAVL(vertice.padre);

        if(balance(vertice)==-2){

            VerticeAVL p= verticeAVL(vertice.izquierdo);
            VerticeAVL q= verticeAVL(vertice.derecho);
            VerticeAVL x= verticeAVL(q.izquierdo);

            if(balance(q)==1){

                super.giraDerecha(q);
                actualizaAltura(q);
                actualizaAltura(x);

            }

            q= verticeAVL(vertice.derecho);
            int balanceQ=balance(q);

            if(balanceQ==0 || balanceQ==-1 || balanceQ==-2){

                super.giraIzquierda(vertice);
                actualizaAltura(vertice);
                actualizaAltura(q);
                padreFinal= verticeAVL(q.padre);

            }else
                padreFinal= verticeAVL(p.padre);
        }

        if(balance(vertice)==2){

            VerticeAVL p=verticeAVL(vertice.izquierdo);
            VerticeAVL x=verticeAVL(p.izquierdo);

            if(balance(p)==-1) {

                super.giraIzquierda(p);
                actualizaAltura(p);
                actualizaAltura(x);

            }

            p=verticeAVL(vertice.izquierdo);

            if(balance(p)==0 || balance(p)==1 || balance(p)==2){
                super.giraDerecha(vertice);
                actualizaAltura(vertice);
                actualizaAltura(p);
                padreFinal=verticeAVL(p.padre);
            }else
                padreFinal= verticeAVL(vertice.padre) ;
        }
        rebalancea(padreFinal);
    }
    private int balance(VerticeAVL vertice){
            return altura(verticeAVL(vertice.izquierdo)) -altura(verticeAVL(vertice.derecho));
    }
    private void actualizaAltura(VerticeAVL vertice){
        if(vertice==null)
            return;
        vertice.altura=Math.max(altura(verticeAVL(vertice.izquierdo)), altura(verticeAVL(vertice.derecho)))+1;
    }
    private int altura(VerticeAVL vertice){
        if(vertice==null)
            return -1;
        return vertice.altura;
    }
    private VerticeAVL verticeAVL(Vertice vertice){
        return (VerticeAVL) vertice;
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
