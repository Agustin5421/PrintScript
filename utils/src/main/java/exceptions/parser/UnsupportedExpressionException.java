package exceptions.parser;

public class UnsupportedExpressionException extends RuntimeException {
    public UnsupportedExpressionException(String message) {
        super("Unsupported expression: " + message);
    }
}
