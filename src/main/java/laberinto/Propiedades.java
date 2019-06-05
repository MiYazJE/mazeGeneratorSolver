/**
 * @author Ruben Saiz
 */
package laberinto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class Propiedades {

    private String rutaAbsoluta;
    private File archivo;
    private static final Map<String, String> propiedadesIniciales = Map.of(
            "ABIERTO",  "#FFFFFF",
            "PARED",    "#000000",
            "VISITADO", "#0027FF",
            "ACTUAL",   "#2AFF00",
            "LLEGADA",  "#FF0000"
    );
    Properties propiedades;

    public Propiedades() {
        this.archivo = new File("propiedades.properties");
        this.rutaAbsoluta = archivo.getAbsolutePath();
        this.propiedades = new Properties();
    }

    public void crearPropiedades() {

        if (!verificarSiExiste()) {

            try { archivo.createNewFile(); }
            catch (IOException e) {
                System.out.println("Problemas al crear el fichero.\n" + e.getMessage());
            }
            asignarPropiedadesIniciales();

        }

    }

    private void asignarPropiedadesIniciales() {

        try {

            FileInputStream file = new FileInputStream(archivo);
            propiedades.load(file);
            for (Map.Entry<String, String> entry : propiedadesIniciales.entrySet()) {
                propiedades.setProperty(entry.getKey(), entry.getValue());
            }
            propiedades.store(new FileWriter(this.rutaAbsoluta), rutaAbsoluta + "\nEscribiendo configuración predeterminada.");

        } catch (IOException e) {
            System.out.println("Problemas al escribir en el fichero de configuración.\n" +
                    e.getMessage());
        }

    }

    private boolean verificarSiExiste() {
        return archivo.exists();
    }


}
