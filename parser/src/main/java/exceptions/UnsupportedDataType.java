package exceptions;

import token.Token;

public class UnsupportedDataType extends RuntimeException {
  public UnsupportedDataType(Token tokens) {
    super(buildErrorMessage(tokens));
  }

  private static String buildErrorMessage(Token tokens) {
    return "Unsupported data node type: " + formatTokens(tokens);
  }

  private static String formatTokens(Token tokens) {
    return tokens.toString();
  }
}
