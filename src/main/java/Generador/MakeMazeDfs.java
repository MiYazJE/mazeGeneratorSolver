/**
 * @author Ruben Saiz
 */
package Generador;

import Celda.EstadoCeldas;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;

/**
 * Esta clase es encargada de crear un laberinto utilizando "Deep First Search".
 * Almacena el laberinto en una matriz bidimensional de enteros.
 */
public class MakeMazeDfs implements EstadoCeldas {
    
    private Stack<Nodo> stack = new Stack<>();
    private Random rnd = new Random();
    private int[][] laberinto;
    private int dimension;

    public MakeMazeDfs(  ) {

    }

    public void crearLaberinto(int dim) {

        dimension = dim;
        laberinto = new int[dim][dim];

        stack.push(new Nodo(0,0));
        while (!stack.empty()) {
            Nodo next = stack.pop();
            if (nodoValido(next)) {
                laberinto[next.f][next.c] = ABIERTO;
                ArrayList<Nodo> vecinos = buscarVecinos(next);
                agregarVecinos(vecinos);
            }
        }

    }

    /**
     * Comprueba si un nodo es válido con los siguientes pasos:
     *  1.- Suma todos los nodos adyacentes que cumplan:
     *    1.1- Las coordenadas del nodo se encuentren dentro del laberinto.
     *    1.2- El nodo adyacente no puede ser el mismo que el que se esta comprobando.
     *    1.3- Que el valor de su coordenada sea el sinónimo a ABIERTO(1).
     *  2.- Si la suma de estos es menor a 3 y el nodo pasado como paámetro es PARED(0).
     * @param node
     * @return
     */
    private boolean nodoValido(Nodo node) {
        int numeroVecinos = 0;
        for (int f = node.f - 1; f < node.f + 2; f++)
            for (int c = node.c -1; c < node.c + 2; c++)
                if (seguro(f, c) && notTheSame(node, f, c) && laberinto[f][c] == 1) // 1, me refiero a ABIERTO
                    numeroVecinos++;
        return (numeroVecinos < 3) && laberinto[node.f][node.c] != 1;
    }

    /**
     * Introduce aleatoriamente los nodos en una lista.
     * Esto le da aleatoriedad a la generación del laberinto.
     * @param nodos
     */
    private void agregarVecinos(ArrayList<Nodo> nodos) {
        int eliminar;
        while (!nodos.isEmpty()) {
            eliminar = rnd.nextInt(nodos.size());
            this.stack.push(nodos.remove(eliminar));
        }
    }

    /**
     * Introduce en una lista los nodos adyacentes válidos.
     * Nodos válidos son aquellos que:
     *  1.- Sus coordenadas se encuentran dentro del laberinto.
     *  2.- Las coordenadas del nodo adyacente no pueden estar en diagonal respecto al nodo a comprobar.
     *  3.- Que no sea el mismo nodo que el nodo a comprobar.
     * @param nodo
     * @return ArrasyList<Nodo> : lista de los vecinos
     */
    private ArrayList<Nodo> buscarVecinos(Nodo nodo) {
        ArrayList<Nodo> vecinos = new ArrayList<>();
        for (int f = nodo.f - 1; f < nodo.f + 2; f++)
            for (int c = nodo.c - 1; c < nodo.c + 2; c++)
                if (seguro(f, c) && noEsquina(nodo, f, c) && notTheSame(nodo, f, c))
                        vecinos.add(new Nodo(f, c));
        return vecinos;
    }

    /**
     * Comprobar que las coordenadas con válidas, que se encuentren dentro del laberinto.
     * @param f
     * @param c
     * @return boolean
     */
    private boolean seguro(int f, int c) {
        return f >= 0 && c >= 0 && f < dimension && c < dimension;
    }

    /**
     * Comprueba si las coordenadas estan en la misma fila o columna que el nodo.
     * Si se cumple, esto quiere decir que las coordenadas no se encuentran en ningúna esquina.
     * ej -> 0 1 0   Los 1s són nodos válidos.
     *       1 X 1   1 = Nodos adyacentes, X = Nodo a comprobar
     *       0 1 0
     * @param nodo
     * @param f
     * @param c
     * @return
     */
    private boolean noEsquina(Nodo nodo, int f, int c) {
        return (nodo.f == f || nodo.c == c);
    }

    /**
     * Compruba que el nodo no se encuentra en la misma localización que las coordenadas f y c.
     * Gracias a esto no se estará validando el mismo nodo.
     * @param nodo
     * @param f
     * @param c
     * @return boolean
     */
    private boolean notTheSame(Nodo nodo, int f, int c) {
        return !(nodo.f == f && nodo.c == c);
    }

    public int[][] getLaberinto() {
        return this.laberinto;
    }

}