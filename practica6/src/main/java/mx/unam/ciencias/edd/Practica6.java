package mx.unam.ciencias.edd;

import java.util.Random;

/**
 * Práctica 6: Gráficas.
 */
public class Practica6 {


    private static <T> void g(Grafica<T> g, T e){

        Cola<VerticeGrafica<T>> q= new Cola<VerticeGrafica<T>>();
        VerticeGrafica<T> v=g.vertice(e);
        g.setColor(v, Color.NEGRO);
        q.mete(v);

        while (!q.esVacia()){

            v=q.saca();
            for(VerticeGrafica<T> u: v.vecinos()){
                if(u.getColor() ==Color.ROJO){
                    g.setColor(u, Color.NEGRO);
                    q.mete(u);
                }
            }
        }
    }
    private static <T> int f(Grafica<T> g){
        g.paraCadaVertice((v) -> g.setColor(v, Color.ROJO));
        boolean c;
        int n=0;
        do{
            c=false;
            for(T e: g){
                VerticeGrafica<T> v=g.vertice(e);
                if(v.getColor() !=Color.ROJO)
                    continue;
                c=true;
                g(g, e);
                n++;
            }
        }while (c);

        return n;
    }
    private static void uso(){
        System.err.println("necesitounnumero");
        System.exit(1);
    }




    /* Cadena para que usen las lambdas. */
    private static String cadena;

    public static void main(String[] args) {


        int N=-1;
        if(args.length !=1)
            uso();
        try{
            N=Integer.parseInt(args[0]);
        }catch (NumberFormatException nfe){
            uso();
        }
        Grafica<Integer> grafica = new Grafica<>();
        System.out.println("este es N: "+N);
        for(int i=0; i<N; i++) {

            grafica.agrega(i);

        }

        for(int i=0; i<N; i+=2){
            if(i+1 >=N) {
                continue;

            }

            grafica.conecta(i,i+1);
        }
        System.out.println(f(grafica));
        System.out.println(grafica.esConexa());
        System.out.println(grafica);





        /*
         *    b─────d
         *   ╱│╲    │╲
         *  ╱ │ ╲   │ ╲
         * a  │  ╲  │  f
         *  ╲ │   ╲ │ ╱
         *   ╲│    ╲│╱
         *    c─────e
         */
        /*
        Grafica<String> grafica = new Grafica<String>();
        grafica.agrega("a");
        grafica.agrega("b");
        grafica.agrega("c");
        grafica.agrega("d");
        grafica.agrega("e");
        grafica.agrega("f");

        grafica.conecta("a", "b");
        grafica.conecta("a", "c");
        grafica.conecta("b", "c");
        grafica.conecta("b", "d");
        grafica.conecta("b", "e");
        grafica.conecta("c", "e");
        grafica.conecta("d", "e");
        grafica.conecta("d", "f");
        grafica.conecta("e", "f");

        System.out.println(grafica);

        /* BFS */
        /*
        grafica.paraCadaVertice(v -> grafica.setColor(v, Color.ROJO));
        Cola<VerticeGrafica<String>> c = new Cola<VerticeGrafica<String>>();
        VerticeGrafica<String> vertice = grafica.vertice("a");
        grafica.setColor(vertice, Color.NEGRO);
        c.mete(vertice);
        cadena = "BFS 1: ";
        while (!c.esVacia()) {
            vertice = c.saca();
            cadena += vertice.get() + ", ";
            for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                if (vecino.getColor() == Color.ROJO) {
                    grafica.setColor(vecino, Color.NEGRO);
                    c.mete(vecino);
                }
            }
        }
        System.out.println(cadena);

        /* BFS de la clase */
        /*
        cadena = "BFS 2: ";
        grafica.bfs("a", v -> cadena += v.get() + ", ");
        System.out.println(cadena);

        /* DFS */
        /*
        grafica.paraCadaVertice(v -> grafica.setColor(v, Color.ROJO));
        Pila<VerticeGrafica<String>> p = new Pila<VerticeGrafica<String>>();
        vertice = grafica.vertice("a");
        grafica.setColor(vertice, Color.NEGRO);
        p.mete(vertice);
        cadena = "DFS 1: ";
        while (!p.esVacia()) {
            vertice = p.saca();
            cadena += vertice.get() + ", ";
            for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                if (vecino.getColor() == Color.ROJO) {
                    grafica.setColor(vecino, Color.NEGRO);
                    p.mete(vecino);
                }
            }
        }
        System.out.println(cadena);

        /* DFS de la clase */
        /*
        cadena = "DFS 2: ";
        grafica.dfs("a", v -> cadena += v.get() + ", ");
        System.out.println(cadena);

         */
    }
}
