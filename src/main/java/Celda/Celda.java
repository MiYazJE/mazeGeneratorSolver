package Celda;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Celda extends Rectangle implements EstadoCeldas {

    private int fila;
    private int columna;

    public Celda(int fila, int columna, int estado, double size) {
        super(size, size);
        this.fila = fila;
        this.columna = columna;
        aplicarEstilo(estado);
    }

    private void aplicarEstilo(int estado) {
        if (estado == PARED)
            setFill(Color.BLACK);
        if (estado == ABIERTO)
            setFill(Color.WHITE);
        if (estado == LLEGADA)
            setFill(Color.RED);
        if (estado == ACTUAL)
            setFill(Color.GREEN);
        if (estado == VISITADO)
            setFill(Color.BLUE);
        if (!(estado != VISITADO &&
              estado != ACTUAL))
            setStroke(Color.BLACK);
    }

    public void pintarCelda(Color color) {
        this.setFill(color);
    }

}

