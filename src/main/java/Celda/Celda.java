package Celda;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Celda extends Rectangle implements EstadoCeldas {

    private int fila;
    private int columna;
    Properties propiedades;

    public Celda(int fila, int columna, int estado, double size) {
        super(size, size);
        this.fila = fila;
        this.columna = columna;
        cargarPropiedades();
        aplicarEstilo(estado);
    }

    private void cargarPropiedades() {
        this.propiedades = new Properties();
        try {
            FileInputStream file = new FileInputStream(new File("propiedades.properties"));
            propiedades.load(file);
        } catch (IOException e) {
            System.out.println("Problemas al cargar las propiedades desde la clase Celda.");
            System.out.println(e.getMessage());
        }
    }

    private void aplicarEstilo(int estado) {
        String test = propiedades.getProperty("PARED");
        if (estado == PARED)
            setFill(Color.valueOf(propiedades.getProperty("PARED")));
        if (estado == ABIERTO)
            setFill(Color.valueOf(propiedades.getProperty("ABIERTO")));
        if (estado == LLEGADA)
            setFill(Color.valueOf(propiedades.getProperty("LLEGADA")));
        if (estado == ACTUAL)
            setFill(Color.valueOf(propiedades.getProperty("ACTUAL")));
        if (estado == VISITADO)
            setFill(Color.valueOf(propiedades.getProperty("VISITADO")));
        if (!(estado != VISITADO &&
              estado != ACTUAL))
            setStroke(Color.BLACK);
    }

    public void pintarCelda(Color color) {
        this.setFill(color);
    }

}

