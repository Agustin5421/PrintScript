package formatter.newimpl.strategy;

import ast.root.AstNode;
import ast.statements.CallExpression;
import formatter.newimpl.FormatterVisitor2;
import java.util.List;

public class CallExpressionStrategy implements FormattingStrategy {
  private final List<FormattingStrategy> strategies;

  public CallExpressionStrategy(List<FormattingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor2 visitor) {
    CallExpression callExpression = (CallExpression) node;
    FormatterVisitor2 visit = (FormatterVisitor2) callExpression.methodIdentifier().accept(visitor);

    StringBuilder formattedCode = new StringBuilder();
    formattedCode
        .append(strategies.get(0).apply(node, visitor))
        .append(visit.getCurrentCode())
        .append("(")
        .append(strategies.get(1).apply(node, visitor))
        .append(")");

    return formattedCode.toString();
  }
}
