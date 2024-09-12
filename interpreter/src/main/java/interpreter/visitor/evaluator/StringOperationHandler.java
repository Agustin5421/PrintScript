package interpreter.visitor.evaluator;

import ast.literal.Literal;
import ast.literal.StringLiteral;
import ast.root.AstNodeType;
import exceptions.UnsupportedExpressionException;
import java.util.List;

public class StringOperationHandler {
  public static Literal<?> performStringConcatenation(Literal<?> left, Literal<?> right) {
    return new StringLiteral(
        left.value().toString() + right.value().toString(), left.start(), right.end());
  }

  public static boolean isStringOperation(Literal<?> left, Literal<?> right, String operator) {
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
