package mx.unam.ciencias.edd.proyecto2;

public abstract class PropiedadesGrafica extends Fabrica {

    /**
     * Dibuja un vertice, representado como un circulo de radio 20
     * @param x la coordenada x
     * @param y la coordenada y
     */
    public  String dibujaVertice(int x, int y){
        String vertice= "\t\t<circle cx='"+x+"' cy='"+y+"' r='20' stroke='red' stroke-width='1' fill='white' />\n"; //stroke es el contorno
        return vertice;
    }
    /**
     * Dibuja una linea.
     * Une dos vertices.
     */
    public String dibujaLinea(int x1, int y1, int x2, int y2){
        String s="\t\t<line x1='"+x1+"' y1='"+y1+"' x2='"+x2+"' y2='"+y2+"' stroke='blue' stroke-width='1' />\n";
        return s;
    }




}
