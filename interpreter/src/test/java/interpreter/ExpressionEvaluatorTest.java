package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import interpreter.visitor.InterpreterVisitorV1;
import interpreter.visitor.evaluator.BinaryExpressionEvaluator;
import interpreter.visitor.repository.VariablesRepository;
import java.io.ByteArrayInputStream;
import java.util.List;
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

  @Test
  public void testReadInputNumberDouble() {
    String input = "42.0";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "readInput(\"Number:\");";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);

    assertEquals(
        42.0,
        repository
            .getVariable(new Identifier("readInput", new Position(0, 0), new Position(0, 0)))
            .value());
  }

  @Test
  public void readInputPlusStringTest() {
    String input = "hello";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "let x: string = readInput(\"Name:\"); println(x+\" world\");";

    Interpreter interpreter = new Interpreter("1.1");
    List<String> printedValues = interpreter.interpret(code);

    assertEquals("hello world", printedValues.get(0));
  }

  // TODO: solve these tests

  //  @Test
  //  public void readInputPlusStringTest2() {
  //    String input = "hello";
  //    System.setIn(new ByteArrayInputStream(input.getBytes()));
  //
  //    String code = "let x: string = readInput(\"Name:\") + \" world\";";
  //
  //    Interpreter interpreter = new Interpreter("1.1");
  //    VariablesRepository repository = interpreter.executeProgram(code);
  //
  //    assertEquals("hello world", repository.getNewVariable(new VariableIdentifier("x")).value());
  //  }

}
