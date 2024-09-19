package formatter;

import ast.root.AstNode;
import ast.root.AstNodeType;
import formatter.context.FormattingContext;
import formatter.strategy.FormattingStrategy;
import output.OutputResult;
import strategy.StrategyContainer;

public class FormattingEngine {
  private final FormattingContext context;
  private final StrategyContainer<AstNodeType, FormattingStrategy> strategies;
  private final OutputResult<String> formattingResult;

  public FormattingEngine(
      FormattingContext context,
      StrategyContainer<AstNodeType, FormattingStrategy> strategies,
      OutputResult<String> formattingResult) {
    this.context = context;
    this.strategies = strategies;
    this.formattingResult = formattingResult;
  }

  public FormattingEngine format(AstNode node) {
    AstNodeType nodeType = node.getNodeType();
    return strategies.getStrategy(nodeType).apply(node, this);
  }

  public void write(String code) {
    formattingResult.saveResult(code);
  }

  public FormattingEngine changeContext() {
    return new FormattingEngine(context.changeContext(), strategies, formattingResult);
  }

  public String getCurrentCode() {
    return formattingResult.getResult();
  }

  public void addContext() {
    write(context.addContext());
  }
}
