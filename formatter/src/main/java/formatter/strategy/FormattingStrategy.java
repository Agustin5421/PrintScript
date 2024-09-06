package formatter.strategy;

import ast.root.AstNode;
import formatter.FormatterVisitor;

public interface FormattingStrategy {
  String apply(AstNode node, FormatterVisitor visitor);
}
