package ast.literal;

import exceptions.UnsupportedDataType;
import token.Token;
import token.types.TokenType;
import token.types.TokenValueType;

// TODO: Implement a Literal parser for each tokenType of literal
public class LiteralFactory {
  public static Literal<?> createLiteral(Token token) {
    TokenType tokenType = token.tokenType();
    if (tokenType == TokenValueType.STRING) {
      return new StringLiteral(
          removeQuotes(token.value()), token.initialPosition(), token.finalPosition());
    }

    if (tokenType == TokenValueType.NUMBER) {
      if (token.value().contains(".")) {
        return new NumberLiteral(
            Double.parseDouble(token.value()), token.initialPosition(), token.finalPosition());
      } else {
        return new NumberLiteral(
            Integer.parseInt(token.value()), token.initialPosition(), token.finalPosition());
      }
    }

    if (tokenType == TokenValueType.BOOLEAN) {
      return new BooleanLiteral(
          Boolean.parseBoolean(token.value()), token.initialPosition(), token.finalPosition());
    }

    throw new UnsupportedDataType(token);
  }

  private static String removeQuotes(String value) {
    return value.substring(1, value.length() - 1);
  }
}
