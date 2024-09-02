package parsers.expressions;

import ast.expressions.Expression;
import ast.literal.LiteralFactory;
import java.util.List;
import parsers.Parser;
import token.Token;
import token.types.TokenValueType;

public class LiteralParser implements ExpressionParser {

  // TODO: Implement a Literal parser for each type of literal
  @Override
  public Expression parse(Parser parser, List<Token> tokens) {
    return LiteralFactory.createLiteral(tokens.get(0));
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return tokens.size() == 1 && tokens.get(0).type() instanceof TokenValueType;
  }
}
