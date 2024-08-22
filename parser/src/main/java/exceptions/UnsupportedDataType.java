package exceptions;

public class UnsupportedDataType extends RuntimeException {
  public UnsupportedDataType(String message) {
    super("Unsupported data type: " + message);
  }
}
