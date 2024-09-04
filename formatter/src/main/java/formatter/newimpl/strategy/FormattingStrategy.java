package formatter.newimpl.strategy;

import ast.root.AstNode;
import formatter.newimpl.FormatterVisitor2;

public interface FormattingStrategy {
  String apply(AstNode node, FormatterVisitor2 visitor);
}
