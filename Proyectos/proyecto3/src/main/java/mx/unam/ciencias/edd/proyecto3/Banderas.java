package mx.unam.ciencias.edd.proyecto3;

/**
 * Clase que verifica que se hayan pasado correctamente
 * los parametros de la linea de comandos al usar el
 * programa.
 */

public class Banderas {

    /*Las columnas del laberinto*/
    static int columnas;
    /*Los renglones del laberinto*/
    static int renglones;
    /*Marcador de la bandera -s*/
    static Boolean semilla=false;
    /*La semilla del laberinto*/
    static int semillaInt;
    /*Marcador de la bandera -g*/
    static boolean generaLaberinto=false;

    /**
     * Indica como se usa el programa
     */
    private static void uso(){
        System.err.println("Uso: java -jar target/proyecto3.jar -g -w N -h N");
        System.err.println("Donde N es un entero mayor a 1 y menor que 256");
        System.exit(1);
    }

    /**
     *  Regresa el entero que está en la posicion siguiente a la posicion
     *  actual. Ocurre un error si no hay elemento siguiente o si el
     *  elemento en la posicion i+1 no es un entero.
     * @param i el indice actual
     */
    private static int getSiguienteIteracion(int i, String[] argumentos){
        int entero=0;
        if(i+1 >= argumentos.length) {
            if(semilla)
                System.err.println("Debes ingresar un numero entero despues de la bandera -s");

            uso();
        }

        try{
            entero = Integer.parseInt(argumentos[i + 1]);
        } catch(NumberFormatException e ) {
            if(semilla)
                System.err.println("Debes ingresar un numero entero despues de la bandera -s");

            uso();
        }
        return entero;
    }

    /** Revisa los parametros recibidos y los guarda, para
     *  su posterior uso en la creacion del laberinto.
     */

    public static void revisaArgumentos(String[] argumentos){

        for(int i=0; i< argumentos.length; i++){

            if(argumentos[i].equals("-w")) {
                columnas= getSiguienteIteracion(i, argumentos);
                continue;
            }
            if(argumentos[i].equals("-h")){
                renglones= getSiguienteIteracion(i, argumentos);
                continue;
            }

            if(argumentos[i].equals("-g")) {
                generaLaberinto = true;
                continue;
            }

            if(argumentos[i].equals("-s")) {
                semilla = true;
                semillaInt= getSiguienteIteracion(i, argumentos);
            }

        }
        if (!generaLaberinto || columnas < 2 || renglones < 2 || columnas > 255 || renglones > 255)
            uso();

    }
}
