import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import laberinto.Maze2d;

public class interfaz extends Application {

    @Override
    public void start(Stage ventana) throws Exception {

        Maze2d laberinto = new Maze2d(100);
        BorderPane pane = new BorderPane(laberinto);
        Scene scene = new Scene(pane);
        ventana.setScene(scene);
        ventana.setTitle("Laberinto");
        ventana.setResizable(false);
        ventana.show();
        laberinto.requestFocus();

        Thread thread = new Thread(laberinto);
        thread.setDaemon(true);
        thread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
