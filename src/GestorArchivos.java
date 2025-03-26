import java.io.*;
import java.util.Scanner;

public class GestorArchivos {
    private String carpetaSeleccionada;
    private String ficheroSeleccionado;

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
            System.out.println("Bien, has escogido correctamente la carpeta " + carpetaSeleccionada);
            return true;
        } else {
            System.out.println("Nop, la ruta que me has dado no me consta en mis datos eh");
            return false;
        }
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
            System.out.println("Bien, has seleccionado correctamente el fichero: " + ficheroSeleccionado);
            return true;
        } else {
            System.out.println("Nop, el fichero que me has dado no me consta en mis datos eh");
            return false;
        }
    }
}