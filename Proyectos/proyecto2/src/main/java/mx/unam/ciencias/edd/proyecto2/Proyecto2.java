package mx.unam.ciencias.edd.proyecto2;
import mx.unam.ciencias.edd.*;

public class Proyecto2 {

    public static void main(String [] args){//Hay demasiadas cosas a mejorar, especialmente la parte de Graficas.
                                            //Calificacion recibida: 10

        try {
            Reader uno = new Reader(args);
            Lista<Integer> datos = uno.getDatos();

            String estructura = uno.getTipoEstructura();
            LocalDeEstructuras local=new LocalDeEstructuras();
            DibujaEstructura edd=local.getTipoDeEstructura(estructura.toUpperCase(), datos);

            if(edd==null) { //caso default de getTipoDeEstructura: No se recibio alguna estructura valida
                System.out.println("Nombre de estructura de dato no permitido");
                System.exit(1);
            }
            edd.dibujar();
        }catch (Exception e){
            System.out.println("Error");
        }
    }
}
