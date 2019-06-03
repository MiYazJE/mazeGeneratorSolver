/**
 * @author Ruben Saiz
 */
package laberinto;

import Celda.Celda;

import java.util.Random;
import java.util.Stack;

public class GenerarLaberinto {

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

    private void crearLaberinto() {

        int i,j;
        int emptyCt = 0;
        int wallCt = 0;

        int[] wallrow = new int[(filas*columnas)/2];
        int[] wallcol = new int[(filas*columnas)/2];

        for (i = 0; i<filas; i++)
            for (j = 0; j < columnas; j++)
                maze[i][j] = 1;

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

        // Reemplazar valores negativos por casillas vacias(0)
        for (i=1; i<filas-1; i++)
            for (j=1; j<columnas-1; j++)
                if (maze[i][j] < 0)
                    maze[i][j] = 0;
    }

    void tearDown(int row, int col) {
        if (row % 2 == 1 && maze[row][col-1] != maze[row][col+1]) {
            fill(row, col-1, maze[row][col-1], maze[row][col+1]);
            maze[row][col] = maze[row][col+1];
        }
        else if (row % 2 == 0 && maze[row-1][col] != maze[row+1][col]) {
            fill(row-1, col, maze[row-1][col], maze[row+1][col]);
            maze[row][col] = maze[row+1][col];
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
