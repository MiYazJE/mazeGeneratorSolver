import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import controladores.VentanaSeleccionColores;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import laberinto.Maze2d;
import utils.Propiedades;
import utils.Mensaje;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Interfaz extends Application {

    StackPane contenedorGlobal;
    BorderPane contenedorLeft;
    JFXButton btnCrearLaberinto;
    JFXButton btnEmpezar;
    JFXButton btnConfiguracion;
    Maze2d laberinto;
    BorderPane pane;
    Thread thread;
    JFXSlider slider;
    Label velocidad;
    Propiedades propiedades;
    VBox cajaComponentes;

    @Override
    public void start(Stage ventana) throws IOException {

        cargarPropiedades();

        laberinto = new Maze2d();
        pane = new BorderPane();
        pane.setCenter(laberinto);

        btnConfiguracion = new JFXButton("CONFIGURACIÓN");
        btnConfiguracion.getStyleClass().add("btn-configuracion");

        btnCrearLaberinto = new JFXButton("Crear laberinto");
        btnCrearLaberinto.getStyleClass().add("btn-rightPanel");

        btnEmpezar = new JFXButton("Empezar");
        btnEmpezar.getStyleClass().add("btn-rightPanel");

        VBox cajaSlider = new VBox();
        slider = new JFXSlider();
        propiedadesSlider();
        velocidad = new Label("Velocidad " + slider.getValue());
        cajaSlider.getChildren().addAll(slider, velocidad);
        cajaSlider.setAlignment(Pos.TOP_CENTER);

        cajaComponentes = new VBox();
        propiedadesComponentes();
        cajaComponentes.getChildren().addAll(btnCrearLaberinto, btnEmpezar,
                cajaSlider);

        contenedorLeft = new BorderPane();
        contenedorLeft.setCenter(cajaComponentes);
        contenedorLeft.setBottom(btnConfiguracion);
        BorderPane.setMargin(btnConfiguracion, new Insets(0, 60, 50, 70));
        propiedadesContenedorLeft();
        aplicarMargenes();

        pane.setLeft(contenedorLeft);
        contenedorGlobal = new StackPane(pane);

        crearEventos();

        Scene scene = new Scene(contenedorGlobal);
        scene.getStylesheets().add("estilos.css");

        ventana.setScene(scene);
        ventana.setTitle("Laberinto");
        ventana.setResizable(false);
        ventana.show();

    }

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

    private void crearEventos() {

        slider.valueProperty().addListener((o, oldValue, newValue) -> {
            this.velocidad.setText("Velocidad: " + (int)slider.getValue());
            int num = Double.valueOf(slider.getValue()).intValue();
            propiedades.almacenarVelocidad(String.valueOf(num));
        });

        btnCrearLaberinto.setOnAction(e -> {
            if (this.thread.isAlive()) {
                this.thread.stop();
                this.laberinto.generarLaberinto();
            }
            if (!this.thread.isAlive()) {
                this.laberinto.generarLaberinto();
            }
        });

        btnEmpezar.setOnAction(e -> {
            thread = new Thread(laberinto);
            this.thread.start();
        });

        btnConfiguracion.setOnAction(e -> lanzarVentanaConfiguracion());

    }

    private void aplicarMargenes() {
        BorderPane.setMargin(btnCrearLaberinto, new Insets(50, 30, 30, 30));
        BorderPane.setMargin(slider, new Insets(30, 30, 30, 30));
        BorderPane.setMargin(btnEmpezar, new Insets(5, 30, 30, 30));
    }

    private void propiedadesSlider() {
        slider.setMaxWidth(150);
        Integer valorSlider = Integer.valueOf(propiedades.obtenerPropiedad("VELOCIDAD"));
        slider.setValue(valorSlider);
        slider.setMin(1);
        slider.setMax(150);
    }

    private void lanzarVentanaConfiguracion() {
        Mensaje.mostrar(contenedorGlobal);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
