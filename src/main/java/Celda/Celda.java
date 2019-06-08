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
    private int anteriorColor;

    public Celda(int f, int c, int estado, double size) {
        super(size, size);
        this.fila = f;
        this.columna = c;
        this.valor = estado;
        p = new Propiedades();
        aplicarEstilo(estado);
        //if (p.obtenerPropiedad("MODO").equals("PINTAR")) aplicarEventos();
        aplicarEventos();
    }

    /**
     * Colorea este objeto Rectangle dependiendo argumento.
     * @param estado
     */
    private void aplicarEstilo(int estado) {
        if (estado == PARED)
            setPared();

        if (estado == ABIERTO)
            setAbierto();

        if (estado == LLEGADA)
            setLlegada();

        if (estado == ACTUAL)
            setActual();

        if (estado == INICIO)
            setInicio();
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
     * Se añaden los siguientes eventos:
     *  -Click izquierdo: pinta la celda del tipo "ABIERTO"
     *  -Click derecho: pinta la celda del tipo "PARED"
     *  -Drag con el Shift: pinta la celda del tipo "ABIERTO"
     *  -Drag con el altIzquierdo: pinta la celda del tipo "PARED"
     */
    private void aplicarEventos() {
        this.setOnMouseClicked(e -> {

            if (e.getButton() == MouseButton.SECONDARY) {
                if (this.getValor() == LLEGADA) {
                    if (anteriorColor == PARED)   setPared();
                    if (anteriorColor == INICIO)  setInicio();
                    if (anteriorColor == ABIERTO) setAbierto();
                }
                else {
                    anteriorColor = this.getValor();
                    this.setLlegada();
                }
            }

            if (e.getButton() == MouseButton.PRIMARY) {
                if (this.getValor() == INICIO) {
                    if (anteriorColor == PARED)   setPared();
                    if (anteriorColor == LLEGADA) setLlegada();
                    if (anteriorColor == ABIERTO) setAbierto();
                }
                else {
                    anteriorColor = this.getValor();
                    this.setInicio();
                }
            }

        });
        this.setOnMouseEntered(e -> {
            if (e.isShiftDown()) {
                this.setAbierto();
            }
            if (e.isAltDown()) {
                this.setPared();
            }
        });
    }

    public void setAbierto() {
        this.valor = ABIERTO;
        pintarCelda("ABIERTO");
    }

    public void setPared() {
        this.valor = PARED;
        pintarCelda("PARED");
    }

    public void setEnd() {
        this.valor = LLEGADA;
    }

    public void setActual() {
        this.valor = ACTUAL;
        pintarCelda("ACTUAL");
    }

    public void setInicio() {
        this.valor = INICIO;
        pintarCelda("INICIO");
    }

    public void setLlegada() {
        this.valor = LLEGADA;
        pintarCelda("LLEGADA");
    }

    public boolean isOpen() {
        return (this.valor == ABIERTO);
    }

    public boolean isEnd() {
        return this.valor == LLEGADA;
    }

    public int getValor() {
        return this.valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public boolean isStart() {
        return this.valor == INICIO;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}

