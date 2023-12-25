package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;

public class FabricaDeLista extends PropiedadesLista{

    private Lista<Integer> datos;
    private String svg;

    /**
     * constructor de la FabricaDeLista
     */
    public FabricaDeLista(Lista<Integer> datos){
        this.datos=datos;
    }

    @Override
    public void dibujar() {

        svg=INICIO;
        int altura=30;
        int numElementos=datos.getElementos();
        int largo=(30*numElementos)+(8*(numElementos-1)+(5*(numElementos-1)))+10;
        svg+=tamanioLienzo(largo, altura);

        if(datos.esVacia())
            throw new IllegalArgumentException("La lista es vacia");
        int ejeX=5;
        int i=1;

        for(Integer elemento: datos){

            svg +=dibujaCaja(ejeX, 5);
            ejeX+=12;
            svg +=dibujaElemento(ejeX, 19, elemento, "black");
            ejeX +=20;

            //Caso cuando se llega al final de la lista: no deseamos dibujar mas flechas
            if(i==numElementos)
                break;

            svg += dibujaFlecha(ejeX, 18);
            ejeX += 11;
            i++;

        }

        svg+=FINAL;
        System.out.println(svg);
    }

    private String dibujaFlecha(int x, int y){
        return dibujaElemento(x, y,"↔","black");
    }
}
