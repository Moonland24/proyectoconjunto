import java.util.Scanner;

public class App {
        public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("MENÚ PRINCIPAL");
            System.out.println("1. Seleccionar carpeta");
            System.out.println("2. Lectura de fichero");
            System.out.println("3. Conversión a (csv, json, xml)");
            System.out.println("4. Salir");
            System.out.println("Ruta de la carpeta seleccionada: ");
            System.out.println("Fichero seleccionado: " );
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1:
                    seleccionarCarpeta;
                    break;
                case 2:
                    leerFichero;
                    break;
                case 3:
                    convertirFichero;
                    break;
                case 4:
                    salir = true;
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        scanner.close();
    }
}