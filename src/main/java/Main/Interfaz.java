package Main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import controladores.VentanaConfiguracion;
import controladores.VentanaModoUso;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import laberinto.Grid;
import utils.Propiedades;
import utils.Mensaje;

import java.io.IOException;

public class Interfaz extends Application {

    public static StackPane contenedorGlobal;
    private BorderPane contenedorLeft;
    private JFXButton btnCrearLaberinto;
    private JFXButton btnEmpezar;
    private JFXButton btnConfiguracion;
    private Grid laberinto;
    private BorderPane pane;
    private Thread thread;
    private JFXSlider slider;
    private Label velocidad;
    private Propiedades propiedades;
    private VBox cajaComponentes;
    private JFXButton btnLimpiar;
    private JFXButton btnModoUso;
    private JFXButton btnCreditos;
    private VentanaModoUso ventanaModoUso;
    private VentanaConfiguracion ventanaConfiguracion;


    @Override
    public void start(Stage ventana) throws IOException {

        cargarPropiedades();

        laberinto = new Grid();
        this.thread = new Thread(laberinto);
        pane = new BorderPane();
        pane.setCenter(laberinto);

        btnConfiguracion = new JFXButton("CONFIGURACIÓN");
        btnConfiguracion.getStyleClass().add("btn-bottomPanel");

        btnModoUso = new JFXButton("MODO USO");
        btnModoUso.getStyleClass().add("btn-bottomPanel");

        btnCreditos = new JFXButton("CREDITOS");
        btnCreditos.getStyleClass().add("btn-bottomPanel");

        btnCrearLaberinto = new JFXButton("Crear laberinto");
        btnCrearLaberinto.getStyleClass().add("btn-rightPanel");

        btnEmpezar = new JFXButton("Empezar");
        btnEmpezar.getStyleClass().add("btn-rightPanel");

        VBox cajaSlider = new VBox();
        slider = new JFXSlider();
        propiedadesSlider();
        velocidad = new Label("Milisegundos " + slider.getValue());
        btnLimpiar = new JFXButton("Limpiar");
        btnLimpiar.getStyleClass().add("btn-limpiar");
        cajaSlider.getChildren().addAll(slider, velocidad, btnLimpiar);
        cajaSlider.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(btnLimpiar, new Insets(20,0, 0, 0));

        cajaComponentes = new VBox();
        propiedadesComponentes();
        cajaComponentes.getChildren().addAll(btnCrearLaberinto, btnEmpezar,
                cajaSlider);

        contenedorLeft = new BorderPane();
        contenedorLeft.setCenter(cajaComponentes);

        VBox cajaBottom = new VBox(btnConfiguracion, btnModoUso, btnCreditos);
        cajaBottom.setAlignment(Pos.CENTER);
        cajaBottom.setSpacing(20);

        contenedorLeft.setBottom(cajaBottom);
        BorderPane.setMargin(btnConfiguracion, new Insets(0, 60, 50, 70));
        BorderPane.setMargin(cajaBottom, new Insets(0, 0, 40, 0));
        propiedadesContenedorLeft();
        aplicarMargenes();

        pane.setLeft(contenedorLeft);
        contenedorGlobal = new StackPane(pane);

        crearEventos();
        creacionVentanas();

        Scene scene = new Scene(contenedorGlobal);
        scene.getStylesheets().add("estilos.css");

        ventana.setScene(scene);
        ventana.setTitle("El Laberinto");
        ventana.getIcons().add(new Image("/imagenes/iconMaze.png"));
        ventana.setResizable(false);
        ventana.show();

    }

    /**
     * Inicializa el objeto propiedades y lanza el método crearPropiedades
     * para que en caso de no existir el fichero .properties este sea
     * credo con las opciones predeterminadas.
     */
    private void cargarPropiedades() {
        propiedades = new Propiedades();
        propiedades.crearPropiedades();
    }

    private void propiedadesContenedorLeft() {
        BackgroundImage imagen = new BackgroundImage(new Image("/imagenes/laberinto.jpg",1920,1080,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        contenedorLeft.setBackground(new Background(imagen));
    }

    private void propiedadesComponentes() {
        cajaComponentes.setAlignment(Pos.TOP_CENTER);
        cajaComponentes.setSpacing(50);
        BorderPane.setMargin(cajaComponentes, new Insets(40, 40, 40 ,40));
    }

    /**
     * Creación de los eventos utilizados en la interfaz.
     */
    private void crearEventos() {

        slider.valueProperty().addListener((o, oldValue, newValue) -> {
            this.velocidad.setText("Milisegundos: " + (int)slider.getValue());
            int num = Double.valueOf(slider.getValue()).intValue();
            propiedades.almacenarVelocidad(String.valueOf(num));
        });

        btnLimpiar.setOnAction(e -> {
            if (!thread.isAlive())
                laberinto.limpiarLaberinto();
        });

        btnCrearLaberinto.setOnAction(e -> {
            this.thread.stop();
            this.laberinto.generarLaberinto();
            this.thread = new Thread(laberinto);
        });

        btnEmpezar.setOnAction(e -> {
            if (!this.thread.isAlive()) {
                this.thread = new Thread(laberinto);
                this.thread.start();
            }
        });

        /*
        EVENTOS BOTONES Panel Bottom
         */
        btnConfiguracion.setOnAction(e -> lanzarVentanaConfiguracion());
        btnModoUso.setOnAction(e -> lanzarVentanaModoUso());
        btnCreditos.setOnAction(e -> lanzarVentanaCreditos());

    }

    /**
     * Margenes utilizados en el contenedorLeft para sus componentes hijos
     */
    private void aplicarMargenes() {
        BorderPane.setMargin(btnCrearLaberinto, new Insets(50, 30, 30, 30));
        BorderPane.setMargin(slider, new Insets(30, 30, 30, 30));
        BorderPane.setMargin(btnEmpezar, new Insets(5, 30, 30, 30));
    }

    /**
     * Propiedades del componente slider
     */
    private void propiedadesSlider() {
        slider.setMaxWidth(150);
        Integer valorSlider = Integer.valueOf(propiedades.obtenerPropiedad("VELOCIDAD"));
        slider.setValue(valorSlider);
        slider.setMin(1);
        slider.setMax(150);
    }

    private void creacionVentanas() {
        ventanaConfiguracion = new VentanaConfiguracion();
        ventanaModoUso = new VentanaModoUso();
    }

    /**
     * Muestra un mensaje con un efecto.
     */
    private void lanzarVentanaConfiguracion() {
        ventanaConfiguracion.cargarValores();
        Mensaje.mostrar(contenedorGlobal, ventanaConfiguracion);
    }

    /**
     * Lanza una ventana mostrando los modos de uso del programa
     */
    private void lanzarVentanaModoUso() {
        Mensaje.mostrar(contenedorGlobal, ventanaModoUso);
    }

    /**
     * Lanaza y muestra una ventana con los creditos del programa.
     */
    private void lanzarVentanaCreditos() {
        Mensaje.mostrarCreditos(contenedorGlobal);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
