package Jugador;

/**
 * Clase que obtiene las posiciones de un jugador en un laberinto,
 * puede moverse por el.
 */
public class Jugador {

    public int x;
    public int y;

    /**
     * Constructor para la creación de un jugador
     */
    public Jugador() {
        this.x = 1;
        this.y = 1;
    }

    public void mover(int f, int c) {
        this.y += f;
        this.x += c;
    }

}
