package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Random;

/** Clase que reconstruye o crea un laberinto en
 *  formato svg.
 */
public class Laberinto implements Iterable<Laberinto.Casilla>{

    private class Iterador implements Iterator<Casilla> {
        /* Los renglones del laberinto*/
        private int renglones;
        /**Las columnas del laberinto*/
        private int columna;
        /* Nos dice si hay un elemento siguiente. */
        @Override
        public boolean hasNext() {
            return renglones !=alto && columna !=ancho;
        }

        /* Nos da el elemento siguiente. */
        @Override
        public Casilla next() {
            Casilla actual = laberinto[renglones][columna];
            if(columna == ancho-1){
                columna=0;
                renglones = renglones +1;
                return actual;
            }
            columna = columna+1;
            return actual;
        }
    }
    /**
     * Regresa un iterador para recorrer el laberinto.
     * @return un iterador para recorrer el laberinto.
     */
    @Override
    public Iterator<Casilla> iterator() {
        return new Iterador();
    }

    /** Clase para las casillas del laberinto*/

    public class Casilla{
        /* Puerta norte de la casilla*/
        private boolean norte;
        /* Puerta sur. */
        private boolean sur;
        /* Puerta este. */
        private boolean este;
        /* Puerta oeste. */
        private boolean oeste;
        /* Marcador de casillas para saber si ya lo hemos visitado. */
        private boolean visitado;
        /* Posicion en X de la casilla. */
        private int coordX;
        /* Posicion en Y de la casilla. */
        private int coordY;
        /* El tipo de casilla. */
        private int tipo;
        /* Inicio del laberinto. */
        private boolean inicio;
        /* Casilla destino. */
        private boolean fin;
        /* Puntaje de la casilla. */
        private int puntaje;
        /** Crea una casilla en la posicion (x, y) del laberinto*/
        public Casilla(int coordX, int coordY){
            this.coordX=coordX;
            this.coordY=coordY;
            norte=true;
            sur=true;
            este=true;
            oeste=true;
            asignaTipoCasilla();
            this.puntaje =  random.nextInt(16) ;
        }
        /** Reconstruye una casilla segun la informacion del byte de la casilla. */
        public Casilla(int coordX, int coordY, byte b){

            this.puntaje= (b & 0xF0)>>4;
            int casilla = (b & 0x0F);

            if((casilla & 1) !=0 )
                    este=true;

            if((casilla & (1<<1)) !=0)
                    norte=true;

            if((casilla & (1<<2)) !=0)
                    oeste=true;

            if((casilla & (1<<3)) !=0)
                    sur=true;

            this.coordX= coordX;
            this.coordY=coordY;
            asignaTipoCasilla();
        }

        /** Regresa la representacion en formato byte de una casilla
         * @return el byte de la casilla.
         */
        public byte toByte(){
            int casillaToByte=0;

            if(sur)
                casillaToByte+=1<<3;
            if(oeste)
                casillaToByte+=1<<2;
            if(norte)
                casillaToByte+=1<<1;
            if(este)
                casillaToByte+=1;

            puntaje= puntaje<<4;
            casillaToByte=puntaje+casillaToByte;

            return (byte)(casillaToByte & 0xFF);
        }

        /** Regresa la representacion de una casilla*/

        @Override
        public String toString() {
            return parteArriba() + "\n"+parteAbajo();
        }
        /** Regresa la representacion de la parte superior
         * de la casilla.
         */
        private String parteArriba(){
            String casilla="";
            if(!norte && !este && !oeste)
                casilla= "    ";
            else if(!norte && !oeste)
                casilla= "   |";
            else if(!norte && !este)
                casilla= "|   ";
            else if(!norte)
                casilla= "|  |";
            else if(!oeste && este)
                casilla= " ¯¯|";
            else if(!oeste)
                casilla = " ¯¯ ";
            else if(!este)
                casilla= "|¯¯ ";

            else casilla= "|¯¯|";

            return casilla;
        }
        /** Regresa la representacion de la parte inferior
         * de la casilla.
         */

