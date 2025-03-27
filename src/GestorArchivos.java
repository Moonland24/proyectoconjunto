import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.border.StrokeBorder;

public class GestorArchivos<JSONArray> {
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
    
    private void recogerJSON(String rutaFichero) {
        datosOriginales.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaFichero))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea);
            }
            String jsonStr = contenido.toString().trim();
            if (jsonStr.startsWith("[") && jsonStr.endsWith("]")) {
                jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
            }
            String[] objetos = jsonStr.split("\\},\\s*\\{");
                for (String obj : objetos) {
                    if (!obj.startsWith("{")) obj = "{" + obj;
                    if (!obj.endsWith("}")) obj = obj + "}";
                    Registro registro = new Registro();
                    String[] pares = obj.split(",");
        
                    for (String par : pares) {
                        String[] keyValue = par.split(":");
                        if (keyValue.length == 2) {
                            String key = keyValue[0].replaceAll("[\"{}]", "").trim();
                            String value = keyValue[1].replaceAll("[\"{}]", "").trim();
                            registro.agregarCampo(key, value);
                        }
                    }
                    datosOriginales.add(registro);
                }
                System.out.println("JSON procesado. Total registros: " + datosOriginales.size());
            } catch (IOException e) {
                System.err.println("Error al leer el archivo JSON: " + e.getMessage());
            }
        }

    // Parsear CSV

    private void recogerCSV(String rutaFichero) {
        datosOriginales.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaFichero))) {
            String linea; 
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                    if (campos.length ==5 ){
                        Registro registro = new Registro();
                        registro.agregarCampo("Marca : ", campos[0].trim());
                        registro.agregarCampo("Modelo : ", campos[1].trim());
                        registro.agregarCampo("Año : ", campos[2].trim());
                        registro.agregarCampo("COlor : ", campos[3].trim());
                        registro.agregarCampo("Precio : ", campos[4].trim());
                        datosOriginales.add(registro);
                         }
                    }
                    System.out.println("CSV procesado. Total de registros : "+ datosOriginales.size());    
            }
            catch (IOException e){
                System.err.println("Error al leer el archivo CVS: "+ e.getMessage());
            }
        }
// FUncion converti fichero 

    public void convertirFichero(Scanner scanner) {
            if (ficheroSeleccionado == null || datosOriginales.isEmpty()) {
                System.out.println("Primero selecciona y procesa un fichero.");
                return;
            }
        
            System.out.println("\nMENÚ DE CONVERSIÓN");
            System.out.println("1. CSV");
            System.out.println("2. JSON");
            System.out.println("3. XML");
            System.out.print("Seleccione un formato de salida: ");
            int opcion = scanner.nextInt();
                scanner.nextLine(); 
        
            System.out.print("Ingrese el nombre del fichero de salida (sin extensión): ");
            String nombreSalida = scanner.nextLine();
    
            switch (opcion) {
                case 1:
                    convertirACSV(nombreSalida);
                    break;
                case 2:
                    convertirAJSON(nombreSalida);
                    break;
                case 3:
                    convertirAXML(nombreSalida);
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }

    // Convertir a XML

    private void convertirAXML(String nombreSalida) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(carpetaSeleccionada + File.separator + nombreSalida + ".xml"))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<coches>");
            for (Registro registro : datosOriginales) {
                writer.println("  <coche>");
                writer.println("    <marca>" + registro.obtenerCampo("Marca") + "</marca>");
                writer.println("    <modelo>" + registro.obtenerCampo("Modelo") + "</modelo>");
                writer.println("    <año>" + registro.obtenerCampo("Año") + "</año>");
                writer.println("    <color>" + registro.obtenerCampo("Color") + "</color>");
                writer.println("    <precio>" + registro.obtenerCampo("Precio") + "</precio>");
                writer.println("  </coche>");
            }
            writer.println("</coches>");
            System.out.println("Archivo XML generado: " + nombreSalida + ".xml");
        } catch (IOException e) {
            System.err.println("Error al generar XML: " + e.getMessage());
        }
    }
    // Covertir a JSON

    private void convertirAJSON(String nombreSalida) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(carpetaSeleccionada + File.separator + nombreSalida + ".json"))) {
            writer.println("[");
            for (int i = 0; i < datosOriginales.size(); i++) {
                Registro registro = datosOriginales.get(i);
                writer.println("  {");
                writer.println("    \"Marca\": \"" + registro.obtenerCampo("Marca") + "\",");
                writer.println("    \"Modelo\": \"" + registro.obtenerCampo("Modelo") + "\",");
                writer.println("    \"Año\": " + registro.obtenerCampo("Año") + ",");
                writer.println("    \"Color\": \"" + registro.obtenerCampo("Color") + "\",");
                writer.println("    \"Precio\": " + registro.obtenerCampo("Precio"));
                writer.print("  }");
                if (i < datosOriginales.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }
            writer.println("]");
            System.out.println("Archivo JSON generado: " + nombreSalida + ".json");
        } catch (IOException e) {
            System.err.println("Error al generar JSON: " + e.getMessage());
        }
    }

    // Convertir a CSV

    private void convertirACSV(String nombreSalida) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(carpetaSeleccionada + File.separator + nombreSalida + ".csv"))) {
            for (Registro registro : datosOriginales) {
                writer.println(registro.obtenerCampo("Marca") + "," +
                               registro.obtenerCampo("Modelo") + "," +
                               registro.obtenerCampo("Año") + "," +
                               registro.obtenerCampo("Color") + "," +
                               registro.obtenerCampo("Precio"));
            }
            System.out.println("Archivo CSV generado: " + nombreSalida + ".csv");
        } catch (IOException e) {
            System.err.println("Error al generar CSV: " + e.getMessage());
        }
    }

}