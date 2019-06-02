package laberinto;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Celda extends Rectangle {

    public int fila;
    public int columna;

    private final int ABIERTO  = 0;
    private final int PARED    = 1;
    private final int LLEGADA  = 7;
    private final int ACTUAL   = -1;
    private final int VISITADO = 2;

    public Celda(int fila, int columna, int estado, double size) {
        super(size, size);
        this.fila = fila;
        this.columna = columna;
        aplicarEstilo(estado);
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    private void aplicarEstilo(int estado) {
        if (estado == PARED)    setFill(Color.BLACK);
        if (estado == ABIERTO)  setFill(Color.WHITE);
        if (estado == LLEGADA)  setFill(Color.RED);
        if (estado == ACTUAL)   setFill(Color.GREEN);
        if (estado == VISITADO) setFill(Color.BLUE);
        if (!(estado != VISITADO && estado != ACTUAL)) setStroke(Color.BLACK);
    }

    public void cambiarPos(int POS) {
        switch (POS) {
            case ACTUAL: this.setFill(Color.GREEN); break;
            case ABIERTO: this.setFill(Color.WHITE); break;
        }
    }

    public void pintarCelda(Color color) {
        this.setFill(color);
    }

}

