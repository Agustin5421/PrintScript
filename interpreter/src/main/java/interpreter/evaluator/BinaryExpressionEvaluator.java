package interpreter.evaluator;

import ast.expressions.BinaryExpression;
import ast.literal.Literal;
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
    if (IntegerOperationHandler.checkBothIntegers(leftNumber, rightNumber)) {
      return IntegerOperationHandler.performIntegerOperation(
          leftNumber.intValue(), rightNumber.intValue(), operator, left, right);
    } else {
      return DoubleOperationHandler.performDoubleOperation(
          leftNumber.doubleValue(), rightNumber.doubleValue(), operator, left, right);
    }
  }
}
