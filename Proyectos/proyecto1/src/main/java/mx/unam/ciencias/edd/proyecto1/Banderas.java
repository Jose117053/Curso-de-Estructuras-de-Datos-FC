package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.Lista;
public class Banderas {

    /**
     * Verifica si la bandera -r está entre los parametros recibidos
     * @param archivos los archivos a verificar.
     * @return true si la bandera -r está, false en otro caso.
     */

    public static boolean BanderaReversa(Lista<String> archivos){
        boolean tiene = archivos.contiene("-r");
        if(tiene){
            while(archivos.contiene("-r")){
                archivos.elimina("-r");
            }
            if(System.console() !=null && archivos.getLongitud() ==0) {//si system.console es igual a null, se está trabajando en entrada estandar, por lo que no es necesario imprimir este mensaje.
                System.out.println("El argumento -r necesita un archivo a leer");
                System.exit(1);
            }
            return true;
        }
        return false;
    }

    /**
     * Verifica si la bandera -o está entre los parametros recibidos,
     * además, comprueba si se paso mas de una vez la bandera -o con
     * salidas distintas, o si no tiene identificador
     * @param archivos los archivos a verificar.
     * @return null si archivos no contiene la bandera -o, en otro caso
     * devolverá el String con el identificador.
     */
    public static String BanderaGuarda(Lista<String> archivos){

        if(!archivos.contiene("-o"))
            return null;

        String identificador;

        int indicetemporal= archivos.indiceDe("-o");
        int indiceIdentificador=indicetemporal+1;

        if(archivos.getLongitud()<=indiceIdentificador ) {//verifica que -o tenga identificador
            System.out.println("Despues de la bandera -o ingresa el nombre del archivo en el que quieres guardar"); //caso en que pasan una bandera -o
            System.exit(1);                                                                                   // sin identificador
        }

        identificador=archivos.get(indiceIdentificador);

        while(archivos.contiene("-o")){         //while que considera dos o más banderas -o proporcionados
            int indice= archivos.indiceDe("-o");
            int indiceMasUno=indice+1;

            if(archivos.getLongitud()<=indiceMasUno) {
                System.out.println("despues de la bandera -o ingresa el nombre del archivo en el que quieres guardar"); //caso en que pasan mas de una bandera -o
                System.exit(1);                                                                                   // con alguno sin identificador
            }

            String argumento2=archivos.get(indiceMasUno);

            if(!identificador.equals(argumento2)){
                System.out.println("Multiples salidas de archivo recibidos!");                                          //Caso en que pasan mas de dos banderas -o
                System.exit(1);                                                                                   //con identificadores diferentes
            }
            archivos.elimina(argumento2);//Si no se elimina argumento2, y se
                                        // quiere sobreescribir el archivo, leera el archivo además del otro,
                                        // por lo que leera 2 archivos lo cual no es deseado.
           archivos.elimina("-o");
        }

        return identificador;
    }
}
