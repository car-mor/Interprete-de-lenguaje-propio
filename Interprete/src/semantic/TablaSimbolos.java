package semantic;

import parser.*;
import semantic.SemanticException;
import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {

    public final Map<String, Object> values = new HashMap<>();

    boolean existeIdentificador(String identificador){
        return values.containsKey(identificador);
    }

    Object obtener(String identificador) throws SemanticException {
        if (values.containsKey(identificador)) {
            return values.get(identificador);
        }
        throw new SemanticException(new StringBuilder("Variable no definida '" + identificador + "'."));
    }

    void asignar(String identificador, Object valor){
        values.put(identificador, valor);
    }


}
