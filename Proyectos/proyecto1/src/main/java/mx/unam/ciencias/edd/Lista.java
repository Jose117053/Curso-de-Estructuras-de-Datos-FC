package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            this.elemento=elemento;
            anterior=null;
            siguiente=null;

        }
    }
    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            // Aquí va su código.
            anterior=null;
            siguiente=cabeza;

        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return siguiente !=null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if(siguiente==null)
                throw new NoSuchElementException("No hay siguiente :(");
            anterior=siguiente;
            siguiente=siguiente.siguiente;
            return anterior.elemento; //es s
        }
        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            // Aquí va su código.
            return anterior!=null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if(anterior==null)
                throw new NoSuchElementException("No hay anterior :(");
            siguiente=anterior;
            anterior=anterior.anterior;
            return siguiente.elemento; //es a
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            // Aquí va su código.
            //if(siguiente==null)
            anterior=null;
            siguiente=cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            // Aquí va su código.
            siguiente=null;
            anterior=rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        // Aquí va su código.
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return getLongitud();
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return longitud == 0;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.

        if (elemento==null)
            throw new IllegalArgumentException("Elemento no valido :D");
        longitud++;
        Nodo n=new Nodo(elemento);
        if(rabo==null){
            cabeza=rabo=n;
        }else{
            rabo.siguiente=n;
            n.anterior=rabo;
            rabo=n;
        }
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        // Aquí va su código.
        agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        // Aquí va su código.
        if (elemento==null)
            throw new IllegalArgumentException("UN error ha occurrido(El elemento ingresado es null) :D");
        longitud++;
        Nodo n=new Nodo(elemento);
        if(cabeza==null){
            cabeza=rabo=n;
        }else{
            cabeza.anterior=n;
            n.siguiente=cabeza;
            cabeza=n;
        }
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        // Aquí va su código.
        if(elemento==null)
            throw new IllegalArgumentException("No se acepta null como parametro");
        if(i<=0) {
            agregaInicio(elemento);
            return;
        }
        if(i>=longitud) {
            agregaFinal(elemento);
            return;
        }
        longitud++;
        Nodo n=new Nodo(elemento);
        Nodo s=iesimo(i); //metodo auxiliar definido unas lineas abajo
        Nodo a=s.anterior;
        n.anterior=a;
        a.siguiente=n;
        n.siguiente=s;
        s.anterior=n;
    }
    //////////////metodo auxiliar que busca un  Nodo y lo devuelve

    private  Nodo buscaNodo(T elemento){
        Nodo n=cabeza;
        while(n!=null){
            if(n.elemento.equals(elemento))
                return  n;
            n=n.siguiente;
        }
        return null;
    }
    ////////////metodo auxiliar que regresa el iesimo Nodo
    private Nodo iesimo(int i){
        Nodo c=cabeza;
        for(int contador=0; contador<i; contador++){
            c=c.siguiente;
        }
        return c;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Nodo n=buscaNodo(elemento);
        if(n==null)
            return;

        longitud--;

        if(cabeza==rabo) {
            cabeza = rabo = null;
            return;
        }

        if(n==cabeza){
            Nodo s=cabeza.siguiente;
            s.anterior=null;
            cabeza=s;
            return;
        }

        if(n==rabo){
            Nodo s=rabo.anterior;
            s.siguiente=null;
            rabo=s;
            return;
        }

        Nodo a= n.anterior;
        Nodo s= n.siguiente;
        a.siguiente=s;
        s.anterior=a;
    }


    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        // Aquí va su código.
        if(cabeza==null)
            throw new NoSuchElementException("La lista es vacia D:");

        longitud--;
        T removed= cabeza.elemento;
        if(cabeza==rabo){
            cabeza=rabo=null;
        }else{
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        }
        return removed;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        // Aquí va su código.
        if(rabo==null)
            throw new NoSuchElementException("La lista es vacia D:");

        longitud--;
        T removed=rabo.elemento;
        if(rabo==cabeza){
            cabeza=rabo=null;
        }else {
            rabo = rabo.anterior;
            rabo.siguiente = null;
        }
        return removed;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        Nodo n= buscaNodo(elemento);
        return n != null;
    }
    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        // Aquí va su código.
        Lista<T> copionAlReves = new Lista<T>();
        Nodo c=cabeza;
        while(c !=null){
            copionAlReves.agregaInicio(c.elemento);
            c=c.siguiente;
        }
        return copionAlReves;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        // Aquí va su código.
        Lista<T> copion = new Lista<T>();
        Nodo c=cabeza;
        while(c !=null){
            copion.agrega(c.elemento);
            c=c.siguiente;
        }
        return copion;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        cabeza=rabo=null;
        longitud=0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        // Aquí va su código.
        Nodo c=cabeza;
        if(c==null)
            throw new NoSuchElementException("La lista está vacia :(");
        return c.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        // Aquí va su código.
        if(rabo ==null)
            throw new NoSuchElementException("La lista es vacia :(");
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        // Aquí va su código.
        if(i<0)
            throw new ExcepcionIndiceInvalido("No se permiten numeros menores a 0");
        if(i>=longitud)
            throw new ExcepcionIndiceInvalido("El indice ingresado es mayor a la longitud de la lista");
        return iesimo(i).elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        // Aquí va su código.
        int contador=0;
        Nodo c=cabeza;
        while (c !=null){
            if (c.elemento.equals(elemento))
                return contador;
            c=c.siguiente;
            contador++;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {

        // Aquí va su código.
        if(cabeza==null)
            return "[]";
        String r="["+cabeza.elemento;
        Nodo sig= cabeza.siguiente;
        while(sig!=null){
            r=r+", "+sig.elemento;
            sig=sig.siguiente;
        }
        return r+"]";
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        // Aquí va su código.

        if(longitud !=lista.longitud)
            return false;

        Nodo c1= cabeza;
        Nodo c2= lista.cabeza;
        while(c1 !=null) {
            if (!c1.elemento.equals(c2.elemento))
                return false;
            c1 = c1.siguiente;
            c2 = c2.siguiente;
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        // Aquí va su código.
        if(this.longitud<=1)
            return this.copia();
        Lista<T> l1=new Lista<>();
        Lista<T> l2=new Lista<>();
        int i=this.longitud/2;
        l1=partirEn2(0,i);
        l2=partirEn2(i,this.longitud);
        l1=l1.mergeSort(comparador);
        l2=l2.mergeSort(comparador);
        return mezcla(l1, l2, comparador);
    }
    private Lista<T> partirEn2(int i, int j){
        Nodo primero=iesimo(i);
        Lista<T> mitad= new Lista<>();

        while(i!=j){
            mitad.agrega(primero.elemento);
            primero=primero.siguiente;
            i++;
        }
        return mitad;
    }
    private   Lista<T> mezcla(Lista<T> lis1, Lista<T> lis2, Comparator<T> comparador){
        //creo no funciona o si? idk
        Lista<T> lfinal=new Lista<>();
        Nodo i=lis1.cabeza;
        Nodo j=lis2.cabeza;
        while(i !=null && j!=null){
            if(comparador.compare(i.elemento,j.elemento)<=0){
                lfinal.agrega(i.elemento);
                i=i.siguiente;
            }else{
                lfinal.agrega(j.elemento);
                j=j.siguiente;
            }
        }
        while(i!=null){
            lfinal.agrega(i.elemento);
            i=i.siguiente;
        }
        while(j!=null){
            lfinal.agrega(j.elemento);
            j=j.siguiente;
        }
        return lfinal;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        // Aquí va su código.
        //creo que solo comprueba que un elemento está en la lista.
        while(this.cabeza !=null){
            if(comparador.compare(elemento, this.cabeza.elemento)==0)
                return true;
            this.cabeza=this.cabeza.siguiente;
        }
        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }

}
