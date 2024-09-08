package ast.literal;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import exceptions.UnsupportedDataType;
import token.Position;
import token.Token;
import token.types.TokenType;
import token.types.TokenValueType;

// TODO: Implement a Literal parser for each nodeType of literal
public class LiteralFactory {
  public static Literal<?> createLiteral(Token token) {
    TokenType tokenType = token.nodeType();
    if (tokenType == TokenValueType.STRING) {
      return new StringLiteral(token.value(), token.initialPosition(), token.finalPosition());
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

    Position position = token.initialPosition();
    String exceptionMessage = getExceptionMessage(token.value(), position.row(), position.col());

    throw new UnsupportedDataType(exceptionMessage);
  }
}
