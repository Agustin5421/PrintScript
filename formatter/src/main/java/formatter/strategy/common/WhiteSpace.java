package formatter.strategy.common;

import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitorV1;

public class WhiteSpace implements FormattingStrategy {
  @Override
  public String apply(AstNode node, FormatterVisitorV1 visitor) {
    return " ";
  }
}
