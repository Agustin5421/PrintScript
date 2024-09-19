package linter.visitor;

import ast.root.AstNode;
import ast.root.AstNodeType;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.NewLinterVisitor;
import output.OutputResult;
import strategy.StrategyContainer;

public class ReworkedLinterVisitor implements NewLinterVisitor {
  private final StrategyContainer<AstNodeType, LintingStrategy> strategies;
  private final OutputResult<String> output;

  public ReworkedLinterVisitor(
      StrategyContainer<AstNodeType, LintingStrategy> strategies, OutputResult<String> output) {
    this.strategies = strategies;
    this.output = output;
  }

  @Override
  public NewLinterVisitor lintNode(AstNode node) {
    AstNodeType nodeType = node.getNodeType();

    LintingStrategy strategy = strategies.getStrategy(nodeType);

    return strategy.apply(node, this);
  }

  @Override
  public OutputResult<String> getOutput() {
    return output;
  }
}
