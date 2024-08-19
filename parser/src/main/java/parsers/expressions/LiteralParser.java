package parsers.expressions;

import ast.literal.LiteralFactory;
import ast.root.AstNode;
import java.util.List;
import token.Token;
import token.tokenTypes.TokenValueType;

public class LiteralParser implements ExpressionParser {

  @Override
  public AstNode parse(List<Token> tokens) {
    return LiteralFactory.createLiteral(tokens.get(0));
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return tokens.size() == 1 && tokens.get(0).getType() instanceof TokenValueType;
  }
}
