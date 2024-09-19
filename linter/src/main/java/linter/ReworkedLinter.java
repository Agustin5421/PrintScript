package linter;

import ast.root.AstNode;
import linter.visitor.strategy.NewLinterVisitor;

public class ReworkedLinter {
  private final NewLinterVisitor visitor;

  public ReworkedLinter(NewLinterVisitor visitor) {
    this.visitor = visitor;
  }

  public ReworkedLinter lint(AstNode node) {
    NewLinterVisitor visitor2 = visitor.lintNode(node);

    return new ReworkedLinter(visitor2);
  }
}
