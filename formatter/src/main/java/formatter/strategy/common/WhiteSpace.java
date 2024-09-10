package formatter.strategy.common;

import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;

public class WhiteSpace implements FormattingStrategy {
  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    return " ";
  }
}
