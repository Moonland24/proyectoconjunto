import java.util.HashMap;
import java.util.Map;

public class Registro {
    Map<String, String> campos = new HashMap<>();

    public void agregarCampo(String clave, String valor){
        campos.put(clave, valor);
    }

    public String obtenerCampo(String clave){
        return campos.get(clave);
    }
}
