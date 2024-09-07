package interpreter.evaluator;

public class NumberOperationHandler {
  public static double performDoubleOperation(
      double leftValue, double rightValue, String operator) {
    return switch (operator) {
      case "+" -> leftValue + rightValue;
      case "-" -> leftValue - rightValue;
      case "*" -> leftValue * rightValue;
      case "/" -> leftValue / rightValue;
      case "%" -> leftValue % rightValue;
      default -> throw new IllegalArgumentException("Invalid operator: " + operator);
    };
  }

  public static boolean returnsInteger(Number a, Number b, String operator) {
    return a instanceof Integer && b instanceof Integer && !operator.equals("/");
  }
}
