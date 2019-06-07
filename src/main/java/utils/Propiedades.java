/**
 * @author Ruben Saiz
 */
package utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Propiedades {

    private String rutaAbsoluta;
    private File archivo;
    private static final Map<String, String> propiedadesIniciales = Map.of(
            "ABIERTO",   "#FFFFFF",
            "PARED",     "#000000",
            "VISITADO",  "#0027FF",
            "ACTUAL",    "#2AFF00",
            "LLEGADA",   "#FF0000",
            "DIMENSION", "31",
            "MODO",      "PINTAR",
            "VELOCIDAD", "50"
    );
    private Properties propiedades;

    public Propiedades() {
        this.archivo = new File("propiedades.properties");
        this.rutaAbsoluta = archivo.getAbsolutePath();
        this.propiedades = new Properties();
    }

    /**
     * Crear el fichero propiedades.properties, si ya existe no sera sobreescrito,
     * sino se agregaran las propiedades predeterminada al archivo
     */
    public void crearPropiedades() {

        if (!verificarSiExiste()) {
            try { archivo.createNewFile(); }
            catch (IOException e) {
                System.out.println("Problemas al crear el fichero.\n" + e.getMessage());
            }
            asignarPropiedadesIniciales();
        }

    }

    /**
     * Escribir en un fichero .properties la configuracion predeterminada
     * del programa, como los colores...
     */
    private void asignarPropiedadesIniciales() {

        try {

            FileInputStream file = new FileInputStream(archivo);
            propiedades.load(file);
            for (Map.Entry<String, String> entry : propiedadesIniciales.entrySet()) {
                propiedades.setProperty(entry.getKey(), entry.getValue());
            }
            propiedades.store(new FileWriter(this.rutaAbsoluta), rutaAbsoluta + "\nEscribiendo configuracion predeterminada.");

        } catch (IOException e) {
            System.out.println("Problemas al escribir en el fichero de configuración.\n" +
                    e.getMessage());
        }

    }

    private boolean verificarSiExiste() {
        return this.archivo.exists();
    }

    /**
     * Metodo que obtiene en un mapa<String, String> toda la informacion
     * almacenada de un archivo .properties
     * @return HashMap<String, String> : mapa de propiedades del .properties
     */
    public static HashMap<String, String> cargarPropiedades() {

        Properties p = new java.util.Properties();
        HashMap<String, String> conf = new HashMap<>();
        try {
            FileInputStream file = new FileInputStream(new File("propiedades.properties"));
            p.load(file);
            conf.put("ABIERTO", p.getProperty("ABIERTO"));
            conf.put("VISITADO", p.getProperty("VISITADO"));
            conf.put("PARED", p.getProperty("PARED"));
            conf.put("LLEGADA", p.getProperty("LLEGADA"));
            conf.put("ACTUAL", p.getProperty("ACTUAL"));
            conf.put("DIMENSION", p.getProperty("DIMENSION"));
            conf.put("MODO", p.getProperty("MODO"));
            conf.put("VELOCIDAD", p.getProperty("VELOCIDAD"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass().getName());
        }

        return conf;
    }

    public void almacenarVelocidad(String velocidad) {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(rutaAbsoluta);
            config.setProperty("VELOCIDAD", Long.parseLong(velocidad));
            config.save();
        } catch (ConfigurationException e) {}
    }

    public String obtenerPropiedad(String propiedad) {
        try {
            FileInputStream file = new FileInputStream(new File("propiedades.properties"));
            propiedades.load(file);
            return propiedades.getProperty(propiedad);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass().getName());
            return "";
        }
    }

}
