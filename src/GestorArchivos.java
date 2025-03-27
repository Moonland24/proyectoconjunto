import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.border.StrokeBorder;

public class GestorArchivos {
    private String carpetaSeleccionada;
    private String ficheroSeleccionado;
    private List<Registro> datosOriginales = new ArrayList<>();

    public GestorArchivos() {
        this.carpetaSeleccionada = null;
        this.ficheroSeleccionado = null;
    }

    public boolean seleccionarCarpeta(Scanner scanner) {
        System.out.println("Introduce la ruta de tu carpeta porfis:");
        String rutaCarpeta = scanner.nextLine();
        File carpeta = new File(rutaCarpeta);

        if (carpeta.exists() && carpeta.isDirectory()) {
            this.carpetaSeleccionada = rutaCarpeta;
            System.out.println("Bien, has escogido correctamente la carpeta " + carpeta.getName());
            File[] archivosEnCarpeta = carpeta.listFiles();
            System.out.println("El contenido de la carpeta es: ");
            for (File file : archivosEnCarpeta) {
                System.out.println(file.getName());
            }

            return true;
        } else {
            System.out.println("Nop, la ruta que me has dado no me consta en mis datos eh");
            return false;
        }
    }

    private String conseguirExtension(String fichero) {
        String extension = "";
        int indicePunto = fichero.lastIndexOf('.');
        if (indicePunto > 0) {
            extension = fichero.substring(indicePunto + 1).toLowerCase();
        }
        return extension;
    }

    public boolean seleccionarFichero(Scanner scanner) {
        if (carpetaSeleccionada == null) {
            System.out.println("Primero selecciona una carpeta en la que vas a buscar el archivo");
            return false;
        }

        System.out.println("Introduce el nombre del fichero dentro de la carpeta seleccionada, por favor:");
        String nombreFichero = scanner.nextLine();
        File fichero = new File(carpetaSeleccionada, nombreFichero);

        if (fichero.exists() && fichero.isFile()) {
            this.ficheroSeleccionado = nombreFichero;
            String extension = conseguirExtension(nombreFichero);
            String rutaFichero = carpetaSeleccionada + "/" + nombreFichero;
            System.out.println("La extension del archivo seleccionado es: " + extension);
            System.out.println("Comienza el registro del archivo... ");
            switch (extension) {
                case "csv":
                    recogerCSV(rutaFichero);
                    break;
                case "xml":
                    System.out.println("Introduce la etiqueta raiz del archivo xml: ");
                    String etiquetaRaiz = scanner.nextLine();
                    recogerXML(rutaFichero, etiquetaRaiz);
                    break;
                case "json":
                    recogerJSON(rutaFichero);
                    break;
                default:
                    System.out.println("El archivo no tiene una extensión compatible");
                    break;
            }
            System.out.println("Bien, has seleccionado correctamente el fichero: " + ficheroSeleccionado);
            return true;
        } else {
            System.out.println("Nop, el fichero que me has dado no me consta en mis datos eh");
            return false;
        }

    }

    // Parsear XML
    private void recogerXML(String fichero, String etiquetaRaiz) {
        datosOriginales.clear(); // Esto asegura que si se elige otro formato en cualquier momento, se elimine el
                                 // formato anterior
        try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
            String linea;
            Registro registroActual = null;

            while ((linea = reader.readLine()) != null) {
                linea = linea.trim(); // Esto elimina los espacios en blanco que pueda haber en la linea

                if (linea.startsWith("<" + etiquetaRaiz + ">")) {
                    registroActual = new Registro();
                } else if (linea.startsWith("</" + etiquetaRaiz + ">")) {
                    if (registroActual != null) {
                        datosOriginales.add(registroActual);
                    }
                } else if (registroActual != null) {
                    int indiceInicio = linea.indexOf('<');
                    int indiceFin = linea.indexOf('>');

                    if (indiceInicio != -1 && indiceFin != -1 && indiceFin < linea.lastIndexOf('<')) {
                        String clave = linea.substring(indiceInicio + 1, indiceFin);
                        int indiceValorInicio = indiceFin + 1;
                        int indiceValorFin = linea.lastIndexOf('<');

                        if (indiceValorInicio < indiceValorFin) {
                            String valor = linea.substring(indiceValorInicio, indiceValorFin).trim();
                            registroActual.agregarCampo(clave, valor);
                        }
                    }

                }
            }

            System.out.println("XML recogido. Total registros: " + datosOriginales.size());
        } catch (Exception e) {
            System.err.println("Error al recoger el XML: " + e.getMessage());
        }
    }

    // Parsear JSON
    private void recogerJSON(String fichero) {

    }

    // Parsear CSV
    private void recogerCSV(String fichero) {

    }

    // Convertir a XML
    // Covertir a JSON
    // Convertir a CSV
}