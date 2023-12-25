package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.*;

public class FabricaDeArbolRojinegro extends PropiedadesGrafica {

    private ArbolRojinegro<Integer> datos;
    private String svg;
    private String linea;
    private String vertices;
    public FabricaDeArbolRojinegro(Lista<Integer> datos){
        this.datos=new ArbolRojinegro<>(datos);
        linea="";
        vertices="";
    }

    @Override
    public void dibujar() {

        svg = INICIO;
        int altura=(datos.altura()*2+1)*41+10;
        int largo=datos.getElementos()*50+10;
        svg +=tamanioLienzo(largo, altura);
        dibujaAux(0, largo, largo/2,25,datos.raiz());
        svg +=linea;
        svg+=vertices;
        svg +=FINAL;
        svg +=System.out.printf(svg);

    }

    private void dibujaAux(int inicio, int fin, int x, int y, VerticeArbolBinario<Integer> vertice){
        vertices +=dibujaVerticeRojinegro(x,y, vertice);
        vertices += dibujaElemento(x-2, y+2, vertice.get(), "white");
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
     * Dibuja el vertice segun sea color(rojo o negro)
     * @param x la coordenada x del vertice
     * @param y la coordenada y del vertice
     * @param  vertice el vertice a dibujar
     * @return un string que dibuja el vertice segun el color del vertice
     */
    private String dibujaVerticeRojinegro(int x, int y,VerticeArbolBinario<Integer> vertice){
        String datoVertice=vertice.toString();
        boolean esRojo=datoVertice.contains("R");
        String fin;
       if(esRojo)
             fin= "\t\t<circle cx='"+x+"' cy='"+y+"' r='20' stroke='red' stroke-width='1' fill='red' />\n"; //stroke es el contorno
        else
            fin="\t\t<circle cx='"+x+"' cy='"+y+"' r='20' stroke='red' stroke-width='1' fill='black' />\n";
        return fin;
    }

}
