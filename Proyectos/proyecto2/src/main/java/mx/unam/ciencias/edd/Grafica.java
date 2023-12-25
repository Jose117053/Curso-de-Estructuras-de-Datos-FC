package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador= vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        private T elemento;
        /* El color del vértice. */
        private Color color;
        /* La lista de vecinos del vértice. */
        private Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento=elemento;
            this.color=Color.NINGUNO;
            this.vecinos=new Lista<Vertice>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices=new Lista<Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {

        if(elemento==null)
            throw new IllegalArgumentException("Elemento no valido");

        if(contiene(elemento))
            throw new IllegalArgumentException("El elemento ya está en la grafica");

        Vertice vertice=new Vertice(elemento);
        vertices.agrega(vertice);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {

        if(a==b)
            throw new IllegalArgumentException("No puedes conectar un vertice a si mismo");

        Vertice uno=busca(a);
        Vertice dos=busca(b);

        if(sonVecinos(a, b))
            throw new IllegalArgumentException("Los vertices ya están conectados");


        dos.vecinos.agrega(uno);
        uno.vecinos.agrega(dos);

        aristas++;

    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {

        if(a==b)
            throw new IllegalArgumentException("No puedes conectar un vertice a sí mismo");

        Vertice vecino1=busca(a);
        Vertice vecino2=busca(b);

        if(!sonVecinos(a,b))
            throw new IllegalArgumentException("Los vertices no están conectados");

        vecino2.vecinos.elimina(vecino1);
        vecino1.vecinos.elimina(vecino2);

        aristas--;

    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {

        if(elemento==null)
            return false;

        for (Vertice v: vertices)
            if (v.elemento.equals(elemento))
                return true;

        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        if(elemento==null)
            throw new IllegalArgumentException("Elemento no valido");

        Vertice vertice=busca(elemento);
        vertices.elimina(vertice);

        for(Vertice vecino: vertice.vecinos) {
            vecino.vecinos.elimina(vertice);
            aristas--;
        }
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {

        Vertice uno=busca(a);
        Vertice dos=busca(b);

        return uno.vecinos.contiene(dos) && dos.vecinos.contiene(uno);

    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {

        for(Vertice vertices: vertices)
            if(vertices.elemento.equals(elemento))
                return vertices;

        throw new NoSuchElementException("No está el elemento");
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {

        if(vertice.getClass() != Vertice.class)
            throw new IllegalArgumentException("El vertice no es valido");

        Vertice v= (Vertice) vertice;
        v.color=color;

    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {

        eligeEstructura(vertices.getPrimero().elemento, e -> {}, new Cola<>());
        for (Vertice v: vertices)
            if (v.color !=Color.NEGRO)
                return false;

        return true;
    }


    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice vertice: vertices)
            accion.actua(vertice);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {

        eligeEstructura(elemento, accion, new Cola<>());
        for(Vertice vertice: vertices)
            vertice.color=Color.NINGUNO;

    }
    private Vertice busca(T elemento){

        for(Vertice vertices: vertices)
            if(vertices.elemento.equals(elemento))
                return vertices;

        throw new NoSuchElementException("No está el elemento");
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        eligeEstructura(elemento, accion, new Pila<>());
        for(Vertice vertice: vertices)
            vertice.color=Color.NINGUNO;
    }
    private void eligeEstructura(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> estructura){

        Vertice vertice=busca(elemento);

        for(Vertice v: vertices)
            v.color=Color.ROJO;

        vertice.color=Color.NEGRO;
        estructura.mete(vertice);

        while (!estructura.esVacia()) {
            Vertice sacado=estructura.saca();
            accion.actua(sacado);

            for(Vertice vecino: sacado.vecinos)
                if(vecino.color ==Color.ROJO) {
                    vecino.color = Color.NEGRO;
                    estructura.mete(vecino);
                }

        }
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas=0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {

        String elementos="{";
        for(Vertice vertice: vertices)
            elementos+=vertice.elemento.toString()+", ";
        elementos+="}, ";

        String aristas="{";

        Lista<T> pasados=new Lista<>();
        for(Vertice vertice: vertices){
            for (Vertice vecino: vertice.vecinos) {
                if (!pasados.contiene(vecino.elemento))
                    aristas += "(" + vertice.elemento.toString() + ", " + vecino.elemento.toString() + "), ";
            }
            pasados.agrega(vertice.elemento);
        }
        aristas+="}";


        return elementos+aristas;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;

        if(getElementos() !=grafica.getElementos() || aristas !=grafica.aristas)
            return false;

        for(Vertice vertice: vertices) {

            Vertice verticeGrafica = grafica.busca(vertice.elemento);

            if (!grafica.contiene(vertice.elemento))
                return false;

            if(vertice.vecinos.getElementos() != verticeGrafica.vecinos.getElementos())
                return false;

            boolean tiene=false;

            for(Vertice vecino: vertice.vecinos)
                for(Vertice vecino2: verticeGrafica.vecinos)
                    if(vecino.elemento.equals(vecino2.elemento))
                        tiene= true;

            if(!tiene)
                return false;
        }
        return  true ;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
