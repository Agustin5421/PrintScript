package exceptions;

import java.util.List;
import java.util.stream.Collectors;
import token.Token;

public class UnsupportedExpressionException extends RuntimeException {
  public UnsupportedExpressionException(List<Token> tokens) {
    super(buildErrorMessage(tokens));
  }

  private static String buildErrorMessage(List<Token> tokens) {
    return "Unsupported expression: " + formatTokens(tokens);
  }

  private static String formatTokens(List<Token> tokens) {
    return tokens.stream().map(Token::toString).collect(Collectors.joining(" "));
  }
}
