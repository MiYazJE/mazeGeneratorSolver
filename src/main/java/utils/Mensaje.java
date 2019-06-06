package utils;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import controladores.VentanaSeleccionColores;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;

public class Mensaje {

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


}
