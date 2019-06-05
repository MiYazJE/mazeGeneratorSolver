package utils;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;

public class Mensaje {

    public static void mostrar(StackPane stackPane, Node nodo) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.getChildren().add(nodo);

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        BoxBlur blur = new BoxBlur(3, 3, 3);
        dialog.show();

        Node pane = (Node) stackPane.getChildren().get(0);

        dialog.setOnDialogClosed((e) -> {
            pane.setEffect(null);
        });

        pane.setEffect( blur );
    }


}
