package exceptions;

import token.Token;

public class UnexpectedTokenException extends RuntimeException {
  public UnexpectedTokenException(Token unexpectedToken, String expected) {
    super(buildErrorMessage(unexpectedToken, expected));
  }

  private static String buildErrorMessage(Token unexpectedToken, String expected) {
    return "Syntax error: unexpected token '"
        + unexpectedToken.toString()
        + "', expected '"
        + expected
        + "' at line: "
        + unexpectedToken.finalPosition().row()
        + ", column: "
        + unexpectedToken.finalPosition().col();
  }
}
