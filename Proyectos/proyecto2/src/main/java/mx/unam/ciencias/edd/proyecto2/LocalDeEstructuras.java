package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;

public class LocalDeEstructuras {

    /**
     * Evalua el tipo de estructura que se pasó en el archivo
     * @param tipo el tipo de estructura que es.
     * @param datos los datos con los que se realizara la estructura
     */

    public DibujaEstructura getTipoDeEstructura(String tipo, Lista<Integer> datos){
        if(datos.esVacia()){
            System.out.println("No ingresaste ningun numero");
            System.exit(1);
        }

        switch (tipo){
            case "LISTA":
                return new FabricaDeLista(datos);
            case "PILA":
                return new FabricaDePila(datos);
            case "COLA":
                return new FabricaDeCola(datos);
            case "ARBOLBINARIOCOMPLETO":
                return new FabricaDeArbolBinarioCompleto(datos);
            case "ARBOLAVL":
                return new FabricaDeArbolAVL(datos);
            case "ARBOLROJINEGRO":
                return new FabricaDeArbolRojinegro(datos);
            case "ARBOLBINARIOORDENADO":
                return new FabricaDeArbolBinarioOrdenado(datos);
            case "GRAFICA":
                if(datos.getElementos() %2 !=0) {
                    System.out.println("debes ingresar un numero par de elementos");
                    System.exit(1);
                }
                return new FabricaDeGrafica(datos);
            default:
                return null;
        }

    }
}
