/**
 * @author Ruben Saiz
 */
package laberinto;

import Celda.*;
import Generador.GenerarLaberinto;
import Generador.MakeMazeDfs;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.Mensaje;
import utils.Propiedades;
import Main.Interfaz;
import java.util.HashMap;

public class Grid extends VBox implements Runnable, EstadoCeldas {

    private int[][] maze;
    // Almacena cada una de las celdas generadas en el laberinto
    private Celda[][] laberinto;
    private int dimension;
    private double sizeCelda;
    private GenerarLaberinto gen;
    private HashMap<String, String> conf;
    private Propiedades propiedades;
    private int inicioX;
    private int inicioY;
    private MakeMazeDfs genDFS;

    public Grid() {

        propiedades = new Propiedades();
        this.gen = new GenerarLaberinto();
        genDFS = new MakeMazeDfs();
        generarLaberinto();
    }

    /**
     * M�todo que crea un laberinto c muestra un laberinto de la siguiente forma:
     * 1.- Cre�mos un componente HBOX.
     * 2.- Cre�mos una Celda c la introducimos en el HBOX.
     * 3.- Introducimos dentro de el objeto VBOX que estamos heredando el HBOX
     * con todas las celdas de una fila de la matriz.
     * 4.- Repetimos este proceso hasta que no queden mas filas por recorrer.
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

    /**
     * Cambia el estado de todas las celdasa ABIERTO
     */
    public void crearTodoAbierto() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                laberinto[i][j].setAbierto();
            }
        }
    }

    /**
     * Cambia el estado de todas las celdas que no sean Pared a Abierto.
     */
    public void limpiarLaberinto() {
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                if (laberinto[i][j].getValor() != PARED)
                    laberinto[i][j].setAbierto();
    }

    private void cargarDimensiones() {
        this.dimension = Integer.parseInt(conf.get("DIMENSION"));
        this.sizeCelda = 850 / dimension;
        this.maze = new int[dimension][dimension];
        this.laberinto = new Celda[dimension][dimension];
    }

    public void generarLaberinto() {
        conf = Propiedades.cargarPropiedades();
        cargarDimensiones();
        if (!conf.get("MODO").equals("PINTAR")) {
            if (conf.get("ALGORITMO").equals("DFS")) {
                genDFS.crearLaberinto(dimension);
                maze = genDFS.getLaberinto();
            }
            else {
                gen.crearLaberinto(dimension);
                maze = gen.getLaberinto();
            }
        }
        pintarLaberinto();
    }

    private Long leerVelocidad() {
        return Long.parseLong(propiedades.obtenerPropiedad("VELOCIDAD"));
    }

    @Override
    public void run() {
        inicioX = 1; inicioY = 1;
        if (buscarInicio()) {
            if (resolver(inicioY, inicioX))
                System.out.println("El laberinto ha sido resuelto.");
            else
                System.out.println("El laberinto no ha sido resuelto.");
        } else {
            // Cannot start exception, declare initial point.
            Platform.runLater(() -> {
                Mensaje.mostrarNotificacion(Interfaz.contenedorGlobal, 2);
                System.out.println("ERROR. Cannot start exception, declare initial point.");
            });
        }
    }

    private boolean buscarInicio() {
        boolean found = false;
        for (int i = 0; i < dimension && !found; i++) {
            for (int j = 0; j < dimension && !found; j++) {
                if (laberinto[i][j].isStart()) {
                    this.inicioY = laberinto[i][j].getFila();
                    this.inicioX = laberinto[i][j].getColumna();
                    found = true;
                }
            }
        }
        return found;
    }

    private boolean fuera(int f, int c) {
        return (f < 0 || c < 0 || f >= dimension || c >= dimension);
    }

    /**
     * Comprueba si la celda es valida si su estado es:
     *  - ABIERTO
     *  - INICIO
     *  - FINAL
     * @param celda
     * @return
     */
    private boolean isValid(Celda celda) {
        return celda.isOpen() || celda.isStart() || celda.isEnd();
    }

    private boolean resolver(int i, int j) {

        boolean resuelto = false;

        if (!fuera(i, j) && isValid(laberinto[i][j])) {

            if (laberinto[i][j].isOpen())
                laberinto[i][j].setActual();

            if (laberinto[i][j].isEnd())
                resuelto = true;

            synchronized (this) {
                try {
                    wait(leerVelocidad());
                } catch (InterruptedException e) { }
            }

            if      (resolver(i - 1, j) ||
                    resolver(i + 1, j) ||
                    resolver(i, j - 1) ||
                    resolver(i, j + 1))
                resuelto = true;

            if (resuelto) {
                if (laberinto[i][j].getValor() != INICIO && laberinto[i][j].getValor() != LLEGADA)
                    laberinto[i][j].pintarCelda("VUELTA");
                synchronized (this) {
                    try {
                        wait(leerVelocidad());
                    } catch (InterruptedException e) {}
                }
            }

        }

        return resuelto;
    }

}