        private String parteAbajo(){
            String casilla="";
            if(!sur && !este && !oeste)
                casilla= "    ";
            else if(!sur && !oeste && este)
                casilla= "   |";
            else if(!sur && oeste && !este)
                casilla= "|   ";
            else if(!sur && oeste && este)
                casilla= "|  |";
            else if(sur && !oeste && este)
                casilla= " __|";
            else if( sur && !oeste && !este)
                casilla = " __ ";
            else if(sur && oeste && !este)
                casilla= "|__ ";

            else casilla= "|__|";

            return casilla;
        }
        /** Dependiendo de la posicion de la casilla se le asigna
         *  un tipo.
         */

        /*
        |8 ||1 ||2 |
        |__||__||__|
        |7 ||9 ||3 |
        |__||__||__|
        |6 ||5 ||4 |
        |__||__||__|
        */
        private void asignaTipoCasilla(){//se puede factorizar
            if(!norte && !sur && !este && !oeste)
                tipo=0;
            if(coordX == 0 && coordY > 0 && coordY < ancho-1)
                tipo=1;
            if(coordX == 0 && coordY == ancho-1)
                tipo=2;
            if(coordX > 0 && coordX < alto-1 && coordY==ancho-1)
                tipo=3;
            if(coordX == alto-1 && coordY == ancho -1)
                tipo=4;
            if(coordX == alto-1 && coordY>0 && coordY<ancho-1)
                tipo=5;
            if(coordX == alto-1 && coordY == 0)
                tipo=6;
            if(coordX > 0 && coordX < alto-1 && coordY == 0)
                tipo=7;
            if(coordX==0 && coordY==0)
                tipo=8;
            if(coordX !=0 && coordY !=0 && coordX > 0 && coordX < alto-1 && coordY > 0 && coordY < ancho-1)
                tipo=9;

        }
        /**Regresa una linea en formato svg
         * @param x1 la coordenada x1
         * @param y1 la coordenada y1
         * @param x2 la coordenada x2
         * @param y2 la coordenada y1
         * @return una linea en formato svg del punto(x1,y1)
         * al punto (x2, y2)
         */
        private String linea(int x1, int y1, int x2, int y2){
            StringBuilder linea=new StringBuilder();
            linea.append("\t\t<line x1='").append(x1+10).append("' y1='").append(y1+10).append("' x2='").append(x2+10).append("' y2='").append(y2+10).append("' stroke='black' stroke-width='3' />\n");
            return linea.toString();
        }

        /**Regresa la representacion en formato svg de una casilla*/

        private String toSVG(){
            StringBuilder casillaSVG=new StringBuilder();

            int pixeles=20;//dimension de cada casilla en el laberinto, cada casilla es de pixeles x pixeles

            if(norte && coordX==0)
                casillaSVG.append(linea(coordY * pixeles, 0,pixeles+coordY*pixeles, 0));

            if(sur )
                casillaSVG.append(linea(pixeles + coordY*pixeles, pixeles + coordX * pixeles, coordY * pixeles, pixeles + coordX * pixeles ));

            if(este )
                casillaSVG.append(linea(pixeles + coordY * pixeles, coordX * pixeles,pixeles + coordY * pixeles ,pixeles + coordX * pixeles));

            if(oeste && coordY == 0)
                casillaSVG.append(linea(0, pixeles + coordX * pixeles, 0, coordX * pixeles));

            return casillaSVG.toString();

        }

        /** En base a una casilla (de las orillas) ya elegido
         *  se define la puerta que tendra abierta para establecerlo
         *  como el punto de inicio o llegada. Si la casilla es una
         *  de las esquinas, se eligira al azar cual de sus dos opciones
         *  de puertas tendra abierto.
         */
        private void abrePuertasDeInicioYLlegada(){
            if(tipo == 1) {
                norte = false;
                return;
            }
            if(tipo == 3){
                este=false;
                return;
            }
            if(tipo == 5) {
                sur = false;
                return;
            }
            if(tipo == 7) {
                oeste = false;
                return;
            }
            //norte y sur  es true
            //este y oeste es false
            boolean decide=random.nextBoolean();
            if(tipo == 8) {
                if (decide)
                    norte = false;
                else
                    oeste = false;
                return;
            }

            if(tipo == 2) {
                if (decide)
                    norte = false;
                else
                    este = false;
                return;
            }
            if(coordX == alto-1 && coordY == ancho-1) {
                if (decide)
                    sur = false;
                else
                    este = false;
                return;
            }
            if(tipo == 6) {
                if (decide)
                    sur = false;
                else
                    oeste = false;
            }
        }

