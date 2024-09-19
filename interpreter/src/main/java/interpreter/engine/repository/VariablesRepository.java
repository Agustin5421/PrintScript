package interpreter.engine.repository;

import ast.literal.Literal;
import ast.root.AstNodeType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VariablesRepository {
  // Single map to store all variables
  private final Map<VariableIdentifier, Literal<?>> newVariables;

  public VariablesRepository() {
    this(new HashMap<>());
  }

  public VariablesRepository(Map<VariableIdentifier, Literal<?>> newVariables) {
    this.newVariables = newVariables;
  }

  public Map<VariableIdentifier, Literal<?>> getNewVariables() {
    return newVariables;
  }

  public VariablesRepository addNewVariable(VariableIdentifier identifier, Literal<?> value) {
    Map<VariableIdentifier, Literal<?>> newVariables = new HashMap<>(getNewVariables());
    boolean isAlreadyDefined = newVariables.containsKey(identifier);

    if (isAlreadyDefined) {
      throw new IllegalArgumentException("Variable " + identifier + " is already defined.");
    }

    newVariables.put(identifier, value);

    return new VariablesRepository(newVariables);
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

    if (differentVarTypes(keyId, value)) {
      throw new IllegalArgumentException(
          "Variable " + identifier.name() + " is not of the same type.");
    }

    newVariables.put(keyId, value);
    return new VariablesRepository(newVariables);
  }

  public <T> Literal<T> getNewVariable(VariableIdentifier identifier) {

    //    if (value == null) {
    //      throw new IllegalArgumentException("Variable " + identifier.name() + " is not
    // defined.");
    //    }

    return (Literal<T>) getNewVariables().get(identifier);
  }

  private VariableIdentifier getKeyId(VariableIdentifier identifier) {
    for (VariableIdentifier key : newVariables.keySet()) {
      if (key.equals(identifier)) {
        return key;
      }
    }
    return null;
  }

  private boolean differentVarTypes(VariableIdentifier keyId, Literal<?> value) {
    return (Objects.equals(keyId.type(), "string")
            && value.getNodeType() != AstNodeType.STRING_LITERAL)
        || (Objects.equals(keyId.type(), "number")
            && value.getNodeType() != AstNodeType.NUMBER_LITERAL)
        || (Objects.equals(keyId.type(), "boolean")
            && value.getNodeType() != AstNodeType.BOOLEAN_LITERAL);
  }

  public VariablesRepository update(VariablesRepository other) {
    Map<VariableIdentifier, Literal<?>> mergedVariables = new HashMap<>(this.newVariables);
    for (Map.Entry<VariableIdentifier, Literal<?>> entry : other.getNewVariables().entrySet()) {
      if (mergedVariables.containsKey(entry.getKey())) {
        mergedVariables.put(entry.getKey(), entry.getValue());
      }
    }
    return new VariablesRepository(mergedVariables);
  }
}
