/**
 * @author Ruben Saiz
 */
package laberinto;

import Celda.*;
import Jugador.Player;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Stack;

enum direcciones {
    ARRIBA, ABAJO, DERECHA, IZQUIERDA
}

public class Maze2d extends VBox implements Runnable, EstadoCeldas {

    private int[][] m;
    private Celda[][] laberinto; // Mapa de los cuadrados generados en el laberinto
    private int dimension;
    private double sizeCelda;
    private Stack<String> ruta = new Stack<>();
    private final int velocidad = 50;
    Player player;

    public Maze2d() {
        laberinto = new Celda[dimension][dimension];
        pintarLaberinto();
    }

    public Maze2d(int dim) {

        this.player = new Player();
        crearEventosMovimiento();

        // El algoritmo de creacion de laberintos solo permite dimensiones impares
        this.dimension = (dim %2 == 0) ? dim+1 : dim;
        this.sizeCelda = 1000 / dimension;

        laberinto = new Celda[dimension][dimension];
        GenerarLaberinto gen = new GenerarLaberinto(dimension, dimension);
        this.m = gen.getLaberinto();

        //MakeMazeDfs make = new MakeMazeDfs(dimension);
        //this.m = make.getLaberinto();
        pintarLaberinto();
    }

    private void crearEventosMovimiento() {

        this.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.UP)    {
                moverJugador(direcciones.ARRIBA);
            }
            if (key.getCode() == KeyCode.DOWN)  {
                moverJugador(direcciones.ABAJO);
            }
            if (key.getCode() == KeyCode.LEFT)  {
                moverJugador(direcciones.IZQUIERDA);
            }
            if (key.getCode() == KeyCode.RIGHT) {
                moverJugador(direcciones.DERECHA);
            }
        });

        this.setOnKeyReleased(e -> {
            if (player.x == dimension-1 && player.y == dimension-1)
                System.out.println("VICTORIA");
        });

    }

    private void pintarCeldaVacio(Celda celda) {
        celda.pintarCelda(Color.WHITE);
    }

    private void pintarCeldaActual(Celda celda) {
        celda.pintarCelda(Color.GREEN);
    }

    private void moverJugador(direcciones direccion) {

        int f = player.y;
        int c = player.x;

        switch (direccion) {
            case ABAJO:
                if (esSeguro(f+1, c)) {
                    player.mover(1, 0);
                    pintarCeldaVacio(laberinto[f][c]);
                    pintarCeldaActual(laberinto[f+1][c]);
                }
                break;
            case ARRIBA:
                if (esSeguro(f-1, c)) {
                    player.mover(-1, 0);
                    pintarCeldaVacio(laberinto[f][c]);
                    pintarCeldaActual(laberinto[f-1][c]);
                }
                break;
            case DERECHA:
                if (esSeguro(f, c+1)) {
                    player.mover(0, 1);
                    pintarCeldaVacio(laberinto[f][c]);
                    pintarCeldaActual(laberinto[f][c+1]);
                }
                break;
            case IZQUIERDA:
                if (esSeguro(f, c-1)) {
                    player.mover(0, -1);
                    pintarCeldaVacio(laberinto[f][c]);
                    pintarCeldaActual(laberinto[f][c-1]);
                }
        }
    }

    private void pintarLaberinto() {
        getChildren().clear();
        for (int i = 0; i < dimension; i++) {
            HBox fila = new HBox();
            for (int j = 0; j < dimension; j++) {
                Celda celda = new Celda(i, j, m[i][j], sizeCelda);
                laberinto[i][j] = celda;
                fila.getChildren().add(celda);
            }
            getChildren().add(fila);
        }
    }

    private boolean esSeguro(int f, int c) {
        return (f >= 0 && f < m.length &&
                c >= 0 && c < m[0].length &&
                m[f][c] != PARED);
    }

    @Override
    public void run() {
        resolver();
        System.out.println(ruta);
    }

    private boolean dentro(int f, int c) {
        return (f < 0 || c < 0 || f >= dimension || c >= dimension);
    }

    private boolean resolver() {
        this.m[1][1] = ABIERTO;
        return resolver(1, 1);
    }

    private boolean resolver(int i, int j) {

        if (!dentro(i, j) && m[i][j] == ABIERTO) {

            m[i][j] = ACTUAL;
            ruta.push( ("["+i+", "+j+"]") );
            pintar(i, j, Color.GREEN);

            if (i == dimension-2 && j == dimension-2)
                return true;

            synchronized (this) {
                try {
                    wait(velocidad);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if ( resolver(i-1, j) ||
                 resolver(i+1, j) ||
                 resolver(i, j-1) ||
                 resolver(i, j+1) )
                return true;

            m[i][j] = VISITADO;
            ruta.pop();
            pintar(i, j, Color.BLUE);
            synchronized (this) {
                try {
                    wait(velocidad);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private void pintar(int f, int c, Color color) {
        laberinto[f][c].pintarCelda(color);
    }

}
