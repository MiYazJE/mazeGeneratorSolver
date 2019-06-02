import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import laberinto.Maze2d;

public class interfaz extends Application {

    @Override
    public void start(Stage ventana) throws Exception {

        Maze2d laberinto = new Maze2d();
        BorderPane panel = new BorderPane(laberinto);
        Parent root = panel;
        Scene scene = new Scene(root, 800, 800);
        ventana.setScene(scene);
        ventana.setTitle("Laberinto");
        ventana.setResizable(false);
        ventana.show();
        laberinto.requestFocus();

        new Thread(laberinto).start();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
