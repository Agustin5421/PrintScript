package ast.literal;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import exceptions.UnsupportedDataType;
import token.Position;
import token.Token;
import token.types.TokenType;
import token.types.TokenValueType;

public class LiteralFactory {
  public static Literal<?> createLiteral(Token token) {
    TokenType tokenType = token.type();
    if (tokenType == TokenValueType.STRING) {
      return new StringLiteral(token.value(), token.initialPosition(), token.finalPosition());
    }

    if (tokenType == TokenValueType.NUMBER) {
      return new NumberLiteral(
          Integer.parseInt(token.value()), token.initialPosition(), token.finalPosition());
    }

    Position position = token.initialPosition();
    String exceptionMessage = getExceptionMessage(token.value(), position.row(), position.col());

    throw new UnsupportedDataType(exceptionMessage);
  }
}
