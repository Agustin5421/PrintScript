package exceptions;

public class UnsupportedCharacter extends RuntimeException {
  public UnsupportedCharacter(String message) {
    super("Unsupported character: " + message);
  }
}
