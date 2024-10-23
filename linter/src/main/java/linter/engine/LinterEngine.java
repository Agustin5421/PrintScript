package linter.engine;

import ast.root.AstNode;
import ast.root.AstNodeType;
import linter.engine.strategy.LintingStrategy;
import output.OutputResult;
import report.Report;
import strategy.StrategyContainer;

public class LinterEngine {
  private final StrategyContainer<AstNodeType, LintingStrategy> strategies;
  private final OutputResult<Report> output;

  public LinterEngine(
      StrategyContainer<AstNodeType, LintingStrategy> strategies, OutputResult<Report> output) {
    this.strategies = strategies;
    this.output = output;
  }

  public LinterEngine lintNode(AstNode node) {
    AstNodeType nodeType = node.getNodeType();

    LintingStrategy strategy = strategies.getStrategy(nodeType);

    if (strategy == null) {
      throw new IllegalArgumentException(
          "Can not lint node "
              + node.getNodeType()
              + " from "
              + node.start()
              + " to "
              + node.end());
    }

    return strategy.apply(node, this);
  }

  public OutputResult<Report> getOutput() {
    return output;
  }
}
