package linter.visitor.strategy;

import ast.root.AstNode;
import output.OutputResult;

public interface NewLinterVisitor {
  NewLinterVisitor lintNode(AstNode node);

  OutputResult<String> getOutput();
}
