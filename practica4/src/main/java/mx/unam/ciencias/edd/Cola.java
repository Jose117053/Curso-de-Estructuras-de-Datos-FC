package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        // Aquí va su código.
        Nodo n=cabeza;
        String r="";
        while(n !=null){
            r+=n.elemento.toString()+",";
            n=n.siguiente;
        }
        return r;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        // Aquí va su código.
        if (elemento == null)
            throw new IllegalArgumentException("El elemento ingresado es nulo");
        Nodo n = new Nodo(elemento);
        if (rabo == null) {
            cabeza = rabo = n;
        } else {
            rabo.siguiente = n;
            rabo = n;
        }
    }
}
