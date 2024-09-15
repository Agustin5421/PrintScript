package exceptions;

import ast.identifier.Identifier;

public class VariableAlreadyDeclaredException extends RuntimeException {
  public VariableAlreadyDeclaredException(Identifier identifier) {
    super(
        "Variable '"
            + identifier.name()
            + "' at ["
            + identifier.start()
            + "] has already been declared");
  }
}
