package exceptions;

import java.util.List;
import java.util.stream.Collectors;
import token.Token;

public class UnsupportedStatementException extends RuntimeException {
  public UnsupportedStatementException(List<Token> tokens) {
    super(buildErrorMessage(tokens));
  }

  private static String buildErrorMessage(List<Token> tokens) {
    return "No parser found for statement: "
        + formatTokens(tokens)
        + "\n at line: "
        + tokens.get(0).finalPosition().row()
        + " column: "
        + tokens.get(0).finalPosition().col();
  }

  private static String formatTokens(List<Token> tokens) {
    return tokens.stream().map(Token::toString).collect(Collectors.joining(" "));
  }
}
