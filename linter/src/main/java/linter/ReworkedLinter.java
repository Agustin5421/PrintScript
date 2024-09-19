package linter;

import ast.root.AstNode;
import linter.visitor.ReworkedLinterVisitor;

public class ReworkedLinter {
  private ReworkedLinterVisitor visitor;

  public ReworkedLinter(ReworkedLinterVisitor visitor) {
    this.visitor = visitor;
  }

  public ReworkedLinter lint(AstNode node) {
    ReworkedLinterVisitor visitor2 = (ReworkedLinterVisitor) visitor.lintNode(node);

    return new ReworkedLinter(visitor2);
  }
}
