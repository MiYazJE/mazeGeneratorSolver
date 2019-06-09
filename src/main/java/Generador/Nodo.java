/**
 * @author Ruben Saiz
 */

package Generador;

/**
 * Esta clase simula una celda de un laberinto, es utilizada para conocer
 * sus coordenadas
 */
public class Nodo {

    public final int f;
    public final int c;

    /**
     * Contructor del objeto Nodo
     * @param f : Coordenada x
     * @param c : Coordenada y
     */
    public Nodo(int f, int c) {
        this.f = f;
        this.c = c;
    }

}