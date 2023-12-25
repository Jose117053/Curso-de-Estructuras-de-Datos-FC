package mx.unam.ciencias.edd.proyecto2;
import  mx.unam.ciencias.edd.Lista;

import java.io.*;

public class Reader {

    Lista<Integer> datos;

    String tipoEstructura;
    /**
     * lee el archivo dependiendo de donde se esté trabajando
     * (Entrada estandar o el otro)
     */

    public Reader(String[] args){

        datos=new Lista<>();
        tipoEstructura ="";
        BufferedReader br;

    if(args.length == 0){//lee entrada estandar

        br=new BufferedReader(new InputStreamReader(System.in));
        leeEntrada(br);

     }else if(args.length==1){//lee parametros de linea de comandos
        try{

            br=new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
            leeEntrada(br);

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

    }else{

        System.out.println("Error");
        System.exit(1);

        }
    }


    /**
     * lee el archivo recibido y obtiene los datos de lo que
     * se quiere dibujar
     */
    private void leeEntrada(BufferedReader br){

         try{

        /*El caracter de la linea*/
        int caracter;

             boolean soloDebeHabernumeros=false;

        while ((caracter=br.read()) !=-1) { //-1 cuando llega al final del documento


            if (esEspacio(caracter) && tipoEstructura.equals(""))
                continue;

            //salta la linea
            if (esAlmohadilla(caracter)) {
                while (caracter != 10)
                    caracter = br.read();
                continue;
            }

            if (esLetra(caracter)) {
                tipoEstructura += String.valueOf((char) caracter);
                continue;
            }

            if(esSaltoDeLinea(caracter))
                continue;

            String num="";
            if(esGuion(caracter)) {
                num +="-";
                caracter= br.read();
            }


            if(esLetra(caracter)){
                System.out.println("No existen letras negativas! ");
                System.exit(1);
            }

            if (esNumero(caracter)) {

                num += String.valueOf((char) caracter);
                while (esNumero(caracter = br.read()))
                    num += String.valueOf((char) caracter);

                if(esLetra(caracter)){
                    System.out.println("No debes ingresar letras entre medio de numeros!");
                    System.exit(1);
                }

                if (esEspacio(caracter) || esSaltoDeLinea(caracter)) {
                    int i = Integer.parseInt(num);
                    datos.agrega(i);
                    continue;
                }

                //cuando caracter==-1, no hay nada que verificar, es el ultimo caso.
                int i = Integer.parseInt(num);
                datos.agrega(i);

            }

        }
            br.close();
            tipoEstructura = tipoEstructura.trim();
            if(tipoEstructura.equals("")){
                System.out.println("Debes especificar el nombre de una estructura de dato");
                System.exit(1);
            }
        }catch (Exception e){
             e.printStackTrace();
             System.exit(1);

     }
    }

    public Lista<Integer> getDatos(){
        return datos;
    }
    public String getTipoEstructura(){
        return tipoEstructura;
    }

    /**
     * Nos dice si es una letra, considerando el codigo ascii
     * @param entero el entero a evaluar.
     * @return <code>true</code> si <code>entero</code> es una letra,
     *         <code>false</code> en otro caso.
     */
    private boolean esLetra(int entero){
        return (65 <= entero && entero<=90) || (97 <= entero && entero<=122);
    }
    /**
     * Nos dice si es un numero, considerando el codigo ascii.
     * @param entero el entero a evaluar.
     * @return <code>true</code> si <code>entero</code> es un numero,
     *         <code>false</code> en otro caso.
     */
    private boolean esNumero(int entero){
        return 48<=entero && entero<=57;
    }
    private boolean esSaltoDeLinea(int entero){
        return  entero==10 || entero==13;
    }
    private boolean esEspacio(int entero){
        return entero==32;
    }
    private boolean esAlmohadilla(int entero){
        return entero==35;
    }
    private boolean esGuion(int entero){
        return entero==45;
    }




}
