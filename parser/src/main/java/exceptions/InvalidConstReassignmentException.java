package exceptions;

import ast.identifier.Identifier;

public class InvalidConstReassignmentException extends RuntimeException {
  public InvalidConstReassignmentException(Identifier identifier) {
    super("Constant " + identifier.name() + " at " + identifier.start() + " cannot be reassigned");
  }
}
