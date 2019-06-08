package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import utils.Propiedades;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class VentanaSeleccionColores extends AnchorPane implements Initializable  {

    @FXML private JFXColorPicker colorAbierto;
    @FXML private JFXColorPicker colorPared;
    @FXML private JFXColorPicker colorVueltaFin;
    @FXML private JFXColorPicker colorRecorrido;
    @FXML private JFXColorPicker colorInicio;
    @FXML private JFXColorPicker colorLlegada;
    @FXML private JFXButton btnAplicar;
    @FXML private JFXTextField fieldDimension;
    @FXML private ImageView imgQuestion;
    @FXML private JFXCheckBox checkModo;

    private Map<String, String> conf;
    private Parent parent;

    public VentanaSeleccionColores() {
        init();
        getChildren().add(parent);
        conf = Propiedades.cargarPropiedades();
        cargarValores();
    }

    /**
     * Propiedades de la vista.
     */
    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/SeleccionColor.fxml"));
            loader.setController(this);
            this.parent = loader.load();
        } catch (IOException e) {
            System.out.println("Problemas al cargar las propiedades la clase " + this.getClass().getName());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        insertarToolTip(this.imgQuestion);
        fieldSoloNumeros(this.fieldDimension);

        btnAplicar.setOnAction(e -> {
            guardarConfiguracion();
        });

    }

    private void insertarToolTip(ImageView imagen) {
        Tooltip tooltip = new Tooltip("Debe ser un número entero.");
        tooltip.setFont(new Font("Quicksand", 14));
        tooltip.setShowDelay(Duration.millis(300));
        Tooltip.install(imagen, tooltip);

    }

    /**
     * Añade un evento al componente pasado como parámetro para que cuándo
     * se escriban carácteres que no sean números sean eliminados.
     * @param field
     */
    private void fieldSoloNumeros(JFXTextField field) {
        field.textProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                field.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Al iniciar la ventana carga todos los componentes con los valores guardados
     * en el archivo propiedades.properties
     */
    private void cargarValores() {
        this.colorAbierto.setValue(Color.valueOf(conf.get("ABIERTO")));
        this.colorPared.setValue(Color.valueOf(conf.get("PARED")));
        this.colorVueltaFin.setValue(Color.valueOf(conf.get("VUELTA")));
        this.colorRecorrido.setValue(Color.valueOf(conf.get("ACTUAL")));
        this.colorInicio.setValue( Color.valueOf(conf.get("INICIO")) );
        this.colorLlegada.setValue(Color.valueOf(conf.get("LLEGADA")));
        this.fieldDimension.setText(conf.get("DIMENSION"));
        this.checkModo.setSelected( conf.get("MODO").equals("PINTAR") );
    }

    /**
     * Guarda todos los cambios realizados en el fichero propiedades.properties
     * @return boolean
     */
    private boolean guardarConfiguracion() {

        try {

            File file = new File("propiedades.properties");
            PropertiesConfiguration p = new PropertiesConfiguration(file.getAbsolutePath());

            p.setProperty("ABIERTO", this.colorAbierto.getValue().toString());
            p.setProperty("PARED", this.colorPared.getValue().toString());
            p.setProperty("VUELTA", this.colorVueltaFin.getValue().toString());
            p.setProperty("ACTUAL", this.colorRecorrido.getValue().toString());

            // Las dimensiones del laberinto solo pueden ser impares,
            // el algoritmo de creación de laberintos lo requiere *indexAutoOfBonds*
            int dimension = Integer.valueOf(this.fieldDimension.getText());
            dimension = (dimension %2 == 0) ? dimension+1 : dimension;
            p.setProperty("DIMENSION", String.valueOf(dimension));

            String modo = (this.checkModo.isSelected()) ? "PINTAR" : "LABERINTO";
            p.setProperty("MODO", modo);

            p.save();

            return true;

        } catch (ConfigurationException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass().getName());
        }

        return false;
    }

}
