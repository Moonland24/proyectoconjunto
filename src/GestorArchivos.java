import java.io.*;
import java.util.List;
import java.util.Scanner;

public class GestorArchivos {
    private String carpetaSeleccionada;
    private String ficheroSeleccionado;
    private List<String> xmlDatos;
    private List<JSONObject> jsonDatos;
    private List<String[]> csvDatos;

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

    private String conseguirExtension(String fichero){
        String extension = "";
        int indicePunto = fichero.lastIndexOf('.');
        if(indicePunto > 0){
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
            System.out.println("La extension del archivo seleccionado es: " + extension);
            System.out.println("Bien, has seleccionado correctamente el fichero: " + ficheroSeleccionado);
            return true;
        } else {
            System.out.println("Nop, el fichero que me has dado no me consta en mis datos eh");
            return false;
        }
    }
}