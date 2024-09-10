package formatter.strategy;

import ast.root.AstNode;
import formatter.visitor.FormatterVisitor;

public interface FormattingStrategy {
  String apply(AstNode node, FormatterVisitor visitor);
}
