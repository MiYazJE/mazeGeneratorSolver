package dominio;

public class Laberinto {

    private int[][] laberinto;
    private int columnas;
    private int filas;
    private boolean[][] visitados;
    private boolean resuelto;

    public Laberinto(int columnas, int filas) {
        this.columnas = columnas;
        this.filas = filas;
        this.laberinto = new int[filas][columnas];
        this.resuelto = false;
    }

    public Laberinto(int[][] laberinto) {
        this.laberinto = laberinto;
        this.filas     = laberinto[0].length;
        this.columnas  = laberinto.length;
        this.resuelto  = false;
    }

    public int[][] getLaberinto() {
        return laberinto;
    }

    public void setLaberinto(int[][] laberinto) {
        this.laberinto = laberinto;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    //METODOS

    public void mostrarLaberinto() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(" " + laberinto[i][j] + "   ");
            }
            System.out.println("");
        }
    }

    public void resolver() {
        this.visitados = new boolean[filas][columnas];
        int i = -1, j = -1;
        for (int k = 0; k < filas; k++) {
            for (int l = 0; l < columnas; l++) {
                if (laberinto[k][l] == -1) {
                    i = k; j = l;
                    break;
                }
            }
        }
        if (i == -1 && j == -1) System.out.println("El laberinto necesita tener un punto inicial (-1)");
        resolve(i, j);
        System.out.println((resuelto)
                ? "El laberinto ha sido resuelto."
                : "El laberinto ha sido resuelto.");
    }

    private int resolve(int f, int c) {

        if (!valido(f, c)) {

        }
        else {

            if (laberinto[f][c] == 7) {
                laberinto[f][c] = 2;
                this.resuelto = true;
                return 0;
            }
            if (!visitados[f][c] && laberinto[f][c] != 1) {
                visitados[f][c] = true;
                laberinto[f][c] = 2;
                return  resolve(f-1, c) +
                        resolve(f+1, c) +
                        resolve(f, c-1) +
                        resolve(f, c+1);
            }

        }

        return 0;
    }

    private boolean valido(int i, int j) {
        if ((i >= 0 && i < laberinto[0].length) &&
            (j >= 0 && j < laberinto.length)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder salida = new StringBuilder();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                salida.append(laberinto[i][j] + " ");
            }
            salida.append("\n");
        }
        return salida.toString();
    }
}


