package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.ArbolBinarioOrdenado;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeArbolBinario;

public class FabricaDeArbolBinarioOrdenado extends PropiedadesGrafica{
    private ArbolBinarioOrdenado<Integer> datos;
    private String svg;
    private String linea;
    private String vertices;

    public FabricaDeArbolBinarioOrdenado(Lista<Integer> datos){
        this.datos=new ArbolBinarioOrdenado<>(datos);
        linea="";
        vertices="";
    }

    @Override
    public void dibujar() {

        svg = INICIO;
        int altura=(datos.altura()*2+1)*41+10;
        int largo=datos.getElementos()*50+10;   //los ordenados son más altos, necesitan de más espacio
        svg +=tamanioLienzo(largo, altura);
        dibujaAux(0, largo, largo/2,25,datos.raiz());
        svg +=linea;
        svg+=vertices;
        svg +=FINAL;
        svg +=System.out.printf(svg);

    }

    private void dibujaAux(int inicio, int fin, int x, int y, VerticeArbolBinario<Integer> vertice){
        vertices +=dibujaVertice(x,y);
        vertices += dibujaElemento(x-2, y+2, vertice.get(), "black");
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
}
