package interpreter;

import ast.literal.Literal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VariablesRepository {
  // TODO: Manage more than just string and number variables.

  // Single map to store all variables
  private final Map<String, Literal<?>> variables;

  public VariablesRepository() {
    this.variables = new HashMap<>();
  }

  private VariablesRepository(Map<String, Literal<?>> variables) {
    this.variables = Collections.unmodifiableMap(new HashMap<>(variables));
  }

  public Map<String, Literal<?>> getVariables() {
    return new HashMap<>(variables);
  }

  @SuppressWarnings("unchecked")
  public <T> Literal<T> getVariable(String name) {
    if (variables.containsKey(name)) {
      return (Literal<T>) variables.get(name);
    } else {
      throw new IllegalArgumentException("Variable " + name + " is not defined");
    }
  }

  public VariablesRepository addVariable(String name, Literal<?> value) {
    Map<String, Literal<?>> newVariables = new HashMap<>(variables);
    newVariables.put(name, value);
    return new VariablesRepository(newVariables);
  }
}
