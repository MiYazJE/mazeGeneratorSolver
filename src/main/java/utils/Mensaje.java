package utils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;
import java.net.URI;

/**
 * Clase utilizada para ventanas.
 */
public class Mensaje {

    /**
     * Este método lanza una ventana con un efecto y el fondo lo emborrona con un efecto blur.
     * Utiliza una libreria llamada com.jfoenix.controls.JFXDialogLayout
     * @param stackPane
     */
    public static void mostrar(StackPane stackPane, Node ventana) {

        JFXDialogLayout content = new JFXDialogLayout();
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

    /**
     * Crea y lanza una ventana con un mensaje, este mensaje depende del estado que le pases
     * 0: Todo OK
     * 1: MAL
     * 2: Inserte un nodo de inicio.
     * @param stackPane : Este componente es el que indica las coordenadas de la ventana creada
     * @param estado : El mensaje que muestre la ventana depende de este estado.
     */
    public static void mostrarNotificacion(StackPane stackPane, int estado) {

        JFXDialogLayout content = new JFXDialogLayout();
        Text titulo = new Text("ATENCIÓN!");
        titulo.setFont(new Font(20));
        content.setHeading(titulo);

        String[] mensajes = {
                "La configuración ha sido correctamente almacenda.",
                "Han ocurrido problemas al almacenar la configuración.",
                "Por favor indique un nodo inicio y final antes de comenzar."
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

    public static void mostrarCreditos(StackPane ventana) {

        JFXDialogLayout content = new JFXDialogLayout();
        Text titulo = new Text("Créditos");
        titulo.setFont(new Font(20));
        content.setHeading(titulo);

        Label textoMensaje = new Label("Este programa esta desarrollado por Rubén Saiz,\npuedes seguir el poyecto en github.");
        textoMensaje.getStyleClass().add("labelCreditos");
        content.setBody(textoMensaje);

        JFXDialog dialog = new JFXDialog(ventana, content, JFXDialog.DialogTransition.CENTER);

        JFXButton button = new JFXButton("Vale");
        button.getStyleClass().add("btnCreditos");

        JFXButton botonGitHub = new JFXButton("Ir a Github");
        botonGitHub.getStyleClass().add("btnCreditos");

        BoxBlur blur = new BoxBlur(3, 3, 3);
        content.setActions(button, botonGitHub);
        dialog.show();

        Node pane = (Node) ventana.getChildren().get(0);

        button.setOnAction( e -> dialog.close() );

        botonGitHub.setOnAction( e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/MiYazJE/mazeGeneratorSolver"));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        dialog.setOnDialogClosed( e -> pane.setEffect(null) );

        pane.setEffect( blur );
    }

}
