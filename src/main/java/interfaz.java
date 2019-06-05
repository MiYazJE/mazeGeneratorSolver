import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import laberinto.Maze2d;
import laberinto.Propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class interfaz extends Application {

    FlowPane contenedorRight;
    JFXButton btnCrearLaberinto;
    JFXButton btnEmpezar;
    JFXButton btnTerminar;
    Maze2d laberinto;
    BorderPane pane;
    Thread thread;
    JFXSlider slider;
    Label velocidad;
    Propiedades propiedades;
    Label tituloColor;
    JFXColorPicker colorAbierto;

    @Override
    public void start(Stage ventana) throws Exception {

        propiedades = new Propiedades();
        propiedades.crearPropiedades();

        laberinto = new Maze2d(100);
        pane = new BorderPane();
        pane.setCenter(laberinto);

        contenedorRight = new FlowPane();
        BackgroundImage imagen = new BackgroundImage(new Image("/imagenes/laberinto.jpg",1920,1000,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        contenedorRight.setBackground(new Background(imagen));
        btnCrearLaberinto = new JFXButton("Crear laberinto");
        btnCrearLaberinto.getStyleClass().add("btn-rightPanel");
        btnEmpezar = new JFXButton("Empezar");
        btnEmpezar.getStyleClass().add("btn-rightPanel");

        btnTerminar = new JFXButton("Terminar");
        btnTerminar.getStyleClass().add("btn-rightPanel");

        slider = new JFXSlider();
        slider.setValue(20);
        slider.setMin(1);
        slider.setMax(1000);

        VBox cajaColor = cargarComponentesEleccionColor();

        contenedorRight.getChildren().addAll(btnCrearLaberinto, btnEmpezar,
                btnTerminar, velocidad, slider, cajaColor);
        contenedorRight.setAlignment(Pos.TOP_CENTER);
        contenedorRight.setOrientation(Orientation.valueOf("VERTICAL"));
        contenedorRight.setColumnHalignment(HPos.valueOf("CENTER"));
        contenedorRight.setMinWidth(300);
        FlowPane.setMargin(btnCrearLaberinto, new Insets(20, 20, 20, 20));
        FlowPane.setMargin(btnEmpezar, new Insets(20, 20, 20, 20));
        FlowPane.setMargin(btnTerminar, new Insets(20, 20, 20, 20));
        FlowPane.setMargin(slider, new Insets(5, 20, 20, 20));
        FlowPane.setMargin(cajaColor, new Insets(40, 20, 20, 20));
        pane.setRight(contenedorRight);

        crearEventos();

        Scene scene = new Scene(pane);
        scene.getStylesheets().add("/estilos.css");

        ventana.setScene(scene);
        ventana.setTitle("Laberinto");
        ventana.setResizable(false);
        ventana.show();
        laberinto.requestFocus();

        thread = new Thread(laberinto);

    }

    private void crearEventos() {

        slider.valueProperty().addListener((o, oldValue, newValue) -> {
            this.velocidad.setText("Velocidad: " + (int)slider.getValue());
        });

        btnCrearLaberinto.setOnAction(e -> {
            if (this.thread.isAlive()) {
                this.thread.stop();
            }
            if (!this.thread.isAlive()) {
                this.laberinto.generarLaberinto((int)slider.getValue());
            }
        });

        btnEmpezar.setOnAction(e -> {
            if (thread != null) {
                if (!this.thread.isAlive()) {
                    this.laberinto.generarLaberinto((int)slider.getValue());
                    this.thread = new Thread(laberinto);
                    this.thread.start();
                }
            }
        });

        btnTerminar.setOnAction(e -> this.thread.stop());

    }

    private VBox cargarComponentesEleccionColor() {

        VBox cajaColor = new VBox();
        velocidad = new Label("Velocidad: " + slider.getValue());

        tituloColor = new Label("Selecciona un color");
        tituloColor.getStyleClass().add("seleccion-color");

        Map<String, String> conf = cargarColores();
        colorAbierto = new JFXColorPicker(Color.valueOf(conf.get("ABIERTO")));

        cajaColor.getChildren().addAll(new HBox(tituloColor), new HBox(colorAbierto));

        return cajaColor;
    }

    private Map<String, String> cargarColores() {

        Properties p = new Properties();
        Map<String, String> conf = new HashMap<>();
        try {
            FileInputStream file = new FileInputStream(new File("propiedades.properties"));
            p.load(file);
            /*
PARED=\#000000
ABIERTO=\#FFFFFF
LLEGADA=\#FF0000
VISITADO=\#0027FF
ACTUAL=\#2AFF00
             */
            conf.put("ABIERTO", p.getProperty("ABIERTO"));

        } catch (IOException e) {}

        return conf;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
