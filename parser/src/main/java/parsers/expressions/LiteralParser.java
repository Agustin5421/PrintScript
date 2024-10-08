package parsers.expressions;

import ast.expressions.ExpressionNode;
import ast.literal.LiteralFactory;
import java.util.List;
import parsers.Parser;
import token.Token;
import token.types.TokenValueType;

public class LiteralParser implements ExpressionParser {

  // TODO: Implement a Literal parser for each tokenType of literal
  @Override
  public ExpressionNode parse(Parser parser, List<Token> tokens) {
    return LiteralFactory.createLiteral(tokens.get(0));
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return tokens.get(0).tokenType() instanceof TokenValueType && tokens.size() <= 2;
  }
}
