import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class interfaz extends Application {

    @Override
    public void start(Stage ventana) throws Exception {

        Parent root = null;

        root = FXMLLoader.load(getClass().getResource("/fxml/"));

    }

    public static void main(String[] args) {
        launch(args);
    }

}
