package formatter.strategy;

import ast.root.AstNode;
import formatter.visitor.FormatterVisitorV1;

public interface FormattingStrategy {
  String apply(AstNode node, FormatterVisitorV1 visitor);
}
