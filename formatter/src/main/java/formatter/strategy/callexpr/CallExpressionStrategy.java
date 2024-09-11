package formatter.strategy.callexpr;

import ast.root.AstNode;
import ast.statements.CallExpression;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.CallStrategy;
import formatter.visitor.FormatterVisitor;

public class CallExpressionStrategy implements FormattingStrategy {
  private final CallStrategy callStrategy;

  public CallExpressionStrategy(CallStrategy callStrategy) {

    this.callStrategy = callStrategy;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    CallExpression callExpression = (CallExpression) node;
    FormatterVisitor visit = (FormatterVisitor) callExpression.methodIdentifier().accept(visitor);
    String identifier = visit.getCurrentCode();

    CallStrategy newCallStrategy = callStrategy.newStrategy(identifier, callExpression.arguments());

    return newCallStrategy.apply(node, visitor);
  }
}
