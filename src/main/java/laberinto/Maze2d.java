/**
 * @author Ruben Saiz
 */
package laberinto;

import Celda.*;
import Generador.GenerarLaberinto;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import utils.Propiedades;

import java.util.HashMap;
import java.util.Stack;

public class Maze2d extends VBox implements Runnable, EstadoCeldas {

    private int[][] maze;
    // Almacena cada una de las celdas generadas en el laberinto
    private Celda[][] laberinto;
    private int dimension;
    private double sizeCelda;
    private Stack<String> ruta = new Stack<>();
    private GenerarLaberinto gen;
    private HashMap<String, String> conf;
    private Propiedades propiedades;
    private int inicioX;
    private int inicioY;

    public Maze2d() {

        propiedades = new Propiedades();
        this.gen = new GenerarLaberinto();
        generarLaberinto();

        //MakeMazeDfs make = new MakeMazeDfs(dimension);
        //this.maze = make.getLaberinto();
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
        this.sizeCelda = 1000 / dimension;
        this.maze = new int[dimension][dimension];
        this.laberinto = new Celda[dimension][dimension];
    }

    public void generarLaberinto() {
        conf = Propiedades.cargarPropiedades();
        cargarDimensiones();
        if (!conf.get("MODO").equals("PINTAR")) {
            this.gen.crearLaberinto(this.dimension);
            this.maze = gen.getLaberinto();
        }
        pintarLaberinto();
    }

    private Long leerVelocidad() {
        return Long.parseLong(propiedades.obtenerPropiedad("VELOCIDAD"));
    }

    @Override
    public void run() {
        buscarInicio();
        resolver(inicioY, inicioX);
        marcarRuta();
    }

    private void buscarInicio() {
        boolean salir = false;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (laberinto[i][j].isStart()) {
                    this.inicioY = laberinto[i][j].getFila();
                    this.inicioX = laberinto[i][j].getColumna();
                    salir = true; break;
                }
            }
            if (salir) break;
        }
    }

    private boolean fuera(int f, int c) {
        return (f < 0 || c < 0 || f >= dimension || c >= dimension);
    }

    private boolean isValid(Celda celda) {
        return celda.isOpen() || celda.isStart() || celda.isEnd();
    }

    private boolean resolver(int i, int j) {

        if (!fuera(i, j) && isValid(laberinto[i][j])) {

            ruta.push( i + " " + j );

            if (laberinto[i][j].isOpen())
                laberinto[i][j].setActual();

            if (laberinto[i][j].isEnd())
                return true;

            synchronized (this) {
                try {
                    wait(leerVelocidad());
                } catch (InterruptedException e) { }
            }

            if (resolver(i - 1, j) ||
                    resolver(i + 1, j) ||
                    resolver(i, j - 1) ||
                    resolver(i, j + 1))
                return true;

            if (!ruta.isEmpty()) ruta.pop();

            /*laberinto[i][j].pintarCelda("VISITADO");
            laberinto[i][j].setVisitado();

            synchronized (this) {
                try {
                    wait(leerVelocidad());
                } catch (InterruptedException e) { }
            }*/

        }

        return false;
    }

    private void marcarRuta() {

        Integer f;
        Integer c;

        while (!ruta.isEmpty()) {

            String str = ruta.pop();
            f = Integer.valueOf(str.split(" ")[0]);
            c = Integer.valueOf(str.split(" ")[1]);

            if (laberinto[f][c].getValor() == ACTUAL) {
                laberinto[f][c].pintarCelda("VUELTA");
            }

            laberinto[f][c].setValor(ABIERTO);

            System.out.println(f + " " + c);

            synchronized (this) {
                try {
                    wait(leerVelocidad());
                } catch (InterruptedException e) { }
            }

        }

        // Limpiamos las rutas para la siguiente busqueda
        ruta.clear();
        System.out.println("Terminando de imprimir ruta...");
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
