package utils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import controladores.VentanaSeleccionColores;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Clase utilizada para ventanas.
 */
public class Mensaje {

    /**
     * Este método lanza una ventana con un efecto y el fondo lo emborrona con un efecto blur.
     * Utiliza una libreria llamada com.jfoenix.controls.JFXDialogLayout
     * @param stackPane
     */
    public static void mostrar(StackPane stackPane) {

        JFXDialogLayout content = new JFXDialogLayout();
        VentanaSeleccionColores ventana = new VentanaSeleccionColores();
        content.getChildren().add(ventana);

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        BoxBlur blur = new BoxBlur(3, 3, 3);
        dialog.show();

        Node pane = stackPane.getChildren().get(0);

        dialog.setOnDialogClosed((e) -> {
            pane.setEffect(null);
        });

        pane.setEffect( blur );
    }

    public static void mostrarNotificacion(StackPane stackPane, int estado) {

        JFXDialogLayout content = new JFXDialogLayout();
        Text titulo = new Text("ATENCIÓN!");
        titulo.setFont(new Font(20));
        content.setHeading(titulo);

        String[] mensajes = {
                "La configuración ha sido correctamente almacenda.",
                "Han ocurrido problemas al almacenar la configuración."
        };

        Text textoMensaje = new Text( mensajes[estado] );
        textoMensaje.setFont(new Font(15));
        content.setBody(textoMensaje);

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        JFXButton button = new JFXButton("Vale");
        button.setStyle("-fx-background-color: #2f2f2fa3; -fx-cursor: HAND; -fx-padding: 10px; -fx-text-fill: white;");

        BoxBlur blur = new BoxBlur(3, 3, 3);
        content.setActions(button);
        dialog.show();

        Node pane = (Node) stackPane.getChildren().get(0);

        button.setOnAction( e -> dialog.close() );
        dialog.setOnDialogClosed( e -> pane.setEffect(null) );
        pane.setEffect( blur );

    }

}
