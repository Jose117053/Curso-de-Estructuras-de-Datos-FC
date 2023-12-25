package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.VerticeArbolBinario;

public class FabricaDeArbolAVL extends PropiedadesGrafica {

    private ArbolAVL<Integer> datos;
    private String svg;
    private String linea;
    private String vertices;

    /**
     * El constructor de la FabricaDeArbolAVL
     */

    public FabricaDeArbolAVL(Lista<Integer> datos){
        this.datos=new ArbolAVL<>(datos);
        linea="";
        vertices="";
    }

    @Override
    public void dibujar() {
        svg = INICIO;
        int altura=(datos.altura()*2+1)*41+20;
        int largo=datos.getElementos()*41+10;   //circulos de radio 41, 10 de margen
        svg +=tamanioLienzo(largo, altura);
        dibujaAux(0, largo, largo/2+10,40,datos.raiz());
        svg +=linea;
        svg+=vertices;
        svg +=FINAL;
        svg +=System.out.printf(svg);
    }

    private void dibujaAux(int inicio, int fin, int x, int y, VerticeArbolBinario<Integer> vertice){
        vertices +=dibujaVertice(x,y);
        vertices += dibujaElemento(x-3, y+3, vertice.get(), "black");
        vertices += escribeTexto(x, y-25, "{"+getAlturaYBalance(vertice)+"}");

        if(vertice.hayIzquierdo()){
            int xi=(x+inicio)/2;
            int yi=y+80;
            linea +=dibujaLinea(x, y, xi, yi);
            dibujaAux(inicio,x, xi, yi, vertice.izquierdo());
        }
        if(vertice.hayDerecho()){
            int xd=(x+fin)/2;
            int yd=y+80;
            linea+=dibujaLinea(x, y, xd, yd);
            dibujaAux(x, fin, xd, yd, vertice.derecho());
        }
    }
    /**
     * Devuelve la Altura y balance del vertice
     * @param vertice vertice a obtener su altura y balance
     * @return Un string con la altura y balance del vertice
     */
    private String getAlturaYBalance(VerticeArbolBinario<Integer> vertice){
        String tostring=vertice.toString();
        String[] dato=tostring.split("\\s");
        return dato[1].toString();
    }
}
