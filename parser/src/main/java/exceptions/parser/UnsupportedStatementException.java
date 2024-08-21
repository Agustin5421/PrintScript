package exceptions.parser;

public class UnsupportedStatementException extends RuntimeException {
  public UnsupportedStatementException(String message) {
    super("No parser found for statement: " + message);
  }
}
