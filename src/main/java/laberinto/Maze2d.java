/**
 * @author Ruben Saiz
 */
package laberinto;

import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;


public class Maze2d extends VBox implements Runnable, Initializable {

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
    private Celda[][] laberinto; // Mapa de los cuadrados generados en el laberinto
    private int filas;
    private int columnas;
    private int posF = 1;
    private int posC = 1;
    private double sizeCelda;
    private final int velocidad = 5;

    private final int ABIERTO  = 0;
    private final int PARED    = 1;
    private final int ACTUAL   = -1;
    private final int VISITADO = 2;

    public Maze2d() {
        this.filas    = m.length;
        this.columnas = m[0].length;

        laberinto = new Celda[filas][columnas];
        pintarLaberinto();
    }

    public Maze2d(int f, int c) {

        // El algoritmo de creacion de laberintos solo permite numeros impares
        this.filas = (f %2 == 0) ? f+1 : f;
        this.columnas = (c %2 == 0) ? c+1 : c;

        this.sizeCelda = 1000 / filas;

        laberinto = new Celda[filas][columnas];
        GenerarLaberinto gen = new GenerarLaberinto(filas, columnas);
        this.m = gen.getLaberinto();
        pintarLaberinto();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    private void pintarLaberinto() {
        getChildren().clear();
        for (int i = 0; i < filas; i++) {
            HBox fila = new HBox();
            for (int j = 0; j < columnas; j++) {
                Celda celda = new Celda(i, j, m[i][j], sizeCelda);
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
        if (esSeguro(posF, posC +1)) {
            mover(posF, posC +1);
        }
    }

    private void movIzquierda() {
        if (esSeguro(posF, posC -1)) {
            mover(posF, posC -1);
        }
    }

    private void movArriba() {
        if (esSeguro(posF -1, posC)) {
            mover(posF -1, posC);
        }
    }

    private void movAbajo() {
        if (esSeguro(posF +1, posC)) {
            mover(posF +1, posC);
        }
    }

    private void mover(int f, int c) {
        laberinto[this.posF][this.posC].cambiarPos(ABIERTO);
        laberinto[f][c].cambiarPos(ACTUAL);
        this.posF = f;
        this.posC = c;
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

            if (i == filas-2 && j == columnas-2)
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
