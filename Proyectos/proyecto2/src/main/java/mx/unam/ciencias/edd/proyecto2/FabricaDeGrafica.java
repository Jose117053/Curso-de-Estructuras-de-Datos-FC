package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.*;
public class FabricaDeGrafica extends PropiedadesGrafica {

    private Grafica<Integer> datos;
    Lista<Integer> recibidos;
    Lista<Integer> copia;
    Lista<Integer> verticesAislados;
    private String svg;
    private String linea;
    private String vertices;
    private int angulo;
    private int altura;
    private int largo;
    private int extraDeLargo;

    /**
     * Constructor de la grafica
     * @param lista En base a esta lista se crea la grafica
     */

    public FabricaDeGrafica(Lista<Integer> lista){
        verticesAislados=new Lista<>();

        /*Lista que se ocupara para graficar la grafica*/
        recibidos=new Lista<>();
        this.datos=new Grafica<>();
        this.copia=lista;
        int cantidad=lista.getElementos();

        for(int i=0; i<cantidad;i+=2) {
            int j=i+1;
            int indice=lista.get(i);
                if(!datos.contiene(indice)){
                    datos.agrega(indice);
                    recibidos.agrega(indice);
                }
                if(!datos.contiene(lista.get(j))){
                    datos.agrega(lista.get(j));
                    recibidos.agrega(lista.get(j));
                }
            }

        for(int i=0; i<lista.getElementos(); i+=2) {
            int j = i + 1;
            int indice=lista.get(i);
            int indiceMasUno=lista.get(j);

            if(datos.sonVecinos(indice, indiceMasUno))//cuando ya son vecinos, pasamos a la siguiente iteracion
                continue;

            if(indice==indiceMasUno) {//Por ahora solo ignorare cuando hayan dos vertices iguales
                verticesAislados.agrega(indice);
                continue;
            }

            datos.conecta(indice, indiceMasUno);
        }
        linea="";
        vertices="";

    }

    @Override
    public void dibujar() {

        svg = INICIO;
        altura=500;
        largo=500;
        extraDeLargo=500+verticesAislados.getElementos()*200;
        svg +=tamanioLienzo(extraDeLargo, altura);
        angulo=0;

        dibujaAux(recibidos);

        svg +=linea;
        svg+=vertices;
        svg +=FINAL;
        svg +=System.out.printf(svg);

    }

    /**
     * Dibuja los vertices de forma simetrica y guarda sus
     * coordenadas para su posterior uso en dibujar las aristas
     * @param recibidos los datos con que dibuja los vertices
     */

    private void dibujaAux(Lista<Integer> recibidos){
        int aislado=500;
        for(int i=0; i<verticesAislados.getElementos(); i++){

            vertices += dibujaVertice(aislado, (largo/2));
            vertices += dibujaElemento(aislado-2, (largo/2)+2, verticesAislados.get(i), "black");
            aislado+=100;
        }


        int intervalo=360/recibidos.getLongitud();
        Lista<Integer> coordenadasX=new Lista<>();
        Lista<Integer> coordenadasY=new Lista<>();

        int i=0;
        while (i !=recibidos.getElementos()){

            double radian=Math.toRadians(angulo);
            double x=(largo/2)+(seno(radian)*10);
            double y=(altura/2)+(coseno(radian))*10;

            int equis=(int)x;
            int ye=(int)y;

            coordenadasX.agrega(equis);
            coordenadasY.agrega(ye);

            vertices+=dibujaVertice(equis,ye);
            vertices += dibujaElemento(equis-2, ye+2, recibidos.get(i), "black");
            angulo+=intervalo;
            i++;

        }
        dibujaLineas(copia,coordenadasX, coordenadasY, recibidos);


    }
    /**
     * Dibuja las aristas segun dos vertices sean adyacentes
     * @param datos La lista original
     * @param recibidos La lista con solo vertices(No repetidos).
     * @param equis Las coordenadas del eje x de cada elemento de la lista recibidos
     * @param ye Las coordenadas del eje y de cada elemento de la lista recibidos
     */
    private void dibujaLineas(Lista<Integer> datos, Lista<Integer> equis, Lista<Integer> ye, Lista<Integer> recibidos){

        int x1=0;
        int x2=0;
        int y1=0;
        int y2=0;
        for(int i=0; i<datos.getElementos(); i+=2){
            for(int j=0; j<recibidos.getElementos(); j++) {
                if (datos.get(i).equals(recibidos.get(j))) {
                    x1 = equis.get(j);
                    y1 = ye.get(j);
                }
                if(datos.get(i+1).equals(recibidos.get(j))) {
                    x2 = equis.get(j);
                    y2 = ye.get(j);
                }
            }
            linea +=dibujaLinea(x1, y1, x2, y2);
        }
    }

    /**
     * Calcula el seno del angulo
     * @param angulo el angulo de referencia ?
     */
    private double seno(double angulo){//radio 20
        double doble= 20 *Math.sin(angulo);
        return doble;
    }
    /**
     * Calcula el coseno del angulo
     * @param angulo el angulo de referencia ?
     */
    private double coseno(double angulo){
        double doble= 20 *Math.cos(angulo);
        return doble;
    }
}
