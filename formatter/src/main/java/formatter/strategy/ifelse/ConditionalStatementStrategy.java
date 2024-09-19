package formatter.strategy.ifelse;

import ast.expressions.ExpressionNode;
import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.CallStrategy;
import java.util.List;

public class ConditionalStatementStrategy implements FormattingStrategy {
  private final String keyword;
  private final ExpressionNode condition;
  // Strategy for adding the conditional statement
  private final CallStrategy callStrategy;

  public ConditionalStatementStrategy(
      CallStrategy callStrategy, String keyword, ExpressionNode condition) {
    this.keyword = keyword;
    this.condition = condition;
    this.callStrategy = callStrategy;
  }

  public ConditionalStatementStrategy(CallStrategy callStrategy) {
    this(callStrategy, "", null);
  }

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    CallStrategy newCallStrategy = callStrategy.newStrategy(keyword, List.of(condition));
    return newCallStrategy.apply(node, engine);
  }

  public ConditionalStatementStrategy newStrategy(String keyword, ExpressionNode condition) {
    return new ConditionalStatementStrategy(callStrategy, keyword, condition);
  }
}
