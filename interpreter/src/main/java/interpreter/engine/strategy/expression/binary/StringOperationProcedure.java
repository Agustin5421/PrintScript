package interpreter.engine.strategy.expression.binary;

import ast.literal.Literal;
import ast.literal.StringLiteral;
import ast.root.AstNodeType;
import exceptions.UnsupportedExpressionException;
import java.util.List;

public class StringOperationProcedure implements BinaryProcedure {
  @Override
  public Literal<?> applyProcedure(Literal<?> left, Literal<?> right, String operator) {
    String concat = left.value().toString() + right.value().toString();
    return new StringLiteral(concat, left.start(), right.end());
  }

  @Override
  public boolean isApplicable(Literal<?> left, Literal<?> right, String operator) {
    if (left.getNodeType().equals(AstNodeType.STRING_LITERAL)
        || right.getNodeType().equals(AstNodeType.STRING_LITERAL)) {
      if (operator.equals("+")) {
        return true;
      } else {
        // TODO: should throw IllegalOperation
        throw new UnsupportedExpressionException(List.of());
      }
    }
    return false;
  }
}
