package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedInputStream;

public class Proyecto3 {
    public static void main(String[] args){//Todo funciona como deberia, no te recomiendo copiar este proyecto

        if (args.length == 0) {

            BufferedInputStream in = new BufferedInputStream(System.in);
            Laberinto laberinto = new Laberinto(in);
            laberinto.resuelveLaberinto();
            System.out.println(laberinto.toSVG());

        }else{
            Banderas.revisaArgumentos(args);
            Laberinto laberinto = new Laberinto(Banderas.columnas,Banderas.renglones, Banderas.semillaInt);
            laberinto.imprimeBytes();

        }

    }


}
