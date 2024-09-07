package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.expressions.BinaryExpression;
import ast.literal.NumberLiteral;
import interpreter.evaluator.BinaryExpressionEvaluator;
import interpreter.visitor.InterpreterVisitorV1;
import org.junit.jupiter.api.Test;
import token.Position;

public class ExpressionEvaluatorTest {
  private final VariablesRepository variablesRepository = new VariablesRepository();
  private final InterpreterVisitorV1 visitor = new InterpreterVisitorV1(variablesRepository);
  private final BinaryExpressionEvaluator expressionEvaluator = new BinaryExpressionEvaluator();

  private NumberLiteral evaluateBinaryExpression(
      NumberLiteral left, NumberLiteral right, String operator) {
    Position position = new Position(1, 0);
    BinaryExpression binaryExpression =
        new BinaryExpression(left, right, operator, position, position);
    return (NumberLiteral) expressionEvaluator.evaluate(binaryExpression, visitor);
  }

  @Test
  public void minusTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(5, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(3, position, position);
    NumberLiteral result = evaluateBinaryExpression(numberLiteral, numberLiteral2, "-");
    assertEquals(2, result.value());
  }

  @Test
  public void divTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(10, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(5, position, position);
    NumberLiteral result = evaluateBinaryExpression(numberLiteral, numberLiteral2, "/");
    assertEquals(2d, result.value());
  }

  @Test
  public void remainderTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(10, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(8, position, position);
    NumberLiteral result = evaluateBinaryExpression(numberLiteral, numberLiteral2, "%");
    assertEquals(2, result.value());
  }

  @Test
  public void mulTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(2, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(1, position, position);
    NumberLiteral result = evaluateBinaryExpression(numberLiteral, numberLiteral2, "*");
    assertEquals(2, result.value());
  }

  @Test
  public void minusDoubleTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(5.0, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(3.0, position, position);
    NumberLiteral result = evaluateBinaryExpression(numberLiteral, numberLiteral2, "-");
    assertEquals(2.0, result.value());
  }

  @Test
  public void divDoubleTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(10.0, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(5.0, position, position);
    NumberLiteral result = evaluateBinaryExpression(numberLiteral, numberLiteral2, "/");
    assertEquals(2.0, result.value());
  }

  @Test
  public void remainderDoubleTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(10.0, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(8.0, position, position);
    NumberLiteral result = evaluateBinaryExpression(numberLiteral, numberLiteral2, "%");
    assertEquals(2.0, result.value());
  }

  @Test
  public void mulDoubleTest() {
    Position position = new Position(1, 0);
    NumberLiteral numberLiteral = new NumberLiteral(2.0, position, position);
    NumberLiteral numberLiteral2 = new NumberLiteral(1.0, position, position);
    NumberLiteral result = evaluateBinaryExpression(numberLiteral, numberLiteral2, "*");
    assertEquals(2.0, result.value());
  }
}
