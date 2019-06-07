/**
 * @author Ruben Saiz
 */
package laberinto;

import Celda.*;
import Generador.GenerarLaberinto;
import Jugador.Jugador;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import utils.Propiedades;

import java.util.HashMap;
import java.util.Stack;

public class Maze2d extends VBox implements Runnable, EstadoCeldas {

    private enum direcciones {
        ARRIBA, ABAJO, DERECHA, IZQUIERDA
    }

    private int[][] maze;
    // Almacena cada una de las celdas generadas en el laberinto
    private Celda[][] laberinto;
    private int dimension;
    private double sizeCelda;
    private Stack<String> ruta = new Stack<>();
    private int velocidad;
    private Jugador player;
    private GenerarLaberinto gen;
    public static HashMap<String, String> conf;

    public Maze2d() {

        conf = Propiedades.cargarPropiedades();

        //this.player = new Jugador();
        //crearEventosMovimiento();

        cargarDimensiones();

        if (conf.get("MODO").equals("PINTAR")) {
            pintarLaberinto();
        }
        else {
            generarLaberinto(velocidad);
        }

        //MakeMazeDfs make = new MakeMazeDfs(dimension);
        //this.maze = make.getLaberinto();
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
            if (player.x == dimension-2 && player.y == dimension-2)
                System.out.println("VICTORIA");
        });

    }

    private void pintarCeldaVacio(Celda celda) {
        celda.pintarCelda("ABIERTO");
    }

    private void pintarCeldaActual(Celda celda) {
        celda.pintarCelda("ACTUAL");
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

    /**
     * Método que crea un laberinto y muestra un laberinto de la siguiente forma:
     *  1.- Creámos un componente HBOX.
     *  2.- Creámos una Celda y la introducimos en el HBOX.
     *  3.- Introducimos dentro de el objeto VBOX que estamos heredando el HBOX
     *      con todas las celdas de una fila de la matriz.
     *  4.- Repetimos este proceso hasta que no queden mas filas por recorrer.
     */
    private void pintarLaberinto() {
        getChildren().clear();
        for (int i = 0; i < dimension; i++) {
            HBox fila = new HBox();
            for (int j = 0; j < dimension; j++) {
                Celda celda = new Celda(i, j, maze[i][j], sizeCelda);
                laberinto[i][j] = celda;
                fila.getChildren().add(celda);
            }
            getChildren().add(fila);
        }
    }

    private void cargarDimensiones() {
        this.dimension = Integer.parseInt(conf.get("DIMENSION"));
        this.sizeCelda = 900 / dimension;
        gen = new GenerarLaberinto(dimension);
        laberinto = new Celda[dimension][dimension];
        maze = new int[dimension][dimension];
        this.velocidad = Integer.parseInt(conf.get("VELOCIDAD"));
    }

    public void generarLaberinto(int velocidad) {
        conf = Propiedades.cargarPropiedades();
        cargarDimensiones();
        if (conf.get("MODO").equals("PINTAR")) {
            pintarLaberinto();
            this.velocidad = Integer.parseInt(conf.get("VELOCIDAD"));
            return;
        }
        this.gen.crearLaberinto(this.dimension);
        this.maze = gen.getLaberinto();
        this.velocidad = velocidad;
        pintarLaberinto();
    }

    /**
     * Método que indicando la posición X e Y de un laberinto devuelve true
     * si no nos salimos de él y si no nos chocamos contra una pared.
     * @param fila
     * @param columna
     * @return boolean
     */
    private boolean esSeguro(int fila, int columna) {
        return (fila >= 0 && fila < maze.length &&
                columna >= 0 && columna < maze[0].length &&
                maze[fila][columna] != PARED);
    }

    @Override
    public void run() {
        resolver(1, 1);
        imprimir();
        marcarRuta();
    }

    private boolean fuera(int f, int c) {
        return (f < 0 || c < 0 || f >= dimension || c >= dimension);
    }

    private boolean resolver(int i, int j) {

        if (!fuera(i, j) && laberinto[i][j].estaAbierta()) {

            ruta.push( i + " " + j);
            laberinto[i][j].pintarCelda("ACTUAL");
            laberinto[i][j].setActual();

            if (i == dimension-2 && j == dimension-2)
                return true;

            synchronized (this) {
                try {
                    wait(velocidad);
                } catch (InterruptedException e) { }
            }

            if ( resolver(i-1, j) ||
                 resolver(i+1, j) ||
                 resolver(i, j-1) ||
                 resolver(i, j+1) )
                return true;

            ruta.pop();
            laberinto[i][j].pintarCelda("VISITADO");
            laberinto[i][j].setVisitado();

            synchronized (this) {
                try {
                    wait(velocidad);
                } catch (InterruptedException e) { }
            }

        }

        return false;
    }

    private void marcarRuta() {
        Integer f;
        Integer c;
        System.out.println(ruta);
        while (!ruta.isEmpty()) {
            String str = ruta.pop();
            f = Integer.valueOf(str.split(" ")[0]);
            c = Integer.valueOf(str.split(" ")[1]);
            laberinto[f][c].pintarCelda("LLEGADA");
            if (f == 1 && c == 1) break;
            try {
                wait(velocidad);
            } catch (InterruptedException e) { }
        }
    }

    private void imprimir() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                System.out.print((laberinto[i][j].getValor()==ABIERTO)
                                ? ABIERTO+" " : PARED+" ");
            }
            System.out.println();
        }
    }
    
}
