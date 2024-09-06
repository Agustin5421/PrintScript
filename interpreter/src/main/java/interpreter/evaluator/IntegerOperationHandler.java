package interpreter.evaluator;

import ast.literal.Literal;
import ast.literal.NumberLiteral;

public class IntegerOperationHandler {
  public static Literal<?> performIntegerOperation(
      int leftValue, int rightValue, String operator, Literal<?> left, Literal<?> right) {
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

  public static boolean checkBothIntegers(Number a, Number b) {
    return a instanceof Integer && b instanceof Integer;
  }
}
