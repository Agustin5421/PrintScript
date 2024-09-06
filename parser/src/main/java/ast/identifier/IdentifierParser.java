package ast.identifier;

import ast.expressions.Expression;
import java.util.List;
import parsers.Parser;
import parsers.expressions.ExpressionParser;
import token.Token;
import token.types.TokenSyntaxType;

public class IdentifierParser implements ExpressionParser {
  @Override
  public Expression parse(Parser parser, List<Token> tokens) {
    return new Identifier(
        tokens.get(0).value(), tokens.get(0).initialPosition(), tokens.get(0).finalPosition());
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    if (tokens.size() != 1) {
      return false;
    }
    return tokens.get(0).nodeType() == TokenSyntaxType.IDENTIFIER;
  }
}
