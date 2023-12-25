package mx.unam.ciencias.edd.proyecto1;
import mx.unam.ciencias.edd.Lista;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class ReaderAndWritter {
     /*Bandera revera */
     static boolean reversa=false;
     /*Bandera guardar */
     static boolean guardar=false;
      /*Identificador de la bandera guardar*/
     static String identificador=null;

    /**
     * Concatena los posibles multiples archivos a leer, para
     * poder tratarlos como un solo archivo.
     * @return Lista de BufferedReaders
     */
    private static Lista<BufferedReader> concatena(Lista<String> recibe) {

        if(Banderas.BanderaReversa(recibe))
            reversa=true;

        identificador = Banderas.BanderaGuarda(recibe);

        if(identificador !=null)
            guardar=true;

        Lista<BufferedReader> archivos=new Lista<>();

        for (String datos : recibe) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(datos)));
                archivos.agrega(br);
            } catch (FileNotFoundException e) {
                System.out.println("Error al buscar "+e.getLocalizedMessage());
            }
        }
        return archivos;
    }

    /**
     * Guarda una lista de lineas(el cual sera ordenado por el
     * ordenadorLexicografico) en el archivo especificado
     */
    private static void guardaArchivo(Lista<Linea> lineas, String archivo){

        try{
            BufferedWriter bw =new BufferedWriter(new FileWriter(archivo));
            for(Linea cadena: lineas) {
                bw.write(cadena.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error");
        }

    }

    /**
     * Lee los parametros recibidos en la linea de comandos
     * el metodo engloba todos los casos en que se reciban
     * las banderas
     */
    public static void leerParametros(Lista<String> archivos) {

        Lista<BufferedReader> buffereds=concatena(archivos);
        Lista<Linea> cadenas = new Lista<>();
        String linea;

        try {
            for (BufferedReader br : buffereds)
                while ((linea = br.readLine()) != null)
                    cadenas.agrega(new Linea(linea));

            if(reversa && guardar) {
                guardaArchivo(OrdenadorLexicografico.ordenaAlReves(cadenas), identificador);
                return;
            }

            if(reversa) {
                OrdenadorLexicografico.ordenaAlReves(cadenas);
                return;
            }
            if(guardar) {
                guardaArchivo(OrdenadorLexicografico.ordena(cadenas), identificador);
                return;
            }
            OrdenadorLexicografico.ordena(cadenas);

        }
        catch(IOException e){
                System.out.println("Error");
            }
    }

    /**
     * Lee los parametros recibidos en la entrada estandar
     * el metodo engloba todos los casos en que se reciban
     * las banderas
     */

    public static void entradaEstandar(Lista<String> recibe){

        if(recibe.getLongitud() !=0) { //Verifica si se pasan banderas
            if(!recibe.contiene("-o") && !recibe.contiene("-r")){ //Verifica si se reciben parametros no deseados
                System.out.println("Solo se aceptan las banderas -r o -o");
                System.exit(1);
            }
            identificador = Banderas.BanderaGuarda(recibe);
        }

        if(identificador !=null)
            guardar=true;

        if(Banderas.BanderaReversa(recibe))
            reversa=true;

        Lista<Linea> cadenas=new Lista<>();

        try {
                BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
                String linea = read.readLine();

                while (linea != null) {
                    cadenas.agrega(new Linea(linea));
                    linea = read.readLine();
                }
                if(reversa && guardar) {
                    guardaArchivo(OrdenadorLexicografico.ordenaAlReves(cadenas), identificador);
                    return;
                }

                if(reversa) {
                    OrdenadorLexicografico.ordenaAlReves(cadenas);
                    return;
                }
                if(guardar) {
                    guardaArchivo(OrdenadorLexicografico.ordena(cadenas), identificador);
                    return;
                }
                OrdenadorLexicografico.ordena(cadenas);
        }catch (IOException e){
            System.out.println("ioexesfdfjods");
        }
    }

}
