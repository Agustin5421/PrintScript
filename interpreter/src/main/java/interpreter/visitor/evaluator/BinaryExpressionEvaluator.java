package interpreter.visitor.evaluator;

import ast.expressions.BinaryExpression;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.root.AstNodeType;
import interpreter.visitor.InterpreterVisitor;

public class BinaryExpressionEvaluator {
  public Literal<?> evaluate(BinaryExpression node, InterpreterVisitor visitor) {
    Literal<?> left = ((InterpreterVisitor) node.left().accept(visitor)).getValue();
    Literal<?> right = ((InterpreterVisitor) node.right().accept(visitor)).getValue();
    return performOperation(left, right, node.operator());
  }

  private Literal<?> performOperation(Literal<?> left, Literal<?> right, String operator) {
    if (StringOperationHandler.isStringOperation(left, right, operator)) {
      return StringOperationHandler.performStringConcatenation(left, right);
    }
    if (checkBoolean(left, right)) {
      throw new UnsupportedOperationException("Trying to operate with booleans.");
    }
    // If one of them is not a string, they are both numbers
    Number leftNumber = (Number) left.value();
    Number rightNumber = (Number) right.value();
    double result =
        NumberOperationHandler.performDoubleOperation(
            leftNumber.doubleValue(), rightNumber.doubleValue(), operator);
    // If both numbers are integers and the operator is not division, we return the int value
    if (NumberOperationHandler.returnsInteger(leftNumber, rightNumber, operator)) {
      return new NumberLiteral((int) result, left.start(), right.end());
    } else {
      return new NumberLiteral(result, left.start(), right.end());
    }
  }

  private boolean checkBoolean(Literal<?> left, Literal<?> right) {
    AstNodeType leftType = left.getNodeType();
    AstNodeType rightType = right.getNodeType();
    return leftType.equals(AstNodeType.BOOLEAN_LITERAL)
        && rightType.equals(AstNodeType.BOOLEAN_LITERAL);
  }
}
