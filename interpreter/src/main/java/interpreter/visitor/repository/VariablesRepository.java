package interpreter.visitor.repository;

import ast.identifier.Identifier;
import ast.literal.Literal;
import java.util.HashMap;
import java.util.Map;

public class VariablesRepository {
  // Single map to store all variables
  private final Map<Identifier, Literal<?>> variables;
  private final Map<VariableIdentifier, Literal<?>> newVariables;

  public VariablesRepository() {
    this(new HashMap<>(), new HashMap<>());
  }

  public VariablesRepository(Map<Identifier, Literal<?>> variables) {
    this(variables, new HashMap<>());
  }

  public VariablesRepository(
      Map<Identifier, Literal<?>> variables, Map<VariableIdentifier, Literal<?>> newVariables) {
    this.variables = variables;
    this.newVariables = newVariables;
  }

  public Map<Identifier, Literal<?>> getVariables() {
    return new HashMap<>(variables);
  }

  public Map<VariableIdentifier, Literal<?>> getNewVariables() {
    return newVariables;
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
    Map<Identifier, Literal<?>> newVariables = new HashMap<>(getVariables());
    newVariables.put(identifier, value);
    return new VariablesRepository(newVariables);
  }

  public VariablesRepository addNewVariable(VariableIdentifier identifier, Literal<?> value) {
    Map<VariableIdentifier, Literal<?>> newVariables = new HashMap<>(getNewVariables());
    boolean isAlreadyDefined = newVariables.containsKey(identifier);

    if (isAlreadyDefined) {
      throw new IllegalArgumentException("Variable " + identifier + " is already defined.");
    }

    newVariables.put(identifier, value);

    return new VariablesRepository(getVariables(), newVariables);
  }

  public VariablesRepository setNewVariable(VariableIdentifier identifier, Literal<?> value) {
    Map<VariableIdentifier, Literal<?>> newVariables = new HashMap<>(getNewVariables());
    VariableIdentifier keyId = getKeyId(identifier);

    if (keyId == null) {
      throw new IllegalArgumentException("Variable " + identifier.name() + " is not defined.");
    }

    if (!keyId.isModifiable()) {
      throw new IllegalArgumentException("Variable " + identifier.name() + " is not modifiable.");
    }

    newVariables.put(keyId, value);
    return new VariablesRepository(getVariables(), newVariables);
  }

  public <T> Literal<T> getNewVariable(VariableIdentifier identifier) {
    Literal<T> value = (Literal<T>) getNewVariables().get(identifier);

    if (value == null) {
      throw new IllegalArgumentException("Variable " + identifier.name() + " is not defined.");
    }

    return value;
  }

  private VariableIdentifier getKeyId(VariableIdentifier identifier) {
    for (VariableIdentifier key : newVariables.keySet()) {
      if (key.equals(identifier)) {
        return key;
      }
    }
    return null;
  }

  public VariablesRepository update(VariablesRepository other) {
    Map<VariableIdentifier, Literal<?>> mergedVariables = new HashMap<>(this.newVariables);
    for (Map.Entry<VariableIdentifier, Literal<?>> entry : other.getNewVariables().entrySet()) {
      if (mergedVariables.containsKey(entry.getKey())) {
        mergedVariables.put(entry.getKey(), entry.getValue());
      }
    }
    return new VariablesRepository(Map.of(), mergedVariables);
  }
}