        /** Regresa la representacion en formato svg de un
         *  vertice.
         * @param radio el radio del vertice
         */

        private String dibujaVertice(int radio){
            StringBuilder circulo = new StringBuilder("\t\t<circle cx='");
            circulo.append(10+10+coordY*20).append("' cy='").append(10+10+coordX*20).append("' r='").append(radio).append("' fill='red' stroke-width='1' />\n");
            return circulo.toString();
        }

    }

    /* El laberinto. */
    private Casilla[][] laberinto;
    /* El inicio del laberinto. */
    private Casilla inicio;
    /* La casilla destino del laberinto. */
    private Casilla fin;
    /* Herramienta para generar laberintos aleatorios. */
    private Random random;
    /* El ancho del laberinto. */
    private  int ancho;
    /* Lo alto laberinto. */
    private int alto;
    /** Laberinto en formato grafica. */
    private Grafica<Casilla> grafica;
    /** Lineas svg con la solucion al laberinto. */
    private StringBuilder svgSolucion;

    /** Construye un laberinto dado el ancho y alto del laberinto.
     * Si se paso la bandera -s se ocupa el RNG con la semilla
     * recibida, de lo contrario utiliza la semilla por omision.
     * @param ancho el ancho (columnas) del laberinto.
     * @param alto el alto (renglones) del laberinto.
     * @param semilla la semilla del laberinto.
     */

    public Laberinto(int ancho, int alto, int semilla){
        random = Banderas.semilla ? new Random(semilla) : new Random();

        this.ancho=ancho;
        this.alto=alto;
        this.laberinto=new Casilla[alto][ancho];

        for(int x=0; x<alto; x++)
            for(int y=0;y<ancho;y++)
                laberinto[x][y] =new Casilla(x,y);

        Lista<Casilla> inicioYFin;
        inicioYFin=seleccionaInicioYFin();

        inicio=inicioYFin.getPrimero();
        inicio.abrePuertasDeInicioYLlegada();
        inicio.inicio=true;

        fin=inicioYFin.getUltimo();
        fin.abrePuertasDeInicioYLlegada();
        fin.fin=true;

        construyeLaberinto();
    }
    /** Reconstruye un laberinto que fue recibido
     * en formato de bytes.
     * @param input el archivo mze del laberinto. */
    public Laberinto(BufferedInputStream input){
        try{

            int indice=0;
            int caracterActual=0;
            int x=0;
            int y=0;

            while ((caracterActual=input.read()) !=-1){
                if(indice >5){//leer casillas
                    laberinto[x][y] = new Casilla(x,y,(byte)caracterActual);
                    if(y==ancho-1){
                        y=0;
                        x=x+1;
                    }else
                        y +=1;

                }else{//verificar que empiece con las especificaciones
                    if(indice== 0 && caracterActual != 0x4d) {//m
                        System.out.println("El primer caracter debe ser 0x4d");
                        System.exit(1);
                    }
                    if( indice == 1 && caracterActual != 0x41) {//a
                        System.out.println("El segundo caracter debe ser 0x41");
                        System.exit(1);
                    }
                    if(indice == 2 && caracterActual != 0x5a) {//z
                        System.out.println("El tercer caracter debe ser 0x5a");
                        System.exit(1);
                    }

                    if(indice == 3 && caracterActual != 0x45) {//e
                        System.out.println("El cuarto caracter debe ser 0x45");
                        System.exit(1);
                    }
                    if(indice==4)//alto
                        this.alto=caracterActual;
                    if(indice==5) {//ancho
                        this.ancho = caracterActual;
                        laberinto = new Casilla [alto][ancho];
                    }
                }
                indice++;
            }
            input.close();
        } catch (Exception e) {
          System.out.println("Formato no valido");
          System.exit(1);
        }

        Lista<Casilla> candidatos=new Lista<>();

        //recorren las orillas del laberinto en busca del inicio y del final
        for(int x=0; x<alto;x++){
            if (!laberinto[x][0].oeste)
                candidatos.agrega(laberinto[x][0]);
            if(!laberinto[x][ancho-1].este)
                candidatos.agrega(laberinto[x][ancho-1]);
        }

        for(int y=0; y<ancho; y++) {
            if (!laberinto[0][y].norte)
                candidatos.agrega(laberinto[0][y]);
            if(!laberinto[alto-1][y].sur)
                candidatos.agrega(laberinto[alto-1][y]);
        }

        if(candidatos.getElementos() !=2){
            System.err.println("Archivo no valido, debe haber exactamente una entrada y una salida en el laberinto.");
            System.exit(1);
        }

        this.inicio=candidatos.getPrimero();
        inicio.inicio=true;

        this.fin=candidatos.getUltimo();
        fin.fin=true;

        creaGrafica();

    }

