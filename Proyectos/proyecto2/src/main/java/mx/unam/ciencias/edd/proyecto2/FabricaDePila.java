package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;

public class FabricaDePila extends PropiedadesLista{
    private Lista<Integer> datos;
    private String svg;

    public FabricaDePila(Lista<Integer> datos){
        this.datos=datos;
    }
    @Override
    public void dibujar() {

        svg=INICIO;
        int largo=30;
        int numElementos=datos.getElementos();
        int altura=(30*numElementos)+10;
        svg+=tamanioLienzo(largo, altura);

        int ejeY=35;

        if(datos.esVacia())
            throw new IllegalArgumentException("La lista es vacia");

        //Los bordes izquierdo y derecho de la pila, se considera un margen de longitud 5
        svg+=dibujaLinea(5,5,5,altura-5);
        svg+=dibujaLinea(25,5,25,altura-5);

        //El borde inferior de la pila
        svg+=dibujaLinea(5, altura-5, 25, altura-5);
        //perdone los pixeles que se fueron en las esquinas :(

        int i=1;
        int j=23;
        for(Integer elemento: datos){

            svg +=dibujaElemento(12, altura-j+5, elemento, "black");
            j +=30;
            if(i==numElementos)
                break;

            svg +=dibujaLinea(5, ejeY,25,ejeY);
            ejeY+=30;
            i++;
        }

        svg+=FINAL;
        System.out.println(svg);

    }
}
