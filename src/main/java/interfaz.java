import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Application;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import laberinto.Maze2d;

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
    IntegerBinding velocity;

    @Override
    public void start(Stage ventana) throws Exception {

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

        velocidad = new Label("Velocidad: " + slider.getValue());

        contenedorRight.getChildren().addAll(btnCrearLaberinto, btnEmpezar, btnTerminar, velocidad, slider);
        contenedorRight.setAlignment(Pos.TOP_CENTER);
        contenedorRight.setOrientation(Orientation.valueOf("VERTICAL"));
        contenedorRight.setColumnHalignment(HPos.valueOf("CENTER"));
        contenedorRight.setMinWidth(300);
        FlowPane.setMargin(btnCrearLaberinto, new Insets(20, 20, 20, 20));
        FlowPane.setMargin(btnEmpezar, new Insets(20, 20, 20, 20));
        FlowPane.setMargin(btnTerminar, new Insets(20, 20, 20, 20));
        FlowPane.setMargin(slider, new Insets(5, 20, 20, 20));
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

        //velocity = slider.valueProperty().multiply(1);
        //velocidad.textProperty().bind(velocity.asString("Velocidad %d"));

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

    public static void main(String[] args) {
        launch(args);
    }

}
