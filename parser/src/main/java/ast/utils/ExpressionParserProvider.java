package ast.utils;

import ast.expressions.Expression;
import ast.identifier.IdentifierParser;
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

    throw new IllegalArgumentException("Error: No parser found for statement");
  }
}
