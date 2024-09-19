package linter.visitor;

import ast.root.AstNode;
import ast.root.AstNodeType;
import java.util.Map;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.NewLinterVisitor;
import output.OutputResult;

public class ReworkedLinterVisitor implements NewLinterVisitor {
  private final Map<AstNodeType, LintingStrategy> strategies;
  private final OutputResult<String> output;

  public ReworkedLinterVisitor(
      Map<AstNodeType, LintingStrategy> strategies, OutputResult<String> output) {
    this.strategies = strategies;
    this.output = output;
  }

  @Override
  public NewLinterVisitor lintNode(AstNode node) {
    return null;
  }

  @Override
  public OutputResult<String> getOutput() {
    return output;
  }
}
