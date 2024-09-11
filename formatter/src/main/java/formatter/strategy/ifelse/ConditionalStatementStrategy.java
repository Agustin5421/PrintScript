package formatter.strategy.ifelse;

import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;
import java.util.List;

public class ConditionalStatementStrategy implements FormattingStrategy {
  private final String keyword;
  private final String condition;
  // Consists of four strategies that can be NoSpace or WhiteSpace
  private final List<FormattingStrategy> strategies;

  public ConditionalStatementStrategy(
      List<FormattingStrategy> strategies, String keyword, String condition) {
    this.keyword = keyword;
    this.condition = condition;
    this.strategies = strategies;
  }

  public ConditionalStatementStrategy(List<FormattingStrategy> strategies) {
    this(strategies, "", "");
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    StringBuilder formattedCode = new StringBuilder();
    formattedCode
        .append(keyword)
        .append(strategies.get(0).apply(node, visitor))
        .append("(")
        .append(strategies.get(1).apply(node, visitor))
        .append(condition)
        .append(strategies.get(2).apply(node, visitor))
        .append(")")
        .append(strategies.get(3).apply(node, visitor));
    return formattedCode.toString();
  }

  public ConditionalStatementStrategy newStrategy(String keyword, String condition) {
    return new ConditionalStatementStrategy(strategies, keyword, condition);
  }
}
