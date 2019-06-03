package Jugador;

public class Player {

    public int x;
    public int y;

    /**
     * Creacion de un jugador
     */
    public Player() {
        this.x = 1;
        this.y = 1;
    }

    public void mover(int f, int c) {
        this.y += f;
        this.x += c;
    }

}
