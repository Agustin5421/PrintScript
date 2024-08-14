package interpreter;

import java.util.HashMap;
import java.util.Map;

public class VariablesRepository {
    // TODO: Manage more than just string and number variables.
    private final Map<String, String> stringVars;
    private final Map<String, Number> numberVars;

    public VariablesRepository() {
        this.stringVars = new HashMap<>();
        this.numberVars = new HashMap<>();
    }

    VariablesRepository(Map<String, String> stringVars, Map<String, Number> numberVars) {
        this.stringVars = stringVars;
        this.numberVars = numberVars;
    }

    // For tests purposes:

    public Map<String, Number> getNumberVars() {
        return new HashMap<>(numberVars);
    }

    public Map<String, String> getStringVars() {
        return new HashMap<>(stringVars);
    }

    // For tests purposes:

    public String getStringVariable(String name) {
        if(!stringVars.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " is not defined as a string");
        }

        return stringVars.get(name);
    }

    public Number getNumberVariable(String name) throws IllegalArgumentException {
        if(!numberVars.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " is not defined as a number");
        }

        return numberVars.get(name);
    }

    public VariablesRepository addVariable(String name, Object value) {
        if(value instanceof String) {
            return addVariable(name, (String) value);
        } else if(value instanceof Number) {
            return addVariable(name, (Number) value);
        } else {
            throw new IllegalArgumentException("Unknown literal type");
        }
    }

    private VariablesRepository addVariable(String name, String value) throws IllegalArgumentException {
        if(numberVars.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " is already defined as a number");
        }

        Map<String, String> newStringVars = new HashMap<>(stringVars);
        newStringVars.put(name, value);
        return new VariablesRepository(newStringVars, numberVars);
    }

    private VariablesRepository addVariable(String name, Number value) throws IllegalArgumentException {
        if(stringVars.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " is already defined as a string");
        }

        Map<String, Number> newNumberVars = new HashMap<>(numberVars);
        newNumberVars.put(name, value);
        return new VariablesRepository(stringVars, newNumberVars);
    }
}
