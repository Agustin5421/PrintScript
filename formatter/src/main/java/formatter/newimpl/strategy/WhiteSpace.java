package formatter.newimpl.strategy;

import ast.root.AstNode;
import formatter.newimpl.FormatterVisitor2;

public class WhiteSpace implements FormattingStrategy {
  @Override
  public String apply(AstNode node, FormatterVisitor2 visitor) {
    return " ";
  }
}
