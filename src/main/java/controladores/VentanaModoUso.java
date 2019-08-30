/**
 * @author Rubén Saiz
 */
package controladores;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Esta clase muestra con mensajes explicativos e imágenes los modos de uso que tiene el programa.
 */
public class VentanaModoUso extends ScrollPane {

    private VBox contenedor;

    public VentanaModoUso() {
        setPrefSize(700, 800);
        this.setHbarPolicy(ScrollBarPolicy.valueOf("NEVER"));
        propiedadesPrincipales();
        cargarInfo();
    }

    /**
     * Iniciazaliza los principales componentes de esta ventana
     */
    private void propiedadesPrincipales() {

        this.contenedor = new VBox();
        this.contenedor.setSpacing(20);
        this.contenedor.setAlignment(Pos.TOP_CENTER);
        this.contenedor.setPadding(new Insets(10, 10, 10, 10));

        Text texto = new Text("MODO DE USO");
        texto.setFont(new Font("Quicksand", 40));
        texto.setWrappingWidth(650);
        texto.setTextAlignment(TextAlignment.CENTER);
        contenedor.getChildren().add(texto);

        setContent(contenedor);
    }

    private void cargarInfo() {

        String ruta = "/imagenes/gifs/";

        Label textoClickIzquierdo = new Label(
                "Con click izquierdo se colocan puntos de inicio.\n" +
                        "Si hacemos click encima de nuevo este se reemplazara \n" +
                        "por su anterior estado."
        );
        estilosLabel(textoClickIzquierdo);
        ImageView gifClickIzquierdo = new ImageView(new Image(ruta + "clickIzquierdo.gif"));
        contenedor.getChildren().addAll(textoClickIzquierdo, gifClickIzquierdo);

        Label textoClickDerecho = new Label(
                "Con click derecho se colocan los puntos finales.\n" +
                    "Funciona de la misma forma que el click izquierdo."
        );
        estilosLabel(textoClickDerecho);
        ImageView gifClickDerecho = new ImageView(new Image(ruta + "clickDerecho.gif"));
        contenedor.getChildren().addAll(textoClickDerecho, gifClickDerecho);

        Label textoControlarVelocidad = new Label(
            "Una vez tengamos el punto inicial y final establecidos \n" +
                "ya podemos ejecutar clickando en el boton EMPEZAR, \n" +
                "podemos regular la velocidad de la solución\n" +
                "del algoritmo con el slider."
        );
        estilosLabel(textoControlarVelocidad);
        ImageView gifVelocidad   = new ImageView(new Image(ruta + "controlarVelocidad.gif"));
        contenedor.getChildren().addAll(textoControlarVelocidad, gifVelocidad);

        Label textoLimpiar = new Label(
                "Podemos limpiar el laberinto, nos lo dejara \n" +
                    "como estaba al principio."
        );
        estilosLabel(textoLimpiar);
        ImageView gifLimpiar = new ImageView(new Image(ruta + "btnLimpiar.gif"));
        contenedor.getChildren().addAll(textoLimpiar, gifLimpiar);

        Label textoCambioAlgoritmo = new Label(
                "Podemos elegir distintos algoritmos de generación\n" +
                    "de laberintos."
        );
        estilosLabel(textoCambioAlgoritmo);
        ImageView gifCambioAlgoritmo = new ImageView(new Image(ruta + "cambioAlgoritmo.gif"));
        contenedor.getChildren().addAll(textoCambioAlgoritmo, gifCambioAlgoritmo);

        Label textoCambioModo = new Label(
                "Podemos cambiar el modo en el menu configuración,\n" +
                    "con este podremos crear nuestro propio laberinto."
        );
        estilosLabel(textoCambioModo);
        ImageView gifCambioModo = new ImageView(new Image(ruta + "cambioModo.gif"));
        contenedor.getChildren().addAll(textoCambioModo, gifCambioModo);

        Label textoPintarAbierto = new Label(
                "En el modo Pintar para pintar celdas abiertas\n" +
                    "debemos presional SHIF izquierdo y pasar el ratón\n" +
                    "por encima de las celdas que queramos seleccionar."
        );
        estilosLabel(textoPintarAbierto);
        ImageView gifPintarAbierto = new ImageView(new Image(ruta + "pintarAbierto.gif"));
        contenedor.getChildren().addAll(textoPintarAbierto, gifPintarAbierto);

        Label textoPintarPared = new Label(
                "En el modo Pintar el botón 'Limpiar' tiene una\n" +
                    "función diferente, abre todas las celdas.\n" +
                    "Para poder cambiar el estado de las celdas a \n" +
                    "cerradas debemos presionar ALT izquierdo y \n" +
                    "y pasar por encima de las celdas."
        );
        estilosLabel(textoPintarPared);
        ImageView gifPintarPared = new ImageView(new Image(ruta + "pintarPared.gif"));
        contenedor.getChildren().addAll(textoPintarPared, gifPintarPared);

    }

    /**
     * Cambia estilos visuales como el texto al label pasado por parámetro
     * @param label
     */
    private void estilosLabel(Label label) {
        label.getStyleClass().add("textoModoUso");
        label.setAlignment(Pos.CENTER);
    }

}
