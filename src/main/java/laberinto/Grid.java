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

import java.util.*;

public class Grid extends VBox implements Runnable, EstadoCeldas {

    /*private int[][] maze = {
            {0,0,0,0,0,0,0},
            {0,1,1,1,1,1,0},
            {0,1,0,1,0,0,0},
            {0,1,0,1,0,1,0},
            {0,0,0,1,0,1,0},
            {0,1,1,1,1,1,0},
            {0,0,0,0,0,0,0}
    };*/
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

    // Needed for A * search
    private ArrayList<Celda> parentList;
    private Celda startNode, endNode;
    private boolean resuelto;

    public Grid() {
        propiedades = new Propiedades();
        this.gen = new GenerarLaberinto();
        genDFS = new MakeMazeDfs();
        generarLaberinto();
        parentList = new ArrayList<>();
    }

    /**
     * Método que crea un laberinto c muestra un laberinto de la siguiente forma:
     * 1.- Creámos un componente HBOX.
     * 2.- Creámos una Celda c la introducimos en el HBOX.
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
                laberinto[i][j].fullRestart();
            }
        }
    }

    /**
     * Cambia el estado de todas las celdas que no sean Pared a Abierto.
     */
    public void limpiarLaberinto() {
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                laberinto[i][j].restart();
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
        if (buscarInicioyFinal()) {
            resuelto = false;
            parentList = new ArrayList<>();
            if (resolverAStar(startNode)) {
                System.out.println( "LABERINTO RESUELTO!" );
                paintPath(endNode.parent);
                System.out.println("FIN");
            } else {
                System.out.println( "LABERINTO NO RESUELTO!" );
            }
//            if (resolverDfs(inicioY, inicioX))
//                System.out.println("El laberinto ha sido resuelto.");
//            else
//                System.out.println("El laberinto no ha sido resuelto.");
        } else {
            // Cannot start exception, declare initial and final points.
            Platform.runLater(() -> {
                Mensaje.mostrarNotificacion(Interfaz.contenedorGlobal, 2);
                System.out.println("ERROR. Cannot start exception, declare initial and final points.");
            });
        }
    }

    private void paintPath(Celda node) {
        if (node.parent == null) return;

        System.out.println( node.getFila() + "-" + node.getColumna() );
        laberinto[node.getFila()][node.getColumna()].pintarCelda("VUELTA");

        synchronized (this) {
            try {
                wait(leerVelocidad());
            } catch (InterruptedException e) { }
        }

        paintPath(node.parent);
    }

    private boolean buscarInicioyFinal() {
        boolean inicio = false, end = false;
        for (int i = 0; i < dimension && (!inicio || !end); i++) {
            for (int j = 0; j < dimension && (!inicio || !end); j++) {
                if (laberinto[i][j].isStart()) {
                    this.inicioY = laberinto[i][j].getFila();
                    this.inicioX = laberinto[i][j].getColumna();
                    startNode = laberinto[i][j];
                    inicio = true;
                }
                if (laberinto[i][j].isEnd()) {
                    this.endNode = laberinto[i][j];
                    end = true;
                }
            }
        }
        return end && inicio;
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

    private boolean resolverAStar(Celda current) {

        current.closed = true;

        while (true) {

            if (!current.closed) {
                synchronized (this) {
                    try {
                        laberinto[current.getFila()][current.getColumna()].pintarCelda("EXPANSION");
                        wait(leerVelocidad());
                    } catch(InterruptedException e) { }
                }
            }

            for (int i = current.getFila() - 1; i <= current.getFila() + 1 && !resuelto; i++) {
                for (int j = current.getColumna() - 1; j <= current.getColumna() + 1 && !resuelto; j++) {

                    // Evitar si el nodo se encuentra en cualquier diagonal
                    // if (isDiagonal(i, j, current.getFila(), current.getColumna())) continue;

                    // Si el nodo es el mismo que el parent o si es una pared, seguimos...
                    if (fuera(i, j) || (current.equals(laberinto[i][j])) || laberinto[i][j].isWall()) continue;

                    if (i != current.getFila() && j != current.getColumna()) {
                        if (pathBlockedDiagonaly(i, j)) continue;
                    }

                    // Si el nodo ya ha sido calculado lo descartamos.
                    if (laberinto[i][j].closed) continue;

                    // El nodo final ya ha sido encontrado
                    if (laberinto[i][j].equals(endNode)) {
                        resuelto = true;
                        endNode.parent = laberinto[current.getFila()][current.getColumna()];
                    }
                    else {
                        laberinto[i][j].closed = true;

                        laberinto[i][j].Gcost = calculateCost(current, laberinto[i][j]);
                        laberinto[i][j].Hcost = calculateCost(endNode, laberinto[i][j]);
                        laberinto[i][j].Fcost = laberinto[i][j].Gcost + laberinto[i][j].Hcost;

                        laberinto[i][j].parent = current;
                        parentList.add(laberinto[i][j]);

                        synchronized (this) {
                            try {
                                laberinto[i][j].pintarCelda("EXPANSION");
                                wait(leerVelocidad());
                            } catch(InterruptedException e) { }
                        }

                    }
                }
            }

            if (parentList.isEmpty() || resuelto) break;
            current = getLowestNode();
        }

        return resuelto;
    }

    // Devuelve el nodo con menos coste gracias a que la lista es previamente ordenada.
    private Celda getLowestNode() {
        parentList.sort(new SortCeldas());
        if (parentList.get(0) != null) {
            int i = parentList.get(0).getFila();
            int j = parentList.get(0).getColumna();
            laberinto[i][j].pintarCelda("ACTUAL");
        }
        return parentList.remove(0);
    }

    /**
     * Calcula el coste entre dos nodos (* 14 las casillas diagonalmente, * 10 las casillas horizontales y verticales).
     * @param target
     * @param current
     * @return coste
     */
    private int calculateCost(Celda target, Celda current) {
        return Math.abs(target.getFila() - current.getFila()) * 14 +
               Math.abs(target.getColumna() - current.getColumna()) * 10;
    }

    private boolean isDiagonal(int i, int j, int y, int x) {
        return (Math.abs(i - y) == Math.abs(j - x));
    }

    /**
     * Comprueba si el nodo actual esta bloqueado por paredes diagonalmente.
     * @param i, j
     * @return
     */
    private boolean pathBlockedDiagonaly(int i, int j) {
        return (!fuera(i+1, j) && laberinto[i+1][j].isWall() && !fuera(i, j+1) && laberinto[i][j+1].isWall())  || // left upper corner
                (!fuera(i, j-1) && laberinto[i][j-1].isWall() && !fuera(i+1, j) && laberinto[i+1][j].isWall()) || // right upper
                (!fuera(i-1, j) && laberinto[i-1][j].isWall() && !fuera(i, j+1) && laberinto[i][j+1].isWall()) || // left bottom
                (!fuera(i-1, j) && laberinto[i-1][j].isWall() && !fuera(i, j-1) && laberinto[i][j-1].isWall());   // right bottom
    }

    private boolean resolverDfs(int i, int j) {

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

            if      (resolverDfs(i - 1, j) ||
                    resolverDfs(i + 1, j)  ||
                    resolverDfs(i, j - 1)  ||
                    resolverDfs(i, j + 1))
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

class SortCeldas implements Comparator<Celda> {
    @Override
    public int compare(Celda o1, Celda o2) {
        return o1.Fcost - o2.Fcost;
    }
}
