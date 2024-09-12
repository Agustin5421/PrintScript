package formatter.strategy.ifelse;

import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;

public class IndentStrategy implements FormattingStrategy {
  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    return "\t".repeat(visitor.getValue());
  }
}
