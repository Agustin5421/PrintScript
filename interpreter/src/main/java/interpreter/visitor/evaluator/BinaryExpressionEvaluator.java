package interpreter.visitor.evaluator;

import ast.expressions.BinaryExpression;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import interpreter.visitor.InterpreterVisitorV1;

public class BinaryExpressionEvaluator {
  public Literal<?> evaluate(BinaryExpression node, InterpreterVisitorV1 visitor) {
    Literal<?> left = ((InterpreterVisitorV1) node.left().accept(visitor)).getValue();
    Literal<?> right = ((InterpreterVisitorV1) node.right().accept(visitor)).getValue();
    return performOperation(left, right, node.operator());
  }

  private Literal<?> performOperation(Literal<?> left, Literal<?> right, String operator) {
    if (StringOperationHandler.isStringOperation(left, right, operator)) {
      return StringOperationHandler.performStringConcatenation(left, right);
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
}
