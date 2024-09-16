package exceptions;

public class VariableNotDeclaredException extends RuntimeException {
  public VariableNotDeclaredException(String message) {
    super("Variable '" + message + "' is not declared");
  }
}
