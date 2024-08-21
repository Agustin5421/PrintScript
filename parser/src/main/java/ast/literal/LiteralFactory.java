package ast.literal;

import exceptions.parser.UnsupportedDataType;
import token.Token;
import token.types.TokenType;
import token.types.TokenValueType;

import java.util.List;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

public class LiteralFactory {
  public static Literal<?> createLiteral(Token token) {
    TokenType tokenType = token.getType();
    if (tokenType == TokenValueType.STRING) {
      return new StringLiteral(
          token.getValue(), token.getInitialPosition(), token.getFinalPosition());
    }

    if (tokenType == TokenValueType.NUMBER) {
      return new NumberLiteral(
          Integer.parseInt(token.getValue()), token.getInitialPosition(), token.getFinalPosition());
    }

    // TODO: Add support for identifiers

    String exceptionMessage = getExceptionMessage(List.of(token));

    throw new UnsupportedDataType(exceptionMessage);
  }
}