    /** Resuelve el laberinto en base a su representacion
     *  en forma de grafica.
     */
    public void resuelveLaberinto(){

        svgSolucion =new StringBuilder();
        Lista<VerticeGrafica<Casilla>> lista= grafica.dijkstra(inicio, fin);

        if(lista.esVacia()){
            System.err.println("Laberinto no valido, no tiene solucion");
            System.exit(1);
        }

        Casilla[] casillas=new Casilla[lista.getLongitud()];

        int i=0;
        for(VerticeGrafica<Casilla> casilla: lista) {
            casillas[i]=casilla.get();
            i++;
        }

        for(int j=0; j<casillas.length-1; j++){
            Casilla actual=casillas[j];
            Casilla siguiente= casillas[j+1];
            svgSolucion.append(linea(actual.coordY*20+10, actual.coordX*20+10, siguiente.coordY*20+10,siguiente.coordX*20+10));
        }// los 10 que se suman son del centro de cada casilla, pues cada casilla es de 20x20, en el metodo linea se agregan otros 10 por el margen

        svgSolucion.append(casillas[0].dibujaVertice(5));
        svgSolucion.append(casillas[casillas.length-1].dibujaVertice(5));

    }

    /** Imprime el laberinto en formato de bytes. Estos
     *  son pasados a un archivo .mze, para su posterior
     *  lectura.
     */
    public void imprimeBytes(){

        byte m= (byte)(0x4d);
        byte a=(byte) (0x41);
        byte z=(byte) (0x5a);
        byte e=(byte) (0x45);
        byte filas=(byte) (alto & 0xFF);
        byte columnas=(byte) (ancho & 0xFF);

        try{
            PrintStream out=new PrintStream(System.out);
            out.write(m);
            out.write(a);
            out.write(z);
            out.write(e);
            out.write(filas);
            out.write(columnas);

            for(Casilla casilla: this) {
                int b=((int)(casilla.toByte())) & 0xff;
                out.write(b);
            }

            out.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    /** Regresa la representacion del laberinto en formato
     *  svg.
     */

    public String toSVG(){

        StringBuilder s= new StringBuilder("<?xml version='1.0' encoding='UTF-8' ?>\n");
        s.append("<svg width='").append(ancho * 20+20).append("' height='").append(alto * 20+20).append("'>\n\t<g>\n");
        s.append(svgSolucion);

        for(Casilla casilla: this)
            s.append(casilla.toSVG());

        s.append("\t</g>\n" + "</svg>");
        return s.toString();

    }

    /** Regresa una representacion en cadena del laberinto.
     */

    @Override
    public String toString() { //tostring que ayuda en la elaboracion del laberinto.
        StringBuilder cadena= new StringBuilder();

        for(int i=0; i<alto; i++) {
            StringBuilder arriba= new StringBuilder();
            StringBuilder abajo= new StringBuilder();
            for (int j = 0; j < ancho; j++) {

                arriba.append(laberinto[i][j].parteArriba());
                abajo.append(laberinto[i][j].parteAbajo());
            }
            cadena.append(arriba).append("\n").append(abajo).append("\n");
        }

        return cadena.toString();

    }

    /** Selecciona al azar dos casillas de cualquiera de
     *  las orillas, estas marcan la casilla inicial y la
     *  casilla destino.
     * @return  Una lista de casillas con exactamente dos casillas
     */
    private Lista<Casilla> seleccionaInicioYFin(){
        Lista<Casilla> orillas=new Lista<>();
        Lista<Casilla> elegidos=new Lista<>();

        for(int x=0; x<alto;x++){
            orillas.agrega(laberinto[x][0]);
            orillas.agrega(laberinto[x][ancho-1]);
        }

        for(int y=1; y<ancho-1; y++) {
            orillas.agrega(laberinto[0][y]);
            orillas.agrega(laberinto[alto-1][y]);
        }

        int longitud=orillas.getLongitud();
        int inicio=random.nextInt(longitud);
        int fin;

        //evitar que se eliga la misma casilla como inicio y final
        do
            fin= random.nextInt(longitud);
        while (fin==inicio);

        elegidos.agrega(orillas.get(inicio));
        elegidos.agrega(orillas.get(fin));

        return elegidos;
    }

    /** Construye un laberinto de forma aleatoria
     */

    private void construyeLaberinto(){

        Pila<Casilla> pila=new Pila<>();
        pila.mete(inicio);
        grafica = new Grafica<>();
        grafica.agrega(inicio);

        while (!pila.esVacia()){
            Casilla c= pila.mira();
            c.visitado=true;
            Lista<Casilla> posibilidades= posibilidades(c);

            if(!posibilidades.esVacia())
                if(posibilidades.getLongitud()==1) {
                    tiraPuertas(c, posibilidades.getPrimero());
                    pila.mete(posibilidades.getPrimero());


                }else{
                    int r=random.nextInt(posibilidades.getLongitud());
                    Casilla siguiente=posibilidades.get(r);
                    tiraPuertas(c, siguiente);
                    pila.mete(siguiente);

                }
            else
                pila.saca();

        }

    }
    /** Dados dos casillas tira una puerta entre estos para
     *  poder viajar de una casilla a la otra. A la vez, en
     *  la representacion en forma de grafica se agrega la
     *  segunda casilla recibida, y se conectan. Lo puede
     *  reemplazar el metodo creaGrafica()
     * @param c1 la primera casilla
     * @param c2 la segunda casilla (la que se agrega a la grafica).
     */

    private void tiraPuertas(Casilla c1, Casilla c2) {
        grafica.agrega(c2);
        grafica.conecta(c1,c2,1+c1.puntaje+c2.puntaje);

        if(c1.coordX == c2.coordX)//horizontalmente
            if(c1.coordY < c2.coordY) {//casilla c1 antes que c2
                c1.este = false;
                c2.oeste = false;
            }else{
                c1.oeste = false;
                c2.este = false;
            }
        else{//verticalmente
            if(c1.coordX < c2.coordX) {//casilla c1 antes que c2{
                c1.sur = false;
                c2.norte = false;
            }else{
                c1.norte = false;
                c2.sur = false;
            }
        }
    }

    /** Dependiendo el tipo de la casilla recibida tiene
     *  posibles casillas con las que se podria conectar.
     *  Dado una casilla mete en la lista recibida las
     *  casillas con las que se puede conectar (tirar puertas).
     * @param norte la puerta norte.
     * @param sur la puerta sur.
     * @param este la puerta este.
     * @param oeste la puerta oeste.
     * @param lista la lista en que se meteran las casillas
     * @param actual la casilla que se verificaran sus opciones.
     */

    private void verificaOpciones(boolean norte, boolean sur, boolean este, boolean oeste, Lista<Casilla> lista, Casilla actual){
        //se pueden utilizar directamente casillas en lugar de un arreglo.
        Casilla[] direcciones=new Casilla[4];                                   //direcciones[0] es la coordenada norte
        if(norte) {                                                             //direcciones[1] es la sur
            direcciones[0] = laberinto[actual.coordX - 1][actual.coordY];       //direcciones[2] es la este
            if(!direcciones[0].visitado)                                        //direcciones[3] es la oeste
                lista.agrega(direcciones[0]);

        }
        if(sur) {
            direcciones[1] = laberinto[actual.coordX + 1][actual.coordY];
            if(!direcciones[1].visitado)
                lista.agrega(direcciones[1]);
        }
        if(este) {
                direcciones[2] = laberinto[actual.coordX][actual.coordY + 1];
                if (!direcciones[2].visitado)
                     lista.agrega(direcciones[2]);
        }

        if(oeste) {
                 direcciones[3] = laberinto[actual.coordX][actual.coordY - 1];
                 if(!direcciones[3].visitado)
                    lista.agrega(direcciones[3]);
        }

    }
    /** Dado una casilla, regresa una lista con las casillas a las que
     *  podría viajar (tirar puertas).
     * @param actual la casilla que queremos verificar sus opciones
     * @return lista de casillas con las que se podría conectar.
     */
    private Lista<Casilla> posibilidades(Casilla actual) {

        Lista<Casilla> ocpiones = new Lista<>();
        switch (actual.tipo) {
            case 1:
                verificaOpciones(false, true, true, true, ocpiones, actual);
                break;
            case 2:
                verificaOpciones(false, true, false, true, ocpiones, actual);
                break;
            case 3:
                verificaOpciones(true, true, false, true, ocpiones, actual);
                break;
            case 4:
                verificaOpciones(true, false, false, true, ocpiones,actual);
                break;
            case 5:
                verificaOpciones(true, false, true, true, ocpiones,actual);
                break;
            case 6:
                verificaOpciones(true, false, true, false, ocpiones, actual);
                break;
            case 7:
                verificaOpciones(true, true, true, false, ocpiones, actual);
                break;
            case 8:
                verificaOpciones(false, true, true, false, ocpiones, actual);
                break;
            case 9:
                verificaOpciones(true, true, true, true, ocpiones, actual);
            default:
                break;
        }
        return ocpiones;
    }

    /** Crea la representacion en grafica del laberinto,
     *  cada casilla es un vertice, dos vertices estaran
     *  conectados si y solo si hay una puerta que los conecte
     *  (es decir no hay puerta, por lo que hay forma de
     *  pasar de una casilla a otra).
     */

    private void creaGrafica() { //se ocupa en la reconstruccion de un laberinto, no en la creacion.
        grafica = new Grafica<>();
        for (Casilla casilla : this)
            grafica.agrega(casilla);

        for (Casilla casilla : this) {

            if (!casilla.sur && casilla.coordX != alto-1)  //alto-1 casos donde la casilla está en la orilla sur.
                grafica.conecta(casilla, laberinto[casilla.coordX + 1][casilla.coordY], 1+casilla.puntaje + laberinto[casilla.coordX + 1][casilla.coordY].puntaje);

            if (!casilla.este && casilla.coordY != ancho - 1)//ancho-1 casos donde la casilla está en la orilla este
                grafica.conecta(casilla, laberinto[casilla.coordX][casilla.coordY + 1], 1 + casilla.puntaje+laberinto[casilla.coordX][casilla.coordY + 1].puntaje);
        }

    }

    /** Representacion en formato svg de una linea. Representan
     * las lineas que dan solucion al laberinto.
     */
    private String linea(int x1, int y1, int x2, int y2) {
        StringBuilder linea = new StringBuilder();
        linea.append("\t\t<line x1='").append(x1+10).append("' y1='").append(y1+10).append("' x2='").append(x2+10).append("' y2='").append(y2+10).append("' stroke='orange' stroke-width='3' />\n");
        return linea.toString();
    }

}

