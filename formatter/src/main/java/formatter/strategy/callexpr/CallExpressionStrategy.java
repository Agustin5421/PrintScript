package formatter.strategy.callexpr;

import ast.root.AstNode;
import ast.statements.CallExpression;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;
import java.util.List;

public class CallExpressionStrategy implements FormattingStrategy {
  private final List<FormattingStrategy> strategies;

  public CallExpressionStrategy(List<FormattingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    CallExpression callExpression = (CallExpression) node;
    FormatterVisitor visit = (FormatterVisitor) callExpression.methodIdentifier().accept(visitor);

    StringBuilder formattedCode = new StringBuilder();
    formattedCode
        .append(strategies.get(0).apply(node, visitor))
        .append(visit.getCurrentCode())
        .append("(")
        .append(strategies.get(1).apply(node, visitor))
        .append(");");

    return formattedCode.toString();
  }
}
