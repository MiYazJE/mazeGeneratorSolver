package controladores;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import utils.Mensaje;
import utils.Propiedades;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Esta clase crea y muestra componentes para el cambio de configuraci�n de
 * propiedades del programa.
 */
public class VentanaConfiguracion extends AnchorPane implements Initializable  {

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
    @FXML private JFXComboBox<String> comboAlgoritmoCreacion;
    @FXML private JFXComboBox<String> comboAlgoritmoBusqueda;
    @FXML private JFXCheckBox checkDiagonales;

    private ObservableList<String> algoritmosCreacion = FXCollections.observableArrayList(
            Arrays.asList("DFS", "Random Recursive"));
    private ObservableList<String> algoritmosBusqueda = FXCollections.observableArrayList(
            Arrays.asList("DFS", "A star"));

    private HashMap<String, String> conf;
    private Parent parent;

    /**
     * Contructor con 0 par�metros que inicializa la ventana y carga todos los componentes
     * con los valores recogidos del archivo .properties
     */
    public VentanaConfiguracion() {
        init();
        getChildren().add(parent);
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

        this.comboAlgoritmoCreacion.setItems(algoritmosCreacion);
        this.comboAlgoritmoBusqueda.setItems(algoritmosBusqueda);

        insertarToolTip(this.imgQuestion);
        fieldSoloNumeros(this.fieldDimension);

        btnAplicar.setOnAction(e -> {
            StackPane root = (StackPane) parent.getScene().getRoot();
            if (guardarConfiguracion())
                Mensaje.mostrarNotificacion(root, 0);
            else
                Mensaje.mostrarNotificacion(root, 1);
        });

    }

    /**
     * Inserta un tooltip en la im�gen pasada como par�metro
     * indicando un mensaje de ayuda.
     * @param imagen
     */
    private void insertarToolTip(ImageView imagen) {
        Tooltip tooltip = new Tooltip("Debe ser un n�mero entero.");
        tooltip.setFont(new Font("Quicksand", 14));
        tooltip.setShowDelay(Duration.millis(300));
        Tooltip.install(imagen, tooltip);

    }

    /**
     * A�ade un evento al componente pasado como par�metro para que cu�ndo
     * se escriban car�cteres que no sean n�meros sean eliminados.
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
    public void cargarValores() {
        conf = Propiedades.cargarPropiedades();
        this.fieldDimension.setText(conf.get("DIMENSION"));
        this.colorAbierto.setValue(Color.valueOf(conf.get("ABIERTO")));
        this.colorPared.setValue(Color.valueOf(conf.get("PARED")));
        this.colorVueltaFin.setValue(Color.valueOf(conf.get("VUELTA")));
        this.colorRecorrido.setValue(Color.valueOf(conf.get("ACTUAL")));
        this.colorInicio.setValue( Color.valueOf(conf.get("INICIO")) );
        this.colorLlegada.setValue(Color.valueOf(conf.get("LLEGADA")));
        this.checkModo.setSelected( conf.get("MODO").equals("PINTAR") );
        this.checkDiagonales.setSelected( conf.get("DIAGONALES").equals("SI") );
        this.comboAlgoritmoCreacion.setValue(conf.get("ALGORITMOCREACION"));
        this.comboAlgoritmoBusqueda.setValue(conf.get("ALGORITMOBUSQUEDA"));
    }

    /**
     * Guarda todos los cambios realizados en el fichero propiedades.properties
     * @return boolean
     */
    private boolean guardarConfiguracion() {

        try {

            File file = new File("propiedades.properties");
            PropertiesConfiguration p = new PropertiesConfiguration(file.getAbsolutePath());

            p.setProperty("ABIERTO",   this.colorAbierto.getValue().toString());
            p.setProperty("PARED",     this.colorPared.getValue().toString());
            p.setProperty("VUELTA",    this.colorVueltaFin.getValue().toString());
            p.setProperty("ACTUAL",    this.colorRecorrido.getValue().toString());
            p.setProperty("LLEGADA",   this.colorLlegada.getValue().toString());
            p.setProperty("INICIO",    this.colorInicio.getValue().toString());
            p.setProperty("ALGORITMOCREACION", this.comboAlgoritmoCreacion.getValue());
            p.setProperty("ALGORITMOBUSQUEDA", this.comboAlgoritmoBusqueda.getValue());

            // Las dimensiones del laberinto solo pueden ser impares,
            // el algoritmo de creaci�n de laberintos lo requiere *indexAutoOfBonds*
            int dimension = Integer.valueOf(this.fieldDimension.getText());
            dimension = (dimension %2 == 0) ? dimension+1 : dimension;
            p.setProperty("DIMENSION", String.valueOf(dimension));

            String modo = (this.checkModo.isSelected()) ? "PINTAR" : "LABERINTO";
            p.setProperty("MODO", modo);

            String diagonales = (this.checkDiagonales.isSelected()) ? "SI" : "NO";
            p.setProperty("DIAGONALES", diagonales);

            p.save();

            return true;

        } catch (ConfigurationException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass().getName());
        }

        return false;
    }

}
