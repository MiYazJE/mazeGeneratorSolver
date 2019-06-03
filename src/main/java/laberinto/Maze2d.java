/**
 * @author Ruben Saiz
 */
package laberinto;

import Celda.Celda;
import Generador.MakeMazeDfs;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;


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
    private int dimension;
    private int posF = 1;
    private int posC = 1;
    private double sizeCelda;
    private final int velocidad = 10;
    private Stack<String> ruta = new Stack<>();

    public final int ACTUAL   = -1;
    public final int PARED    = 0;
    public final int ABIERTO  = 1;
    public final int VISITADO = 2;
    public final int LLEGADA  = 7;

    public Maze2d() {
        laberinto = new Celda[dimension][dimension];
        pintarLaberinto();
    }

    public Maze2d(int dim) {

        // El algoritmo de creacion de laberintos solo permite numeros impares
        this.dimension = (dim %2 == 0) ? dim+1 : dim;
        this.sizeCelda = 1000 / dimension;

        laberinto = new Celda[dimension][dimension];
        //GenerarLaberinto gen = new GenerarLaberinto(filas, columnas);
        //this.m = gen.getLaberinto();

        MakeMazeDfs make = new MakeMazeDfs(dimension);
        this.m = make.getLaberinto();
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
                m[f][c] != 1);
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
        System.out.println(ruta);
    }

    private boolean dentro(int f, int c) {
        return (f < 0 || c < 0 || f >= dimension || c >= dimension);
    }

    private boolean resolver() {
        this.m[1][1] = ABIERTO;
        return resolver(0, 0);
    }

    private boolean resolver(int i, int j) {

        if (!dentro(i, j) && m[i][j] == ABIERTO) {

            m[i][j] = ACTUAL;
            ruta.push( ("["+i+", "+j+"]") );
            pintar(i, j, Color.GREEN);

            if (i == dimension-1 && j == dimension-1)
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
