package ast.literal;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import exceptions.UnsupportedDataType;
import token.Position;
import token.Token;
import token.types.TokenType;
import token.types.TokenValueType;

// TODO: Implement a Literal parser for each type of literal
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

    if (tokenType == TokenValueType.BOOLEAN) {
      boolean value;
      value = token.value().equals("true");
      return new BooleanLiteral(value, token.initialPosition(), token.finalPosition());
    }

    Position position = token.initialPosition();
    String exceptionMessage = getExceptionMessage(token.value(), position.row(), position.col());

    throw new UnsupportedDataType(exceptionMessage);
  }
}
