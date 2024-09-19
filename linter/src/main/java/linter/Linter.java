package linter;

import ast.root.AstNode;
import linter.engine.LinterEngine;

public record Linter(LinterEngine engine) {

  public Linter lint(AstNode node) {
    LinterEngine newEngine = engine.lintNode(node);

    return new Linter(newEngine);
  }
}
