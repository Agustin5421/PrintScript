package interpreter.visitor.strategy.binary;

import ast.literal.Literal;
import ast.root.AstNodeType;

public class BooleanFailingProcedure implements BinaryProcedure {
  @Override
  public Literal<?> applyProcedure(Literal<?> left, Literal<?> right, String operator) {
    throw new UnsupportedOperationException("Trying to operate with booleans.");
  }

  @Override
  public boolean isApplicable(Literal<?> left, Literal<?> right, String operator) {
    AstNodeType leftType = left.getNodeType();
    AstNodeType rightType = right.getNodeType();
    return leftType.equals(AstNodeType.BOOLEAN_LITERAL)
        && rightType.equals(AstNodeType.BOOLEAN_LITERAL);
  }
}
