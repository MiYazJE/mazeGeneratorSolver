package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class VentanaSeleccionColores extends AnchorPane implements Initializable  {

    @FXML private JFXColorPicker colorAbierto;
    @FXML private JFXColorPicker colorPared;
    @FXML private JFXColorPicker colorFin;
    @FXML private JFXColorPicker colorBacktrack;
    @FXML private JFXColorPicker colorRecorrido;
    @FXML private JFXButton btnAplicar;
    @FXML private JFXTextField fieldDimension;
    @FXML private ImageView imgQuestion;
    @FXML private AnchorPane root;

    private Map<String, String> conf;
    private Parent parent;

    public VentanaSeleccionColores() {
        init();
        getChildren().add(parent);
        conf = cargarPropiedades();
        cargarColores();
    }

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

    private void fieldSoloNumeros(JFXTextField field) {
        field.textProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                field.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
    }

    private Map<String, String> cargarPropiedades() {

        Properties p = new Properties();
        Map<String, String> conf = new HashMap<>();
        try {
            FileInputStream file = new FileInputStream(new File("propiedades.properties"));
            p.load(file);
            conf.put("ABIERTO", p.getProperty("ABIERTO"));
            conf.put("VISITADO", p.getProperty("VISITADO"));
            conf.put("PARED", p.getProperty("PARED"));
            conf.put("LLEGADA", p.getProperty("LLEGADA"));
            conf.put("ACTUAL", p.getProperty("ACTUAL"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass().getName());
        }

        return conf;
    }

    private void cargarColores() {
        this.colorAbierto.setValue(Color.valueOf(conf.get("ABIERTO")));
        this.colorBacktrack.setValue(Color.valueOf(conf.get("VISITADO")));
        this.colorPared.setValue(Color.valueOf(conf.get("PARED")));
        this.colorFin.setValue(Color.valueOf(conf.get("LLEGADA")));
        this.colorRecorrido.setValue(Color.valueOf(conf.get("ACTUAL")));
    }

    private boolean guardarConfiguracion() {

        Properties p = new Properties();

        try {

            File file = new File("propiedades.properties");
            FileInputStream fis = new FileInputStream(file);
            p.load(fis);

            p.setProperty("ABIERTO", this.colorAbierto.getValue().toString());
            p.setProperty("PARED", this.colorPared.getValue().toString());
            p.setProperty("VISITADO", this.colorBacktrack.getValue().toString());
            p.setProperty("LLEGADA", this.colorFin.getValue().toString());
            p.setProperty("ACTUAL", this.colorRecorrido.getValue().toString());
            p.store(new FileWriter(file.getAbsolutePath()), "Actualiando configuración");

            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass().getName());
            return false;
        }

    }

}
