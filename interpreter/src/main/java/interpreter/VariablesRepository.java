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

  public Object getVariable(String name) {
    if (stringVars.containsKey(name)) {
      return stringVars.get(name);
    } else if (numberVars.containsKey(name)) {
      return numberVars.get(name);
    } else {
      throw new IllegalArgumentException("Variable " + name + " is not defined");
    }
  }

  public void addVariable(String name, Object value) {
    if (value instanceof String) {
      addVariable(name, (String) value);
    } else if (value instanceof Number) {
      addVariable(name, (Number) value);
    } else {
      throw new IllegalArgumentException("Unknown literal type");
    }
  }

  private void addVariable(String name, String value)
      throws IllegalArgumentException {
    if (numberVars.containsKey(name)) {
      throw new IllegalArgumentException("Variable " + name + " is already defined as a number");
    }

//    Map<String, String> newStringVars = new HashMap<>(stringVars);
//    newStringVars.put(name, value);
    stringVars.put(name, value);

  }

  private void addVariable(String name, Number value)
      throws IllegalArgumentException {
    if (stringVars.containsKey(name)) {
      throw new IllegalArgumentException("Variable " + name + " is already defined as a string");
    }

//    Map<String, Number> newNumberVars = new HashMap<>(numberVars);
//    newNumberVars.put(name, value);
    numberVars.put(name, value);
  }
}
