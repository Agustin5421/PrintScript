package formatter.newimpl.strategy;

import ast.root.AstNode;
import formatter.newimpl.FormatterVisitor2;
import java.util.List;

public class CallExpressionStrategy implements FormattingStrategy {
  private final List<FormattingStrategy> strategies;

  public CallExpressionStrategy(List<FormattingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor2 visitor) {
    StringBuilder formattedCode = new StringBuilder();
    formattedCode.append("(");
    for (FormattingStrategy strategy : strategies) {
      formattedCode.append(strategy.apply(node, visitor));
    }
    formattedCode.append(")");
    return formattedCode.toString();
  }
}
