package formatter.strategy.common;

import ast.root.AstNode;
import formatter.FormatterVisitor;
import formatter.strategy.FormattingStrategy;

public class WhiteSpace implements FormattingStrategy {
  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    return " ";
  }
}
