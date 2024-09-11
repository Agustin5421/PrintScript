package interpreter.visitor.repository;

import ast.identifier.Identifier;
import ast.literal.Literal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VariablesRepository {
  // Single map to store all variables
  private final Map<Identifier, Literal<?>> variables;

  public VariablesRepository() {
    this.variables = new HashMap<>();
  }

  private VariablesRepository(Map<Identifier, Literal<?>> variables) {
    this.variables = Collections.unmodifiableMap(new HashMap<>(variables));
  }

  public Map<Identifier, Literal<?>> getVariables() {
    return new HashMap<>(variables);
  }

  @SuppressWarnings("unchecked")
  public <T> Literal<T> getVariable(Identifier identifier) {
    if (variables.containsKey(identifier)) {
      return (Literal<T>) variables.get(identifier);
    } else {
      throw new IllegalArgumentException("Variable " + identifier + " is not defined");
    }
  }

  public VariablesRepository addVariable(Identifier identifier, Literal<?> value) {
    Map<Identifier, Literal<?>> newVariables = new HashMap<>(variables);
    newVariables.put(identifier, value);
    return new VariablesRepository(newVariables);
  }
}
