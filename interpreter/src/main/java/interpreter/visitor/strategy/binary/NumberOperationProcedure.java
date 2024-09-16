package interpreter.visitor.strategy.binary;

import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.root.AstNodeType;

public class NumberOperationProcedure implements BinaryProcedure {
  @Override
  public Literal<?> applyProcedure(Literal<?> left, Literal<?> right, String operator) {
    Number leftNumber = (Number) left.value();
    Number rightNumber = (Number) right.value();
    double leftValue = leftNumber.doubleValue();
    double rightValue = rightNumber.doubleValue();

    double result =
        switch (operator) {
          case "+" -> leftValue + rightValue;
          case "-" -> leftValue - rightValue;
          case "*" -> leftValue * rightValue;
          case "/" -> leftValue / rightValue;
          case "%" -> leftValue % rightValue;
          default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };

    if (returnsInteger(leftNumber, rightNumber, operator)) {
      return new NumberLiteral((int) result, left.start(), right.end());
    } else {
      return new NumberLiteral(result, left.start(), right.end());
    }
  }

  public boolean returnsInteger(Number a, Number b, String operator) {
    return a instanceof Integer && b instanceof Integer && !operator.equals("/");
  }

  @Override
  public boolean isApplicable(Literal<?> left, Literal<?> right, String operator) {
    return left.getNodeType().equals(AstNodeType.NUMBER_LITERAL)
        && right.getNodeType().equals(AstNodeType.NUMBER_LITERAL);
  }
}
