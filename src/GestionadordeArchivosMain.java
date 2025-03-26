import java.util.Scanner;

public class GestionadordeArchivosMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        GestorArchivos gestor = new GestorArchivos();

        while (!salir) {
            System.out.println("MENÚ PRINCIPAL");
            System.out.println("1. Seleccionar carpeta");
            System.out.println("2. Lectura de fichero");
            System.out.println("3. Conversión a (csv, json, xml)");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    gestor.seleccionarCarpeta(scanner);
                    break;
                case 2:
                    //gestor.leerFichero(scanner);
                    break;
                case 3:
                    //gestor.convertirFichero(scanner);
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