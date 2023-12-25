package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
        extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            color=Color.NINGUNO;
            // Aquí va su código.

        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            // Aquí va su código.
            return (this.color == Color.ROJO ? "R{" : "N{") + this.elemento.toString() + "}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
            VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
            return color==vertice.color && super.equals(objeto);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        VerticeRojinegro rjn=(VerticeRojinegro) vertice;
        return rjn.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        VerticeRojinegro nuevo=(VerticeRojinegro) ultimoAgregado;
        nuevo.color=Color.ROJO;
        rebalanceoAgrega(nuevo);
    }
    private void rebalanceoAgrega(VerticeRojinegro vertice){

        if(vertice==null || !esRojo(vertice))
            return;
        if(vertice.padre==null) {
            vertice.color = Color.NEGRO;
            return;
        }
        VerticeRojinegro papa=(VerticeRojinegro) vertice(vertice.padre);
        if(esNegro(papa))
            return;
        VerticeRojinegro abuelo=(VerticeRojinegro) vertice(papa.padre);
        VerticeRojinegro tio=asignaHermano(papa); //el hermano del papa del vertice es el tio

        if(tio!=null && esRojo(tio)  ) {
            tio.color = Color.NEGRO;
            papa.color = Color.NEGRO;
            abuelo.color = Color.ROJO;
            rebalanceoAgrega(abuelo);
            return;
        }

        if(esIzquierdo(papa) && esDerecho(vertice)) {
            super.giraIzquierda(papa);
            VerticeRojinegro temp=vertice;
            vertice=papa;
            papa=temp;
        }else if(esDerecho(papa) && esIzquierdo(vertice)) {//si el papa es hijo derecho y el vertice es hijo izquierdo
            super.giraDerecha(papa);
            VerticeRojinegro temp=vertice;
            vertice=papa;
            papa=temp;
        }
        papa.color=Color.NEGRO;
        abuelo.color=Color.ROJO;

        if(esIzquierdo(vertice))
            super.giraDerecha(abuelo);
        else
            super.giraIzquierda(abuelo);
    }


    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */


    ///EL METODO TOSTRING DE ARBOLBINARIO ESTABA MAL :(, (aunque pasaba los tests) ESTO AFECTABA LOS TESTS EN ESTA PRACTICA :C :C
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        if(elemento==null)
            return;

        VerticeRojinegro vertice=(VerticeRojinegro) super.busca(elemento);
        if(vertice==null)
            return;

        if(vertice.hayDerecho() && vertice.hayIzquierdo())
            vertice=(VerticeRojinegro) super.intercambiaEliminable(vertice);

        VerticeRojinegro fantasma=(VerticeRojinegro) nuevoVertice(null);
        fantasma.color=Color.ROJO;

        if(!vertice.hayIzquierdo() && !vertice.hayDerecho()) {
            fantasma.color=Color.NEGRO;
            vertice.izquierdo=fantasma;
            fantasma.padre=vertice;
        }

        VerticeRojinegro hijo;
        if(vertice.hayIzquierdo())
            hijo=(VerticeRojinegro) vertice.izquierdo;
        else
            hijo=(VerticeRojinegro)vertice.derecho;

        super.eliminaVertice(vertice);
        elementos--;

        if(esRojo(hijo) || esRojo(vertice) ) {//no debe retornar, si no, no se elimina al fantasma
            hijo.color=Color.NEGRO;
        }else
            rebalanceoElimina(hijo);//cuando hijo y vertice son negros

        if(fantasma==hijo){
            if(raiz==fantasma) {
                super.eliminaVertice(fantasma);
                super.limpia();
            }else if(esIzquierdo(fantasma))
                fantasma.padre.izquierdo=null;
            else
                fantasma.padre.derecho=null;
        }

    }

    private void rebalanceoElimina(VerticeRojinegro vertice){
        if(vertice==null)
            return;
        //caso 1: padre null
        if(vertice.padre==null)
            return;
        VerticeRojinegro papa=(VerticeRojinegro) vertice.padre;
        VerticeRojinegro hermano=asignaHermano(vertice);

        //caso 2: el hermano es rojo
        if(esRojo(hermano)) {
            papa.color = Color.ROJO;
            hermano.color=Color.NEGRO;
            if(esDerecho(vertice))
                super.giraDerecha(papa);
            else
                super.giraIzquierda(papa);
            papa=(VerticeRojinegro) vertice.padre;
            if(esIzquierdo(vertice))
                hermano=(VerticeRojinegro) papa.derecho;
            else
                hermano=(VerticeRojinegro) papa.izquierdo;
        }

        VerticeRojinegro hi=(VerticeRojinegro) hermano.izquierdo;
        VerticeRojinegro hd=(VerticeRojinegro) hermano.derecho;
        //caso 3: papa,hermano, hi y hd son negros
        if(esNegro(hermano) && esNegro(hi) && esNegro(hd)) {
            if(esNegro(papa)) {
                hermano.color = Color.ROJO;
                rebalanceoElimina(papa);
                return;
            }else{//cuando papa es rojo(caso 4)
                hermano.color=Color.ROJO;
                papa.color=Color.NEGRO;
                return;
            }
        }

        if((esIzquierdo(vertice) && esRojo(hi) && esNegro(hd)) || (esDerecho(vertice) && esNegro(hi) && esRojo(hd))) {
            hermano.color = Color.ROJO;

            if(esIzquierdo(vertice)){//caso 5 v es izquierdo
                hi.color=Color.NEGRO;
                super.giraDerecha(hermano);
            }else{//caso 5.1 v es derecho
                hd.color=Color.NEGRO;
                super.giraIzquierda(hermano);
            }
            //reasigna hermano
            hermano=asignaHermano(vertice);
            //reasigna hermano.izquierdo y hermano.derecho
            hi=(VerticeRojinegro) hermano.izquierdo;
            hd=(VerticeRojinegro) hermano.derecho;
        }

        hermano.color=papa.color;
        papa.color=Color.NEGRO;
        if(esDerecho(vertice)) {
            hi.color = Color.NEGRO;
            super.giraDerecha(papa);
        }
        else {
            hd.color = Color.NEGRO;
            super.giraIzquierda(papa);
        }

    }
    //se podria utilizar un solo metodo de color y si es izquierdo o derecho, pero considero que el codigo es más legible
    //si cada caso tiene su metodo
    private boolean esRojo(VerticeRojinegro vertice){
        if(vertice==null)
            return true;
        return vertice.color==Color.ROJO;
    }
    private boolean esNegro(VerticeRojinegro vertice){
        if(vertice==null)
            return true;
        return vertice.color==Color.NEGRO;
    }
    private boolean esIzquierdo(VerticeRojinegro vertice){
        return vertice.padre.izquierdo==vertice;
    }
    private boolean esDerecho(VerticeRojinegro vertice){
        return vertice.padre.derecho==vertice;
    }
    private VerticeRojinegro asignaHermano(VerticeRojinegro vertice){
        VerticeRojinegro hermano;
        if(esIzquierdo(vertice))
            hermano=(VerticeRojinegro) vertice.padre.derecho;
        else
            hermano=(VerticeRojinegro) vertice.padre.izquierdo;
        return hermano;
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                "pueden girar a la izquierda " +
                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                "pueden girar a la derecha " +
                "por el usuario.");
    }
}
