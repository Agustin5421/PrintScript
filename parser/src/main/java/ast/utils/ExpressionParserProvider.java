package ast.utils;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import ast.expressions.Expression;
import ast.identifier.IdentifierParser;
import exceptions.parser.UnsupportedExpressionException;
import java.util.List;
import parsers.expressions.BinaryExpressionParser;
import parsers.expressions.ExpressionParser;
import parsers.expressions.LiteralParser;
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

    String exceptionMessage = getExceptionMessage(statement);
    throw new UnsupportedExpressionException(exceptionMessage);
  }
}
