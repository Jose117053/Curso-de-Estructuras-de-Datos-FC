package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.Lista;

public class Principal {

    public static void main(String[] args){ //Calificacion recibida: 9, debido a el main, mejorarlo.


        Lista<String> argumentos=new Lista<>();
        for (String arg : args)
            argumentos.agrega(arg);

        if(System.console()==null) {                        //si System.console()==null se está trabajando con la entrada estandar
            ReaderAndWritter.entradaEstandar(argumentos);
        }else if(args.length>0){                            //si System.console() no es null y args.lenght>0 se está trabajando con parametros
            ReaderAndWritter.leerParametros(argumentos);    //de la linea de comandos
        }else {
            System.out.println("Debe especificar un archivo de texto como argumento");
            System.exit(1);
        }
        }
}
