/**
 * @author Ruben Saiz
 */
package Generador;

import Celda.EstadoCeldas;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;


public class MakeMazeDfs implements EstadoCeldas {
    
    private Stack<Node> stack = new Stack<>();
    private Random rnd = new Random();
    private int[][] laberinto;
    private int dimension;

    public MakeMazeDfs(int dim) {
        dimension = dim;
        laberinto = new int[dim][dim];
        generarLaberinto();
    }

    private void generarLaberinto() {
        
        stack.push(new Node(0,0));
        while (!stack.empty()) {
            Node next = stack.pop();
            if (nodoValido(next)) {
                laberinto[next.y][next.x] = ABIERTO;
                ArrayList<Node> vecinos = buscarVecinos(next);
                agregarVecinos(vecinos);
            }
        }

    }

    private boolean nodoValido(Node node) {
        int numeroVecinos = 0;
        for (int y = node.y-1; y < node.y+2; y++) {
            for (int x = node.x-1; x < node.x+2; x++) {
                if (seguro(x, y) && noEsNodo(node, x, y) && laberinto[y][x] == ABIERTO) {
                    numeroVecinos++;
                }
            }
        }
        return (numeroVecinos < 3) && laberinto[node.y][node.x] != 1;
    }

    private void agregarVecinos(ArrayList<Node> nodos) {
        int eliminar;
        while (!nodos.isEmpty()) {
            eliminar = rnd.nextInt(nodos.size());
            stack.push(nodos.remove(eliminar));
        }
    }

    private ArrayList<Node> buscarVecinos(Node node) {
        ArrayList<Node> vecinos = new ArrayList<>();
        for (int y = node.y-1; y < node.y+2; y++) {
            for (int x = node.x-1; x < node.x+2; x++) {
                if (seguro(x, y) && noEsquina(node, x, y)
                    && noEsNodo(node, x, y)) {
                    vecinos.add(new Node(x, y));
                }
            }
        }
        return vecinos;
    }

    private Boolean seguro(int x, int y) {
        return x >= 0 && y >= 0 && x < dimension && y < dimension;
    }

    private Boolean noEsquina(Node node, int x, int y) {
        return (x == node.x || y == node.y);
    }
    
    private Boolean noEsNodo(Node node, int x, int y) {
        return !(x == node.x && y == node.y);
    }

    public int[][] getLaberinto() {
        return this.laberinto;
    }

}