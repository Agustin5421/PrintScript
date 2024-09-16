package interpreter.visitor.strategy.binary;

import ast.expressions.BinaryExpression;
import ast.literal.Literal;
import ast.root.AstNode;
import ast.visitor.NodeVisitor;
import interpreter.ValueCollector;
import interpreter.visitor.strategy.InterpretingStrategy;
import java.util.List;

public class BinaryExpressionStrategy implements InterpretingStrategy {
  private final List<BinaryProcedure> procedures;

  public BinaryExpressionStrategy(List<BinaryProcedure> procedures) {
    this.procedures = procedures;
  }

  @Override
  public NodeVisitor interpret(AstNode node, NodeVisitor visitor) {
    BinaryExpression binExp = (BinaryExpression) node;
    ValueCollector valueCollector = (ValueCollector) visitor;

    Literal<?> left = ((ValueCollector) binExp.left().accept(valueCollector)).getValue();
    Literal<?> right = ((ValueCollector) binExp.right().accept(valueCollector)).getValue();

    for (BinaryProcedure procedure : procedures) {
      if (procedure.isApplicable(left, right, binExp.operator())) {
        Literal<?> result = procedure.applyProcedure(left, right, binExp.operator());
        return valueCollector.setValue(result);
      }
    }

    throw new UnsupportedOperationException("Unsupported binary operation: " + binExp.operator());
  }
}
