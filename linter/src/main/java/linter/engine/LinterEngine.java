package linter.engine;

import ast.root.AstNode;
import ast.root.AstNodeType;
import linter.engine.strategy.LintingStrategy;
import output.OutputResult;
import strategy.StrategyContainer;

public class LinterEngine {
  private final StrategyContainer<AstNodeType, LintingStrategy> strategies;
  private final OutputResult<String> output;

  public LinterEngine(
      StrategyContainer<AstNodeType, LintingStrategy> strategies, OutputResult<String> output) {
    this.strategies = strategies;
    this.output = output;
  }

  public LinterEngine lintNode(AstNode node) {
    AstNodeType nodeType = node.getNodeType();

    LintingStrategy strategy = strategies.getStrategy(nodeType);

    if (strategy == null) {
      // todo: specify position of node.
      throw new IllegalArgumentException("No strategy found for node type: " + nodeType);
    }

    return strategy.apply(node, this);
  }

  public OutputResult<String> getOutput() {
    return output;
  }
}
