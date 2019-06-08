package Celda;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import laberinto.Maze2d;
import utils.Propiedades;

import java.util.HashMap;

/**
 * Esta clase representa una Celda del Laberinto.
 */
public class Celda extends Rectangle implements EstadoCeldas {

    private int fila;
    private int columna;
    private int valor;
    private Propiedades p;

    public Celda(int f, int c, int estado, double size) {
        super(size, size);
        this.fila = f;
        this.columna = c;
        this.valor = estado;
        p = new Propiedades();
        aplicarEstilo(estado);
        if (p.obtenerPropiedad("MODO").equals("PINTAR")) aplicarEventos();
    }

    /**
     * Colorea este objeto Rectangle dependiendo argumento.
     * @param estado
     */
    private void aplicarEstilo(int estado) {
        if (estado == PARED)
            pintarCelda("PARED");
        if (estado == ABIERTO)
            pintarCelda("ABIERTO");
        if (estado == LLEGADA)
            pintarCelda("LLEGADA");
        if (estado == ACTUAL)
            pintarCelda("ACTUAL");
        if (estado == VISITADO)
            pintarCelda("VISITADO");
        if (!(estado != VISITADO &&
              estado != ACTUAL))
            setStroke(Color.BLACK);
    }

    /**
     * Colorea el fondo de este objeto Rectangle con el color
     * que se le pase como argumento.
     * Ej: Si se le pasa "ABIERTO", leera la propiedad llamada
     * "ABIERTO" del fichero .properties y este le devolvera un
     * String con el codigo del color.
     * <p>
     * IMPORTANTE! Devuelve un String vacio si la propiedad no
     * existe
     * @param color : String de la propiedad del color en el fichero .properties
     */
    public void pintarCelda(String color) {
        this.setFill(Color.valueOf(p.obtenerPropiedad(color)));
    }

    /**
     * Se a�aden los siguientes eventos:
     *  -Click izquierdo: pinta la celda del tipo "ABIERTO"
     *  -Click derecho: pinta la celda del tipo "PARED"
     *  -Drag con el Shift: pinta la celda del tipo "ABIERTO"
     *  -Drag con el altIzquierdo: pinta la celda del tipo "PARED"
     */
    private void aplicarEventos() {
        this.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                pintarCelda("PARED");
                this.setPared();
            }
            if (e.getButton() == MouseButton.PRIMARY){
                pintarCelda("ABIERTO");
                this.setAbierto();
            }
        });
        this.setOnMouseEntered(e -> {
            if (e.isShiftDown()) {
                pintarCelda("ABIERTO");
                this.setAbierto();
            }
            if (e.isAltDown()) {
                pintarCelda("PARED");
                this.setPared();
            }
        });
    }

    private void setAbierto() {
        this.valor = ABIERTO;
    }

    private void setPared() {
        this.valor = PARED;
    }

    public void setActual() {
        this.valor = ACTUAL;
    }

    public void setVisitado() {
        this.valor = VISITADO;
    }

    public boolean estaAbierta() {
        return (this.valor == ABIERTO);
    }

    public int getFila() {
        return this.fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return this.columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getValor() {
        return this.valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

}

