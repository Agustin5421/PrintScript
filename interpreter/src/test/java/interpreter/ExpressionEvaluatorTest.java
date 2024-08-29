package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.expressions.BinaryExpression;
import ast.literal.NumberLiteral;
import interpreter.runtime.ExpressionEvaluator;
import org.junit.jupiter.api.Test;
import token.Position;

public class ExpressionEvaluatorTest {
  @Test
  public void minusTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(5, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(3, position, position);
    BinaryExpression binaryExpression =
        new BinaryExpression(numberLiteral, numberLiteral2, "-", position, position);

    ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(new VariablesRepository(), 1);

    NumberLiteral result = (NumberLiteral) expressionEvaluator.evaluate(binaryExpression);

    assertEquals(2, result.value());
  }

  @Test
  public void divTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(10, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(5, position, position);
    BinaryExpression binaryExpression =
        new BinaryExpression(numberLiteral, numberLiteral2, "/", position, position);

    ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(new VariablesRepository(), 1);

    NumberLiteral result = (NumberLiteral) expressionEvaluator.evaluate(binaryExpression);

    assertEquals(2, result.value());
  }

  @Test
  public void remainderTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(10, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(8, position, position);
    BinaryExpression binaryExpression =
        new BinaryExpression(numberLiteral, numberLiteral2, "%", position, position);

    ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(new VariablesRepository(), 1);

    NumberLiteral result = (NumberLiteral) expressionEvaluator.evaluate(binaryExpression);

    assertEquals(2, result.value());
  }

  @Test
  public void mulTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(2, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(1, position, position);
    BinaryExpression binaryExpression =
        new BinaryExpression(numberLiteral, numberLiteral2, "*", position, position);

    ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(new VariablesRepository(), 1);

    NumberLiteral result = (NumberLiteral) expressionEvaluator.evaluate(binaryExpression);

    assertEquals(2, result.value());
  }
}
