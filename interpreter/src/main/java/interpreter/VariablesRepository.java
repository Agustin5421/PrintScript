package interpreter;

import java.util.HashMap;
import java.util.Map;

public class VariablesRepository {
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

    public VariablesRepository setStringVar(String name, String value) {
        if(numberVars.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " is already defined as a number");
        }

        Map<String, String> newStringVars = new HashMap<>(stringVars);
        newStringVars.put(name, value);
        return new VariablesRepository(newStringVars, numberVars);
    }

    public VariablesRepository setNumberVar(String name, Number value) {
        if(stringVars.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " is already defined as a string");
        }

        Map<String, Number> newNumberVars = new HashMap<>(numberVars);
        newNumberVars.put(name, value);
        return new VariablesRepository(stringVars, newNumberVars);
    }
}
