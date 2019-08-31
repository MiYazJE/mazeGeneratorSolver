package Celda;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utils.Propiedades;

import java.util.Comparator;
import java.util.Objects;

/**
 * Esta clase representa una Celda en una cuadricula.
 */
public class Celda extends Rectangle implements EstadoCeldas {

    private int fila;
    private int columna;
    private int valor;
    private Propiedades p;
    private int anteriorColor;

    public Celda parent;
    public int Gcost;
    public int Hcost;
    public int Fcost;
    public boolean closed;

    public Celda(int f, int c) {
        this.fila = f;
        this.columna = c;
    }

    public Celda(int f, int c, int estado, double size) {
        super(size, size);
        this.fila = f;
        this.columna = c;
        this.valor = estado;
        p = new Propiedades();
        aplicarEstilo(estado);
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
     * "ABIERTO" del fichero .properties c este le devolvera un
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
     *  -Click izquierdo: cambia el estado de la celda a INICIO
     *  -Click derecho: cambia el estado de la celda a FIN
     *  -Drag con el Shift: cambia el estado de la celda a ABIERTO
     *  -Drag con el altIzquierdo: cambia el estado de la celda a PARED
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

    /**
     * Pinta la celda y cambia su valor a ABIERTO
     */
    public void setAbierto() {
        restart();
        this.valor = ABIERTO;
        pintarCelda("ABIERTO");
    }

    /**
     * Pinta la celda y cambia su valor a ABIERTO
     */
    public void setPared() {
        this.valor = PARED;
        pintarCelda("PARED");
    }

    /**
     * Cambia su valor a LLEGADA
     */
    public void setEnd() {
        this.valor = LLEGADA;
    }

    /**
     * Pinta la celda y cambia su valor a ACTUAL
     */
    public void setActual() {
        this.valor = ACTUAL;
        pintarCelda("ACTUAL");
    }

    /**
     * Pinta la celda y cambia su valor a INICIO
     */
    public void setInicio() {
        this.valor = INICIO;
        pintarCelda("INICIO");
    }

    /**
     * Pinta la celda y cambia su valor a LLEGADA
     */
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

    public boolean isWall() {
        return this.valor == PARED;
    }

    public void restart() {
        this.Gcost = 0;
        this.Hcost = 0;
        this.Fcost = 0;
        this.closed = false;
        this.parent = null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Celda celda = (Celda) o;
        return fila == celda.fila &&
                columna == celda.columna;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fila, columna);
    }

    @Override
    public String toString() {
        return "fila=" + fila + ", columna=" + columna + ", hcost=" + Hcost + ", gcost=" + Gcost + ", fcost=" + Fcost;
    }
}
