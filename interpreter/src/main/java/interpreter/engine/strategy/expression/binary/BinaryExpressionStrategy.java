package interpreter.engine.strategy.expression.binary;

import ast.expressions.BinaryExpression;
import ast.literal.Literal;
import ast.root.AstNode;
import interpreter.engine.ValueCollector;
import interpreter.engine.strategy.expression.ExpressionStrategy;
import java.util.List;

public class BinaryExpressionStrategy implements ExpressionStrategy {
  private final List<BinaryProcedure> procedures;

  public BinaryExpressionStrategy(List<BinaryProcedure> procedures) {
    this.procedures = procedures;
  }

  @Override
  public ValueCollector apply(AstNode node, ValueCollector engine) {
    BinaryExpression binExp = (BinaryExpression) node;

    Literal<?> left = engine.evaluate(binExp.left()).getValue();
    Literal<?> right = engine.evaluate(binExp.right()).getValue();

    for (BinaryProcedure procedure : procedures) {
      if (procedure.isApplicable(left, right, binExp.operator())) {
        Literal<?> result = procedure.applyProcedure(left, right, binExp.operator());
        return engine.setValue(result);
      }
    }

    throw new UnsupportedOperationException("Unsupported binary operation: " + binExp.operator());
  }
}
