/**
 * @author Ruben Saiz
 */
package laberinto;

import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.*;


public class Maze2d extends VBox implements Runnable {

    private int[][] m = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,-1,1,0,1,0,1,0,0,0,0,0,1},
        {1,0,1,0,0,0,1,0,1,1,1,0,1},
        {1,0,0,0,1,1,1,0,0,0,0,0,1},
        {1,0,1,0,0,0,0,0,1,1,1,0,1},
        {1,0,1,0,1,1,1,0,1,0,0,0,1},
        {1,0,1,0,1,0,0,0,1,1,1,0,1},
        {1,0,1,0,1,1,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,1,7,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1}
    };
    private Celda[][] laberinto;
    private int filas;
    private int columnas;
    private int f = 1;
    private int c = 1;

    private final int ABIERTO  = 0;
    private final int PARED    = 1;
    private final int LLEGADA  = 7;
    private final int ACTUAL   = -1;
    private final int VISITADO = 2;

    public Maze2d() {
        this.filas    = m.length;
        this.columnas = m[0].length;
        laberinto = new Celda[filas][columnas];
        setAlignment(Pos.TOP_CENTER);
        pintarLaberinto();

        this.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.UP)    {
                movArriba();
            }
            if (key.getCode() == KeyCode.DOWN)  {
                movAbajo();
            }
            if (key.getCode() == KeyCode.LEFT)  {
                movIzquierda();
            }
            if (key.getCode() == KeyCode.RIGHT) {
                movDerecha();
            }
        });

    }

    public Maze2d(int f, int c) {
        this.filas    = f;
        this.columnas = c;
        setAlignment(Pos.TOP_LEFT);
        pintarLaberinto();
    }

    public void pintarLaberinto() {
        getChildren().clear();
        for (int i = 0; i < filas; i++) {
            HBox fila = new HBox();
            for (int j = 0; j < columnas; j++) {
                Celda celda = new Celda(i, j, m[i][j]);
                laberinto[i][j] = celda;
                fila.getChildren().add(celda);
            }
            getChildren().add(fila);
        }
    }

    private boolean esSeguro(int f, int c) {
        if (f >= 0 && f < m.length && c >= 0 && c < m[0].length)
            if (m[f][c] != 1)
                return true;
        return false;
    }

    private void movDerecha() {
        if (esSeguro(f, c+1)) {
            mover(f, c+1);
        }
    }

    private void movIzquierda() {
        if (esSeguro(f, c-1)) {
            mover(f, c-1);
        }
    }

    private void movArriba() {
        if (esSeguro(f-1, c)) {
            mover(f-1, c);
        }
    }

    private void movAbajo() {
        if (esSeguro(f+1, c)) {
            mover(f+1, c);
        }
    }

    private void mover(int f, int c) {
        laberinto[this.f][this.c].cambiarPos(ABIERTO);
        laberinto[f][c].cambiarPos(ACTUAL);
        this.f = f;
        this.c = c;
    }

    @Override
    public void run() {
        resolver();
    }

    private boolean resolver() {
        this.m[1][1] = ABIERTO;
        return resolver(1, 1);
    }

    private boolean resolver(int i, int j) {

        if (m[i][j] == ABIERTO) {

            m[i][j] = ACTUAL;
            pintar(i, j, Color.GREEN);

            if (i == 7 && j == 11)
                return true;

            synchronized (this) {
                try {
                    wait(200);
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
            pintar(i, j, Color.BLUE);
            synchronized (this) {
                try {
                    wait(200);
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
