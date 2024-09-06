package interpreter.evaluator;

import ast.literal.Literal;
import ast.literal.NumberLiteral;

public class DoubleOperationHandler {
  public static Literal<?> performDoubleOperation(
      double leftValue, double rightValue, String operator, Literal<?> left, Literal<?> right) {
    switch (operator) {
      case "+":
        return new NumberLiteral(leftValue + rightValue, left.start(), right.end());
      case "-":
        return new NumberLiteral(leftValue - rightValue, left.start(), right.end());
      case "*":
        return new NumberLiteral(leftValue * rightValue, left.start(), right.end());
      case "/":
        return new NumberLiteral(leftValue / rightValue, left.start(), right.end());
      case "%":
        return new NumberLiteral(leftValue % rightValue, left.start(), right.end());
      default:
        throw new IllegalArgumentException("Invalid operator: " + operator);
    }
  }
}
