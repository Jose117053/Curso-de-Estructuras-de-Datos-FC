package mx.unam.ciencias.edd.proyecto2;

public abstract class PropiedadesLista extends Fabrica{

    String dibujaCaja(int x, int y){
        String caja= "\t\t<rect x=\""+x+"\" y=\""+y+"\" width=\"30\" height=\"20\" stroke=\"black\" stroke-width=\"1\" fill=\"white\" rx=\"1\" ry=\"1\"/>\n";
        return caja;
    }
    String dibujaLinea(int x1, int y1, int x2, int y2){
        String linea="<line x1='"+x1+"' y1='"+y1+"' x2='"+x2+"' y2='"+y2+"' stroke='black' stroke-width='1' />";
        return linea;
    }

}
