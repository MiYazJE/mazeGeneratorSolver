/**
 * @author Ruben Saiz
 */
package laberinto;

import Celda.*;

import java.util.Random;
import java.util.Stack;

public class GenerarLaberinto implements EstadoCeldas {

    private int filas;
    private int columnas;
    private int[][] maze;
    private Stack<Celda> pila = new Stack<>();
    private Celda[][] celdas;
    private Celda actual;
    private Stack<Celda> vecinos;
    private Random rnd = new Random();

    public GenerarLaberinto(int f, int c) {
        this.filas = f;
        this.columnas = c;
        this.maze = new int[filas][columnas];
        crearLaberinto();
    }

    public void crearLaberinto() {

        int i,j;
        int emptyCt = 0;
        int wallCt = 0;

        int[] wallrow = new int[(filas*columnas)/2];
        int[] wallcol = new int[(filas*columnas)/2];

        for (i = 0; i<filas; i++)
            for (j = 0; j < columnas; j++)
                maze[i][j] = PARED;

        for (i = 1; i<filas-1; i += 2)  {
            for (j = 1; j<columnas-1; j += 2) {
                emptyCt++;
                maze[i][j] = -emptyCt;
                if (i < filas-2) {
                    wallrow[wallCt] = i+1;
                    wallcol[wallCt] = j;
                    wallCt++;
                }
                if (j < columnas-2) {
                    wallrow[wallCt] = i;
                    wallcol[wallCt] = j+1;
                    wallCt++;
                }
            }
        }

        int r;
        for (i=wallCt-1; i>0; i--) {
            r = (int)(Math.random() * i);
            tearDown(wallrow[r],wallcol[r]);
            wallrow[r] = wallrow[i];
            wallcol[r] = wallcol[i];
        }

        // Reemplazar valores negativos por casillas abiertas
        for (i=1; i<filas-1; i++)
            for (j=1; j<columnas-1; j++)
                if (maze[i][j] < 0)
                    maze[i][j] = ABIERTO;
    }

    private void tearDown(int fila, int col) {
        if (fila % 2 != 0 && maze[fila][col-1] != maze[fila][col+1]) {
            fill(fila, col-1, maze[fila][col-1], maze[fila][col+1]);
            maze[fila][col] = maze[fila][col+1];
        }
        else if (fila % 2 == 0 && maze[fila-1][col] != maze[fila+1][col]) {
            fill(fila-1, col, maze[fila-1][col], maze[fila+1][col]);
            maze[fila][col] = maze[fila+1][col];
        }
    }

    private void fill(int row, int col, int replace, int replaceWith) {
        if (maze[row][col] == replace) {
            maze[row][col] = replaceWith;
            fill(row+1,col,replace,replaceWith);
            fill(row-1,col,replace,replaceWith);
            fill(row,col+1,replace,replaceWith);
            fill(row,col-1,replace,replaceWith);
        }
    }

    public int[][] getLaberinto() {
        return this.maze;
    }

    private void imprimir() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
