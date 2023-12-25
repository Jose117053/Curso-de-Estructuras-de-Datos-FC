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
            // Aquí va su código.
            iterador= vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
            return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        private T elemento;
        /* El color del vértice. */
        private Color color;
        /* La distancia del vértice. */
        private double distancia;
        /* El índice del vértice. */
        private int indice;
        /* El diccionario de vecinos del vértice. */
        private Diccionario<T, Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            // Aquí va su código.
            this.elemento=elemento;
            this.color=Color.NINGUNO;
            this.vecinos=new Diccionario<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            // Aquí va su código.
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            // Aquí va su código.
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            // Aquí va su código.
            this.indice=indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            // Aquí va su código.
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            // Aquí va su código.
            return Double.compare(distancia, vertice.distancia);
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            // Aquí va su código.
            this.vecino=vecino;
            this.peso=peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            // Aquí va su código.
            return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            // Aquí va su código.
            return vecino.color;
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            return vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino<T> {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica<T>.Vertice v, Grafica<T>.Vecino a);
    }

    /* Vértices. */
    private Diccionario<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        // Aquí va su código.
        vertices=new Diccionario<>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        // Aquí va su código.
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento==null)
            throw new IllegalArgumentException("Elemento no valido");

        if(contiene(elemento))
            throw new IllegalArgumentException("El elemento ya está en la grafica");

        Vertice vertice=new Vertice(elemento);
        vertices.agrega(elemento, vertice);
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
        // Aquí va su código.
        conecta(a,b,1);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        // Aquí va su código.
        if(a==b)
            throw new IllegalArgumentException("No puedes conectar un vertice a si mismo");

        if(peso <=0)
            throw new IllegalArgumentException("EL peso es negativo");

        if(sonVecinos(a, b))
            throw new IllegalArgumentException("Los vertices ya están conectados");

        Vertice uno=busca(a);
        Vertice dos=busca(b);



        dos.vecinos.agrega(a, new Vecino(uno, peso)); //dos.vecinos.agrega(new Vecino(uno, peso));
        uno.vecinos.agrega(b, new Vecino(dos, peso));

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
        // Aquí va su código.

        if(!sonVecinos(a, b))
            throw new IllegalArgumentException("Los vertices no están conectados");

        Vertice verticeA=(Vertice) vertice(a);
        Vertice verticeB=(Vertice) vertice(b);


        verticeA.vecinos.elimina(b);
        verticeB.vecinos.elimina(a);

        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
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
        // Aquí va su código.
        if(elemento==null)
            throw new IllegalArgumentException("Elemento no valido");

        Vertice vertice=(Vertice) vertice(elemento);

        for(Vecino vecino: vertice.vecinos)
            desconecta(vertice.elemento, vecino.vecino.elemento);

        vertices.elimina(elemento);
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
        // Aquí va su código.
        Vertice uno=busca(a);
        Vertice dos=busca(b);

        return obtenerVecino(uno,dos) != null;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        // Aquí va su código.
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException("No está algun elemento en la grafica");

        if(!sonVecinos(a,b))
            throw new IllegalArgumentException("Los elementos no son vecinos");

        if(a==null || b==null)
            throw new IllegalArgumentException("NO se permiten elementos nulos");

        Vertice uno=busca(a);
        Vertice dos=busca(b);

        return obtenerVecino(uno,dos).peso;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        // Aquí va su código.
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException("No está algun elemento en la grafica");

        if(!sonVecinos(a,b))
            throw new IllegalArgumentException("Los elementos no son vecinos");

        if(peso <=0)
            throw new IllegalArgumentException("EL peso es negativo");

        Vecino uno=obtenerVecino(busca(a), busca(b));
        Vecino dos=obtenerVecino(busca(b), busca(a));
        uno.peso=peso;
        dos.peso=peso;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        // Aquí va su código.
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
        // Aquí va su código.
        if (vertice == null || (vertice.getClass() != Vertice.class &&  vertice.getClass() != Vecino.class))
            throw new IllegalArgumentException("Vértice inválido");

        if (vertice.getClass() == Vertice.class) {
            Vertice v = (Vertice)vertice;
            v.color = color;
        }
        if (vertice.getClass() == Vecino.class) {
            Vecino v = (Vecino)vertice;
            v.vecino.color = color;
        }
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        // Aquí va su código.



        for(Vertice vertice: vertices){
            eligeEstructura(vertice.elemento, e -> {}, new Cola<>());
            break;
            }

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
        // Aquí va su código.
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
        // Aquí va su código.
        eligeEstructura(elemento, accion, new Cola<>());
        for(Vertice vertice: vertices)
            vertice.color=Color.NINGUNO;
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
        // Aquí va su código.
        eligeEstructura(elemento, accion, new Pila<>());
        for(Vertice vertice: vertices)
            vertice.color=Color.NINGUNO;
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        vertices.limpia();
        aristas=0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        // Aquí va su código.
        String elementos="{";
        for(Vertice vertice: vertices)
            elementos+=vertice.elemento.toString()+", ";
        elementos+="}, ";

        String aristas="{";

        Lista<T> pasados=new Lista<>();
        for(Vertice vertice: vertices){
            for (Vecino vecino: vertice.vecinos) {
                if (!pasados.contiene(vecino.get()))
                    aristas += "(" + vertice.elemento.toString() + ", " + vecino.get().toString() + "), ";
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
        // Aquí va su código.
        if(getElementos() !=grafica.getElementos() || aristas !=grafica.aristas)
            return false;

        for(Vertice vertice: vertices) {

            Vertice verticeGrafica = grafica.busca(vertice.elemento);

            if (!grafica.contiene(vertice.elemento))
                return false;

            if(vertice.vecinos.getElementos() != verticeGrafica.vecinos.getElementos())
                return false;

            boolean tiene=false;

            for(Vecino vecino: vertice.vecinos)
                for(Vecino vecino2: verticeGrafica.vecinos)
                    if(vecino.get().equals(vecino2.get()))
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

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        // Aquí va su código.
        Lista<VerticeGrafica<T>> trayectoria=new Lista<>();

        Vertice inicio=busca(origen);
        Vertice fin=busca(destino);

        if(inicio.equals(fin)){
            trayectoria.agrega(inicio);
            return trayectoria;
        }else
            for (Vertice vertice : vertices)
                vertice.distancia = Double.MAX_VALUE;

        inicio.distancia = 0;

        Cola<Vertice> cola=new Cola<>();
        cola.mete(inicio);

        while (!cola.esVacia()) {
            Vertice sacado=cola.saca();

            for(Vecino vecino: sacado.vecinos)
                if(vecino.vecino.distancia == Double.MAX_VALUE) {
                    vecino.vecino.distancia= sacado.distancia+1;
                    cola.mete(vecino.vecino);
                }

        }

        if(fin.distancia==Double.MAX_VALUE)
            return trayectoria;

        trayectoria.agrega(fin);

        for(int j=0; j<vertices.getElementos();j++)
            for(Vecino vecino: fin.vecinos)
                if (vecino.vecino.distancia+1 == fin.distancia) {
                    trayectoria.agrega(vecino.vecino);
                    fin = vecino.vecino;
                    fin.vecinos=vecino.vecino.vecinos;
                    break;//una vez encontrado no hace falta verificar el resto(en esta iteracion)
                }

        return trayectoria.reversa();
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        // Aquí va su código.
        if(!contiene(origen))
            throw new NoSuchElementException("El vertice origen no esta en la grafica");

        if(!contiene(destino))
            throw new NoSuchElementException("El vertice origen no esta en la grafica");

        Lista<VerticeGrafica<T>> trayectoria=new Lista<>();

        for(Vertice vertice: vertices)
            vertice.distancia=Double.MAX_VALUE;

        Vertice first=busca(origen);
        Vertice last=busca(destino);
        first.distancia=0;

        MonticuloDijkstra<Vertice> monticulo;

        if(aristas>((vertices.getElementos()*(vertices.getElementos()-1))/2))
            monticulo=new MonticuloMinimo<>(vertices, vertices.getElementos());
        else
            monticulo=new MonticuloArreglo<>(vertices, vertices.getElementos());


        while (!monticulo.esVacia()){
            Vertice eliminado= monticulo.elimina();

            for(Vecino vecino: eliminado.vecinos)
                if(vecino.vecino.distancia> (eliminado.distancia+vecino.peso)) {
                    vecino.vecino.distancia = eliminado.distancia + vecino.peso;
                    monticulo.reordena(vecino.vecino);
                }
        }

        if(last.distancia==Double.MAX_VALUE)
            return trayectoria;

        trayectoria.agrega(last);
        for(int i=0; i<vertices.getElementos();i++)
            for (Vecino vecino : last.vecinos)
                if (vecino.vecino.distancia +vecino.peso== last.distancia) {
                    trayectoria.agrega(vecino.vecino);
                    last = vecino.vecino;
                    break;
                }


        return trayectoria.reversa();
    }

    /**
     * Recorre la grafica en orden bfs, si se elige una cola como estructura,
     * o dfs si se elige una pila.
     * @param elemento elemento a partir del que se comencerá a recorrer la grafica
     * @param accion
     * @param estructura elige cola si quieres bfs, pila si quieres dfs
     */
    private void eligeEstructura(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> estructura){

        Vertice vertice=busca(elemento);

        for(Vertice v: vertices)
            v.color=Color.ROJO;

        vertice.color=Color.NEGRO;
        estructura.mete(vertice);

        while (!estructura.esVacia()) {
            Vertice sacado=estructura.saca();
            accion.actua(sacado);

            for(Vecino vecino: sacado.vecinos)
                if(vecino.getColor() ==Color.ROJO) {
                    setColor(vecino, Color.NEGRO);
                    estructura.mete(vecino.vecino);
                }

        }
    }

    /**
     * Regresa el vertice de la grafica que tiene el elemento.
     * @param elemento el elemento a buscar
     * @return el vertice que contiene el elemento.
     */

    private Vertice busca(T elemento){


        for(Vertice vertices: vertices)
            if(vertices.elemento.equals(elemento))
                return vertices;

        throw new NoSuchElementException("No está el elemento");
    }

    /**
     * Regresa el Vecino del vertice que se pone como primer parametro
     * @param a el primer vertice
     * @param b el segundo vertice
     * @return el Vecino de a
     */
    private Vecino obtenerVecino(Vertice a, Vertice b) {

        for (Vecino vecino: a.vecinos)
            if (vecino.vecino.equals(b))
                return vecino;

        return null;
    }
}
