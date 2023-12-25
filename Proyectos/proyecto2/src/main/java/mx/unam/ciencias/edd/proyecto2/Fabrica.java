package mx.unam.ciencias.edd.proyecto2;


public abstract class Fabrica implements DibujaEstructura{

    /*El inicio del archivo*/
    public final String INICIO="<?xml version='1.0' encoding='UTF-8' ?>\n";
    /*El cierre del archivo*/
    public final String FINAL= "\t</g>\n"+"</svg>\n";
    /*El tamaño de la imagen*/
    public String tamanioLienzo(int width, int height){
        String s= "<svg width='"+width+"' height='"+height+"'>\n\t<g>\n";
        return s;
    }

    /**
     * Dibuja el elemento recibido
     * @param x la coordenada x
     * @param y la coordenada y
     * @param elemento el elemento a dibujar
     * @param color el color del elemento
     * @return El string con todas las propiedades anteriores
     */
    public String dibujaElemento(int x, int y, Object elemento, String color){
        String s= "\t\t<text fill=\""+color+"\" font-family='sans-serif' font-size='10' x='"+x+"' y='"+y+"'>"+elemento+"</text>\n";
        return s;
    }
    /**
     * Dibuja el elemento recibido
     * @param x la coordenada x
     * @param y la coordenada y
     * @param cadena la cadena a dibujar
     * @return El string con todas las propiedades anteriores
     */
    public String escribeTexto(int x, int y, Object cadena){
        String s="\t\t<text x=\""+x+"\" y=\""+y+"\">"+cadena+"</text>";
        return s;
    }

}
