package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;

public class FabricaDeCola extends PropiedadesLista{

    private Lista<Integer> datos;
    private String svg;

    public FabricaDeCola(Lista<Integer> datos){
        this.datos=datos;
    }
    @Override
    public void dibujar() {

        svg=INICIO;
        int altura=30;
        int numElementos=datos.getElementos();
        int largo=(30*numElementos)+10;//10 de margen, -1 de una flecha
        svg+=tamanioLienzo(largo, altura);

        int ejeX=35;

        int i=1;
        svg+=dibujaLinea(5,5,largo-5,5);
        svg+=dibujaLinea(5,25,largo-5,25);
        int j=18;

        for(Integer elemento: datos){


            svg +=dibujaElemento(j, 19, elemento, "black");
            j +=30;

            if(i==numElementos)
                break;

            svg +=dibujaLinea(ejeX, 5,ejeX,25);
            ejeX+=30;

            i++;
        }

        svg+=FINAL;
        System.out.println(svg);

    }
}
