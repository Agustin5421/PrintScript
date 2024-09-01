package ast.utils;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import ast.expressions.Expression;
import ast.identifier.IdentifierParser;
import exceptions.UnsupportedExpressionException;
import java.util.List;
import parsers.expressions.BinaryExpressionParser;
import parsers.expressions.ExpressionParser;
import parsers.expressions.LiteralParser;
import token.Position;
import token.Token;

public class ExpressionParserProvider {
  private static final List<ExpressionParser> parsers =
      List.of(new BinaryExpressionParser(), new LiteralParser(), new IdentifierParser());

  public static Expression parse(List<Token> statement) {
    for (ExpressionParser parser : parsers) {
      if (parser.shouldParse(statement)) {
        return (Expression) parser.parse(statement);
      }
    }

    Token token = statement.get(0);
    Position position = token.initialPosition();
    String exceptionMessage = getExceptionMessage(token.value(), position.row(), position.col());
    throw new UnsupportedExpressionException(exceptionMessage);
  }
}
