package exceptions;

public class MismatchTypeException extends RuntimeException {
  public MismatchTypeException(String name, String expected, String actual) {
    super("Variable '" + name + "' of type '" + expected + "' does not match " + actual);
  }
}
