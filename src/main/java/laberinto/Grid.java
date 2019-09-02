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

    private int[][] maze;
    // Almacena cada una de las celdas generadas en el laberinto
    private Celda[][] laberinto;
    private int dimension;
    private double sizeCelda;
    private GenerarLaberinto gen;
    private HashMap<String, String> conf;
    private Propiedades propiedades;
    private MakeMazeDfs genDFS;

    // Needed for A * search
    private ArrayList<Celda> parentList;
    private Celda startNode, endNode;
    private boolean resuelto;
    private int nodosCerrados;

    public Grid() {
        propiedades = new Propiedades();
        this.gen = new GenerarLaberinto();
        genDFS = new MakeMazeDfs();
        generarLaberinto();
        parentList = new ArrayList<>();
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

    public void generarLaberinto() {
        conf = Propiedades.cargarPropiedades();
        cargarDimensiones();

        if (conf.get("MODO").equals("LABERINTO")) {
            if (conf.get("ALGORITMOCREACION").equals("DFS")) {
                System.out.println("Creando el laberinto con dfs...");
                genDFS.crearLaberinto(dimension);
                maze = genDFS.getLaberinto();
            } else {
                System.out.println("Creando el laberinto con random recursive...");
                gen.crearLaberinto(dimension);
                maze = gen.getLaberinto();
            }
        } else {
            // Para el modo dibujar
            iniciarCeldas();
        }

        pintarLaberinto();
    }

    private Long leerVelocidad() {
        return Long.parseLong(propiedades.obtenerPropiedad("VELOCIDAD"));
    }

    @Override
    public void run() {
        conf = Propiedades.cargarPropiedades();
        if (buscarInicioyFinal()) {
            resuelto = false;
            if (conf.get("ALGORITMOBUSQUEDA").equals("A star")) {
                iniciarAstar();
            }
            else if (conf.get("ALGORITMOBUSQUEDA").equals("DFS")) {
                long inicio = System.nanoTime();
                System.out.println("Iniciando algoritmo de busqueda DFS...");
                if (resolverDfs(startNode.getFila(), startNode.getColumna())) {
                    long fin = System.nanoTime();
                    System.out.println("Resuelto en " + ((fin - inicio) / 1_000_000_000.0) + " segundos");
                }
                else {
                    System.out.println("El laberinto no ha sido resuelto.");
                }
            }
        } else {
            // Cannot start exception, declare initial and final points.
            Platform.runLater(() -> {
                Mensaje.mostrarNotificacion(Interfaz.contenedorGlobal, 2);
                System.out.println("ERROR. Cannot start exception, declare initial and final points.");
            });
        }
    }

    private void iniciarAstar() {
        System.out.println("Iniciando algoritmo de busqueda A star...");
        parentList = new ArrayList<>();
        long inicio = System.nanoTime();
        if (resolverAStar(startNode)) {
            paintPath(endNode.parent);
            long fin = System.nanoTime();
            System.out.println("Resuelto en " + ((fin - inicio) / 1_000_000_000.0) + " segundos");
            System.out.println("Nodos cerrados: " + nodosCerrados);
            System.out.println("Nodos abiertos: " + parentList.size());
        } else {
            System.out.println( "LABERINTO NO RESUELTO!" );
        }

    }

    private boolean buscarInicioyFinal() {
        boolean inicio = false, end = false;
        for (int i = 0; i < dimension && (!inicio || !end); i++) {
            for (int j = 0; j < dimension && (!inicio || !end); j++) {
                if (laberinto[i][j].isStart()) {
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

        int tempHcost, tempFcost, tempGcost;

        while (true) {

            current.closed = true;

            for (int i = current.getFila() - 1; i <= current.getFila() + 1 && !resuelto; i++) {
                for (int j = current.getColumna() - 1; j <= current.getColumna() + 1 && !resuelto; j++) {

                    // Evitar si el nodo se encuentra en cualquier diagonal
                    if (!conf.get("DIAGONALES").equals("SI"))
                        if (isDiagonal(i, j, current.getFila(), current.getColumna())) continue;

                    // Si el nodo es el mismo que el padre o si es una pared, seguimos...
                    if (fuera(i, j) || (current.equals(laberinto[i][j])) || laberinto[i][j].isWall()) continue;

                    if (conf.get("DIAGONALES").equals("SI"))
                        if (i != current.getFila() && j != current.getColumna()) {
                            if (pathBlockedDiagonaly(i, j)) continue;
                        }

                    // Si en el nodo ya se han conocido todos sus vecinos los descartamos.
                    if (laberinto[i][j].closed) continue;

                    // El nodo final ya ha sido encontrado
                    if (laberinto[i][j].equals(endNode)) {
                        resuelto = true;
                        System.out.println(parentList);
                        endNode.parent = laberinto[current.getFila()][current.getColumna()];
                    }
                    else {

                        tempGcost = current.Gcost + calculateCost(current, laberinto[i][j]);
                        tempHcost = calculateCost(endNode, laberinto[i][j]);
                        tempFcost = tempHcost + tempGcost;

                        if (laberinto[i][j].parent == null) {
                            asignarCostes(laberinto[i][j], tempHcost, tempGcost, current);
                            if (dimension <= 11) {
                                laberinto[i][j].setInfo();
                            }
                        }
                        else {
                            if (tempFcost < laberinto[i][j].Fcost) {
                                parentList.remove(laberinto[i][j]);
                                asignarCostes(laberinto[i][j], tempHcost, tempGcost, current);
                            }
                        }

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

    private void paintPath(Celda node) {
        if (node.parent == null) return;

        // System.out.println( node.getFila() + "-" + node.getColumna() );

        paintPath(node.parent);

        synchronized (this) {
            try {
                laberinto[node.getFila()][node.getColumna()].pintarCelda("VUELTA");
                wait(40);
            } catch (InterruptedException e) { }
        }
    }

    private void asignarCostes(Celda nodo, int Hcost, int Gcost, Celda parent) {

        nodo.Gcost = Gcost;
        nodo.Hcost = Hcost;
        nodo.Fcost = Hcost + Gcost;
        nodo.parent = parent;

        if (!parentList.isEmpty() && nodo.Fcost <= parentList.get(0).Fcost && nodo.Hcost <= parentList.get(0).Hcost)
            parentList.add(0, nodo);
        else
            parentList.add( BB(parentList, nodo), nodo );
    }

    // Devuelve el nodo con menos coste gracias a que la lista es previamente ordenada.
    private Celda getLowestNode() {
        if (parentList.get(0) != null) {
            parentList.get(0).pintarCelda("ACTUAL");
        }
        this.nodosCerrados++;
        return parentList.remove(0);
    }

    /**
     * Calcula el coste entre dos nodos (* 14 las casillas diagonalmente, * 10 las casillas horizontales y verticales).
     * @param target
     * @param current
     * @return coste
     */
    private int calculateCost(Celda target, Celda current) {

        int difRows = Math.abs(current.getFila() - target.getFila());
        int difCols = Math.abs(current.getColumna() - target.getColumna());

        int gCost = Math.min(difRows, difCols);
        int hCost = Math.max(difRows, difCols) - Math.min(difRows, difCols);
        return (gCost * 14) + (hCost * 10);
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

    private int BB(ArrayList<Celda> list, Celda node) {
        int left = 0;
        int right = list.size();
        while (left < right) {
            int middle = (left + right) / 2;
            if (list.get(middle).Fcost > node.Fcost && list.get(middle).Hcost > node.Hcost)
                right = middle;
            else
                left = middle + 1;
        }
        return left;
    }

    /**
     * Cambia el estado de todas las celdas que no sean Pared a Abierto.
     */
    public void limpiarLaberinto() {
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                if (!laberinto[i][j].isWall())
                    laberinto[i][j].restart();
                laberinto[i][j].restartLabel();
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

    private void cargarDimensiones() {
        this.dimension = Integer.parseInt(conf.get("DIMENSION"));
        this.sizeCelda = 850 / dimension;
        this.maze = new int[dimension][dimension];
        this.laberinto = new Celda[dimension][dimension];
    }

    private void iniciarCeldas() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                maze[i][j] = ABIERTO;
                laberinto[i][j] = new Celda(i, j);
                laberinto[i][j].setAbierto();
            }
        }
    }

}

class SortCeldas implements Comparator<Celda> {
    @Override
    public int compare(Celda o1, Celda o2) {
        if (o1.Fcost == o2.Fcost)
            return o1.Hcost - o2.Hcost;
        return o1.Fcost - o2.Fcost;
    }
}
