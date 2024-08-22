package exceptions;

public class SyntaxException extends RuntimeException {
  public SyntaxException(String message) {
    super("Syntax error: " + message);
  }
}
