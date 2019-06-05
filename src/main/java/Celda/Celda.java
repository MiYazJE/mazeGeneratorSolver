package Celda;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Celda extends Rectangle implements EstadoCeldas {

    private int fila;
    private int columna;
    private HashMap<String, String> colores;

    public Celda(int fila, int columna, int estado, double size, HashMap<String, String> colores) {
        super(size, size);
        this.colores = colores;
        this.fila = fila;
        this.columna = columna;
        aplicarEstilo(estado);
    }

    private void aplicarEstilo(int estado) {
        if (estado == PARED)
            setFill(Color.valueOf(colores.get("PARED")));
        if (estado == ABIERTO)
            setFill(Color.valueOf(colores.get("ABIERTO")));
        if (estado == LLEGADA)
            setFill(Color.valueOf(colores.get("LLEGADA")));
        if (estado == ACTUAL)
            setFill(Color.valueOf(colores.get("ACTUAL")));
        if (estado == VISITADO)
            setFill(Color.valueOf(colores.get("VISITADO")));
        if (!(estado != VISITADO &&
              estado != ACTUAL))
            setStroke(Color.BLACK);
    }

    public void pintarCelda(Color color) {
        this.setFill(color);
    }

}

