package ast.literal;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import exceptions.parser.UnsupportedDataType;
import java.util.List;

import token.Position;
import token.Token;
import token.types.TokenType;
import token.types.TokenValueType;

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

    Position position = token.getInitialPosition();
    String exceptionMessage = getExceptionMessage(token.getValue(), position.row(), position.col());

    throw new UnsupportedDataType(exceptionMessage);
  }
}
